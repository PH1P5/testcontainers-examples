FROM node:lts-alpine

COPY testdata.txt /testdata.txt
COPY create-testdata.sh /create-testdata.sh

RUN apk update && apk add curl

CMD "/bin/sh" "/create-testdata.sh"
