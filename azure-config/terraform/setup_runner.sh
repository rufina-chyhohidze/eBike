VM_IP="$(az vm list-ip-addresses --resource-group rg-team18 --name vm-team18 --query "[].virtualMachine.network.publicIpAddresses[].ipAddress" -o tsv)"

# Add vm ip to known hosts to be able to ssh into it without prompts
ssh-keyscan -H "$VM_IP" >> ~/.ssh/known_hosts

ssh -i ~/.ssh/azure team18@"$VM_IP" << 'SetupInput'
  sudo dnf install -y dnf-plugins-core

  sudo dnf config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo

  sudo dnf install -y docker-ce docker-ce-cli containerd.io

  sudo systemctl enable --now docker && sudo usermod -aG docker $USER

  echo "Added docker to group $USER"

  sudo reboot
SetupInput