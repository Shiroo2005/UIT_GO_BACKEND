resource "time_static" "now" {}

locals {
  # Lấy năm và tháng hiện tại, ép ngày = 01
  start_of_month = "${substr(time_static.now.rfc3339, 0, 8)}01T00:00:00Z"
}

resource "time_offset" "one_year_later" {
  base_rfc3339 = local.start_of_month
  offset_years = 1
}

resource "azurerm_consumption_budget_resource_group" "project_budget" {
  name              = "uit-go-monthly-budget"
  resource_group_id = azurerm_resource_group.rg.id

  amount     = 50
  time_grain = "Monthly"

  time_period {
    start_date = local.start_of_month
    end_date   = time_offset.one_year_later.rfc3339
  }

  notification {
    enabled        = true
    threshold      = 80.0
    operator       = "EqualTo"
    threshold_type = "Actual"
    contact_emails = ["23521220@gm.uit.edu.com"]
  }

  notification {
    enabled        = true
    threshold      = 100.0
    operator       = "EqualTo"
    threshold_type = "Actual"
    contact_emails = ["phuctieuhoang8888@gmail.com"]
  }
}
