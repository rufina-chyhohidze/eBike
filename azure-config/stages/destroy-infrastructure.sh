#!/bin/bash

function destroyAll() {
  for rg in $(az group list --query "[].name" -o tsv); do
    az group delete --name "$rg" --yes --no-wait
  done
}

function destroyRg() {
  az group delete --name "$1" --yes --no-wait
}

if [[ "${DESTROY_ALL:-false}" == "true" ]]; then
  destroyAll
elif [[ "${DESTROY_DEPLOY:-false}" == "true" ]]; then
  destroyRg "rg-team18-deploy"
elif [[ "${DESTROY_RUNNER:-false}" == "true" ]]; then
  destroyRg "rg-team18"
else
  echo "❌ Error: No DESTROY_* variable set."
  exit 1
fi