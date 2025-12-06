{
  description = "NixOS server configuration flake";

  inputs = {
    nixpkgs = { url = "github:nixos/nixpkgs/nixos-unstable"; };
    nixpkgs-stable = { url = "github:nixos/nixpkgs?ref=nixos-25.11"; };
    sops-nix.url = "github:Mic92/sops-nix";
    sops-nix.inputs.nixpkgs.follows = "nixpkgs";
  };

  outputs = { self, nixpkgs, nixpkgs-stable, sops-nix, ... }:
  let
    system = "aarch64-linux";

    # Configuration for both package sets
    pkgsConfig = {
      allowUnfree = true;
    };

    # Unstable packages (default)
    pkgs = import nixpkgs {
      inherit system;
      config = pkgsConfig;
    };

    # Stable packages
    pkgs-stable = import nixpkgs-stable {
      inherit system;
      config = pkgsConfig;
    };

  in {
    nixosConfigurations.nixos-vps = nixpkgs.lib.nixosSystem {
      inherit system;

      specialArgs = {
        # Make stable packages available as pkgs-stable
        inherit pkgs-stable;
      };

      modules = [
        # The main configuration file
        ./configuration.nix
        sops-nix.nixosModules.sops
      ];
    };
  };
}
