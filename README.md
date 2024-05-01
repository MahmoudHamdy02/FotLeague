## FotLeague

Football game inspired by Fantasy Premier League, where players predict match scores, earn points and compete in leaderboards!

## Project structure

The project consists of three docker containers, which are organised and run by a docker compose file:
- Backend: ExpressJS 
- Database: Postgres
- Python: Web scraping

## Backend 

The backend is built using `Express`, `Typescript` and the `pg` client

## Database

The database is initialised on startup using the init.sql commands

Postgres schema:
![FotLeague](https://github.com/MahmoudHamdy02/FotLeague/assets/90795679/d5c35595-5f63-49ef-b0e6-be8a72cc5929)

## Python

The python script scrapes [site] for live match results, and sends the result to the express server. 

This repeats every 5 minutes in an infinite loop. 

## Native Android App

Todo

## Website

Todo
