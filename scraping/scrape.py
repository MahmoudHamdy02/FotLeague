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

fotmob_date_format = '%Y-%m-%dT%H:%M:%SZ'
db_date_format = '%Y-%m-%dT%H:%M:%S.%fZ'

# Infinite loop that runs every 5 minutes
while True:
    currentSeason = requests.get(f"{backend}/matches/season/current").json()["currentSeason"]

    matches = requests.get(f"{backend}/matches/{currentSeason}").json()

    data = {}
    with open('data.json') as f:
        data = json.load(f)

    updatedMatches = data["matches"]["allMatches"]

    # Get corresponding updated matches for each database match
    for match in matches:
        updatedMatchList = list(filter(lambda updatedMatch: match['gameweek'] == int(updatedMatch["round"]) and match['home'] == updatedMatch["home"]["name"] and match['away'] == updatedMatch["away"]["name"], updatedMatches))

        # len == 1: Match hasn't been aborted
        if len(updatedMatchList) == 1:
            updatedMatch = updatedMatchList[0]
        # len > 1: Match has been aborted, only use latest entry
        else:
            res = sorted(updatedMatchList, reverse=True, key=lambda x: datetime.strptime(x["status"]["utcTime"], fotmob_date_format))
        
        if updatedMatch["status"]["cancelled"]:
            status = 4
        elif updatedMatch["status"]["finished"]:
            status = 3
        elif updatedMatch["status"]["started"]:
            status = 2
        else:
            status = 1

        # Get game score if it has started
        if updatedMatch["status"]["scoreStr"]:
            home_score = int(updatedMatch["status"]["scoreStr"].split("-")[0].strip())
            away_score = int(updatedMatch["status"]["scoreStr"].split("-")[1].strip())
        else:
            home_score = None
            away_score = None
        
        # only update if match data has changed
        # convert datetime to compare them
        updatedMatchDatetime = datetime.strptime(updatedMatch["status"]["utcTime"], fotmob_date_format)
        matchDateTime = datetime.strptime(match["datetime"], db_date_format)
        if status != match["match_status"] or home_score != match["home_score"] or updatedMatchDatetime != matchDateTime or away_score != match["away_score"]:
            requests.post(f"{backend}/matches/update", json={"matchId": match["id"], "status": status, "homeScore": home_score, "awayScore": away_score, "datetime": updatedMatch["status"]["utcTime"]})
        
    break
    sleep(300)
