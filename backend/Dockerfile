FROM node:18-alpine

RUN apk --no-cache add curl

WORKDIR /app

COPY package*.json .

RUN npm install

COPY . .

RUN npx tsc

CMD [ "npm", "run", "start" ]