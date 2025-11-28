variable "resource_group_name" {}
variable "location" {}
variable "tags" {}

output "redis_hostname" {
  value = azurerm_redis_cache.redis.hostname
}

output "redis_key" {
  value = azurerm_redis_cache.redis.primary_access_key
  sensitive = true
}

output "eventhub_namespace_name" {
  value = azurerm_eventhub_namespace.eh_ns.name
}

output "eventhub_connection_string" {
  # Formats the connection string for Kafka JAAS config
  value = "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"$ConnectionString\" password=\"${azurerm_eventhub_namespace.eh_ns.default_primary_connection_string}\";"
  sensitive = true
}