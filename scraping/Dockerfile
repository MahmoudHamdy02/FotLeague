FROM python:3.10-alpine

WORKDIR /app

RUN pip install requests

COPY scrape.py .

CMD ["python", "scrape.py"]