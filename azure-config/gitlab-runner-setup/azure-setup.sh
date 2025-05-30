#!/bin/bash

#########################################################
#########################################################
###  THIS SCRIPT CREATES THE AZURE GITLAB RUNNER      ###
###      (RG & VM) IN WHICH THE PIPELINE RUNS         ###
#########################################################
#########################################################

#########################################################
##### TEAM 18 - INTEGRATION 4 - ACS 202 - 2024/2025 #####
#########################################################


# Can only run with sudo because otherwise it is unable to setup the gitlab runner
[ "$(id -u)" -eq 0 ] || { echo "Script must be executed as root user, otherwise script to setup_runner.sh won't work." ; exit 1 ; }

# Checks that you are executing the script in the correct directory, because of volume mapping dependencies
if [ ! -f ./terraform/main.tf ] || [ ! -f ./terraform/variables.tf ] || [ ! -f ./terraform/outputs.tf ] ; then
  echo "You must execute this script in the '(ROOT_PROJECT_DIR)/azure-config/gitlab-runner-setup/' directory!" >&2
  exit 1;
fi

# Making sure docker is installed locally (only program dependency needed)
command -v docker >/dev/null 2>&1 || { echo "Docker is not installed. Aborting."; exit 1; }

source ../helper.sh

# Running docker in which the whole set up will be executed, it is a custom docker image we made that contains all the necessary tools (OpenTofu, Azure CLI, ssh, Ansible...)
run_with_progress "Running docker container." bash -c '
docker kill azure_setup
docker run --rm --name azure_setup -dit \
-v "./:/azure-config/gitlab-runner-setup/" \
-v "/root/.ssh/:/root/.ssh/" \
-w "/azure-config/gitlab-runner-setup/terraform/" \
anir333/team18-int4:latest'

# Making sure there is an ssh key locally so that user who created the runner can log into it
run_with_progress "Creating ssh key (in host root)" bash -c '
if [ ! -f /root/.ssh/azure ] ; then
  docker exec azure_setup ssh-keygen -t ed25519 -f /root/.ssh/azure -N ""
fi'


# Logging into azure using service principal credentials (within the container)
run_with_progress "Logging into azure" docker exec azure_setup bash "/azure-config/gitlab-runner-setup/terraform/azure_login.sh"

# Function to check if the runner RG exists
function resourceGroupExists() {
  if "$(docker exec azure_setup az group exists --name rg-team18)" ; then
    return 0
    else return 1
  fi
}

if resourceGroupExists ; then
    echo "Resource group already exists, skipping setup."
    docker kill azure_setup >/dev/null 2>&1
  else
    # Creating RG and VM for gitlab runner
    # It uses the docker container to execute the terraform files inside the ./terraform/ dir, the terraform files are configured to create a resource group and a virtual machine...
    run_with_progress "Resource group doesn't exist, initializing setup, creating runner RG and VM, (this might take around 2 minutes)" bash -c '
    docker exec azure_setup tofu init
    docker exec azure_setup tofu apply --auto-approve
    '

    # Setting up gitlab runner & it's dependencies
    run_with_progress "Setting up runner (VM) as gitlab runner and installing docker, (this might take around 3 minutes)" docker exec azure_setup bash "/azure-config/gitlab-runner-setup/terraform/setup_runner.sh"

    VM_IP=$(docker exec azure_setup az vm list-ip-addresses --resource-group rg-team18 --name vm-team18 --query "[].virtualMachine.network.publicIpAddresses[].ipAddress" -o tsv)
    echo "You can now access the gitlab runner VM using the .ssh/azure private key of your ROOT user at the following IP address: $VM_IP"
    echo "Command to log into Runner VM:"
    echo "sudo ssh -i /root/.ssh/azure team18@$VM_IP"
    docker kill azure_setup >/dev/null 2>&1
fi