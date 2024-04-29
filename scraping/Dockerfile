FROM python:3.10

WORKDIR /app

RUN pip install requests

ADD fbref.py .

CMD [ "python", "fbref.py" ]