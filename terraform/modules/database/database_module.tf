resource "azurerm_mysql_flexible_server" "mysql" {
  name                   = "uit-go-mysql-${random_string.suffix.result}"
  resource_group_name    = var.resource_group_name
  location               = var.location
  administrator_login    = var.admin_username
  administrator_password = var.admin_password
  sku_name               = "B_Standard_B1ms"
  version                = "8.0.21"
  
  # Note: For v4, keeping public access usually works, but verify if 'public_network_access_enabled' is required in your specific strict policy.
  # usually it is 'public_network_access_enabled' = true (default) or false.
  
  tags = var.tags
}

resource "random_string" "suffix" {
  length  = 4
  special = false
  upper   = false
}

resource "azurerm_mysql_flexible_database" "user_db" {
  name                = "userdb"
  resource_group_name = var.resource_group_name
  server_name         = azurerm_mysql_flexible_server.mysql.name
  charset             = "utf8"
  collation           = "utf8_unicode_ci"
}

resource "azurerm_mysql_flexible_database" "trip_db" {
  name                = "tripdb"
  resource_group_name = var.resource_group_name
  server_name         = azurerm_mysql_flexible_server.mysql.name
  charset             = "utf8"
  collation           = "utf8_unicode_ci"
}

resource "azurerm_mysql_flexible_server_firewall_rule" "allow_azure" {
  name                = "allow-azure-services"
  resource_group_name = var.resource_group_name
  server_name         = azurerm_mysql_flexible_server.mysql.name
  start_ip_address    = "0.0.0.0"
  end_ip_address      = "0.0.0.0"
}