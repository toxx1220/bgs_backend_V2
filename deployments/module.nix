# NixOS module for the BGS Backend systemd service.
self:
{
  config,
  lib,
  pkgs,
  ...
}:
let
  cfg = config.services.bgs-backend;
  user = "bgs-app";
in
{
  options.services.bgs-backend = {
    enable = lib.mkEnableOption "BGS Backend Service";

    package = lib.mkOption {
      type = lib.types.package;
      default = self.packages.${pkgs.stdenv.hostPlatform.system}.default;
      description = "The bgs-backend package to use.";
    };

    envFile = lib.mkOption {
      type = lib.types.str;
      description = "Path to a file containing environment variables (e.g., database credentials)";
    };
  };

  config = lib.mkIf cfg.enable {
    users.users.${user} = {
      isSystemUser = true;
      group = user;
      description = "BGS Backend Service User";
      home = "/var/lib/bgs-backend";
      createHome = true;
    };
    users.groups.${user} = { };

    systemd.services.bgs-backend = {
      description = "BGS Kotlin Backend";
      after = [
        "network.target"
        "postgresql.service"
      ];
      wantedBy = [ "multi-user.target" ];

      serviceConfig = {
        User = user;
        Group = user;
        WorkingDirectory = "/var/lib/bgs-backend";
        EnvironmentFile = cfg.envFile;
        ExecStart = "${cfg.package}/bin/bgs";

        Restart = "always";
        RestartSec = "10s";

        # Standard hardening
        ProtectSystem = "full"; # Read-only /usr, /boot, /etc
        ProtectHome = true; # Inaccessible /home
        NoNewPrivileges = true; # Cannot escalate to root
        PrivateTmp = true;
      };
    };
  };
}
