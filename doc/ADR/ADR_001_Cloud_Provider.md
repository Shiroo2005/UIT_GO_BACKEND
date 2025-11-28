### ADR-001: Sử dụng Microsoft Azure làm Cloud Provider

#### Bối cảnh (Context)
Dự án UIT-Go yêu cầu triển khai hệ thống microservices lên môi trường điện toán đám mây. Mặc dù tài liệu môn học gợi ý AWS, nhóm cần lựa chọn một nhà cung cấp phù hợp với năng lực tài chính và định hướng Module E (Tối ưu hóa chi phí).

#### Quyết định (Decision)
Chúng tôi quyết định sử dụng Microsoft Azure thay vì AWS.

#### Hệ quả (Consequences)

##### Tích cực (Pros) - Phù hợp Module E

* **Chi phí (Cost)**: Tận dụng được gói Azure for Students (miễn phí $100 credits và các dịch vụ free tier 12 tháng), giúp giảm chi phí hạ tầng xuống mức tối thiểu cho dự án sinh viên.
* **Hệ sinh thái**: Tích hợp tốt với GitHub (cùng thuộc Microsoft), thuận lợi cho việc xây dựng CI/CD pipeline (Module E).
* **Công cụ quản lý**: Azure Portal và Azure Cost Management cung cấp giao diện trực quan để theo dõi ngân sách (Budget Alerts).

##### Tiêu cực (Cons)

* **Tài liệu tham khảo**: Tài liệu gốc của môn học hướng dẫn trên AWS, nên nhóm sẽ tốn thêm thời gian để "map" các khái niệm từ AWS sang Azure (ví dụ: EC2 -> Virtual Machine/App Service, RDS -> Azure Database for MySQL).

* **Cộng đồng**: Một số tutorial cụ thể cho đồ án này có thể ít hơn so với AWS.

##### Biện pháp giảm thiểu

* Sử dụng Terraform (ADR-006) để định nghĩa hạ tầng, giúp việc chuyển đổi provider dễ dàng hơn về mặt tư duy quản lý tài nguyên.