from datetime import datetime
import json
from time import sleep
import requests

def initialize(matches, season):
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
    print(requests.post(f"http://backend:3000/matches/init", json={"matches": matchesJSON}))
    print("Initialized")

# FotMob website
url = "https://www.fotmob.com/api/leagues?id=47"
backend = "http://backend:3000"
timeout = 60 * 15

fotmob_date_format = '%Y-%m-%dT%H:%M:%SZ'
fotmob_date_format_ms = '%Y-%m-%dT%H:%M:%S.%fZ'
db_date_format = '%Y-%m-%dT%H:%M:%S.%fZ'

# Infinite loop that runs every 5/15 minutes
while True:
    # Get token from fotmob website
    x_fm_req = requests.get('http://46.101.91.154:6006/')
    token = x_fm_req.json()
    print(f"{datetime.now()} Getting FotMob data")
    fotmob = requests.get(url, headers=token)
    data = json.loads(fotmob.text)

    # Ping more frequently if there is an ongoing match
    # TODO: hasOngoingMatch sometimes returns True even though it should be False
    timeout = 60 * 5 if data["matches"]["hasOngoingMatch"] else 60 * 15

    updatedMatches = data["matches"]["allMatches"]
    season = data["details"]["latestSeason"]

    currentSeason = requests.get(f"{backend}/matches/season/current").json()["currentSeason"]
    if currentSeason is None:
        initialize(updatedMatches, season)

    currentSeason = requests.get(f"{backend}/matches/season/current").json()["currentSeason"]
    matches = requests.get(f"{backend}/matches/{currentSeason}").json()

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

    sleep(timeout)
