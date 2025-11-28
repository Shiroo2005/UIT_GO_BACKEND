### ADR-012: Sử dụng Redis cho dữ liệu vị trí tài xế thay vì Azure Cosmos DB

#### Bối cảnh (Context)

Tính năng cốt lõi của driver-service là theo dõi vị trí tài xế theo thời gian thực (Real-time Location Tracking) và tìm kiếm tài xế lân cận (Geospatial Query).
* **Tần suất**: Mỗi tài xế gửi tọa độ GPS lên server 5 giây/lần.
* **Số lượng**: Giả sử 1000 tài xế online -> 200 requests/giây (Write heavy).

Lựa chọn thay thế là Azure Cosmos DB (tương đương DynamoDB bên AWS). Đây là Database NoSQL mạnh mẽ, hỗ trợ Geospatial tốt.

#### Quyết định (Decision)

Sử dụng Redis (Azure Cache for Redis hoặc Container Redis) để lưu vị trí "nóng" (Hot Data) của tài xế. Không dùng Azure Cosmos DB cho tính năng này.

#### Lý do (Rationale) - Tập trung vào Module E (Cost & Performance)

1. Mô hình tính phí (Cost Trap):
    * **Cosmos DB**: Tính tiền dựa trên Request Units (RUs). Nghiệp vụ cập nhật vị trí là thao tác GHI (Write heavy). Việc ghi liên tục 200 lần/giây sẽ tiêu tốn lượng RU khổng lồ, làm chi phí tăng vọt mất kiểm soát.
    * **Redis**: Là In-memory store. Việc ghi đè key (update location) tốn chi phí CPU/RAM cực thấp và không bị tính phí theo từng request. Đây là lựa chọn tối ưu nhất cho ví tiền của sinh viên.

2. Độ trễ (Latency):
    * Việc truy vấn "Tìm 5 xe gần nhất trong bán kính 2km" cần phản hồi tức thì (< 10ms). Redis thực hiện tính toán khoảng cách trên RAM (lệnh GEORADIUS), nhanh hơn nhiều so với việc truy vấn từ đĩa của Cosmos DB.

3. Tính chất dữ liệu (Ephemeral Data):
    * Vị trí tài xế là dữ liệu "phù du". Vị trí cách đây 1 phút là vô nghĩa. Chúng ta không cần tính năng bền vững (Durability) cao cấp của Cosmos DB. Nếu Redis mất điện, tài xế gửi lại vị trí mới sau 5s là hệ thống tự hồi phục.

#### Hệ quả (Consequences)
##### Tích cực (Pros)
* **Tiết kiệm chi phí**: Tránh được hóa đơn "trên trời" từ Cosmos DB do tần suất ghi quá lớn.
* **Tốc độ**: Trải nghiệm người dùng mượt mà khi quét tìm xe.

##### Tiêu cực (Cons)
* **Giới hạn bộ nhớ**: RAM đắt hơn đĩa cứng. Không thể lưu lịch sử di chuyển (History) vào Redis.
* **Giải pháp**: Dùng Redis chỉ để lưu vị trí hiện tại. Nếu cần lưu lịch sử chuyến đi (để vẽ lại bản đồ sau này), sẽ dùng process bất đồng bộ (Kafka) để lưu xuống MySQL (Cold Storage) định kỳ.