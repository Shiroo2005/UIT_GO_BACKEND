variable "resource_group_name" {
  default = "uit-go-rg"
}

variable "location" {
  default = "Southeast Asia" # Low latency for Vietnam
}

variable "docker_hub_user" {
  description = "Your Docker Hub username where images are stored"
  type        = string
  default     = "phuctieuhoang"
}

variable "db_admin_username" {
  default = "uitgoadmin"
}

variable "db_admin_password" {
  description = "Strong password for Database"
  sensitive   = true
}

variable "tags" {
  type = map(string)
  default = {
    Project     = "UIT-Go"
    Environment = "Dev"
    Module      = "E-FinOps"
  }
}

variable "subscription_id" {
  type        = string
  description = "The Azure subscription ID to deploy resources"
}
