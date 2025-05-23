#!/bin/bash

# Variables to fill in:
GITLAB_HOST="https://gitlab.com"                # or your self-hosted URL
PROJECT_ID="66765065"                              # your project’s numeric ID
TOKEN="glpat-fcnzAqxYwPhwyVj9zwo8"                         # your personal access token

# Fetch all pipeline IDs, then delete each one
curl --header "PRIVATE-TOKEN: $TOKEN" \
     "$GITLAB_HOST/api/v4/projects/$PROJECT_ID/pipelines?per_page=100" \
  | jq -r '.[].id' \
  | xargs -n1 -I% \
      curl --request DELETE \
           --header "PRIVATE-TOKEN: $TOKEN" \
           "$GITLAB_HOST/api/v4/projects/$PROJECT_ID/pipelines/%"
