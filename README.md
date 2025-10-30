# Đồ án SE360: Hệ thống Backend UIT-Go

Đây là dự án backend cho UIT-Go, một ứng dụng gọi xe giả tưởng, được phát triển trong khuôn khổ môn học SE360. Hệ thống được xây dựng theo kiến trúc microservices và triển khai trên nền tảng cloud-native (AWS).

**Thành viên nhóm:**
* Tiêu Hoàng Phúc-23521220
* Đặng Nguyễn Huy Phong-23521159
* Lê Minh Phát-23521140

[cite_start]**Module chuyên sâu đã chọn:** [Điền tên Module A/B/C/D/E] [cite: 61]

## 🛠️ Công nghệ sử dụng

* **Ngôn ngữ:** Java [cite: 38]
* **Framework:** Spring Boot
* **Giao tiếp:** RESTful API
* **Cơ sở dữ liệu:** MySQL, Redis
* **Containerization:** Docker, Docker Compose
* **Infrastructure as Code:** Terraform
* **Cloud:** AWS (ECS, RDS, ElastiCache, SQS,...)
* **CI/CD:** GitHub Actions (nếu làm Module E)
* **Observability:** CloudWatch, AWS X-Ray (nếu làm Module D)

## 🚀 Hướng dẫn chạy trên Local

[cite_start]Hệ thống có thể được khởi chạy đầy đủ trên môi trường local bằng Docker Compose[cite: 56].

1.  **Clone repository:**
   ```bash
    git clone https://github.com/Shiroo2005/UIT_GO_BACKEND
    cd UIT-GO_BACKEND
    ```

2.  **Cấu hình biến môi trường:**
    Sao chép tệp `.env.example` thành `.env` và điền các thông tin cần thiết (VD: database credentials, API keys...).

3.  **Khởi chạy hệ thống:**
   ```bash
    docker-compose up -d --build
    ```

4.  **Kiểm tra:**
    Hệ thống sẽ chạy với các services được expose tại:
    * **UserService:** `http://localhost:PORT_USER`
    * **TripService:** `http://localhost:PORT_TRIP`
    * **DriverService:** `http://localhost:PORT_DRIVER`

## ☁️ Hướng dẫn triển khai lên AWS

Hệ thống được quản lý hạ tầng bằng Terraform.

1.  **Cài đặt Terraform:**
    [Link hướng dẫn install terraform](https://developer.hashicorp.com/terraform/install)

2.  **Cấu hình AWS Credentials:**
    Đảm bảo bạn đã cấu hình AWS CLI với quyền IAM phù hợp.

3.  **Triển khai hạ tầng:**
    ```bash
    cd terraform
    terraform init
    terraform plan
    terraform apply
    ```
	(Lưu ý: Cần cấu hình backend state cho Terraform để làm việc nhóm hiệu quả).

4.  **Triển khai ứng dụng:
	4.1. Lấy tên ECR Repository URI:** Bạn cần biết địa chỉ (URI) của repository. Bạn có thể lấy nó từ giao diện AWS ECR hoặc dùng lệnh:
    ```bash
    aws ecr describe-repositories --repository-names <your-repo-name> --query "repositories[0].repositoryUri" --output text
    ```
    **4.2. Xác thực Docker với ECR:** Bạn cần lấy một token tạm thời từ AWS để Docker có thể đăng nhập vào registry của ECR.
    ```bash
    aws ecr get-login-password --region <REGION> | docker login --username AWS --password-stdin <ACCOUNT_ID>.dkr.ecr.<REGION>.amazonaws.com
    ```
    **4.3. Build Docker Image:** Từ thư mục gốc của service (nơi có `Dockerfile`), chạy lệnh build.
    ```bash
    # Build image
	docker build -t <your-repo-name> .
    ```
	**4.4. Gắn tag (Tag) cho Image:** Docker cần biết bạn muốn đẩy image này tới repository nào. Bạn phải tag nó với URI bạn đã lấy ở Bước 1. Bạn có thể dùng tag `latest` hoặc một tag cụ thể (ví dụ: mã Git commit) cho việc quản lý phiên bản tốt hơn.
	```bash
	# Ví dụ dùng tag 'latest'
	docker tag <your-repo-name>:latest <ACCOUNT_ID>.dkr.ecr.<REGION>.amazonaws.com/<your-repo-name>:latest

	# Ví dụ dùng tag cụ thể (khuyến khích)
	docker tag <your-repo-name>:latest <ACCOUNT_ID>.dkr.ecr.<REGION>.amazonaws.com/<your-repo-name>:v1.0.1
	```
	**4.5. Đẩy (Push) Image lên ECR:** Giờ thì đẩy image đã được tag lên registry.
	```bash
	docker push <ACCOUNT_ID>.dkr.ecr.<REGION>.amazonaws.com/<your-repo-name>:latest
	```
	