### ADR-013: Ưu tiên RESTful API thay vì gRPC cho giao tiếp liên dịch vụ

#### Bối cảnh (Context)
Hệ thống Microservices cần giao tiếp với nhau (VD: Gateway gọi User, Gateway gọi Trip). Có hai lựa chọn phổ biến:
1. REST (JSON/HTTP): Tiêu chuẩn web truyền thống.
2. gRPC (Protobuf): Chuẩn mới của Google, hiệu năng cao, đóng gói binary.

Mặc dù gRPC được đánh giá cao về tốc độ, nhóm quyết định chọn REST.

#### Quyết định (Decision)
Sử dụng chuẩn RESTful API (JSON over HTTP/1.1) cho toàn bộ giao tiếp nội bộ và bên ngoài.

#### Lý do (Rationale) - Phù hợp năng lực và Module E
1. Khả năng quan sát & Debug (Observability - Module E focus):
    * **REST**: Dữ liệu là JSON (text). Khi hệ thống lỗi, chúng ta có thể đọc log trực tiếp (xem ADR-008), dùng Postman để giả lập request, hoặc dùng trình duyệt để test. Dễ dàng phát hiện lỗi sai tên trường, sai dữ liệu.
    * **gRPC**: Dữ liệu là Binary. Không thể đọc bằng mắt thường. Cần tool chuyên dụng. Việc debug tốn nhiều thời gian (Developer Cost), làm chậm tiến độ đồ án.
2. Hệ sinh thái & Tích hợp:
    * **Gateway**: Spring Cloud Gateway hỗ trợ định tuyến HTTP/REST cực tốt và đơn giản. Cấu hình gRPC transcoder phức tạp hơn nhiều.
3. Hiệu năng đủ dùng (Sufficiency):
    * Chai sạn cổ chai (Bottleneck) của ứng dụng gọi xe nằm ở I/O Database (đọc ghi đĩa) và Network Latency, chứ không nằm ở việc serialize JSON. Tốc độ vượt trội của gRPC là chưa cần thiết với quy mô người dùng hiện tại của đồ án.

#### Hệ quả (Consequences)
##### Tích cực (Pros)
* **Tốc độ phát triển (Velocity)**: Nhóm dev quen thuộc với REST, không mất thời gian học Protobuf, setup gen code .proto.
* **Linh hoạt**: Dễ dàng thay đổi cấu trúc dữ liệu JSON mà không cần compile lại toàn bộ các service phụ thuộc như gRPC.

##### Tiêu cực (Cons)
* **Payload lớn**: JSON verbose (dư thừa các dấu ngoặc, tên trường) tốn băng thông hơn Protobuf.
* **Type Safety**: Không chặt chẽ bằng gRPC (compile-time check). Cần chú ý kỹ khi map DTO giữa các service.