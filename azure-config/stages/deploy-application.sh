#!/bin/bash

cd ./azure-config/deployment/
bash ./azure-login.sh
cd ./ansible/
bash ./setup-ansible-inventory.sh
ansible-playbook -i inventory.ini playbook.yml
