terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "~> 4.0" # Explicitly targeting version 4.x to match your local tools
    }

    time = {
      source  = "hashicorp/time"
      version = "~> 0.9.1" # Dùng phiên bản mới nhất
    }
  }

  
}

provider "azurerm" {
  features {
    resource_group {
      prevent_deletion_if_contains_resources = false
    }
  }

  subscription_id = var.subscription_id
}


resource "azurerm_resource_group" "rg" {
  name     = var.resource_group_name
  location = var.location
  tags     = var.tags
}

# 1. Networking Module
module "networking" {
  source              = "./modules/networking"
  resource_group_name = azurerm_resource_group.rg.name
  location            = azurerm_resource_group.rg.location
}

# 2. Database Module
module "database" {
  source              = "./modules/database"
  resource_group_name = azurerm_resource_group.rg.name
  location            = azurerm_resource_group.rg.location
  admin_username      = var.db_admin_username
  admin_password      = var.db_admin_password
  tags                = var.tags
}

# 3. Middleware Module (Redis & Kafka/EventHubs)
module "middleware" {
  source              = "./modules/middleware"
  resource_group_name = azurerm_resource_group.rg.name
  location            = azurerm_resource_group.rg.location
  tags                = var.tags
}

# 4. Compute Module (App Service Plan & Microservices)

# Shared Plan for Cost Optimization (Module E)
resource "azurerm_service_plan" "main_plan" {
  name                = "uit-go-app-plan"
  resource_group_name = azurerm_resource_group.rg.name
  location            = azurerm_resource_group.rg.location
  os_type             = "Linux"
  sku_name            = "B1" 
  tags                = var.tags
}

# 4.1 Eureka Discovery Server
module "service_eureka" {
  source              = "./modules/compute"
  service_name        = "eureka-server"
  resource_group_name = azurerm_resource_group.rg.name
  location            = azurerm_resource_group.rg.location
  service_plan_id     = azurerm_service_plan.main_plan.id
  docker_image        = "${var.docker_hub_user}/eureka-server"
  docker_tag          = "latest"
  
  app_settings = {
    "SERVER_PORT" = "8761"
  }
}

# 4.2 User Service
module "service_user" {
  source              = "./modules/compute"
  service_name        = "user-service"
  resource_group_name = azurerm_resource_group.rg.name
  location            = azurerm_resource_group.rg.location
  service_plan_id     = azurerm_service_plan.main_plan.id
  docker_image        = "${var.docker_hub_user}/user-service"
  docker_tag          = "latest"

  app_settings = {
    "SPRING_DATASOURCE_URL"      = "jdbc:mysql://${module.database.server_fqdn}:3306/userdb?useSSL=true&requireSSL=false"
    "SPRING_DATASOURCE_USERNAME" = var.db_admin_username
    "SPRING_DATASOURCE_PASSWORD" = var.db_admin_password
    "EUREKA_CLIENT_SERVICEURL_DEFAULTZONE" = "https://${module.service_eureka.default_hostname}/eureka/"
  }
}

# 4.3 Trip Service
module "service_trip" {
  source              = "./modules/compute"
  service_name        = "trip-service"
  resource_group_name = azurerm_resource_group.rg.name
  location            = azurerm_resource_group.rg.location
  service_plan_id     = azurerm_service_plan.main_plan.id
  docker_image        = "${var.docker_hub_user}/trip-service"
  docker_tag          = "latest"

  app_settings = {
    "SPRING_DATASOURCE_URL"      = "jdbc:mysql://${module.database.server_fqdn}:3306/tripdb?useSSL=true&requireSSL=false"
    "SPRING_DATASOURCE_USERNAME" = var.db_admin_username
    "SPRING_DATASOURCE_PASSWORD" = var.db_admin_password
    "EUREKA_CLIENT_SERVICEURL_DEFAULTZONE" = "https://${module.service_eureka.default_hostname}/eureka/"
    "KAFKA_BOOTSTRAP_SERVERS"    = "${module.middleware.eventhub_namespace_name}.servicebus.windows.net:9093"
    "KAFKA_SASL_JAAS_CONFIG"     = module.middleware.eventhub_connection_string
  }
}

# 4.4 Driver Service
module "service_driver" {
  source              = "./modules/compute"
  service_name        = "driver-service"
  resource_group_name = azurerm_resource_group.rg.name
  location            = azurerm_resource_group.rg.location
  service_plan_id     = azurerm_service_plan.main_plan.id
  docker_image        = "${var.docker_hub_user}/driver-service"
  docker_tag          = "latest"

  app_settings = {
    "SPRING_REDIS_HOST"          = module.middleware.redis_hostname
    "SPRING_REDIS_PASSWORD"      = module.middleware.redis_key
    "SPRING_REDIS_PORT"          = "6380"
    "SPRING_REDIS_SSL"           = "true"
    "EUREKA_CLIENT_SERVICEURL_DEFAULTZONE" = "https://${module.service_eureka.default_hostname}/eureka/"
    "KAFKA_BOOTSTRAP_SERVERS"    = "${module.middleware.eventhub_namespace_name}.servicebus.windows.net:9093"
  }
}

# 4.5 Gateway Service
module "service_gateway" {
  source              = "./modules/compute"
  service_name        = "gateway-service"
  resource_group_name = azurerm_resource_group.rg.name
  location            = azurerm_resource_group.rg.location
  service_plan_id     = azurerm_service_plan.main_plan.id
  docker_image        = "${var.docker_hub_user}/gateway-service"
  docker_tag          = "latest"

  app_settings = {
    "EUREKA_CLIENT_SERVICEURL_DEFAULTZONE" = "https://${module.service_eureka.default_hostname}/eureka/"
    "SERVER_PORT" = "8080"
  }
}