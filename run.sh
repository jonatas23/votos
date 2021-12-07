#!/bin/bash
echo "Create data..."
mkdir -p ./data

echo "Create network..."
docker network create back-network

echo "Service..."
docker-compose up --build --force-recreate -d

echo "Run spring..."
mvn spring-boot:run
