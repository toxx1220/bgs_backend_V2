{
  projectRootFile = "flake.nix";

  programs.nixfmt.enable = true;
  programs.prettier.enable = true;
  programs.shfmt.enable = true;

  settings.excludes = [
    "settings.json"
    "secrets.yaml"
    "*.md"
    "flake.lock"
    "dump.sql"
    ".sops.yaml"
  ];

  programs.prettier.excludes = [
    "settings.json"
    "secrets.yaml"
    "*.md"
    "dump.sql"
    ".sops.yaml"
  ];
}
