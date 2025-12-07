resource "azurerm_linux_web_app" "app" {
  name                = "uit-go-${var.service_name}"
  resource_group_name = var.resource_group_name
  location            = var.location
  service_plan_id     = var.service_plan_id

  site_config {
    always_on = true

    application_stack {
      docker_image_name = var.docker_image
      docker_registry_url = "https://index.docker.io"
    }

  }

  app_settings = merge(
    {
      "WEBSITES_PORT" = var.port
    },
    var.app_settings
  )
}