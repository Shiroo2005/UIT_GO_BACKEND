# Redis
resource "azurerm_redis_cache" "redis" {
  name                = "uit-go-redis-${random_string.suffix.result}"
  location            = var.location
  resource_group_name = var.resource_group_name
  capacity            = 0
  family              = "C"
  sku_name            = "Basic"
  
  # FIXED: Renamed attribute for v4.x
  non_ssl_port_enabled = false 
  
  minimum_tls_version = "1.2"
  tags                = var.tags
}

resource "random_string" "suffix" {
  length  = 4
  special = false
  upper   = false
}

# Kafka / Event Hubs
resource "azurerm_eventhub_namespace" "eh_ns" {
  name                = "uit-go-kafka-${random_string.suffix.result}"
  location            = var.location
  resource_group_name = var.resource_group_name
  sku                 = "Standard"
  capacity            = 1
  tags                = var.tags
}

resource "azurerm_eventhub" "trips_topic" {
  name                = "trip-events"
  
  # FIXED: Must use 'namespace_id' now, not 'namespace_name' + 'resource_group_name'
  namespace_id        = azurerm_eventhub_namespace.eh_ns.id
  
  partition_count     = 2
  message_retention   = 1
}