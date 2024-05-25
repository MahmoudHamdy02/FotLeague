from datetime import datetime
import json
from time import sleep
import requests

################################################################
#
# This script is run manually to initialize the database matches
#
################################################################


# FotMob website
url = "https://www.fotmob.com/api/leagues?id=47"


# Health check
print("Pinging backend")
print(requests.get(f"http://localhost:3001/").text)


# Infinite loop that runs every 5 minutes
print(datetime.now(), "Pinging FotMob...")
# Get data from fotmob API
fotmob = requests.get("https://www.fotmob.com/api/leagues?id=47")

# Convert to JSON
js = json.loads(fotmob.text)

season = js["details"]["latestSeason"]

# Select matches
matches = js["matches"]["allMatches"]

matchesJSON = []

# 1 - Upcoming
# 2 - In Progress
# 3 - Played
# 4 - Aborted
for match in matches:
    status = 1
    if match["status"]["started"] and not match["status"]["finished"]:
        status = 2
    elif match["status"]["started"] and match["status"]["finished"]:
        status = 3
    elif match["status"]["cancelled"]:
        status = 4
    matchesJSON.append({
        "gameweek": match["round"],
        "season": season,
        "home": match["home"]["name"],
        "away": match["away"]["name"],
        "home_score": match["status"]["scoreStr"].split("-")[0].strip(),
        "away_score": match["status"]["scoreStr"].split("-")[1].strip(),
        "datetime": match["status"]["utcTime"],
        "match_status": status
    })

print(len(matchesJSON))

# Send to backend
print(requests.post(f"http://localhost:3001/matches/init", json={"matches": matchesJSON}))
#sleep(300)
