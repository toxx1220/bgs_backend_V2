#!/usr/bin/env bash
set -e

rsync -avz ./deployment/docker-compose.yml ./deployment/secrets.yaml hetzner:~/bgs/
rsync -avz ./deployment/configuration.nix ./deployment/flake.nix hetzner:~/bgs/
ssh -t hetzner 'sudo mv ~/bgs/secrets.yaml ~/bgs/configuration.nix ~/bgs/flake.nix /etc/nixos/'
