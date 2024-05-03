from datetime import datetime
from time import sleep
import requests
import pandas as pd
import os

# FBRef website
url = "https://fbref.com/en/comps/9/schedule/Premier-League-Scores-and-Fixtures"

# Wait until backend starts up
sleep(30)

# Health check
print("Pinging backend")
print(requests.get(f"http://backend:3000/").text)


# Infitite loop that runs every 5 minutes
while True:
    break
    print(datetime.now(), "Pinging FBRef...")
    # Detect and read table from website
    tables = pd.read_html(url)
    # Dataframe variable
    df = tables[0]
    # Split score for easier processing
    df[["Home Score", "Away Score"]] = df["Score"].str.split("–", n=1, expand=True)
    # Drop irrelevant columns
    df = df[["Wk", "Day", "Date", "Time", "Home", "Away", "Match Report", "Home Score", "Away Score"]]
    # Drop empty rows used for styling
    df = df[df["Wk"].notna()]
    # Convert to JSON
    data = df.to_json(orient="values")
    # Send to backend
    requests.post(f"http://backend:3000/fbref/", json={"table": data})
    sleep(300)
