#!/bin/bash

mkdir -p ~/.ssh
ssh-keygen -t ed25519 -f ~/.ssh/azure -C "team18" -N "" # Generate key with no passphrase

# Save the public key for attaching to Azure VM
export SSH_PUBLIC_KEY=$(cat ~/.ssh/azure.pub)
echo "$SSH_PUBLIC_KEY" # Optionally, you can output the public key for debugging purposes (remove in production)

# Optionally, echo the public key for debugging purposes
echo "Generated SSH Public Key: $SSH_PUBLIC_KEY"

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
--ssh-key-value "$SSH_PUBLIC_KEY" \
--nsg nsg-team18-integration4 \
--vnet-name vnet-team18-integration4 \
--tags 'ContactEmail=team18integration4@gmail.com'
