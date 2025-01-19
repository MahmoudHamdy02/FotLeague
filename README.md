## FotLeague

Football game inspired by Fantasy Premier League, where players predict match scores, earn points and compete in leaderboards!

## Project Structure

The project consists of several modules:
- Backend & Database
- Web scraping
- Native Android App

The backend services are containerized and managed through docker compose. 

## Running

To run this project locally, start by running the docker containers, which will download all the needed dependencies and start all services:

```docker compose up -d && docker compose logs --follow```

Stop the containers by pressing `Ctrl-C` to stop following the logs, then run:

```docker compose down```


### Further documentation for each module can be found in its respective folder

## Container Archeticture

### 1. Development:

`docker-compose.yaml` consists of 4 containers:
1. Backend
2. Database
3. Scraping
4. Test database

The real database has a volume specified to persist the data, while the test database is an unseeded instance that is used in jest tests.

The backend and scraping folders are locally mounted to update the files inside the containers while developing.

### 2. Production:

`docker-compose.prod.yaml` consists of 4 containers:
1. Backend
2. Database
3. Scraping
4. Watchtower

The production variant uses a `watchtower` container to automatically update the running images whenever changes are pushed to the Dockerhub repository.
