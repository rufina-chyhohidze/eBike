if [ ! -f ./terraform/main.tf ] || [ ! -f ./terraform/variables.tf ] || [ ! -f ./terraform/outputs.tf ] ; then
  echo "You must execute this script in the '(ROOT_PROJECT_DIR)/azure-config' directory!" >&2
  exit 1;
fi

if ! grep -q "f44abeef-ada7-4fd1-a6dc-6173b9d786bd" "./terraform/variables.tf" ; then
  echo "This is not the correct directory, this script can only be executed in the '/ebiketeam18/azure-config/' project directory"
fi

command -v docker >/dev/null 2>&1 || { echo "Docker is not installed. Aborting."; exit 1; }
command -v ssh >/dev/null 2>&1 || { echo "SSH (openssh-clients) is not installed. Aborting."; exit 1; }

if [ ! -f ~/.ssh/azure ] ; then
  echo "Creating ssh key (in host)"
  ssh-keygen -t ed25519 -f ~/.ssh/azure -N ""
fi

docker run --rm --name azure_setup -dit \
  -v "./terraform:/terraform" \
  -v "$HOME/.ssh/:/root/.ssh/" \
  -w /terraform \
  anir333/team18-int4:latest

docker exec azure_setup bash "/terraform/azure_login.sh"


function resourceGroupExists() {
  if "$(docker exec azure_setup az group exists --name rg-team18)" ; then
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
    docker exec azure_setup chmod +x "/terraform/setup_runner.sh"
    docker exec azure_setup bash /terraform/setup_runner.sh
    docker kill azure_setup
fi