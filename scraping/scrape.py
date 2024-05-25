from datetime import datetime
import json
from time import sleep
import requests

# FotMob website
url = "https://www.fotmob.com/api/leagues?id=47"

# Wait until backend starts up
sleep(20)

# Health check
print("Pinging backend")
print(requests.get(f"http://backend:3000/").text)


# Infinite loop that runs every 5 minutes
# while True:    
#     sleep(300)
