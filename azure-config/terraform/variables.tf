variable "subscription_id" {   
  default = "f44abeef-ada7-4fd1-a6dc-6173b9d786bd"
}

variable "resource_group_name" {
  default = "rg-team18"
}
    
variable "location" {
  default = "West Europe"
}
    
variable "vm_name" {
  default = "vm-team18"
}
    
variable "vm_size" {
  default = "Standard_B2s"
}
    
variable "admin_username" {
  default = "team18"
}
    
variable "public_key_path" {
  default = "~/.ssh/azure.pub"
}

