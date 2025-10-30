# Báo cáo Chuyên sâu Đồ án SE360: UIT-Go

**Tên nhóm:** ...
**Thành viên:** 
- Tiêu Hoàng Phúc
- Đặng Nguyễn Huy Phong
- Lê Minh Phát
**Module chuyên sâu:** ...

## 1. Tổng quan Kiến trúc Hệ thống

Hệ thống UIT-Go được thiết kế theo kiến trúc microservices với 3 services lõi: UserService, TripService, và DriverService. Chúng tôi đã áp dụng nguyên tắc "Database per Service" và đóng gói toàn bộ ứng dụng bằng Docker. Hạ tầng được quản lý bằng Terraform và triển khai trên AWS.

(Xem chi tiết tại `docs/ARCHITECTURE.md`)

## 2. Phân tích Module Chuyên sâu: <Tên Module>

Phần này mô tả cách tiếp cận và kết quả đạt được cho module chuyên sâu.

*(Nội dung tùy thuộc vào module, ví dụ:*
* *Module A: Trình bày kết quả load testing (k6) trước và sau khi tối ưu, biểu đồ p95 latency, giải thích về các điểm nghẽn cổ chai đã tìm thấy.*
* *Module B: Trình bày kết quả thực hành Chaos Engineering (ví dụ: tắt 1 AZ), RTO/RPO tính toán được và kết quả thực tế khi DR.*
* *Module C: Trình bày kết quả của Threat Modeling (dùng STRIDE), các lỗ hổng đã xác định và giải pháp, cấu hình Security Group/IAM Role.*
* *Module D: Trình bày Dashboard SLO/SLI (tỷ lệ lỗi, p95 latency), hình ảnh trace request bằng X-Ray qua nhiều service.*
* Module E: Trình bày phân tích chi phí từ Cost Explorer, hiệu quả của giải pháp tối ưu (ví dụ: dùng Spot Instance tiết kiệm X%), sơ đồ pipeline CI/CD.*
*)*

## 3. Tổng hợp Các Quyết định Thiết kế và Trade-off (Quan trọng nhất)

Đây là phần cốt lõi của báo cáo, tổng hợp các quyết định kỹ thuật quan trọng nhất (từ các ADR) và các đánh đổi đã thực hiện.

### Quyết định 1: [Tên quyết định, ví dụ: Giao tiếp nội bộ bằng REST thay vì gRPC]
* **Bối cảnh:** Cần giao tiếp hiệu năng cao giữa các service nội bộ.
* **Lựa chọn:** Sử dụng gRPC.
* **Trade-off:** Chấp nhận độ phức tạp khi cài đặt (file .proto, sinh code) và khó gỡ lỗi hơn (cần công cụ riêng) để đổi lấy hiệu năng cao, tối ưu băng thông và giảm độ trễ.

### Quyết định 2: [Tên quyết định, ví dụ: Dùng Redis (Speed-first) cho DriverService]
* **Bối cảnh:** Yêu cầu nghiệp vụ cần tìm tài xế gần nhất với độ trễ cực thấp (User Story 3 của Tài xế).
* Lựa chọn:** Dùng Redis ElastiCache với tính năng Geospatial.
* **Trade-off:** Chúng tôi ưu tiên tốc độ (Performance) và chấp nhận chi phí vận hành có thể cao hơn và dữ liệu chỉ lưu trong bộ nhớ, thay vì chọn giải pháp tối ưu về chi phí/khả năng mở rộng (Cost/Scale) như DynamoDB + Geohashing.

### Quyết định 3: [Dành riêng cho module chuyên sâu]
* **Bối cảnh:** Mô tả vấn đề của module chuyên sâu].
* **Lựa chọn:** Giải pháp đã chọn, ví dụ: Dùng SQS cho Module A.
* **Trade-off:** Phân tích đánh đổi, ví dụ: Chấp nhận độ trễ tăng nhẹ (Availability) để đổi lấy khả năng chịu tải đột biến (Reliability) và hệ thống không bị sập.

## 4. Thách thức & Bài học kinh nghiệm

[Mô tả các khó khăn kỹ thuật lớn nhất nhóm đã gặp và bài học rút ra[cite: 140]. Ví dụ: Cấu hình Terraform cho VPC, quản lý state, debug lỗi mạng giữa các service, ....

## 5. Kết quả & Hướng phát triển

[Tóm tắt kết quả đạt được và các đề xuất cải tiến trong tương lai[cite: 141]. Ví dụ: hệ thống đã đáp ứng được...; hướng phát triển: thêm service thanh toán, áp dụng Service Mesh, ...].