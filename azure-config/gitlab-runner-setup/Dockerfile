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
