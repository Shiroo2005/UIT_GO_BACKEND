# Tài liệu Kiến trúc Hệ thống UIT-Go

## 1. Sơ đồ Kiến trúc Tổng quan

Đây là kiến trúc "Bộ Xương" Microservices (Giai đoạn 1) của hệ thống.


*Sơ đồ này cần thể hiện rõ 3 microservices chính: UserService, TripService, DriverService, cơ sở dữ liệu riêng biệt cho mỗi service, và cách chúng giao tiếp với nhau (qua API Gateway hoặc trực tiếp).*

![Sơ đồ tổng quan](./images/architecture.png)
![Sơ đồ database](./images/database.png)

### Mô tả các Services

1.  **UserService**:
    * **Trách nhiệm:** Quản lý thông tin người dùng (hành khách, tài xế), đăng ký, đăng nhập.
    * **Cơ sở dữ liệu:** MySQL
2.  **TripService**:
    * **Trách nhiệm:** Xử lý logic tạo và quản lý trạng thái chuyến đi.
    * **Cơ sở dữ liệu:** MySQL.
3.  **DriverService**:
    * **Trách nhiệm:** Quản lý trạng thái, vị trí tài xế và tìm kiếm tài xế ở gần.
    * **Cơ sở dữ liệu:** Redis (ElastiCache).

## 2. Sơ đồ Kiến trúc Module Chuyên sâu

**Tên Module:** <Tên module được chọn>


*(Sơ đồ này cần đi chi tiết vào các thành phần kỹ thuật được áp dụng trong module chuyên sâu. Ví dụ:*
* *Module A: Thể hiện SQS, ElastiCache, Auto Scaling Group, Read Replicas.*
* *Module B: Thể hiện Multi-AZ, Load Balancer, các cơ chế Circuit Breaker.*
* *Module C: Thể hiện rõ kiến trúc VPC (private/public subnets), Security Groups, Cognito, KMS.*
* *Module D: Thể hiện luồng đi của logs, metrics, traces về CloudWatch và X-Ray.*
* *Module E: Thể hiện pipeline CI/CD của GitHub Actions và cấu trúc module của Terraform.*
*)*

### Giải thích thiết kế Module

[Giải thích các quyết định thiết kế chi tiết cho module chuyên sâu, các công nghệ đã chọn và lý do chọn chúng để giải quyết các nhiệm vụ cụ thể của module].