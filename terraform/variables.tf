variable "region" {
  description = "AWS region"
  type        = string
  default     = "ap-southeast-1"
}

variable "project_name" {
  description = "Tên dự án, dùng để prefix cho các tài nguyên"
  type        = string
  default     = "uit-go"
}

variable "availability_zones" {
  description = "Các AZ để triển khai cho High Availability"
  type        = list(string)
  default     = ["ap-southeast-1a", "ap-southeast-1b"]
}

# Biến cho mật khẩu, sẽ được cung cấp qua terraform.tfvars
variable "db_user_password" {
  description = "Password cho CSDL UserService"
  type        = string
  sensitive   = true
}

variable "db_trip_password" {
  description = "Password cho CSDL TripService"
  type        = string
  sensitive   = true
}