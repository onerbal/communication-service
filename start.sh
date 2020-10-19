#!/usr/bin/env bash

function note() {
  local GREEN NC
  GREEN='\033[0;32m'
  NC='\033[0m' # No Color
  printf "\n${GREEN}$@  ${NC}\n" >&2
}

set -e

if [[ $@ == *"build-docker"* ]]; then
  note "Building projects..."
  mvn clean install -DskipTests

  note "Starting docker compose..."
  docker-compose up --build
fi
