# --- 0. ECS Cluster ---
resource "aws_ecs_cluster" "main" {
  name = "${var.project_name}-cluster"
}

# --- 1. ECR Repositories (Nơi lưu Docker images) ---
resource "aws_ecr_repository" "user_service" { name = "${var.project_name}/user-service" }
resource "aws_ecr_repository" "trip_service" { name = "${var.project_name}/trip-service" }
resource "aws_ecr_repository" "driver_service" { name = "${var.project_name}/driver-service" }
resource "aws_ecr_repository" "api_gateway" { name = "${var.project_name}/api-gateway" }
resource "aws_ecr_repository" "eureka_server" { name = "${var.project_name}/eureka-server" }

# --- 2. CloudWatch Log Groups (Nơi lưu logs) ---
resource "aws_cloudwatch_log_group" "user_service" { name = "/ecs/${var.project_name}/user-service" }
resource "aws_cloudwatch_log_group" "trip_service" { name = "/ecs/${var.project_name}/trip-service" }
resource "aws_cloudwatch_log_group" "driver_service" { name = "/ecs/${var.project_name}/driver-service" }
resource "aws_cloudwatch_log_group" "api_gateway" { name = "/ecs/${var.project_name}/api-gateway" }
resource "aws_cloudwatch_log_group" "eureka_server" { name = "/ecs/${var.project_name}/eureka-server" }

# --- 3. Task Definitions & Services ---

# Lấy các Role từ module security
data "aws_iam_role" "ecs_task_execution_role" {
  name = "${var.project_name}-ecs-task-execution-role"
}
data "aws_iam_role" "ecs_task_role" {
  name = "${var.project_name}-ecs-task-role"
}
data "aws_caller_identity" "current" {}


# --- Service: eureka-server ---
resource "aws_ecs_task_definition" "eureka_server" {
  family                   = "${var.project_name}-eureka-server"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = "256" # 0.25 vCPU
  memory                   = "512" # 0.5 GB
  execution_role_arn       = data.aws_iam_role.ecs_task_execution_role.arn
  task_role_arn            = data.aws_iam_role.ecs_task_role.arn

  container_definitions = jsonencode([
    {
      name      = "eureka-server"
      image     = "${data.aws_caller_identity.current.account_id}.dkr.ecr.${var.region}.amazonaws.com/${var.project_name}/eureka-server:latest" # Sửa 'latest' thành tag của bạn
      essential = true
      portMappings = [
        { containerPort = 8761, hostPort = 8761 }
      ]
      environment = [
        # Không cần trỏ đến chính nó
      ]
      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = aws_cloudwatch_log_group.eureka_server.name
          "awslogs-region"        = var.region
          "awslogs-stream-prefix" = "ecs"
        }
      }
    }
  ])
}

resource "aws_ecs_service" "eureka_server" {
  name            = "${var.project_name}-eureka-server"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.eureka_server.arn
  desired_count   = 2 # Chạy 2 instance cho HA
  launch_type     = "FARGATE"

  network_configuration {
    subnets         = var.private_subnets
    security_groups = [var.sg_ecs_services]
  }

  load_balancer {
    target_group_arn = var.internal_alb_tg_eureka
    container_name   = "eureka-server"
    container_port   = 8761
  }
  
  depends_on = [var.internal_alb_tg_eureka]
}

# --- Service: api-gateway ---
resource "aws_ecs_task_definition" "api_gateway" {
  family                   = "${var.project_name}-api-gateway"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = "256"
  memory                   = "512"
  execution_role_arn       = data.aws_iam_role.ecs_task_execution_role.arn
  task_role_arn            = data.aws_iam_role.ecs_task_role.arn

  container_definitions = jsonencode([
    {
      name      = "api-gateway"
      image     = "${data.aws_caller_identity.current.account_id}.dkr.ecr.${var.region}.amazonaws.com/${var.project_name}/api-gateway:latest"
      essential = true
      portMappings = [
        { containerPort = 8080, hostPort = 8080 } # Cổng của Gateway
      ]
      environment = [
        { name = "EUREKA_CLIENT_SERVICEURL_DEFAULTZONE", value = var.eureka_server_url }
      ]
      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = aws_cloudwatch_log_group.api_gateway.name
          "awslogs-region"        = var.region
          "awslogs-stream-prefix" = "ecs"
        }
      }
    }
  ])
}

resource "aws_ecs_service" "api_gateway" {
  name            = "${var.project_name}-api-gateway"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.api_gateway.arn
  desired_count   = 2
  launch_type     = "FARGATE"

  network_configuration {
    subnets         = var.private_subnets
    security_groups = [var.sg_ecs_services]
  }

  load_balancer {
    target_group_arn = var.public_alb_tg_api_gateway
    container_name   = "api-gateway"
    container_port   = 8080
  }
  
  depends_on = [var.public_alb_tg_api_gateway]
}

# --- Service: user-service ---
resource "aws_ecs_task_definition" "user_service" {
  family                   = "${var.project_name}-user-service"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = "256"
  memory                   = "512"
  execution_role_arn       = data.aws_iam_role.ecs_task_execution_role.arn
  task_role_arn            = data.aws_iam_role.ecs_task_role.arn

  container_definitions = jsonencode([
    {
      name      = "user-service"
      image     = "${data.aws_caller_identity.current.account_id}.dkr.ecr.${var.region}.amazonaws.com/${var.project_name}/user-service:latest"
      essential = true
      portMappings = [
        { containerPort = 8080, hostPort = 8080 } # Cổng của User Service
      ]
      environment = [
        { name = "EUREKA_CLIENT_SERVICEURL_DEFAULTZONE", value = var.eureka_server_url },
        { name = "SPRING_DATASOURCE_URL", value = "jdbc:mysql://${var.db_user_endpoint}/user_service_db" },
        { name = "SPRING_DATASOURCE_USERNAME", value = "admin_user" }
        # Mật khẩu nên được truyền qua AWS Secrets Manager, không nên dùng env var
        # Tạm thời dùng: { name = "SPRING_DATASOURCE_PASSWORD", value = var.db_user_password }
      ]
      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = aws_cloudwatch_log_group.user_service.name
          "awslogs-region"        = var.region
          "awslogs-stream-prefix" = "ecs"
        }
      }
    }
  ])
}

resource "aws_ecs_service" "user_service" {
  name            = "${var.project_name}-user-service"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.user_service.arn
  desired_count   = 2
  launch_type     = "FARGATE"

  network_configuration {
    subnets         = var.private_subnets
    security_groups = [var.sg_ecs_services]
  }
  # Service này không cần ALB vì Gateway sẽ tìm nó qua Eureka
}


# --- Service: trip-service (Tương tự user-service) ---
resource "aws_ecs_task_definition" "trip_service" {
  family                   = "${var.project_name}-trip-service"
  # ... (cấu hình tương tự user-service)
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = "256"
  memory                   = "512"
  execution_role_arn       = data.aws_iam_role.ecs_task_execution_role.arn
  task_role_arn            = data.aws_iam_role.ecs_task_role.arn
  
  container_definitions = jsonencode([
    {
      name      = "trip-service"
      image     = "${data.aws_caller_identity.current.account_id}.dkr.ecr.${var.region}.amazonaws.com/${var.project_name}/trip-service:latest"
      essential = true
      portMappings = [ { containerPort = 8080, hostPort = 8080 } ]
      environment = [
        { name = "EUREKA_CLIENT_SERVICEURL_DEFAULTZONE", value = var.eureka_server_url },
        { name = "SPRING_DATASOURCE_URL", value = "jdbc:mysql://${var.db_trip_endpoint}/trip_service_db" },
        { name = "SPRING_DATASOURCE_USERNAME", value = "admin_trip" },
        { name = "KAFKA_BOOTSTRAP_SERVERS", value = var.msk_bootstrap_brokers }
        # { name = "SPRING_DATASOURCE_PASSWORD", value = var.db_trip_password }
      ]
      logConfiguration = {
        logDriver = "awslogs"
        options = { "awslogs-group" = aws_cloudwatch_log_group.trip_service.name, "awslogs-region" = var.region, "awslogs-stream-prefix" = "ecs" }
      }
    }
  ])
}

resource "aws_ecs_service" "trip_service" {
  name            = "${var.project_name}-trip-service"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.trip_service.arn
  desired_count   = 2
  launch_type     = "FARGATE"
  network_configuration {
    subnets         = var.private_subnets
    security_groups = [var.sg_ecs_services]
  }
}

# --- Service: driver-service (Tương tự user-service, dùng Redis) ---
resource "aws_ecs_task_definition" "driver_service" {
  family                   = "${var.project_name}-driver-service"
  # ... (cấu hình tương tự user-service)
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = "256"
  memory                   = "512"
  execution_role_arn       = data.aws_iam_role.ecs_task_execution_role.arn
  task_role_arn            = data.aws_iam_role.ecs_task_role.arn
  
  container_definitions = jsonencode([
    {
      name      = "driver-service"
      image     = "${data.aws_caller_identity.current.account_id}.dkr.ecr.${var.region}.amazonaws.com/${var.project_name}/driver-service:latest"
      essential = true
      portMappings = [ { containerPort = 8080, hostPort = 8080 } ]
      environment = [
        { name = "EUREKA_CLIENT_SERVICEURL_DEFAULTZONE", value = var.eureka_server_url },
        { name = "SPRING_REDIS_HOST", value = var.redis_driver_endpoint },
        { name = "SPRING_REDIS_PORT", value = "6379" },
        { name = "KAFKA_BOOTSTRAP_SERVERS", value = var.msk_bootstrap_brokers }
      ]
      logConfiguration = {
        logDriver = "awslogs"
        options = { "awslogs-group" = aws_cloudwatch_log_group.driver_service.name, "awslogs-region" = var.region, "awslogs-stream-prefix" = "ecs" }
      }
    }
  ])
}

resource "aws_ecs_service" "driver_service" {
  name            = "${var.project_name}-driver-service"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.driver_service.arn
  desired_count   = 2
  launch_type     = "FARGATE"
  network_configuration {
    subnets         = var.private_subnets
    security_groups = [var.sg_ecs_services]
  }
}