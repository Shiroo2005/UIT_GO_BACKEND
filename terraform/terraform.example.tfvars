# -----------------------------------------------------------------------------
# UIT-Go Infrastructure Configuration Values
# Place this file in the root directory alongside main.tf
# -----------------------------------------------------------------------------

# 1. Azure Region & Resource Group
resource_group_name = "uit-go-production-rg"
location            = "Southeast Asia"

# 2. Database Credentials (SENSITIVE)
# WARNING: Change this password before applying!
db_admin_username   = "admin_uitgo"
db_admin_password   = "P@ssw0rd_UIT_2024_Secure!"

# 3. Docker Hub Configuration
# The username where your images are hosted.
docker_hub_user     = "uitgoteam_example"

# 4. Tagging Strategy (Module E requirement)
tags = {
  Project     = "UIT-Go"
  Environment = "Production"
  Owner       = "StudentTeam01"
  Module      = "E-Automation-CostOpt"
  ManagedBy   = "Terraform"
  CostCenter  = "SE360-Course"
}