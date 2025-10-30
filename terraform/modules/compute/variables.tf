variable "project_name" {}
variable "region" {}
variable "vpc_id" {}
variable "private_subnets" { type = list(string) }
variable "sg_ecs_services" {}
variable "public_alb_tg_api_gateway" {}
variable "internal_alb_tg_eureka" {}

variable "eureka_server_url" { type = string }

variable "db_user_endpoint" { type = string }
variable "db_trip_endpoint" { type = string }
variable "redis_driver_endpoint" { type = string }
variable "msk_bootstrap_brokers" { type = string }

variable "db_user_password" { type = string, sensitive = true }
variable "db_trip_password" { type = string, sensitive = true }