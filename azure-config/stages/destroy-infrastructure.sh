#!/bin/bash

function destroyAll() {
    az group delete --name "$1" --yes --no-wait
    az group delete --name "$2" --yes --no-wait
    az group delete --name "$3" --yes --no-wait
}

function destroyRg() {
  az group delete --name "$1" --yes --no-wait
}

if [[ "${DESTROY_ALL:-false}" == "true" ]]; then
  destroyAll "rg-team18" "rg-team18-deploy" "NetworkWatcherRG"
elif [[ "${DESTROY_DEPLOY:-false}" == "true" ]]; then
  destroyRg "rg-team18-deploy"
elif [[ "${DESTROY_RUNNER:-false}" == "true" ]]; then
  destroyRg "rg-team18"
else
  echo "Error: No DESTROY_* variable set."
  exit 1
fi