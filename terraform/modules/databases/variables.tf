variable "project_name" {}
variable "vpc_id" {}
variable "private_subnets" { type = list(string) }
variable "sg_rds_user" {}
variable "sg_rds_trip" {}
variable "sg_redis_driver" {}
variable "sg_msk_infra" {}
variable "db_user_password" { sensitive = true }
variable "db_trip_password" { sensitive = true }