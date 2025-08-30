# Application Deployment Notes
needs its own .env file. Sample:
```env
APP_NAME=bgs-backend
APP_PORT=8080
PG_USER=bgs
PG_PASSWORD=password
PG_PORT=5432
PG_HOST=bgsDbContainer
PG_DATABASE=bgsDb
GIT_TOKEN=token # needs access to git repo with board game data (https://framagit.org/r.g/board-game-data)
SPRING_DATASOURCE_URL=jdbc:postgresql://${PG_HOST}:${PG_PORT}/${PG_DATABASE}
SPRING_DATASOURCE_USERNAME=${PG_USER}
SPRING_DATASOURCE_PASSWORD=${PG_PASSWORD}
SPRING_JPA_HIBERNATE_DDL_AUTO=update
DNS_NAME=dnsname
```

Dockerfile needs to be adjusted if app should get more memory

1. _VPS:_ `mkdir ~/bgs` Make sure the folder `~/bgs` is present
2. _Local:_ Build jar and push it to VPS: `scp ./build/libs/bgs*.jar toxxvps:~/bgs/app.jar` (in this case, a ssh config toxxvps defining the ssh connection exists. If not present use regular `root@ip:/foldername`)
3. _Local:_ Push the deployment folder to VPS `scp ./deployment/* toxxvps:~/bgs/`, including the .env file.
4. _VPS_: Run `docker compose up` in `~/bgs` folder