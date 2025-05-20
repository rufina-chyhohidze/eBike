#!/bin/bash

VM_IP="$(az vm list-ip-addresses --resource-group rg-team18-deploy --name vm-team18-deploy --query "[].virtualMachine.network.publicIpAddresses[].ipAddress" -o tsv)"

# Add vm ip to known hosts to be able to ssh into it without prompts
echo "Adding VM IP to known hosts..."
ssh-keyscan -H "$VM_IP" >> /root/.ssh/known_hosts
echo "VM IP added to known hosts"

echo "Initiating connection with VM to setup runner..."
ssh -i /root/.ssh/azure team18-deploy@"$VM_IP" << 'SetupInput'
  echo "alias cl='clear'" >> ~/.bashrc
  echo "export COMPOSE_BAKE=true" >> ~/.bashrc
  source ~/.bashrc
SetupInput

#  # Installing dependencies for ansible to communciate with docker
#  sudo dnf install -y python3-pip
#  sudo pip3 install docker
#  sudo pip3 install docker-compose
