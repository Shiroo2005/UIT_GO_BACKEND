variable "service_name" {}
variable "resource_group_name" {}
variable "location" {}
variable "service_plan_id" {}
variable "docker_image" {}
variable "docker_tag" {}
variable "app_settings" {
  type    = map(string)
  default = {}
}

output "default_hostname" {
  value = azurerm_linux_web_app.app.default_hostname
}