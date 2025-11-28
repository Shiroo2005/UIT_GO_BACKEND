# A generic module to deploy a Docker container to Azure App Service
resource "azurerm_linux_web_app" "app" {
  name                = "uit-go-${var.service_name}"
  resource_group_name = var.resource_group_name
  location            = var.location
  service_plan_id     = var.service_plan_id

  site_config {
    always_on = true # Keep services running (Basic tier required)
    
    application_stack {
      docker_image     = var.docker_image
      docker_image_tag = var.docker_tag
    }
  }

  app_settings = merge(
    {
      "WEBSITES_PORT" = "8080" # Default Spring Boot port, override if needed
    },
    var.app_settings
  )
}