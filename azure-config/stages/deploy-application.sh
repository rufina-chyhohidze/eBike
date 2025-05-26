#!/bin/bash

#########################################################
###       THIS SCRIPT IS EXECUTED IN THE PIPELINE     ###
#########################################################
###                TEAM 18 - INT 4                    ###
#########################################################
 # The purpose of this script is to execute the ansible configuration files
 # to setup the deployment VM and run the new project


cd ./azure-config/deployment/ || exit
bash ./azure-login.sh

cd ./ansible/ || exit
# Creats inventory.ini file with correct Deployment VM IP Address
bash ./setup-ansible-inventory.sh

# Runs ansible playbook config
ansible-playbook -i inventory.ini playbook.yml
