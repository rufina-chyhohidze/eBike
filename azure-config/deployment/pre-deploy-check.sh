#!/bin/bash

#########################################################
#########################################################
### THIS SCRIPT MUST ONLY BE EXECUTED BY THE PIPELINE ###
#########################################################
#########################################################

#########################################################
##### TEAM 18 - INTEGRATION 4 - ACS 202 - 2024/2025 #####
#########################################################

chmod +x ./azure-login.sh
./azure-login.sh

cd ./terraform/ || exit 1

function deploymentResourceGroupExists() {
  if "$(az group exists --name rg-team18-deploy)" ; then
    return 0
    else return 1
  fi
}

if deploymentResourceGroupExists ; then
    echo "Resource group already exists, skipping setup..."
  else
    echo "Resource group doesn't exist, initializing setup..."
    ls /root/.ssh/
    tofu init
    tofu apply --auto-approve
fi
