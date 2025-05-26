#!/bin/bash

#########################################################
###       THIS SCRIPT IS EXECUTED IN THE PIPELINE     ###
#########################################################
###                TEAM 18 - INT 4                    ###
#########################################################
 # The purpose of this script is to execute the ansible configuration files
 # to setup the deployment VM and run the new project

echo "============= Deployment Stage =================="

echo "Logging into azure..."
cd ./azure-config/deployment/ || exit
bash ./azure-login.sh
echo "Logged into azure."

cat /root/.ssh/azure


echo "Setting up ansible inventory..."
cd ./ansible/ || exit
# Creats inventory.ini file with correct Deployment VM IP Address
bash ./setup-ansible-inventory.sh
echo "Finished setting up ansible inventory."

# Runs ansible playbook config
ansible-playbook -i inventory playbook.yml || exit 1

echo "============= Finished Deployment Stage =================="