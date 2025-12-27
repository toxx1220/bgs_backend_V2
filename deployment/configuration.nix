# Edit this configuration file to define what should be installed on
# your system. Help is available in the configuration.nix(5) man page, on
# https://search.nixos.org/options and in the NixOS manual (`nixos-help`).

{ config, lib, pkgs, pkgs-stable, ... }:
let
    user = "toxx";
    sshPort = 6969;
    hostName = "nixos-vps";
in
{
  imports =
    [ # Include the results of the hardware scan.
      ./hardware-configuration.nix
    ];

  system.autoUpgrade = {
      enable = true;
      flake = "/etc/nixos";
      flags = [
        "--update-input"
        "nixpkgs"
      ];
      dates = "Mon *-*-* 00:00:00";
    };
    nix = {
    	settings.auto-optimise-store = true;
    };

    programs.nh = {
      enable = true;
      clean.enable = true;
      clean.extraArgs = "--keep-since 8d --keep 3";
      clean.dates = "weekly";
      flake = "/etc/nixos";
    };

  # Use the systemd-boot EFI boot loader.
  boot.loader.systemd-boot.enable = true;
  boot.loader.efi.canTouchEfiVariables = true;


  nix.settings.experimental-features = [ "nix-command" "flakes" ];

  networking.hostName = hostName; # Define your hostname.
  networking.networkmanager.enable = true;  # Easiest to use and most distros use this by default.

  # Set your time zone.
  time.timeZone = "Europe/Berlin";

  # Configure network proxy if necessary
  # networking.proxy.default = "http://user:password@proxy:port/";
  # networking.proxy.noProxy = "127.0.0.1,localhost,internal.domain";

  # Select internationalisation properties.
  # i18n.defaultLocale = "en_US.UTF-8";
  # console = {
  #   font = "Lat2-Terminus16";
  #   keyMap = "us";
  #   useXkbConfig = true; # use xkb.options in tty.
  # };

  # Define a user account. Don't forget to set a password with ‘passwd’.
  users.users.${user} = {
     isNormalUser = true;
     extraGroups = [ "wheel" "sudo" "docker" ]; # Enable ‘sudo’ for the user.
     packages = with pkgs; [
     ];
     openssh.authorizedKeys.keys = [ "ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAIEdkWwiBoThxsipUqiK6hPXLn4KxI5GstfLJaE4nbjMO" ];
   };

   sops = {
    defaultSopsFile = ./secrets.yaml;
    age.keyFile = "/home/${user}/.config/sops/age/keys.txt";
    secrets = {
        "docker-env" = { # Load secrets as .env file for docker
            path = "/home/${user}/bgs/.env";
            owner = user;
            mode = "0600";
        };
        "docker-config" = {
            path = "/home/${user}/.docker/config.json";
            owner = user;
            mode = "0600";
        };
    };
   };
   # ensure .docker directory exists with correct permissions
   systemd.tmpfiles.rules = [
     "d /home/${user}/.docker 0700 ${user} users -"
   ];

  # List packages installed in system profile. To search, run:
  # $ nix search wget
   environment.systemPackages = with pkgs; [
     micro
     btop
     tree
     age
     sops
   ];

   virtualisation.docker.enable = true;

  # Some programs need SUID wrappers, can be configured further or are
  # started in user sessions.
  # programs.mtr.enable = true;
  # programs.gnupg.agent = {
  #   enable = true;
  #   enableSSHSupport = true;
  # };

  # List services that you want to enable:

  # Enable the OpenSSH daemon.
  services.openssh = {
	enable = true;
	ports = [ sshPort ];
	settings = {
		AllowUsers = [ user ];
		PermitRootLogin = "no";
		PasswordAuthentication = false;
		KbdInteractiveAuthentication = false;
        UsePAM = false;
	};
	extraConfig = ''
ClientAliveInterval 3600
ClientAliveCountMax 3
	'';
	banner = ''
    .
  .o8
.o888oo  .ooooo.  oooo    ooo oooo    ooo         oooo    ooo oo.ooooo.   .oooo.o
  888   d88' `88b  `88b..8P'   `88b..8P'           `88.  .8'   888' `88b d88(  "8
  888   888   888    Y888'       Y888'    8888888   `88..8'    888   888 `"Y88b.
  888 . 888   888  .o8"'88b    .o8"'88b              `888'     888   888 o.  )88b
  "888" `Y8bod8P' o88'   888o o88'   888o             `8'      888bod8P' 8""888P'
                                                               888
                                                              o888o

	'';
 };

  # Open ports in the firewall.
  networking.firewall.allowedTCPPorts = [80 443 sshPort];
  # networking.firewall.allowedUDPPorts = [ ... ];
  # Or disable the firewall altogether.
  # networking.firewall.enable = false;

  # This option defines the first version of NixOS you have installed on this particular machine,
  # and is used to maintain compatibility with application data (e.g. databases) created on older NixOS versions.
  #
  # Most users should NEVER change this value after the initial install, for any reason,
  # even if you've upgraded your system to a new NixOS release.
  #
  # This value does NOT affect the Nixpkgs version your packages and OS are pulled from,
  # so changing it will NOT upgrade your system - see https://nixos.org/manual/nixos/stable/#sec-upgrading for how
  # to actually do that.
  #
  # This value being lower than the current NixOS release does NOT mean your system is
  # out of date, out of support, or vulnerable.
  #
  # Do NOT change this value unless you have manually inspected all the changes it would make to your configuration,
  # and migrated your data accordingly.
  #
  # For more information, see `man configuration.nix` or https://nixos.org/manual/nixos/stable/options#opt-system.stateVersion .
  system.stateVersion = "24.11"; # Did you read the comment?

}
