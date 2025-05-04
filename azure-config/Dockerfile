FROM mcr.microsoft.com/azure-cli

# Copy login script into container
COPY create-virtual-machine.sh /usr/local/bin/login.sh
RUN chmod +x /usr/local/bin/create-virtual-machine.sh

# Default command (can be overridden)
CMD ["/usr/local/bin/login.sh"]