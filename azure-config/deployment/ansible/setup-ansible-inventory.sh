#!/bin/bash

# This file is executed in the pipeline
#It re-sets the inventory.ini file with the correct Deployment VM IP address so that ansible can log in and set up the project correctly
rm inventory.ini

VM_IP="$(az vm list-ip-addresses --resource-group rg-team18-deploy --name vm-team18-deploy --query "[].virtualMachine.network.publicIpAddresses[].ipAddress" -o tsv)"
echo "INISDE SetupINV: $VM_IP"
# Write to inventory file
cat <<SetupINV >> inventory.ini
[web]
$VM_IP ansible_user=team18-deploy ansible_ssh_private_key_file=/root/.ssh/azure
SetupINV

ssh-keyscan -H "$VM_IP" >> /root/.ssh/known_hosts 2>/dev/null