#!/bin/bash

echo "Running build stage in pipeline $(date)" >> /var/log/cd-gitlab-pipeline.log
echo "========== BUILD STAGE =========="
docker compose build --no-cache
echo "========== BUILD STAGE COMPLETED SUCCESSFULLY =========="
