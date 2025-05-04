# Dockerfile example (save this in ./docker/Dockerfile)
FROM ubuntu:22.04

# Install necessary packages
RUN apt-get update && apt-get install -y curl openssh-client && curl -sL https://aka.ms/InstallAzureCLIDeb | bash

# Set the default command
CMD ["/bin/bash"]
