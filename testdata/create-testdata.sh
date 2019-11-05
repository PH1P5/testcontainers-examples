#!/bin/sh

THRESHOLD=10
TIME=0

while [ "$(curl -s -o /dev/null -w ''%{http_code}'' influxdb:8086/ping)" != "204" ]
do
  sleep 1
  TIME=$((TIME+1))
  if [ "$TIME" -ge "$THRESHOLD" ]; then
    echo "Influx db not ready in time."
    exit 1
  fi
done
echo "Influx db is ready."

curl -i -XPOST "http://influxdb:8086/write?db=sensor&precicion=ns" --data-binary @testdata.txt
echo "Testdata was added."