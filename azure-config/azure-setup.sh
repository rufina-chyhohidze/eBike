command -v docker >/dev/null 2>&1 || { echo "Docker is not installed. Aborting."; exit 1; }

docker run --rm --name azure_setup -dit \
  -v "./terraform:/terraform" \
  -w /terraform \
  anir333/team18-int4:latest

docker exec azure_setup bash  "/terraform/azure_login.sh"


function resourceGroupExists() {
  if "$(az group exists --name rg-team18)" ; then
    return 0
    else return 1
  fi
}

if resourceGroupExists ; then
  echo "Resource group already exists, skipping setup..."
  docker kill azure_setup
  else
    echo "Resource group doesn't exist, initializing setup..."
    docker exec azure_setup tofu init
    docker exec azure_setup tofu apply --auto-approve
    docker kill azure_setup
fi





#"""
#  read -p "Create runner with personal ssh key? (must have a private key called azure in the /home/.ssh/ directory (~/.ssh/azure) yes/no: " OPTION
#
#  case "$OPTION" in
#    y|Y|yes|Yes|YES)
#      echo "You chose YES – proceeding with your own SSH key setup..."
#      ;;
#    n|N|no|No)
#      echo "You chose NO – skipping SSH key setup..."
#      ;;
#    *)
#      echo "Invalid choice: $OPTION"
#      exit 1
#      ;;
#  esac
#
#"""