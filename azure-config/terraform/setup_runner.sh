#!/bin/bash

echo "Getting VM public ip address..."
VM_IP="$(az vm list-ip-addresses --resource-group rg-team18 --name vm-team18 --query "[].virtualMachine.network.publicIpAddresses[].ipAddress" -o tsv)"
echo "VM public ip address: $VM_IP"

# Add vm ip to known hosts to be able to ssh into it without prompts
echo "Adding VM IP to known hosts..."
ssh-keyscan -H "$VM_IP" >> ~/.ssh/known_hosts
echo "VM IP added to known hosts"

echo "Initiating connection with VM to setup runner..."
ssh -i ~/.ssh/azure team18@"$VM_IP" << 'SetupInput'
  echo "Installing docker in VM (runner)..."

  sudo dnf install -y dnf-plugins-core

  sudo dnf config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo

  sudo dnf install -y docker-ce docker-ce-cli containerd.io

  sudo systemctl enable --now docker && sudo usermod -aG docker $USER

  echo "Added docker to group $USER"

  echo "Docker installed."

  echo "--------------------"

  echo "Setting up VM as gitlab runner..."

  # Install dependencies
  sudo dnf install -y curl

  # Add GitLab Runner repo and install
  curl -L https://packages.gitlab.com/install/repositories/runner/gitlab-runner/script.rpm.sh | sudo bash
  sudo dnf install -y gitlab-runner

  # Start and enable the service
  sudo usermod -aG docker gitlab-runner
  sudo systemctl enable --now gitlab-runner
  sudo systemctl start gitlab-runner

  # Register the runner
  sudo gitlab-runner register --non-interactive \
    --url "https://gitlab.com" \
    --registration-token "glrt-rfSqO6W5DJ5ItOpsN-OMlm86MQpwOjEzcjA5NQp0OjMKdTpiNGRiaBg.01.1j08f83qv" \
    --executor "shell" \
    --description "team18-vm" \
    --tag-list "azure,setup" \
    --run-untagged="true" \
    --locked="false"

  # Start and enable the service
  sudo systemctl restart gitlab-runner

  sudo touch /var/log/cd-gitlab-pipeline.log
  sudo chmod 777 /var/log/cd-gitlab-pipeline.log
  echo "alias cl='clear'" >> ~/.bashrc

  echo "Restarting virtual machine..."

  sudo reboot
SetupInput