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
    cd ./terraform/ || exit 1
    tofu init
    tofu apply --auto-approve
    cd ../
    chmod +x ./setup-deploy-vm.sh
    ./setup-deploy-vm.sh
fi

VM_IP="$(az vm list-ip-addresses --resource-group rg-team18-deploy --name vm-team18-deploy --query "[].virtualMachine.network.publicIpAddresses[].ipAddress" -o tsv)"

curl "https://www.duckdns.org/update?domains=team18-int4.duckdns.org&token=b4bb4460-f9d0-42fc-a063-e1dbadd11014&ip=${VM_IP}"