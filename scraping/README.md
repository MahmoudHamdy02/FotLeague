## Web Scraping

This Python module regularly scrapes the FotMob website to get live match results, and send the updated data to the backend server.

FBRef and football-data.org were other possible sources for the football data, however FotMob was the simplest and didn't require a lot of data processing.

The `scrape.py` file consists of two main parts:

### 1. `initialize()`

This function only runs once when the database is first initialized. It populates the matches table with all matches of the current season, along with their current status (Played, Upcoming, etc.).

### 2. Main loop

The scraping code fetches the data from the FotMob API, and updates the matches in the database. Only the matches that were updated are sent to the backend, to avoid unnecessary updates.

The loop runs every 5 minutes, which slows down to every 15 minutes if no games are currently being played.

---

## Dependencies
- Requests
