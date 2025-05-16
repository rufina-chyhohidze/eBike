az login --service-principal \
-u 83665159-87d3-4463-a3ef-53fd62b8b4aa \
-p _p28Q~ZZSuenVgMJU2VbmjpmScCfpw1dSoZS4a~y \
--tenant ed1fc57f-8a97-47e7-9de1-9302dfd786ae

az account set -s "$(az account show --query id --output tsv)"