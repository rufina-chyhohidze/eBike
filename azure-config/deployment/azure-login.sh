#!/bin/bash

# Uses variable from gitlab CI/CD Variables
az login --service-principal \
         --username "$AZURE_CLIENT_ID" \
         --password "$AZURE_CLIENT_SECRET" \
         --tenant "$AZURE_TENANT_ID" > /dev/null

az account set --subscription "$AZURE_SUBSCRIPTION_ID" > /dev/null

#Command used to get ssl certificates:

# First build certduck image with Dockerfile in nginx directory:
#docker build -t certduck {dir of dockerfile}

#docker run \
#-v "./letsencrypt:/etc/letsencrypt" \
#-v "./log/letsencrypt:/var/log/letsencrypt" \
#certduck \
#certonly \
#--non-interactive \
#--agree-tos \
#--email team18@student.kdg.be \
#--preferred-challenges dns \
#--authenticator dns-duckdns \
#--dns-duckdns-token "b4bb4460-f9d0-42fc-a063-e1dbadd11014" \
#-d "team18-int4.duckdns.org"
