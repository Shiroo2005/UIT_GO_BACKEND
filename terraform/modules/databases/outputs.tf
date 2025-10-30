output "db_user_endpoint" {
  value = aws_db_instance.user_service_db.endpoint
}
output "db_trip_endpoint" {
  value = aws_db_instance.trip_service_db.endpoint
}
output "redis_driver_endpoint" {
  value = aws_elasticache_cluster.driver_service_redis.cache_nodes[0].address
}
output "msk_bootstrap_brokers" {
  value = aws_msk_cluster.infra_kafka.bootstrap_brokers
}