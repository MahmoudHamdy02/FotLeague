from datetime import datetime
from time import sleep
import requests

# Wait until backend starts up
sleep(30)

while True:
    print(datetime.now(), "pinging backend")
    requests.get("http://backend:3000")
    sleep(5)
