FROM python:3.10-alpine

WORKDIR /app

RUN pip install requests

COPY fbref.py .

CMD ["python", "fbref.py"]