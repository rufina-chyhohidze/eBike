#!/bin/bash

rm inventory.ini

VM_IP="$(az vm list-ip-addresses --resource-group rg-team18-deploy --name vm-team18-deploy --query "[].virtualMachine.network.publicIpAddresses[].ipAddress" -o tsv)"

# Write to inventory file
cat <<SetupINV >> inventory.ini
[web]
$VM_IP ansible_user=team18-deploy ansible_ssh_private_key_file=/root/.ssh/azure
SetupINV