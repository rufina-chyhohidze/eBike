variable "subscription_id" {   
  default = "f44abeef-ada7-4fd1-a6dc-6173b9d786bd"
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
  default = "/home/team18/.ssh/azure.pub"
}
