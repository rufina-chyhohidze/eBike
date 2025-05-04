#!/bin/bash

# Variables with values needed to login to azure account which are stored (masked and hidden) in gitlab ci/cd variables
USERNAME=$CLIENT_ID
PASSWORD=$CLIENT_SECRET
TENANT=$TENANT_ID

# Perform Azure login using service principal
az login --service-principal \
  --username "$USERNAME" \
  --password "$PASSWORD" \
  --tenant "$TENANT"

# Confirm login
az account show && echo "✅ Azure login successful"# && exit 0

#echo "Error logging in" ; exit 1 ;

# Confirm login and check if it was successful
if az account show; then
  echo "✅ Azure login successful"
  exit 0
else
  echo "❌ Error logging in to Azure"
  exit 1
fi
