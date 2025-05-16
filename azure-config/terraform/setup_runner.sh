# Exits script immediately if a command fails
set -e

echo "Installing docker in VM (runner)..."

VM_IP="$(az vm list-ip-addresses --resource-group rg-team18 --name vm-team18 --query "[].virtualMachine.network.publicIpAddresses[].ipAddress" -o tsv)"

# Add vm ip to known hosts to be able to ssh into it without prompts
ssh-keyscan -H "$VM_IP" >> ~/.ssh/known_hosts


# sudo dnf upgrade -y

ssh -i ~/.ssh/azure team18@"$VM_IP" << 'SetupInput'
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
  sudo systemctl enable --now gitlab-runner
  sudo systemctl start gitlab-runner

  # Register the runner
  sudo gitlab-runner register --non-interactive \
    --url "https://gitlab.com" \
    --registration-token "glrt-rfSqO6W5DJ5ItOpsN-OMlm86MQpwOjEzcjA5NQp0OjMKdTpiNGRiaBg.01.1j08f83qv" \
    --executor "docker" \
    --docker-image "almalinux:9.5" \
    --description "team18-vm" \
    --tag-list "azure,setup" \
    --run-untagged="true" \
    --locked="false"

  # Start and enable the service
  sudo systemctl restart gitlab-runner

  echo "Restarting virtual machine..."

  sudo reboot
SetupInput