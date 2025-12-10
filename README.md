# bgs_backend_V2

Backend for [Board Game Search App](https://github.com/krahl-jan/bgsearchapp).

Currently hosted at [bgsearch.toxx.dev](https://bgsearch.toxx.dev/swagger-ui/index.html)

[![Powered by BoardGameGeek](src/main/resources/powered_by_BGG_01_SM.png)](https://boardgamegeek.com)

To run locally, either provide a .env, or use [sops](https://github.com/getsops/sops).

How to run the App locally:
1. execute Gradle bootJar from Plugin or `./gradlew bootJar`
2. execute `docker compose up`

When using sops, the following command can be used:
` env $(sops -d deployment/secrets.yaml | grep "  [A-Z]" | sed 's/: /=/; s/^ *//; s/"//g') docker-compose up --build`

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
