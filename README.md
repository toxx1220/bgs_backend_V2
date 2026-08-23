# bgs_backend_V2

Backend for [Board Game Search App](https://github.com/krahl-jan/bgsearchapp).

Currently hosted at [bgsearch.toxx.dev](https://bgsearch.toxx.dev/swagger-ui/index.html)

[![Powered by BoardGameGeek](src/main/resources/powered_by_BGG_01_SM.png)](https://boardgamegeek.com)

### Deployment
This App is deployed as a [Nix Module](https://nixos.wiki/wiki/NixOS_modules). 
Deploying is as easy as tagging and pushing a commit. A git action triggers a merge request into my [NixOS VPS](https://github.com/toxx1220/nix-vps), which auto deploys it.