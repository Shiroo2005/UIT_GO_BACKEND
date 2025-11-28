# -----------------------------------------------------------------------------
# MODULE E: Automation & Cost Optimization
# -----------------------------------------------------------------------------

# 1. Cost Alerts (Budget)
# This resource sets a hard budget on the resource group to prevent overspending.
resource "azurerm_consumption_budget_resource_group" "project_budget" {
  name              = "uit-go-monthly-budget"
  resource_group_id = azurerm_resource_group.rg.id

  amount     = 50 # Limit to $50 USD per month for the student project
  time_grain = "Monthly"

  time_period {
    start_date = "2024-01-01T00:00:00Z" # Adjust as needed
    end_date   = "2025-01-01T00:00:00Z"
  }

  notification {
    enabled        = true
    threshold      = 80.0 # Alert at 80% usage
    operator       = "EqualTo"
    threshold_type = "Actual"

    # In a real scenario, adding email addresses here is required
    contact_emails = ["admin@uit-go.project"]
  }

  notification {
    enabled   = true
    threshold = 100.0
    operator  = "EqualTo"
    contact_emails = ["admin@uit-go.project"]
  }
}