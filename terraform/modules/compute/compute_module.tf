resource "azurerm_linux_web_app" "app" {
  name                = "uit-go-${var.service_name}"
  resource_group_name = var.resource_group_name
  location            = var.location
  service_plan_id     = var.service_plan_id

  site_config {
    always_on = true 
    
    application_stack {
      # FIXED: Combined image name and tag for v4.x
      docker_image_name = "${var.docker_image}:${var.docker_tag}"
      # Explicitly state Docker Hub
      docker_registry_url = "https://index.docker.io" 
    }
  }

  app_settings = merge(
    {
      "WEBSITES_PORT" = "8080"
    },
    var.app_settings
  )
}