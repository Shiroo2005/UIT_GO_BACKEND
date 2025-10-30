variable "project_name" {}
variable "vpc_id" {}
variable "public_subnets" { type = list(string) }
variable "private_subnets" { type = list(string) }
variable "sg_public_alb" {}
variable "sg_internal_alb" {}