#!/bin/bash

# Create the resource group
echo "Creating the resource group..."
az group create --name rg-team18-integration4 --location westeurope
echo "Resource group created"

echo "Setting up ssh keys"
mkdir -p ~/.ssh
echo "$PRIVATE_KEY" | base64 -d > ~/.ssh/azure
chmod 600 ~/.ssh/azure

echo "$PUBLIC_KEY" > ~/.ssh/azure.pub
echo "Finished setting up ssh keys"

echo "Creating Virtual Machine"
az vm create --name vm-team18-integration4 \
--resource-group rg-team18-integration4 \
--size Standard_B2s \
--accept-term \
--image 'almalinux:almalinux-x86_64:9-gen1:latest' \
--admin-username team18 \
--public-ip-sku Standard \
--public-ip-address pip-vm-team18-integration4 \
--storage-sku Standard_LRS \
--os-disk-name osdisk-vm-team18-integration4 \
--accelerated-networking false \
--data-disk-delete-option delete \
--os-disk-delete-option delete \
--ssh-key-value ~/.ssh/azure.pub \
--nsg nsg-team18-integration4 \
--vnet-name vnet-team18-integration4 \
--tags 'ContactEmail=team18integration4@gmail.com'
echo "Finished creating Virtual Machine"

VM_IP="$(az vm list-ip-addresses --resource-group rg-team18-integration4 --name vm-team18-integration4 --query "[].virtualMachine.network.publicIpAddresses[].ipAddress" -o tsv)"
echo "IP address of vm created: $VM_IP"

echo "Uploading app to the VM..."
# Add the VM to known hosts
ssh-keyscan -H "$VM_IP" >> ~/.ssh/known_hosts

scp -i ~/.ssh/azure -r "$CI_PROJECT_DIR/build/libs/" "team18@$VM_IP":/home/team18/libs
scp -i ~/.ssh/azure -r "$CI_PROJECT_DIR/docker-compose.yml" "team18@$VM_IP":/home/team18/libs


echo "Opening port"
az vm open-port --port 8080 --resource-group rg-team18-integration4 --name vm-team18-integration4 >/dev/null
echo "Port opened"


ssh -i ~/.ssh/azure "team18@$VM_IP" << 'EOF'
    echo "Entered VM"
    cd /home/team18/libs

    echo "Installing docker"
    sudo dnf -y install dnf-utils
    sudo dnf -y config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
    sudo dnf -y install docker-ce docker-ce-cli containerd.io

    sudo systemctl start docker
    sudo systemctl enable docker

    sudo usermod -aG docker $USER
    newgrp docker
    docker compose up -d

    export WORKBENCH_API_KEY=b9f55fa7-279f-4314-b781-e319b385c463
    export MAIL_USERNAME=team18int4@gmail.com
    export MAIL_PASSWORD=rfxchlbkjwoazmjc

    sudo dnf install -y java-21-openjdk

    java -jar Integration4-0.0.1-SNAPSHOT.jar

    echo "App running"
EOF
echo "exited vm"

```













```

#read -r -p "Do you want to destroy all resources? (yes to proceed, no to cancel): " response
#
#if [[ "$response" == "yes" || "$response" == "y" ]]; then
#  echo "Deleting resource groups... Please wait."
#  az group delete --yes --name rg-team18-integration4
#  az group delete --yes --name NetworkWatcherRG
#  echo "Resource groups have been deleted."
#else
#  echo "No resources were deleted. All created resources remain intact."
#fi