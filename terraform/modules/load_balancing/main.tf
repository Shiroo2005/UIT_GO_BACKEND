# --- 1. Public ALB (cho api-gateway) ---
resource "aws_lb" "public" {
  name               = "${var.project_name}-public-alb"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [var.sg_public_alb]
  subnets            = var.public_subnets

  enable_deletion_protection = false
}

resource "aws_lb_listener" "public_http" {
  load_balancer_arn = aws_lb.public.arn
  port              = 80
  protocol          = "HTTP"

  default_action {
    type = "fixed-response"
    fixed_response {
      content_type = "text/plain"
      message_body = "Cannot route request"
      status_code  = "404"
    }
  }
}

resource "aws_lb_target_group" "api_gateway" {
  name        = "${var.project_name}-tg-api-gateway"
  port        = 8080 # Giả sử api-gateway chạy cổng 8080
  protocol    = "HTTP"
  vpc_id      = var.vpc_id
  target_type = "ip"
  
  health_check {
    path = "/actuator/health" # Cần có health check
  }
}

# Rule: Mọi request đến / đều đẩy qua api-gateway
resource "aws_lb_listener_rule" "api_gateway" {
  listener_arn = aws_lb_listener.public_http.arn
  priority     = 100

  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.api_gateway.arn
  }

  condition {
    path_pattern {
      values = ["/*"]
    }
  }
}


# --- 2. Internal ALB (cho eureka-server) ---
resource "aws_lb" "internal" {
  name               = "${var.project_name}-internal-alb"
  internal           = true
  load_balancer_type = "application"
  security_groups    = [var.sg_internal_alb]
  subnets            = var.private_subnets
}

resource "aws_lb_target_group" "eureka_server" {
  name        = "${var.project_name}-tg-eureka"
  port        = 8761
  protocol    = "HTTP"
  vpc_id      = var.vpc_id
  target_type = "ip"

  health_check {
    path = "/actuator/health"
  }
}

resource "aws_lb_listener" "internal_eureka" {
  load_balancer_arn = aws_lb.internal.arn
  port              = 8761
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.eureka_server.arn
  }
}