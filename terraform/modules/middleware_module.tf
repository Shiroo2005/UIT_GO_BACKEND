# --------------------------
# Redis for Driver Service
# --------------------------
resource "azurerm_redis_cache" "redis" {
  name                = "uit-go-redis-${random_string.suffix.result}"
  location            = var.location
  resource_group_name = var.resource_group_name
  capacity            = 0
  family              = "C"
  sku_name            = "Basic" # Lowest cost for Dev
  enable_non_ssl_port = false
  minimum_tls_version = "1.2"

  tags = var.tags
}

resource "random_string" "suffix" {
  length  = 4
  special = false
  upper   = false
}

# --------------------------
# Kafka (via Azure Event Hubs)
# --------------------------
# Note: Standard Tier is required for Kafka compatibility. 
# This is slightly more expensive but is the "Professional" Cloud-Native way.
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
  namespace_name      = azurerm_eventhub_namespace.eh_ns.name
  resource_group_name = var.resource_group_name
  partition_count     = 2
  message_retention   = 1
}