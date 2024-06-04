from datetime import datetime
import json
from time import sleep
import requests

# FotMob website
url = "https://www.fotmob.com/api/leagues?id=47"
backend = "http://localhost:3001"
# Wait until backend starts up
# sleep(30)

# Health check
print("Pinging backend")
print(requests.get(f"{backend}/").text)


# Infinite loop that runs every 5 minutes
while True:
    currentSeason = requests.get(f"{backend}/matches/season/current").json()["currentSeason"]

    matches = requests.get(f"{backend}/matches/{currentSeason}").json()
    
    data = {}
    with open('data.json') as f:
        data = json.load(f)

    updatedMatches = data["matches"]["allMatches"]

    for updatedMatch in updatedMatches:
        currentMatch = list(filter(lambda match: match['gameweek'] == int(updatedMatch["round"]) and match['home'] == updatedMatch["home"]["name"] and match['away'] == updatedMatch["away"]["name"], matches))
        # compare matches, update status, handle aborted games, calculate score

    break
    sleep(300)
