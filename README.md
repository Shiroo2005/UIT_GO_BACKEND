# Đồ án SE360: Hệ thống Backend UIT-Go

Đây là dự án backend cho UIT-Go, một ứng dụng gọi xe giả tưởng, được phát triển trong khuôn khổ môn học SE360. Hệ thống được xây dựng theo kiến trúc microservices và triển khai trên nền tảng cloud-native (AWS).

**Thành viên nhóm:**
* Tiêu Hoàng Phúc-23521220
* Đặng Nguyễn Huy Phong-23521159
* Lê Minh Phát-23521140

**Module chuyên sâu đã chọn:** [Điền tên Module A/B/C/D/E]

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

Hệ thống có thể được khởi chạy đầy đủ trên môi trường local bằng Docker Compose[cite: 56].

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

2.  **Build và push images lên Docker Hub**
    Làm từng service
	```bash
		docker build -t <dockerhub-username>/<service-name>:latest .
		docker push <dockerhub-username>/<service-name>:latest
	```


3.  **Triển khai hạ tầng:**
    ```bash
		cd terraform
		terraform init
		terraform plan
		terraform apply
	```
	(Lưu ý: Cần cấu hình backend state cho Terraform để làm việc nhóm hiệu quả).

4.  **Triển khai ứng dụng:**
	Azure sẽ pull các image về và chạy