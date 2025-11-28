### ADR-002: Sử dụng Azure App Service & Docker thay vì Kubernetes

#### Bối cảnh (Context)
Hệ thống gồm 5 services (Eureka, User, Trip, Driver, Gateway). Chúng ta cần một môi trường để chạy các Docker container này. Xu hướng hiện đại là dùng Kubernetes (K8s/AKS), nhưng K8s đòi hỏi tài nguyên lớn và chi phí quản lý cao.

#### Quyết định (Decision)
Chúng tôi quyết định **KHÔNG** sử dụng **Kubernetes (AKS)**. Thay vào đó, chúng tôi sẽ sử dụng Azure App Service for Containers (hoặc chạy Docker Compose trên một máy ảo Azure VM duy nhất nếu cần tiết kiệm tối đa). Image sẽ được lưu trữ trên Docker Hub.

#### Hệ quả (Consequences)

##### Tích cực (Pros) - Phù hợp Module E

* **Tối ưu chi phí (Cost)**:
    * AKS (Azure Kubernetes Service) tốn phí quản lý Cluster và yêu cầu tối thiểu các Node pool (VMs), rất đắt đỏ cho một đồ án sinh viên.
    * Azure App Service (Plan B1/F1) hoặc chạy Docker trên 1 VM (B2s) rẻ hơn rất nhiều (tiết kiệm khoảng 70-80% so với cụm K8s tối thiểu).

* **Đơn giản hóa vận hành (Automation)**:
    * Loại bỏ sự phức tạp của việc cấu hình K8s (Pods, Services, Ingress, Helm charts).
    * Tập trung vào Docker Compose giúp setup môi trường Local và Production gần như giống hệt nhau (Dev/Prod Parity).

* **Deploy nhanh**: Pipeline CI/CD chỉ cần push image lên Docker Hub và trigger Webhook để Azure pull về.

##### Tiêu cực (Cons)

* **Khả năng mở rộng (Scalability)**: Khó khăn hơn trong việc auto-scaling từng service riêng lẻ so với K8s.
* **Service Mesh**: Không tận dụng được các tính năng nâng cao của K8s như Istio.

#### Ghi chú kỹ thuật
Image sẽ được build và push lên Docker Hub (Public/Private repo). Azure sẽ pull image từ Docker Hub để chạy.