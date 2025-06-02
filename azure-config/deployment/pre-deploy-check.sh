#!/bin/bash

#########################################################
#########################################################
### THIS SCRIPT MUST ONLY BE EXECUTED BY THE PIPELINE ###
#########################################################
#########################################################

#########################################################
##### TEAM 18 - INTEGRATION 4 - ACS 202 - 2024/2025 #####
#########################################################

# In the pipeline, this script runs in the azure runner we set up
# It runs in docker mode using our custom image
# The purpose of the script is to set up the deployment dependecies (Azure Deployment RG, VM & SQL Database with correct firewall rules)

source ../helper.sh

# Log into azure
run_with_progress "Logging into azure" bash -c ./azure-login.sh

# Func to check if the deployment RG exists or not
function deploymentResourceGroupExists() {
  if "$(az group exists --name rg-team18-deploy)" ; then
    return 0
    else return 1
  fi
}

if deploymentResourceGroupExists ; then
    echo "Resource group already exists, skipping setup..."
  else
    # Uses Terraform configuration files to create deployment RG, VM & SQL DB with a firewall rule for the DB that allows connection to the SQL DB only to the Deployment VM
    run_with_progress "Resource group doesn't exist, initializing setup - (This might take around 7 minutes - it creates the deployment RG, VM & SQL DB with correct firewall rules) ." bash -c '
    cd ./terraform/ || exit 1
    tofu init
    tofu apply --auto-approve'
fi

# Update our dns with the deployment VM IP
VM_IP="$(az vm list-ip-addresses --resource-group rg-team18-deploy --name vm-team18-deploy --query "[].virtualMachine.network.publicIpAddresses[].ipAddress" -o tsv)"
curl "https://www.duckdns.org/update?domains=team18-int4.duckdns.org&token=${DUCK_DNS_TOKEN}&ip=${VM_IP}" # env file already sourced in login phase, no need to re-sourc it for duck dns token