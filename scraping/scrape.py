from datetime import datetime
import json
from time import sleep
import requests

# FotMob website
url = "https://www.fotmob.com/api/leagues?id=47"
backend = "http://backend:3000"
timeout = 60 * 15

# Wait until backend starts up
sleep(20)

# Health check
print("Pinging backend")
print(requests.get(f"{backend}/").text)

fotmob_date_format = '%Y-%m-%dT%H:%M:%SZ'
fotmob_date_format_ms = '%Y-%m-%dT%H:%M:%S.%fZ'
db_date_format = '%Y-%m-%dT%H:%M:%S.%fZ'

# Infinite loop that runs every 5/15 minutes
while True:
    currentSeason = requests.get(f"{backend}/matches/season/current").json()["currentSeason"]

    matches = requests.get(f"{backend}/matches/{currentSeason}").json()

    # Get token from fotmob website
    print("Getting token")
    x_fm_req = requests.get('http://46.101.91.154:6006/')
    token = x_fm_req.json()
    print("Getting FotMob data")
    fotmob = requests.get(url, headers=token)
    data = json.loads(fotmob.text)

    # Ping more frequently if there is an ongoing match
    timeout = 60 * 5 if data["matches"]["hasOngoingMatch"] else 60 * 15

    updatedMatches = data["matches"]["allMatches"]

    # Get corresponding updated matches for each database match
    for match in matches:
        updatedMatchList = list(filter(lambda updatedMatch: match['gameweek'] == int(updatedMatch["round"]) and match['home'] == updatedMatch["home"]["shortName"] and match['away'] == updatedMatch["away"]["shortName"], updatedMatches))

        # len == 1: Match hasn't been aborted
        if len(updatedMatchList) == 1:
            updatedMatch = updatedMatchList[0]
        # len > 1: Match has been aborted, only use latest entry
        else:
            res = sorted(updatedMatchList, reverse=True, key=lambda x: datetime.strptime(x["status"]["utcTime"], fotmob_date_format))
            updatedMatch = res[0]

        if updatedMatch["status"]["cancelled"]:
            status = 4
        elif updatedMatch["status"]["finished"]:
            status = 3
        elif updatedMatch["status"]["started"]:
            status = 2
        else:
            status = 1

        # Get game score if it has started
        if status == 2 or status == 3 and updatedMatch["status"]["scoreStr"]:
            home_score = int(updatedMatch["status"]["scoreStr"].split("-")[0].strip())
            away_score = int(updatedMatch["status"]["scoreStr"].split("-")[1].strip())
        else:
            home_score = 0
            away_score = 0
        
        # only update if match data has changed
        # convert datetime to compare them
        try: # In rare cases Fotmob returns match times with milliseconds for some reason
            updatedMatchDatetime = datetime.strptime(updatedMatch["status"]["utcTime"], fotmob_date_format)
        except ValueError:
            updatedMatchDatetime = datetime.strptime(updatedMatch["status"]["utcTime"], fotmob_date_format_ms)
        matchDateTime = datetime.strptime(match["datetime"], db_date_format)
        if status != match["match_status"] or home_score != match["home_score"] or updatedMatchDatetime != matchDateTime or away_score != match["away_score"]:
            print("updating id:", match["id"])
            requests.post(f"{backend}/matches/update", json={"matchId": match["id"], "status": status, "homeScore": home_score, "awayScore": away_score, "datetime": updatedMatch["status"]["utcTime"]})
        
    break
    sleep(timeout)
