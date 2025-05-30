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


echo "Setting up ansible inventory..."
cd ./ansible/ || exit
# Creats inventory.ini file with correct Deployment VM IP Address
bash ./setup-ansible-inventory.sh
echo "Finished setting up ansible inventory."

# Runs ansible playbook config (we export subdomain var from gitlab variables so that ansible exports it to the deployment vm)
ansible-playbook -i inventory.ini playbook.yml --extra-vars "proxy_host=$SUB_DOMAIN" || exit 1

echo "============= Finished Deployment Stage =================="