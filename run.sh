#!/bin/sh

echo "BASE_PATH=$1" > ".env"
docker-compose up