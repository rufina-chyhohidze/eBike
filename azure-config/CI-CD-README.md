# CI/CD Pipeline Documentation

## Overview

This documentation describes the CI/CD pipeline implementation for the project. The pipeline automates building, testing, and deploying the application to Azure infrastructure.

The implementation consists of two main components:
1. **GitLab Runner Setup** - Setting up a custom GitLab runner on Azure
2. **Pipeline Execution** - The actual CI/CD pipeline with multiple stages

## Table of Contents

- [Prerequisites](#prerequisites)
- [GitLab Runner Setup](#gitlab-runner-setup)
- [Pipeline Stages](#pipeline-stages)
  - [Build Stage](#build-stage)
  - [Test Stage](#test-stage)
  - [Pre-Deployment Stage](#pre-deployment-stage)
  - [Deployment Stage](#deployment-stage)
  - [Destroy Stages](#destroy-stages)
- [Custom Docker Image](#custom-docker-image)
- [Deployment Process](#deployment-process)
- [Infrastructure Management](#infrastructure-management)
- [Troubleshooting](#troubleshooting)
- [Credits](#credits)

## Prerequisites

- Docker installed on your local machine

## GitLab Runner Setup

Before the pipeline can be executed, a custom GitLab runner must be set up on Azure. This is done by executing the `azure-setup.sh` script located in the `azure-config/gitlab-runner-setup/` directory.

### Setup Process

1. Execute the script with root permissions:
   ```bash
   sudo ./azure-setup.sh
   ```

2. The script performs the following actions:
   - Verifies it's running with the correct permissions and in the right directory
   - Checks that Docker is installed on your local machine
   - Runs a custom Docker container with all necessary dependencies
   - Creates an SSH key if one doesn't exist
   - Logs into Azure using service principal credentials
   - Creates a resource group and virtual machine for the GitLab runner (if they don't already exist)
   - Installs Docker and GitLab Runner on the VM
   - Configures the VM as a GitLab runner in both shell and Docker modes

3. The script uses the SSH key at `/root/.ssh/azure.pub` to create the runner VM, allowing the person who runs the script to access both the runner VM and the deployment VM that will be created later.

4. After successful execution, you can access the GitLab runner VM using:
   ```bash
   sudo ssh -i /root/.ssh/azure team18@<VM_IP>
   ```
- Note: To get the Runner or the Deployment VM IP, execute use the azure-config/get-vms-ips.sh script.
```bash
# Prints Runner and Deployment VM IP Addresses...
bash ./get-vms-ips.sh
```

## Pipeline Stages

The pipeline consists of four main stages and three additional destroy stages:

### Build Stage

- **Purpose**: Builds the application
- **Runs on**: dev and main branches
- **Execution environment**: Custom Azure runner in shell mode
- **Process**: Uses Docker Compose in the project root directory to build the application image

### Test Stage

- **Purpose**: Tests the application
- **Runs on**: dev and main branches
- **Execution environment**: Custom Azure runner in shell mode
- **Process**:
  - Uses Docker Compose to run the tests
  - Uploads test XML artifacts to display test results in GitLab
  - Creates an `.env` file using CI/CD variables for the tests

### Pre-Deployment Stage

- **Purpose**: Checks and sets up Azure resources for deployment
- **Runs on**: main branch only
- **Execution environment**: Custom Azure runner in Docker mode using a custom Docker image
- **Process**:
  - Checks if deployment resources already exist
  - If resources exist, skips setup and continues to the next stage
  - If resources don't exist, uses OpenTofu (Terraform) to create:
    - Deployment resource group
    - Virtual machine
    - Azure SQL Database
    - Firewall rules for HTTP to HTTPS access
    - Firewall rule to restrict SQL Database access to the deployment VM
  - Uses GitLab CI/CD variables for Terraform configuration
  - Logs into Azure using a service principal

### Deployment Stage

- **Purpose**: Deploys the application to the deployment VM
- **Runs on**: main branch only
- **Execution environment**: Custom Azure runner in Docker mode using a custom Docker image
- **Process**:
  - Updates DuckDNS to point to the correct IP address
  - Creates an inventory file with the deployment VM IP address
  - Uses Ansible to connect to the deployment VM and execute the following steps:
    - Installs Docker CE on the deployment VM if not already installed
    - Removes any old application
    - Packages the application into a tarball and transfers it to the deployment VM
    - Stops any existing containers
    - Starts the new application using Docker Compose with Nginx for reverse proxy
    - Makes the application accessible at https://team18-int4.duckdns.org

### Destroy Stages

These stages are manual and only run on the main branch:

1. **destroy-deployment-infrastructure**
   - Deletes only the deployment infrastructure (resource group, VM, SQL DB)

2. **destroy-gitlab-runner-infrastructure**
   - Deletes only the GitLab runner infrastructure (resource group, VM)

3. **destroy-all-infrastructure**
   - Deletes all Azure infrastructure (runner, deployment, NetworkWatcherRG)

All destroy jobs run outside the custom runner to avoid deleting themselves. After destroying the runner, you must re-run `azure-setup.sh` to recreate it before the next pipeline execution.

- **Note:** It may seem sometimes that the destroy job failed, but it still deletes the resource group. 
  - This happens because when running the destroy all infrastructure stage, if one of the resources doesn't exist, the pipeline displays that the job failed, even though it did correctly delete the other ones.

## Custom Docker Image

To simplify the deployment process, a custom Docker image is used for the pre-deployment and deployment stages. This image contains all necessary dependencies:

- OpenTofu (open-source version of Terraform) for setting up Azure resources
- Azure CLI (required by OpenTofu)
- OpenSSH clients for generating SSH keys and logging into VMs
- Ansible for deployment automation

The Dockerfile is located in the `azure-config/` directory. The image is based on AlmaLinux and can be pulled using:

```bash

docker pull anir333/team18-int4:latest
```

Using this custom image allows the pipeline to run consistently without installing dependencies on each VM.

## Deployment Process

When code is pushed to the main branch, the following process occurs:

1. The application is built and tested
2. The pre-deployment stage checks if Azure resources exist and creates them if needed
3. The deployment stage:
   - Creates an Ansible inventory file with the deployment VM IP
   - Installs Docker CE on the deployment VM
   - Removes any old application
   - Packages and transfers the application to the deployment VM
   - Stops existing containers
   - Starts the new application with Docker Compose
   - Configures Nginx as a reverse proxy
   - Updates DuckDNS with the deployment VM IP

The application doesn't check if the new version is different from the currently running one. It assumes that pushing to main means you want to replace the existing application.

## Infrastructure Management

The `azure-config/stages/` directory contains two important scripts:

1. `deploy-application.sh` - Handles the deployment process
2. `destroy-infrastructure.sh` - Manages infrastructure destruction based on which destroy stage is triggered

## Troubleshooting

After deployment, you can view application logs by logging into the deployment VM:

```bash
sudo ssh -i /root/.ssh/azure team18-deploy@<DEPLOYMENT_VM_IP>
cd /home/team18-deploy/project/
sudo docker compose logs -f
```
- Note: To get the Runner or the Deployment VM IP, execute use the azure-config/get-vms-ips.sh script.
```bash
# Prints Runner and Deployment VM IP Addresses...
bash ./get-vms-ips.sh
```

## Credits
- **Team:** 18
- **Implementation**: Anir Saddik - ACS202
- **Review**: Erik Gerbreders - ACS202
