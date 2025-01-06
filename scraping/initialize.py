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
x_fm_req = requests.get('http://46.101.91.154:6006/')
token = x_fm_req.json()

# Health check
print("Pinging backend")
print(requests.get(f"http://localhost:3001/").text)


# Infinite loop that runs every 5 minutes
print(datetime.now(), "Pinging FotMob...")
# Get data from fotmob API
fotmob = requests.get("https://www.fotmob.com/api/leagues?id=47", headers=token)
data = json.loads(fotmob.text)
# Convert to JSON
# js = json.loads(fotmob.text)

season = data["details"]["latestSeason"]

# Select matches
matches = data["matches"]["allMatches"]

matchesJSON = []

# 1 - Upcoming
# 2 - In Progress
# 3 - Played
# 4 - Aborted
for i, match in enumerate(matches):
    status = 1
    if match["status"]["started"] and not match["status"]["finished"]:
        status = 2
    elif match["status"]["started"] and match["status"]["finished"]:
        status = 3
    elif match["status"]["cancelled"]:
        status = 4
    
    matchesJSON.append({
        "gameweek": match["round"],
        "season": int(season.split("/")[1]),
        "home": match["home"]["shortName"],
        "away": match["away"]["shortName"],
        "home_score": 0 if "scoreStr" not in match["status"] else match["status"]["scoreStr"].split("-")[0].strip(),
        "away_score": 0 if "scoreStr" not in match["status"] else match["status"]["scoreStr"].split("-")[1].strip(),
        "datetime": match["status"]["utcTime"],
        "match_status": status
    })


# Send to backend
print(requests.post(f"http://localhost:3001/matches/init", json={"matches": matchesJSON}))
