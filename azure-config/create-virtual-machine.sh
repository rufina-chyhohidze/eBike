#!/bin/bash

# Create the resource group
echo "Creating the resource group..."
az group create --name rg-team18-integration4 --location westeurope
echo "Resource group created"


mkdir -p ~/.ssh
#ssh-keygen -t ed25519 -f ~/.ssh/azure -C "team18" -N "" # Generate key with no passphrase
echo "$PRIVATE_KEY" | base64 -d > ~/.ssh/azure
echo "$PUBLIC_KEY" > ~/.ssh/azure.pub


# Save the public key for attaching to Azure VM
SSH_PUBLIC_KEY=$(cat ~/.ssh/azure.pub)

# Optionally, echo the public key for debugging purposes
echo "Generated SSH Public Key: $SSH_PUBLIC_KEY"

az vm create --name vm-team18-integration4 \
--resource-group rg-team18-integration4 \
--size Standard_B2s \
--accept-term \
--image 'almalinux:almalinux-x86_64:9-gen1:latest' \
--admin-username team18 \
--zone 2 \
--public-ip-sku Standard \
--public-ip-dns-name "team18-integration4" \
--storage-sku Standard_LRS \
--os-disk-name osdisk-vm-team18-integration4 \
--accelerated-networking false \
--data-disk-delete-option delete \
--os-disk-delete-option delete \
--ssh-key-value "$SSH_PUBLIC_KEY" \
--nsg nsg-team18-integration4 \
--vnet-name vnet-team18-integration4 \
--tags 'ContactEmail=team18integration4@gmail.com'


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