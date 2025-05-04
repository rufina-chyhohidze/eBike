# Dockerfile example (save this in ./docker/Dockerfile)
FROM ubuntu:latest

# Install necessary packages
RUN apt-get update && apt-get install -y curl openssh-client && curl -sL https://aka.ms/InstallAzureCLIDeb | bash

# Set the default command
CMD ["/bin/bash"]
