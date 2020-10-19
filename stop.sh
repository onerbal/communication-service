#!/usr/bin/env bash

function note() {
  local GREEN NC
  GREEN='\033[0;32m'
  NC='\033[0m' # No Color
  printf "\n${GREEN}$@  ${NC}\n" >&2
}

set -e

if [[ $@ == *"stop-docker"* ]]; then
  note "Stopping docker compose..."
  docker-compose down
fi
