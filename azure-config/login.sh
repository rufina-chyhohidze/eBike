#!/bin/bash

# Path to Azure credentials JSON file (GitLab CI mounts it as a temp file)
CREDENTIALS_FILE="$AZURE_LOGIN_CREDENTIALS"

# Perform Azure login using service principal
az login --service-principal \
  --username "$(jq -r .clientId "$CREDENTIALS_FILE")" \
  --password "$(jq -r .clientSecret "$CREDENTIALS_FILE")" \
  --tenant "$(jq -r .tenantId "$CREDENTIALS_FILE")"

# Confirm login
az account show && echo "✅ Azure login successful" && exit 0

echo "Error logging in" ; exit 1 ;