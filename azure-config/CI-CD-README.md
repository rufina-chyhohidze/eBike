### CI/CD Pipeline Documentation

* The entire Pipeline consists of two main steps:
  * Gitlab Runner setup:
    * If the custom gitlab runner is not set up, you need to execute the (azure-setup.sh) script 
  
      inside the (ROOT_PROJ_DIR)/azure-config/gitlab-runner-setup/ directory
      * This script needs to be executed locally in order to create the runner azure resource group and virtual machine,
      
        it also logs into the runner VM to installs dependencies (download docker and set it up as the gitlab runner)
  * Pipeline:
    * If the runner is set up and running, the pipeline can now be executed.
    * The pipeline contains 4 main stages (and 3 other stages which are for destroying resources)
      * Stage 1: Build (Runs in dev and main branch)
        * Build the application
        * Runs in custom azure runner in shell mode
        * It uses the docker compose in the root project dir to build the image
      * Stage 2: Test (Runs in dev and main branch)
        * Tests the application
        * Runs in custom azure runner in shell mode
        * It uses the docker compose in the root project dir to run the tests
        * It uploads the tests xml artifacts to see the tests results in gitlab
      * Stage 3: Azure pre deployment check (Runs only on main branch)
        * Checks the azure resources
          * If the deployment resources exist it simply skips the setup
          * Else If the deployment resources don't exist it uses opentofu (terraform) to set up 
        * Runs in custom azure runner in a custom docker container which has all the dependencies (azure-cli, opetofu, ssh, ansible...)   
            the deployment resource group, virtual machine, SQL database, firewall rules, etc.

      * Stage 4: Deploys application (Runs only on main branch)
        * Uses ansible configuration to log into deployment VM, install & set up dependencies (docker...), and deploy application using docker compose
        * If there is an old application running, it deletes it and substitues it for the new one (we don't check for changes on the new application because we assume that if you push to main you want the previously running app to be substitued by the new one)
        * Runs in custom azure runner in a custom docker container which has all the dependencies (azure-cli, opetofu, ssh, ansible...)
      
      * Destroy stages: (Only can be ran on main branch) - All manual
        * Made up of three different stages which only run when manually triggered
          * Destroy stage 1: Destroys all azure infrastructure related to deployment (deployment RG, VM & SQL DB)
          * Destroy stage 2: Destroys all azure infrastructure related to the gitlab runner (runner RG & VM)
          * Destroy stage 3: Destroys ALL azure infrastructure including gitlab-runner & deployment

---
Pipeline in detail:

  - Note: The pre-deploy-check stage and deployment stage need multiple dependencies to run, instead of installing all these dependencies in our runner and deployment vm, we instead decided to simply install docker and create our docker image with all these dependencies ot simplify the whole process:
    - To do that it needs these dependencies: OpenTofu (terraform), Azure-CLI, SSH Client, Ansible
    - In order to simplify the process and ensure it can run in all machines, we created a custom docker image with these dependencies:
    - The Dockerfile used to create the image can be foud in the (ROOT_PROJ_DIR)/azure-config/ directory.  
    - This Dockerfile was used to build a custom docker image which is used in multiple stages in the pipeline (pre-deploy check and deployment)
    - This stages still run in our custom azure gitlab runner, but in docker mode instead of shell mode, using our custom image, this way we don't have to install all these dependencies in our deployment virtual machine each time it is created.
    - The image is almalinux based, and it contains necessary tools to run the pipeline:
    - Installs:
    1. OpenTofu (open source version of terraform) -> to set up azure resources
    2. Azure CLI (needed by OpenTofu)
    3. Openssh clients for generating ssh-keys
    4. Ansible -> to set up the deployment environment and run the application in dpeloyment stage
    - You can pull the image doing: docker pull anir333/team18-int4:latest


  - We set up a docker compose which contains containers used for the build, testing and deployment stages
  - When the project is pushed to dev the build & testing stages are executed in shell mode (in our gitlab runner)
  - When the project is pushed to main (production) the following stages are ran:
    - Build
    - Test (using a docker gradle image) (in our custom runner in shell mode)
    - Azure pre deployment setup:
      - This stage sets up the deployment azure resources
      - It runs in our azure gitlab runner in docker mode, in our custom image which contains all the dependencies needed.
      - First it checks the the resources for deployment exist:
        - If they do it simply skips the setup and continues to the next Stage:
        - But if it doesn't exist then it uses our terraform configuration files in the (ROOT_PROJ_DIR)/azure-config/deployment/terraform/ directory to create the deployment environment and set it up.
          - The main.tf file in this directory contains instructions to create a deployment Resource Group, virtual machine, Azure SQL Database, firewall rules allowing port 80 (HTTP) and port 443 (HTTPS) to acces the app and a firwall rule so that only our deployment virtual machine can access the SQL Database
        - This stage makes use of Gitlab CI/CD Variables for the terraform confiuration, specifcially the variables starting with TAR_VAR_* are used in terraform to configure the resources in azure
        - In the process, before executing the terraform configuraiton, the pipeline first logs into azure via CLI, using a service principal we created. The variables are also set up in gitlab ci/cd.
    - Application deployment stage:
      - This stage runs in our custom gitlab runner in docker mode using our cusotm docker image because it needs dependencies (Ansible, ssh client...)
      - It executed a custom Ansible configuration file (can be found in (ROOT_PROJ_DIR)/azure-config/deployment/ansible/ directory) which executes the following stages:
        - First the inventory.ini file is created using the deployment VM IP address so ansible can log into the deployment VM to set up the project.
        - Then Ansible installs docker ce if not already installed in the deployment vm
        - Then it removes the old app running (if any)
          - Note: We don't check if the app is the same because we assume that if you push to main you want to substitue the existing application with the new one.
        - Then it packages the application into a tarball and sends it and unpacks in the deployment VM
        - It stops any existing old app container
        - And finally it stars a new container with the new app
          - It uses our docker compose file to start up the new application using nginx for the reverse proxy
          - The nginx reverse proxy configuratoin template can be found in the (ROOT_PROJ_DIR)/azure-config/nginx/templates/ directory 
          - It also makes use of CI/CD variables for reverse proxy domain and logging into azure... 
          - We use duckdns api to update the IP address each time our deployment is executed to ensure it points to the correct IP Address (of the deployment VM)
        - Once the deployment stage has finished the app is accessible



Gitlab runner in detail:

