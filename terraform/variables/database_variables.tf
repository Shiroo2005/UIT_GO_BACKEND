variable "resource_group_name" {}
variable "location" {}
variable "admin_username" {}
variable "admin_password" {}
variable "tags" {}

output "server_fqdn" {
  value = azurerm_mysql_flexible_server.mysql.fqdn
}