#!/bin/bash

source ./helper.sh
echo "This script is made to easily get the Runner VM and the Deployment VM IP Adresses"

# Making sure docker is installed locally (only program dependency needed)
command -v docker >/dev/null 2>&1 || { echo "Docker is not installed. Aborting."; exit 1; }

run_with_progress "Running docker container." bash -c '
docker kill azure_setup
docker run --rm --name azure_setup -dit \
-v "./:/azure-config/" \
-w "/azure-config/" \
anir333/team18-int4:latest'

# Logging into azure using service principal credentials (within the container)
run_with_progress "Logging into azure" docker exec azure_setup bash "/azure-config/gitlab-runner-setup/terraform/azure_login.sh"

RUNNER_VM_IP="$(docker exec azure_setup az vm list-ip-addresses --resource-group rg-team18 --name vm-team18 --query "[].virtualMachine.network.publicIpAddresses[].ipAddress" -o tsv)"
DEPLOYMENT_VM_IP="$(docker exec azure_setup az vm list-ip-addresses --resource-group rg-team18-deploy --name vm-team18-deploy --query "[].virtualMachine.network.publicIpAddresses[].ipAddress" -o tsv)"

if [ -z "$RUNNER_VM_IP" ]  ; then
  echo ""
  echo "--------------------------------------------------"
  echo "RUNNER VM DOESN'T EXIT, CANNOT GET IP ADDRESS"
  echo "--------------------------------------------------"
  echo ""
else
  echo "--------------------------------------------------"
  echo ""
  echo "Runner VM IP: $RUNNER_VM_IP"
  echo "If you created the runner, you can log into the runner VM doing: "
  echo "sudo ssh -i /root/.ssh/azure team18@$RUNNER_VM_IP"
  echo "--------------------------------------------------"
  echo ""
fi

if [ -z "$DEPLOYMENT_VM_IP" ] ; then
  echo ""
  echo "--------------------------------------------------"
  echo "DEPLOYMENT VM DOESN'T EXIST, CANNOT GET IP ADDRESS"
  echo "--------------------------------------------------"
  echo ""
else
  echo "--------------------------------------------------"
  echo ""
  echo "Deployment VM IP: $DEPLOYMENT_VM_IP"
  echo "If you created the runner, you can log into the deployment VM doing: "
  echo "sudo ssh -i /root/.ssh/azure team18-deploy@$DEPLOYMENT_VM_IP"
  echo "--------------------------------------------------"
  echo ""
fi

# clean up
docker kill azure_setup >/dev/null 2>&1 || true