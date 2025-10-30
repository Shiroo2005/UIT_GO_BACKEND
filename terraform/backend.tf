terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
  
  # Cấu hình backend S3 để lưu state file, giúp làm việc nhóm
  backend "s3" {
    bucket         = "uit-go-terraform-state-bucket" # THAY TÊN BUCKET CỦA BẠN
    key            = "global/terraform.tfstate"
    region         = "ap-southeast-1"
    dynamodb_table = "terraform-lock" # Tạo 1 bảng DynamoDB tên này để khóa state
    encrypt        = true
  }
}

provider "aws" {
  region = var.region
}