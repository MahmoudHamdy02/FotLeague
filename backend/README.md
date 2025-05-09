# Backend 

The backend is built using `Express`, `Typescript` and the `pg` client.

## Layers
- **Routes**: Specify available API routes and their HTTP methods
- **Middleware**: Enforces authentication for protected routes
- **Controllers**: Contain business logic and implementations
- **Services**: Handle database querying and SQL commands

### Other Components
- **`app.ts`**: Initializes libraries, database connection and authentication
- **Strategies**: `Passport` implementations for authentication methods
- **Tests**: Jest integration tests 


## Tests

Tests are run on the test database container when running the development (default) compose file.

To run the `jest` tests, open `sh` inside the container and run the test command:

```
docker exec -it fotleague-backend-1 sh
npm run test
```

The test files run sequentially. 

To only run a specific file:

```
npm run test -- <file>.test.ts
```

## Database

The database is initialised on startup using the `init.sql` file.

Postgres schema:
![FotLeague](../images/FotLeague.drawio.png)