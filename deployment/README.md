# Application Deployment Notes
1. _VPS:_ `mkdir ~/bgs` Make sure the folder `~/bgs` is present
2. _Local:_ Build jar and push it to VPS: `scp ./build/libs/bgs*.jar toxxvps:~/bgs/app.jar` (in this case, a ssh config toxxvps defining the ssh connection exists. If not present use regular `root@ip:/foldername`)
3. _Local:_ Push the deployment folder to VPS `scp ./deployment/* toxxvps:~/bgs/`, including the .env file.
4. _VPS_: Run `docker compose up` in `~/bgs` folder