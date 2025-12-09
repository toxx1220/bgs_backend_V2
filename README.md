# bgs_backend_V2

Backend for [Board Game Search App](https://github.com/krahl-jan/bgsearchapp).

Currently hosted at [bgsearch.toxx.dev](https://bgsearch.toxx.dev/swagger-ui/index.html)

[![Powered by BoardGameGeek](src/main/resources/powered_by_BGG_01_SM.png)](https://boardgamegeek.com)

Currently due to time constraints a`.env` File is needed at project root. Example Env-File:
```
APP_NAME=bgs-backend
APP_PORT=8080
PG_USER=bgs
PG_PASSWORD=password
PG_PORT=5432
PG_HOST=bgsDbContainer
PG_DATABASE=bgsDb
SPRING_DATASOURCE_URL=jdbc:postgresql://${PG_HOST}:${PG_PORT}/${PG_DATABASE}
SPRING_DATASOURCE_USERNAME=${PG_USER}
SPRING_DATASOURCE_PASSWORD=${PG_PASSWORD}
```

How to run the App locally:
1. execute Gradle bootJar from Plugin or `./gradlew bootJar`
2. execute `docker compose up`

Endpoints:
http://localhost:8080/swagger-ui/index.html

Connection to the Database outside of the Docker Container (using example env file data):
`jdbc:postgresql://localhost:5432/bgs`
Connecting from inside the Docker, host needs to be swapped to `${PG_HOST}`

TODO:
- [x] Use Secret Manager
- [ ] Add Union Operators
- [x] Automate Deployments using watchtower or similar -> On Release
- [x] Add manual trigger for db job
