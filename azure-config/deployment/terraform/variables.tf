# In CI CD vars its declared
variable "subscription_id" {
  description = "Azure subscription id"
  sensitive   = true
}

variable "resource_group_name" {
  default = "rg-team18-deploy"
}
    
variable "location" {
  default = "West Europe"
}
    
variable "vm_name" {
  default = "vm-team18-deploy"
}
    
variable "vm_size" {
  default = "Standard_B2s"
}
    
variable "admin_username" {
  default = "team18-deploy"
}
    
variable "public_key_path" {
  default = "/root/.ssh/azure.pub"
}

# IN CI CD VARIABLES, THERE ARE THE USER AND PASSWORDS STORED USING TF_VAR convention, so that terraform reads them from the environment since terraform does not read variables that don't start with TF_VAR_...
variable "postgres_admin_user" {
  description = "PostgreSQL admin username"
}

variable "postgres_admin_password" {
  description = "PostgreSQL admin password"
  sensitive   = true
}
