# Báo cáo Chuyên sâu Đồ án SE360: UIT-Go

**Tên nhóm:** ...
**Thành viên:** 
- Tiêu Hoàng Phúc
- Đặng Nguyễn Huy Phong
- Lê Minh Phát

**Module chuyên sâu:** Module E-Design for Automation & Cost Optimization (FinOps)

## 1. Tổng quan Kiến trúc Hệ thống

Hệ thống UIT-Go được thiết kế và triển khai dựa trên kiến trúc Microservices, đảm bảo tính phân tán, khả năng mở rộng độc lập và khả năng bảo trì cao. Hệ sinh thái bao gồm 5 services cốt lõi giao tiếp với nhau thông qua RESTful API và Apache Kafka cho các tác vụ bất đồng bộ.

(Xem chi tiết tại `docs/ARCHITECTURE.md`)
### 1.1. Tech Stack & Infrastructure
* **Service Discovery**: Netflix Eureka Server.
* **API Gateway**: Gateway Service (đóng vai trò entry-point, routing và load balancing cơ bản).
* **Core Services**:
    * **User Service**: Quản lý định danh và hồ sơ (Database: MySQL).
    * **Trip Service**: Quản lý vòng đời chuyến đi (Database: MySQL).
    * **Driver Service**: Quản lý vị trí và tìm kiếm tài xế (Database: Redis với Geospatial index).
* **Message Broker**: Apache Kafka (xử lý matching chuyến đi và log tracking).
* **Containerization & Orchestration**: Docker & Docker Compose.
* **Image Registry**: Docker Hub.
* **Cloud Provider**: Microsoft Azure.
* **IaC (Infrastructure as Code)**: Terraform.
## 2. Phân tích Module Chuyên sâu (Module E: Automation & Cost Optimization)
Với vai trò Platform & FinOps Engineer, nhóm tập trung vào việc tự động hóa quy trình phát triển và tối ưu hóa chi phí vận hành trên nền tảng đám mây Azure.

### 2.1. Tự động hóa (Automation & CI/CD)
Chúng tôi đã xây dựng nền tảng "Self-Service" giúp giảm thiểu thời gian triển khai (Lead Time for Changes) và giảm rủi ro lỗi con người.
* **Pipeline Architecture: Sử dụng GitHub Actions**.
    * **CI (Continuous Integration)**: Tự động kích hoạt khi có Pull Request. Pipeline thực hiện Unit Test, Build Docker Image và đẩy (Push) image lên Docker Hub.
    * **CD (Continuous Deployment)**: Sử dụng Terraform để provision hạ tầng trên Azure (Azure Virtual Machines, VNet, Azure Database for MySQL). Sau đó, script tự động SSH vào VM để pull image mới nhất từ Docker Hub và khởi động lại container.
* **Infrastructure as Code (IaC)**: Toàn bộ hạ tầng (Resource Group, Network Security Groups, VM Size) được định nghĩa bằng Terraform, giúp quản lý trạng thái hạ tầng (State Management) minh bạch và nhất quán.

### 2.2. Tối ưu hóa Chi phí (Cost Optimization/FinOps)
Thay vì sử dụng các dịch vụ Managed Kubernetes (như AKS) đắt đỏ cho quy mô dự án hiện tại, nhóm đã áp dụng chiến lược "Right-Sizing" và lựa chọn kiến trúc phù hợp với ngân sách.
* **Phân tích chi phí (Cost Analysis)**:
    * Sử dụng Azure Cost Management để theo dõi burn-rate hàng ngày.
    * Thiết lập Azure Budgets để cảnh báo khi chi phí vượt ngưỡng cho phép (Alerting).
* **Chiến lược tối ưu**:
    * **Compute**: Sử dụng Azure B-Series (Burstable) Virtual Machines thay vì D-Series. Dòng B-Series (ví dụ Standard_B2s) cho phép tích lũy tín dụng CPU khi nhàn rỗi và "burst" lên 100% khi tải cao, phù hợp với đặc thù traffic không đều của môi trường dev/testing, giúp tiết kiệm ~40% chi phí compute.
    * **Storage**: Sử dụng Local SSD của VM cho Redis (Cache) thay vì Azure Redis Cache (Managed Service) để tiết kiệm chi phí, chấp nhận trade-off về tính bền vững dữ liệu cache (do Redis chỉ dùng để tìm kiếm tài xế realtime).
    * **Data Transfer**: Tối ưu hóa region, đặt toàn bộ resource cùng một Region (Southeast Asia) để miễn phí băng thông nội bộ (Data Transfer In/Out between Azure services).

## 3. Tổng hợp Các Quyết định Thiết kế và Trade-off
Đây là phần cốt lõi, giải thích lý do lựa chọn công nghệ và những sự đánh đổi để đạt được mục tiêu nghiệp vụ.
#### Quyết định 1: Sử dụng Docker Hub & Azure VMs thay vì Kubernetes (AKS)
* **Bối cảnh**: Team cần triển khai hệ thống microservices nhưng nhân sự vận hành (Ops) hạn chế và ngân sách thấp.
* **Lựa chọn**: Triển khai Container trực tiếp trên Azure Virtual Machines (VM) và dùng Docker Hub làm Registry.
* **Trade-off (Đánh đổi)**:
    * **Ưu điểm**: Tiết kiệm đáng kể chi phí (loại bỏ chi phí Cluster Management của K8s). Đơn giản hóa quá trình vận hành (không cần quản lý Pods, Deployments phức tạp).
    * **Nhược điểm**: Mất đi các tính năng tự động mạnh mẽ của K8s như Self-healing (tự khởi động lại pod chết), Auto-scaling (HPA) và Rolling Updates mượt mà. Phải xử lý thủ công việc cân bằng tải giữa các node nếu mở rộng.

#### Quyết định 2: Sử dụng Redis (Geospatial) cho Driver Service
* **Bối cảnh**: Yêu cầu nghiệp vụ (User Story 3) đòi hỏi truy vấn tìm "tài xế gần nhất" trong bán kính R với độ trễ cực thấp (sub-millisecond latency).
* **Lựa chọn**: Redis (In-memory data structure store).
* **Trade-off**:
    * **Ưu điểm (Performance)**: Tốc độ truy xuất cực nhanh so với truy vấn không gian trên MySQL hay PostgreSQL.
    * **Nhược điểm (Reliability)**: Dữ liệu nằm trên RAM. Nếu instance Redis bị restart mà không cấu hình AOF/RDB persistence kỹ, dữ liệu vị trí tài xế sẽ bị mất. Tuy nhiên, vị trí tài xế là dữ liệu ephemeral (thay đổi liên tục), việc mất mát trong vài giây là chấp nhận được.

#### Quyết định 3: Giao tiếp bất đồng bộ (Asynchronous) qua Kafka cho Booking Flow
* **Bối cảnh**: Khi User đặt chuyến, hệ thống cần thông báo cho tài xế và ghi nhận lịch sử. Việc xử lý đồng bộ (gọi API nối tiếp) có thể gây nghẽn (blocking) TripService nếu DriverService phản hồi chậm.
* **Lựa chọn**: Apache Kafka làm Message Broker. TripService đẩy event TRIP_CREATED vào topic.
* **Trade-off**:
    * **Ưu điểm (Decoupling)**: User/Trip Service không bị phụ thuộc vào tốc độ xử lý của các service phía sau. Tăng khả năng chịu tải (High Throughput).
    * **Nhược điểm (Complexity)**: Tăng độ phức tạp của hệ thống (phải quản lý Kafka Cluster, Zookeeper). Phải xử lý vấn đề "Eventual Consistency" (Tính nhất quán cuối cùng) thay vì nhất quán tức thì.

#### Quyết định 4: Database per Service (MySQL phân tán)
* **Bối cảnh**: User Service và Trip Service có domain data riêng biệt.
* **Lựa chọn**: Mỗi service sở hữu một database MySQL riêng (Logical separation).
* **Trade-off**:
    * **Ưu điểm**: Service này không thể làm sập database của service kia. Dễ dàng thay đổi schema mà không ảnh hưởng toàn hệ thống.
    * **Nhược điểm**: Không thể thực hiện JOIN bảng giữa User và Trip. Phải thực hiện Aggregation ở tầng Application hoặc API Gateway, gây phức tạp khi cần lấy dữ liệu tổng hợp.

## 4. Thách thức & Bài học Kinh nghiệm
Trong quá trình thực hiện Module E và chuyển đổi hạ tầng sang Azure, nhóm đã gặp các thách thức:
1. **Cấu hình Network Security Groups (NSG) trên Azure**: Việc mở port cho Eureka Discovery và giao tiếp giữa các container Docker trên các VM khác nhau gặp nhiều khó khăn ban đầu, dẫn đến lỗi "Connection Refused".
    * **Giải pháp**: Sử dụng Azure Private VNet cho giao tiếp nội bộ và chỉ mở port 80/443 ra public thông qua Gateway.
2. **Quản lý biến môi trường (Environment Variables)**: Khi số lượng service tăng lên, việc quản lý file .env cho Docker Compose trở nên rối rắm.
    * **Giải pháp**: Tích hợp GitHub Secrets để inject biến môi trường an toàn vào pipeline CI/CD.

## 5. Kết quả & Hướng phát triển
### Kết quả:
* Hệ thống backend hoàn chỉnh với 5 microservices hoạt động ổn định trên Azure.
* Pipeline CI/CD tự động hóa 100% từ code commit đến deploy.
* Chi phí vận hành được tối ưu hóa giảm ~30% so với dự toán ban đầu nhờ chọn đúng loại VM và Region.

### Hướng phát triển:
* Nâng cấp từ Docker trên VM sang Azure Container Apps (Serverless Containers) để tận dụng khả năng scale-to-zero khi không có traffic, tối ưu chi phí hơn nữa.
* Triển khai ELK Stack (Elasticsearch, Logstash, Kibana) để thay thế việc xem log thủ công.