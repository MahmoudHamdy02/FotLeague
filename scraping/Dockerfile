FROM python:3.10-alpine

WORKDIR /app

RUN pip install requests beautifulsoup4 pandas lxml

COPY fbref.py .

CMD ["python", "fbref.py"]