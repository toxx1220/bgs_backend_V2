# Application Deployment Notes
1. _VPS:_ `mkdir ~/bgs` Make sure the folder `~/bgs` is present
2. _Local:_ Build jar and push it to VPS: `scp ./build/libs/bgs*.jar toxxvpn:~/bgs/app.jar` (in this case, a ssh config toxxvpn defining the ssh connection exists)
3. _Local:_ Push the deployment folder to VPS `scp -r ./deployment toxxvpn:~/bgs`, including the .env file.
4. _VPS_: Run `docker compose up`