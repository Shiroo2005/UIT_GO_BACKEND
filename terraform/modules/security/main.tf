variable "project_name" {}
variable "vpc_id" {}

# --- Security Groups ---

resource "aws_security_group" "public_alb" {
  name        = "${var.project_name}-sg-public-alb"
  description = "Allow HTTP/HTTPS from public"
  vpc_id      = var.vpc_id

  ingress {
    protocol    = "tcp"
    from_port   = 80
    to_port     = 80
    cidr_blocks = ["0.0.0.0/0"]
  }
  ingress {
    protocol    = "tcp"
    from_port   = 443
    to_port     = 443
    cidr_blocks = ["0.0.0.0/0"]
  }
  egress {
    protocol    = "-1"
    from_port   = 0
    to_port     = 0
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "internal_alb" {
  name        = "${var.project_name}-sg-internal-alb"
  description = "Allow internal traffic"
  vpc_id      = var.vpc_id

  # Chỉ cho phép traffic từ bên trong VPC (ví dụ: các services gọi Eureka)
  ingress {
    protocol    = "tcp"
    from_port   = 8761 # Port của Eureka
    to_port     = 8761
    cidr_blocks = ["10.0.0.0/16"] # VPC CIDR
  }
  egress {
    protocol    = "-1"
    from_port   = 0
    to_port     = 0
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "ecs_services" {
  name        = "${var.project_name}-sg-ecs-services"
  description = "SG cho tất cả ECS Services"
  vpc_id      = var.vpc_id

  # Cho phép ALB gọi vào
  ingress {
    protocol        = "tcp"
    from_port       = 0 # Cổng động
    to_port         = 65535
    security_groups = [aws_security_group.public_alb.id, aws_security_group.internal_alb.id]
  }
  # Cho phép các service trong cùng SG gọi nhau
  ingress {
    protocol        = "tcp"
    from_port       = 0
    to_port         = 65535
    self            = true
  }
  egress {
    protocol    = "-1"
    from_port   = 0
    to_port     = 0
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "rds_user" {
  name        = "${var.project_name}-sg-rds-user"
  description = "Allow UserService to access RDS User"
  vpc_id      = var.vpc_id

  ingress {
    protocol        = "tcp"
    from_port       = 3306
    to_port         = 3306
    security_groups = [aws_security_group.ecs_services.id]
  }
  egress {
    protocol    = "-1"
    from_port   = 0
    to_port     = 0
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "rds_trip" {
  # ... Tương tự rds_user ...
  name        = "${var.project_name}-sg-rds-trip"
  description = "Allow TripService to access RDS Trip"
  vpc_id      = var.vpc_id
  ingress {
    protocol        = "tcp"
    from_port       = 3306
    to_port         = 3306
    security_groups = [aws_security_group.ecs_services.id]
  }
}

resource "aws_security_group" "redis_driver" {
  name        = "${var.project_name}-sg-redis-driver"
  description = "Allow DriverService to access Redis"
  vpc_id      = var.vpc_id

  ingress {
    protocol        = "tcp"
    from_port       = 6379
    to_port         = 6379
    security_groups = [aws_security_group.ecs_services.id]
  }
}

resource "aws_security_group" "msk_infra" {
  name        = "${var.project_name}-sg-msk"
  description = "Allow services to access MSK"
  vpc_id      = var.vpc_id

  ingress {
    protocol        = "tcp"
    from_port       = 9092 # Port Kafka
    to_port         = 9094 # Các port khác của Kafka
    security_groups = [aws_security_group.ecs_services.id]
  }
}

# --- IAM Roles ---

# Role này cho phép ECS agent thực hiện các hành động (pull ECR, gửi log)
resource "aws_iam_role" "ecs_task_execution_role" {
  name = "${var.project_name}-ecs-task-execution-role"
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        }
      },
    ]
  })
}

resource "aws_iam_role_policy_attachment" "ecs_task_execution_role_policy" {
  role       = aws_iam_role.ecs_task_execution_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

# Role này là quyền của bản thân application (ví dụ: quyền gọi S3, SQS...)
# Tạm thời để trống, bạn có thể thêm quyền sau
resource "aws_iam_role" "ecs_task_role" {
  name = "${var.project_name}-ecs-task-role"
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        }
      },
    ]
  })
}