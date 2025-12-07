##############################################
# FINOPS: Azure Budget with Dynamic Dates
##############################################

# Lấy ngày hiện tại (time_static = tạo 1 timestamp cố định lúc apply)
resource "time_static" "now" {}

# Lấy ngày 1 năm sau từ ngày hiện tại
resource "time_offset" "one_year_later" {
  base_rfc3339 = time_static.now.rfc3339
  offset_years = 1
}

resource "azurerm_consumption_budget_resource_group" "project_budget" {
  name              = "uit-go-monthly-budget"
  resource_group_id = azurerm_resource_group.rg.id

  amount     = 50
  time_grain = "Monthly"

  # Dùng formatdate vì Azure yêu cầu format: YYYY-MM-DDTHH:MM:SSZ
  time_period {
    start_date = formatdate("YYYY-MM-DD", time_static.now.rfc3339)
    end_date   = formatdate("YYYY-MM-DD", time_offset.one_year_later.rfc3339)
  }

  # Cảnh báo 80%
  notification {
    enabled        = true
    threshold      = 80.0
    operator       = "EqualTo"
    threshold_type = "Actual"
    contact_emails = ["23521220@gm.uit.edu.com"]
  }

  # Cảnh báo 100%
  notification {
    enabled        = true
    threshold      = 100.0
    operator       = "EqualTo"
    threshold_type = "Actual"
    contact_emails = ["phuctieuhoang8888@gmail.com"]
  }
}
