FROM mcr.microsoft.com/azure-cli

# Copy login script into container
COPY login.sh /usr/local/bin/login.sh
RUN chmod +x /usr/local/bin/login.sh

# Default command (can be overridden)
CMD ["/usr/local/bin/login.sh"]