#!/bin/bash

az login --service-principal \
         --username "83665159-87d3-4463-a3ef-53fd62b8b4aa" \
         --password "_p28Q~ZZSuenVgMJU2VbmjpmScCfpw1dSoZS4a~y" \
         --tenant "ed1fc57f-8a97-47e7-9de1-9302dfd786ae" > /dev/null

az account set --subscription "f44abeef-ada7-4fd1-a6dc-6173b9d786bd"
