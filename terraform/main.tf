module "networking" {
  source = "./modules/networking"
  
  project_name       = var.project_name
  region             = var.region
  availability_zones = var.availability_zones
}

module "security" {
  source = "./modules/security"
  
  project_name = var.project_name
  vpc_id       = module.networking.vpc_id
}

module "load_balancing" {
  source = "./modules/load_balancing"

  project_name    = var.project_name
  vpc_id          = module.networking.vpc_id
  public_subnets  = module.networking.public_subnet_ids
  private_subnets = module.networking.private_subnet_ids
  sg_public_alb   = module.security.sg_public_alb_id
  sg_internal_alb = module.security.sg_internal_alb_id
}

module "database" {
  source = "./modules/database"

  project_name       = var.project_name
  vpc_id             = module.networking.vpc_id
  private_subnets    = module.networking.private_subnet_ids
  sg_rds_user        = module.security.sg_rds_user_id
  sg_rds_trip        = module.security.sg_rds_trip_id
  sg_redis_driver    = module.security.sg_redis_driver_id
  sg_msk_infra       = module.security.sg_msk_id
  db_user_password   = var.db_user_password
  db_trip_password   = var.db_trip_password
}

module "compute" {
  source = "./modules/compute"

  project_name             = var.project_name
  region                   = var.region
  vpc_id                   = module.networking.vpc_id
  private_subnets          = module.networking.private_subnet_ids
  sg_ecs_services          = module.security.sg_ecs_services_id
  
  # Kết nối ALB
  public_alb_tg_api_gateway = module.load_balancing.public_alb_tg_api_gateway_arn
  internal_alb_tg_eureka    = module.load_balancing.internal_alb_tg_eureka_arn

  # Kết nối CSDL & Secrets
  db_user_endpoint      = module.database.db_user_endpoint
  db_trip_endpoint      = module.database.db_trip_endpoint
  redis_driver_endpoint = module.database.redis_driver_endpoint
  msk_bootstrap_brokers = module.database.msk_bootstrap_brokers
  db_user_password      = var.db_user_password
  db_trip_password      = var.db_trip_password
  
  # URL của Eureka (từ ALB nội bộ)
  eureka_server_url = "http://${module.load_balancing.internal_alb_dns_name}:8761/eureka/"
}