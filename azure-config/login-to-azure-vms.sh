#!/bin/bash

#VM_IP=$(docker exec azure_setup az vm list-ip-addresses --resource-group rg-team18-deploy --name vm-team18-deploy --query "[].virtualMachine.network.publicIpAddresses[].ipAddress" -o tsv)
#    echo "If you ran the script to se up the runner, you can now access the deployment virtual machine using the same key you use to log into the runner vm, (.ssh/azure) private key of your ROOT user at the following IP address: $VM_IP"
#    echo "If you did not execute the script to create the runner, you will not be able to access the deployment virtual machine, only the host machine that created the runner will have access to both the runner VM and the deployment VM"
#    echo "Command to log into VM:"
#    echo "sudo ssh -i /root/.ssh/azure team18@$VM_IP"