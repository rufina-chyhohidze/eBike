#!/bin/bash

az login --service-principal \
-u 83665159-87d3-4463-a3ef-53fd62b8b4aa \
-p _p28Q~ZZSuenVgMJU2VbmjpmScCfpw1dSoZS4a~y \
--tenant ed1fc57f-8a97-47e7-9de1-9302dfd786ae

az account set -s "$(az account show --query id --output tsv)"


#Command to get ssl certificates:

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
