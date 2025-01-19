## Backend 

The backend is built using `Express`, `Typescript` and the `pg` client

To run the backend tests, open `sh` inside the container and run the test command:

```
docker exec -it fotleague-backend-1 sh
npm run test
```

## Database

The database is initialised on startup using the init.sql commands

Postgres schema:
![FotLeague](../images/FotLeague.drawio.png)