# --- Subnet Groups (Bắt buộc cho RDS, ElastiCache, MSK) ---
resource "aws_db_subnet_group" "main" {
  name       = "${var.project_name}-db-subnet-group"
  subnet_ids = var.private_subnets
  tags = { Name = "${var.project_name}-db-subnet-group" }
}

resource "aws_elasticache_subnet_group" "main" {
  name       = "${var.project_name}-cache-subnet-group"
  subnet_ids = var.private_subnets
  tags = { Name = "${var.project_name}-cache-subnet-group" }
}

# --- 1. UserService Database (RDS MySQL) ---
resource "aws_db_instance" "user_service_db" {
  identifier           = "${var.project_name}-user-db"
  allocated_storage    = 20
  storage_type         = "gp2"
  engine               = "mysql"
  engine_version       = "8.0"
  instance_class       = "db.t3.micro" # Loại nhỏ nhất cho đồ án
  username             = "admin_user"
  password             = var.db_user_password
  db_name              = "user_service_db"
  db_subnet_group_name = aws_db_subnet_group.main.name
  vpc_security_group_ids = [var.sg_rds_user]
  skip_final_snapshot  = true
  multi_az             = false # Đặt là false để tiết kiệm chi phí
}

# --- 2. TripService Database (RDS MySQL) ---
resource "aws_db_instance" "trip_service_db" {
  identifier           = "${var.project_name}-trip-db"
  allocated_storage    = 20
  storage_type         = "gp2"
  engine               = "mysql"
  engine_version       = "8.0"
  instance_class       = "db.t3.micro"
  username             = "admin_trip"
  password             = var.db_trip_password
  db_name              = "trip_service_db"
  db_subnet_group_name = aws_db_subnet_group.main.name
  vpc_security_group_ids = [var.sg_rds_trip]
  skip_final_snapshot  = true
  multi_az             = false
}

# --- 3. DriverService Cache (ElastiCache Redis) ---
resource "aws_elasticache_cluster" "driver_service_redis" {
  cluster_id           = "${var.project_name}-driver-redis"
  engine               = "redis"
  engine_version       = "7.0"
  node_type            = "cache.t3.micro"
  num_cache_nodes      = 1
  port                 = 6379
  subnet_group_name    = aws_elasticache_subnet_group.main.name
  security_group_ids   = [var.sg_redis_driver]
}

# --- 4. Infrastructure (MSK Kafka) ---
resource "aws_msk_cluster" "infra_kafka" {
  cluster_name           = "${var.project_name}-kafka-cluster"
  kafka_version          = "3.3.1"
  number_of_broker_nodes = 2 # Tối thiểu là 2 (mỗi AZ 1 broker)

  broker_node_group_info {
    instance_type  = "kafka.t3.small"
    client_subnets = var.private_subnets
    security_groups = [var.sg_msk_infra]
    storage_info {
      ebs_storage_info {
        volume_size = 10 # GB
      }
    }
  }
  
  # Cho phép truy cập không cần xác thực (đơn giản cho đồ án)
  client_authentication {
    unauthenticated {
      enabled = true
    }
  }

  tags = {
    Name = "${var.project_name}-msk-cluster"
  }
}