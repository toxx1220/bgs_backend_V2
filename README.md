# bgs_backend_V2

`.env` File needed at project root. Example Env-File:
```
APP_NAME=bgs-backend
APP_PORT=8080
PG_USER=bgs
PG_PASSWORD=password
PG_PORT=5432
PG_HOST=bgsDb
PG_DATABASE=bgs
SPRING_DATASOURCE_URL=jdbc:postgresql://${PG_HOST}:${PG_PORT}/${PG_DATABASE}
SPRING_DATASOURCE_USERNAME=${PG_USER}
SPRING_DATASOURCE_PASSWORD=${PG_PASSWORD}
```

How to run the App:
1. execute Gradle bootJar from Plugin or `./gradlew bootJar`
2. execute `docker compose up`

Endpoints:
http://localhost:8080/swagger-ui/index.html