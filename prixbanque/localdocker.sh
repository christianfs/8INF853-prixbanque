#!/bin/bash
mvn clean compile jib:build
docker compose up -d
