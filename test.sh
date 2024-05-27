docker compose run db_test -d
docker compose run backend npm run test
docker compose stop db_test