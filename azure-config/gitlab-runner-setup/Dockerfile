"""
 This dockerfile was used to build a custom docker image which is used in multiple stages in the pipeline
 The image is almalinux based, and it contains necessary tools to run the pipeline:
    - Installs:
        1. OpenTofu (open source version of terraform) -> to set up azure resources
        2. Azure CLI (needed by OpenTofu)
        3. Openssh clients for generating ssh-keys
        4. Ansible -> to set up the deployment environment and run the application in dpeloyment stage

- We decided to make our own image and publish it to docker hub so that we don't need to constantly set up all the dependencies
   in the runner or the deployment VM. With this implementation, all the VM's need is docker installed to execute all the steps needed.

   You can pull the image doing: docker pull anir333/team18-int4:latest
"""

FROM almalinux:9.5

# Download the installer script:
RUN curl --proto '=https' --tlsv1.2 -fsSL https://get.opentofu.org/install-opentofu.sh -o install-opentofu.sh

# Give it execution permissions:
RUN chmod +x install-opentofu.sh

# Import Microsoft GPG key and add the Azure CLI repo
RUN rpm --import https://packages.microsoft.com/keys/microsoft.asc && \
curl -sSL https://packages.microsoft.com/config/rhel/9/prod.repo \
  -o /etc/yum.repos.d/microsoft-prod.repo

# Install Azure CLI
RUN dnf install -y azure-cli && dnf clean all

# Run the installer:
RUN ./install-opentofu.sh --install-method rpm

# Remove the installer:
RUN rm -f install-opentofu.sh

# Install sshd
RUN dnf clean all
RUN dnf makecache
RUN dnf install -y openssh-clients

# Install ansible
RUN dnf install -y python3-pip
RUN pip3 install ansible

CMD ["/bin/bash"]
