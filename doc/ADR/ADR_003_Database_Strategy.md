### ADR-004: Chiến lược cơ sở dữ liệu đa hình (MySQL & Redis)

#### Bối cảnh (Context)
Các service có đặc thù dữ liệu khác nhau:
1. user-service & trip-service: Dữ liệu có cấu trúc, quan hệ chặt chẽ, cần đảm bảo tính toàn vẹn (ACID) cho giao dịch tài chính/đặt chuyến.
2. driver-service: Cần truy vấn vị trí không gian (Geospatial) cực nhanh và cập nhật vị trí liên tục (High write throughput).

#### Quyết định (Decision)
* Sử dụng MySQL (Azure Database for MySQL) cho user-service và trip-service.
* Sử dụng Redis (Azure Cache for Redis hoặc Container Redis) cho driver-service.

### Hệ quả (Consequences)

#### Tích cực (Pros)
* **Hiệu năng (Performance)**: Redis xử lý các lệnh GEOADD, GEORADIUS với độ trễ thấp (sub-millisecond), phù hợp tính năng "Tìm tài xế gần đây".
* **Toàn vẹn dữ liệu**: MySQL đảm bảo không bị mất dữ liệu chuyến đi và thông tin thanh toán.
* **Độc lập**: Tuân thủ nguyên tắc "Database per Service".

#### Tiêu cực (Cons)

* **Chi phí (Cost - Module E issue)**: Chạy 2 loại DB managed trên Cloud khá đắt.
    * Giải pháp: Trong khuôn khổ đồ án, có thể chạy MySQL và Redis dưới dạng Docker Containers trên cùng VM với ứng dụng để tiết kiệm chi phí Managed Service, nhưng vẫn mount volume ra đĩa cứng (Azure Disk) để đảm bảo dữ liệu không mất khi restart container.

### Rủi ro
* Việc join dữ liệu giữa User và Trip sẽ phức tạp hơn (phải join ở tầng Application/API thay vì SQL).