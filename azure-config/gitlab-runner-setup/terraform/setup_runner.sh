#!/bin/bash

#########################################################
#########################################################
### THIS SCRIPT MUST ONLY BE EXECUTED BY THE PIPELINE ###
#########################################################
#########################################################

#########################################################
##### TEAM 18 - INTEGRATION 4 - ACS 202 - 2024/2025 #####
#########################################################

# Getting VM public ip address
VM_IP="$(az vm list-ip-addresses --resource-group rg-team18 --name vm-team18 --query "[].virtualMachine.network.publicIpAddresses[].ipAddress" -o tsv)"

# Add vm ip to known hosts to be able to ssh into it without prompts
ssh-keyscan -H "$VM_IP" >> /root/.ssh/known_hosts

# Adding key to runner .ssh keys so that when creating the deploy VM form runner
# It is necessary to add both the public and the private key to the runner because:
  # 1. The public key is used in the runner to create the deployment virtual machine with, so that the user who create the runner can also log/ssh into the deployment virtual machine using the same key he uses to log into the runner (keeping it simple)
  # 2. In the deploy stage, ansible requires the private key to log into the deployment VM from the Runner in order to setup the deployment vm and run the project
scp -i /root/.ssh/azure /root/.ssh/azure.pub team18@"$VM_IP":/home/team18/.ssh/
scp -i /root/.ssh/azure /root/.ssh/azure team18@"$VM_IP":/home/team18/.ssh/

# Initiating connection with VM to setup runner...
ssh -i ~/.ssh/azure team18@"$VM_IP" << 'SetupInput'

  # Installing docker in VM (runner)
  sudo dnf install -y dnf-plugins-core
  sudo dnf config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
  sudo dnf install -y docker-ce docker-ce-cli containerd.io
  sudo systemctl enable --now docker && sudo usermod -aG docker $USER

  # Setting up VM as gitlab runner

  # Install dependencies
  sudo dnf install -y curl
  # Add GitLab Runner repo and install
  curl -L https://packages.gitlab.com/install/repositories/runner/gitlab-runner/script.rpm.sh | sudo bash
  sudo dnf install -y gitlab-runner
  # Start and enable the service
  sudo usermod -aG docker gitlab-runner
  sudo systemctl enable --now gitlab-runner
  sudo systemctl start gitlab-runner

  # Registering the runner (as shell runner for some of the stages)
  sudo gitlab-runner register --non-interactive \
    --url "https://gitlab.com" \
    --registration-token "glrt-rfSqO6W5DJ5ItOpsN-OMlm86MQpwOjEzcjA5NQp0OjMKdTpiNGRiaBg.01.1j08f83qv" \
    --executor "shell" \
    --description "team18-vm" \
    --tag-list "azure,shell" \
    --run-untagged="true" \
    --locked="false"

  # Registering the runner as docker runner for some of the stages that run on images specified on the gitlab-ci
  sudo gitlab-runner register --non-interactive \
    --url "https://gitlab.com" \
    --registration-token "glrt-K-kTc8wgUv-g4LlfhfJLaG86MQpwOjEzcjA5NQp0OjMKdTpiNGRiaBg.01.1j0bi57a2" \
    --executor "docker" \
    --description "team18-vm-docker" \
    --docker-image "alpine:latest" \
    --tag-list "azure,docker" \
    --run-untagged="false" \
    --locked="false"

  # Need to add a volume mount for ssh keys for the pre-deployment stage, so that it uses the public key of the host user that created the runner in the first place to create the virtual machine for deployment
  sudo sed -i '/^\s*volumes = \[/ s/\]/, "\/home\/team18\/.ssh:\/root\/.ssh"]/' /etc/gitlab-runner/config.toml

  # Start and enable the service
  sudo systemctl restart gitlab-runner

  sudo touch /var/log/cd-gitlab-pipeline.log
  sudo chmod 777 /var/log/cd-gitlab-pipeline.log
  echo "alias cl='clear'" >> ~/.bashrc
  echo "export COMPOSE_BAKE=true" >> ~/.bashrc
  source ~/.bashrc

  # Restarting virtual machine to correctly apply changes
  sudo reboot
SetupInput