# Application Deployment Notes
Using sops for secrets management. Make sure to supply the right env vars for docker compose.

Dockerfile needs to be adjusted if app should get more memory

Added automatic deployment via dockerhub + watchtower on Github Releases.
To update watchtower out of schedule, run:\
`docker run --rm   -v /var/run/docker.sock:/var/run/docker.sock   -v $HOME/.docker/config.json:/config.json   containrrr/watchtower   --run-once   bgs-backend`

Manual Deployment
1. _VPS:_ `mkdir ~/bgs` Make sure the folder `~/bgs` is present
2. _Local:_ Build jar and push it to VPS: `scp ./build/libs/bgs*.jar toxxvps:~/bgs/app.jar` (in this case, a ssh config toxxvps defining the ssh connection exists. If not present use regular `root@ip:/foldername`)
3. _Local:_ Push the deployment folder to VPS `scp ./deployment/* toxxvps:~/bgs/`, including the .env file.
4. _VPS_: Run `docker compose up` in `~/bgs` folder