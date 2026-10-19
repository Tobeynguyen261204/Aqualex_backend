# AI Contract - Hợp đồng xuất khẩu thực phẩm

## 1. Vấn đề cần giải quyết

Tạo hợp đồng xuất khẩu thực phẩm thủ công thường mất thời gian vì người dùng phải
đồng thời xử lý nghiệp vụ thương mại quốc tế, logistics và yêu cầu an toàn thực
phẩm:

- Tìm mẫu hợp đồng phù hợp với loại thực phẩm và thị trường nhập khẩu.
- Điền thông tin nhà xuất khẩu, nhà nhập khẩu, người nhận hàng và đại lý.
- Thống nhất Incoterms, cảng đi/cảng đến và phương thức vận chuyển.
- Mô tả sản phẩm, quy cách đóng gói, hạn sử dụng và điều kiện bảo quản.
- Đưa các chứng từ cần cung cấp vào hợp đồng.
- Kiểm tra điều khoản thanh toán, kiểm định, khiếu nại và xử lý hàng lỗi.
- Xuất thành file để gửi cho đối tác và bộ phận logistics.

Ứng dụng sẽ giúp người dùng tạo **bản nháp hợp đồng nhanh hơn**, nhưng không nên
được định vị là công cụ thay thế luật sư. Người dùng vẫn cần kiểm tra và chịu
trách nhiệm với nội dung cuối cùng.

## 2. Phạm vi MVP nên làm trước

MVP chỉ nên tập trung vào một luồng chính:

> Người dùng chọn loại hàng thực phẩm và thị trường nhập khẩu, nhập thông tin
> thương mại, hệ thống tạo bản nháp hợp đồng xuất khẩu có cấu trúc, cảnh báo
> dữ liệu thiếu và cho phép xuất file.

### Nên hỗ trợ ban đầu

- Hợp đồng mua bán/xuất khẩu thực phẩm.
- Một số nhóm hàng cụ thể: nông sản khô, cà phê, hạt, thủy sản đông lạnh.
- Một thị trường xuất khẩu đầu tiên, ví dụ Việt Nam -> Nhật Bản hoặc Việt Nam ->
  EU, thay vì hỗ trợ mọi quốc gia ngay.
- Hai ngôn ngữ cho mỗi hợp đồng: tiếng Việt và tiếng Anh.

### Chưa nên làm ngay

- Tư vấn pháp lý tự động có tính kết luận.
- Tự động ký hợp đồng.
- Tự động cam kết hợp đồng chắc chắn hợp pháp ở mọi địa phương.
- Hỗ trợ mọi loại hợp đồng ngay từ phiên bản đầu.
- Fine-tune mô hình ngay khi chưa có dữ liệu chất lượng.
- Tự kết luận sản phẩm có đủ điều kiện nhập khẩu hay chắc chắn đạt quy định của
  nước nhập khẩu.
- Tự sinh chứng nhận, số giấy phép, kết quả kiểm nghiệm hoặc thông tin hải quan.

## 3. Nhóm người dùng và use case

### UC-01: Tạo hợp đồng xuất khẩu từ biểu mẫu

Người dùng chọn thị trường, loại thực phẩm, điền các trường bắt buộc và nhấn tạo.

Ví dụ:

- Nhà xuất khẩu: Công ty ABC, Việt Nam.
- Nhà nhập khẩu: XYZ Foods, Nhật Bản.
- Sản phẩm: Cà phê rang xay, mã HS cần được xác nhận.
- Số lượng: 1.000 kg, quy cách 20 kg/thùng.
- Giá: 8 USD/kg, điều kiện FOB cảng Cát Lái, Incoterms 2020.
- Thanh toán: T/T, đặt cọc 30%, phần còn lại sau khi có vận đơn.
- Yêu cầu thêm: chứng nhận xuất xứ, phiếu kiểm nghiệm và chứng thư kiểm dịch.

### UC-02: Tạo hợp đồng bằng mô tả tự nhiên

Người dùng nhập:

> Tôi cần hợp đồng xuất khẩu 10 tấn hạt điều từ Việt Nam sang Đức, giao CIF
> Hamburg, thanh toán L/C, đóng gói 25 kg/túi, yêu cầu kiểm tra aflatoxin và
> có chứng nhận xuất xứ.

AI chuyển mô tả thành các trường dữ liệu và điều khoản có cấu trúc.

### UC-03: Hỏi lại khi thiếu dữ liệu hoặc điều kiện thương mại chưa rõ

Nếu người dùng chưa cung cấp thị trường nhập khẩu, điều kiện Incoterms, cảng,
đơn vị tiền tệ, phương thức thanh toán, tiêu chuẩn chất lượng hoặc thông tin một
bên, hệ thống không nên tự bịa. Hệ thống phải hỏi lại hoặc đánh dấu:

> Chưa có cảng đến và phiên bản Incoterms. Vui lòng bổ sung trước khi tạo bản nháp.

### UC-04: Xem và chỉnh sửa bản nháp

Người dùng có thể:

- Xem toàn bộ hợp đồng.
- Chỉnh sửa từng điều khoản.
- Yêu cầu AI viết lại một điều khoản.
- Yêu cầu AI giải thích một điều khoản bằng ngôn ngữ dễ hiểu.

### UC-05: Kiểm tra hợp đồng

Hệ thống kiểm tra ở mức hỗ trợ:

- Thiếu thông tin quan trọng.
- Điều khoản bị trùng.
- Mốc thời gian mâu thuẫn.
- Số tiền không thống nhất.
- Có từ khóa rủi ro cần người dùng xem lại.
- Điều kiện giao hàng không khớp với trách nhiệm bảo hiểm/vận chuyển.
- Thời hạn giao hàng không khớp với hạn sử dụng hoặc điều kiện bảo quản.
- Danh sách chứng từ không phù hợp với phương thức thanh toán hoặc thị trường.

Kết quả phải ghi rõ đây là cảnh báo hỗ trợ, không phải kết luận pháp lý.

### UC-06: Lưu phiên bản

Mỗi lần chỉnh sửa tạo một phiên bản hoặc lưu lịch sử thay đổi để người dùng có
thể quay lại bản trước.

### UC-07: Xuất và chia sẻ

Người dùng xuất bản hợp đồng thành:

- PDF.
- DOCX.
- Markdown hoặc HTML để xem trước.

## 4. Functional requirements

### FR-01: Quản lý tài khoản

- Đăng ký, đăng nhập, đăng xuất.
- Quản lý thông tin cá nhân.
- Phân quyền user/admin.
- Không lưu API key AI ở phía client.

### FR-02: Quản lý mẫu hợp đồng

- Danh sách mẫu theo loại thực phẩm, thị trường và ngôn ngữ.
- Mỗi mẫu có metadata: nước xuất khẩu, nước nhập khẩu, phiên bản Incoterms,
  phạm vi sử dụng và ngày cập nhật.
- Mẫu có các biến cần điền.
- Mẫu có các điều khoản bắt buộc và tùy chọn.
- Admin có thể thêm và cập nhật mẫu.

### FR-03: Thu thập thông tin

- Nhập bằng form có validation.
- Nhập bằng text tự nhiên.
- Xác định trường còn thiếu.
- Nhập và kiểm tra các nhóm dữ liệu: các bên, sản phẩm, số lượng, giá, tiền tệ,
  chất lượng, đóng gói, logistics, thanh toán, bảo hiểm, chứng từ và giải quyết
  tranh chấp.
- Bắt buộc chọn thị trường nhập khẩu và phiên bản Incoterms.
- Cho phép người dùng xác nhận dữ liệu trước khi gọi AI tạo bản nháp.

### FR-04: Sinh nội dung

- Tạo nội dung theo mẫu và dữ liệu đã xác nhận.
- Trả về nội dung có cấu trúc theo điều khoản.
- Có trạng thái `draft`, `generating`, `completed`, `failed`.
- Lưu model, prompt version và thời gian tạo để debug/audit.
- Tạo các điều khoản về giao hàng, chuyển rủi ro, kiểm định, bảo hiểm, chứng từ,
  khiếu nại chất lượng và hàng không phù hợp.

### FR-05: Chỉnh sửa bằng AI

Các thao tác nên có:

- Viết lại điều khoản.
- Rút gọn hoặc diễn giải.
- Đổi giọng văn trang trọng/dễ hiểu.
- Dịch ngôn ngữ.
- Thêm điều khoản theo yêu cầu.

### FR-06: Review và cảnh báo

- Phát hiện trường bỏ trống.
- So sánh dữ liệu giữa các điều khoản.
- Đưa cảnh báo kèm lý do.
- Không tự động sửa nội dung nhạy cảm mà không yêu cầu xác nhận.
- Cảnh báo rằng quy định vệ sinh, nhãn hàng hóa, kiểm dịch, dư lượng và giấy
  phép phải được kiểm tra theo nước nhập khẩu và loại sản phẩm.

### FR-07: Lưu trữ và xuất file

- Lưu hợp đồng và các phiên bản.
- Tìm kiếm theo tên, loại, ngày tạo.
- Xuất PDF/DOCX.
- Xóa hợp đồng theo chính sách lưu trữ.
- Xuất kèm checklist chứng từ cần chuẩn bị; checklist không phải là chứng từ
  pháp lý được hệ thống tự cấp.
 
## 5. Non-functional requirements

### Bảo mật

- Mật khẩu phải được hash, không lưu plain text.
- Phân quyền ở backend, không chỉ ẩn nút trên frontend.
- Mã hóa dữ liệu nhạy cảm khi cần.
- Không ghi nội dung hợp đồng đầy đủ vào log.
- Có audit log cho thao tác tạo, sửa, tải xuống và chia sẻ.
- Có cơ chế xóa dữ liệu theo yêu cầu người dùng.

### Độ tin cậy

- AI lỗi hoặc timeout phải trả lỗi rõ ràng.
- Có retry giới hạn cho lỗi tạm thời.
- Không tạo thành công giả khi AI chưa trả kết quả.
- Lưu trạng thái để người dùng biết tiến trình.

### Hiệu năng

- API thông thường phản hồi nhanh.
- Tác vụ AI dài nên chạy bất đồng bộ khi cần.
- Có giới hạn độ dài input và số lần gọi AI.
- Có rate limit theo tài khoản.

### Pháp lý và sản phẩm

- Hiển thị disclaimer: nội dung AI tạo chỉ là bản nháp tham khảo.
- Hiển thị model và thời điểm tạo nếu phù hợp.
- Người dùng phải xác nhận trước khi xuất bản cuối.
- Xác định rõ quốc gia, ngôn ngữ và phạm vi pháp lý mà sản phẩm hỗ trợ.

## 6. Kiến trúc backend Java đề xuất

### Công nghệ

- Java 21.
- Spring Boot.
- Spring Web cho REST API.
- Spring Validation để validate input.
- Spring Security cho authentication/authorization.
- Spring Data JPA.
- PostgreSQL.
- Flyway cho database migration.
- Redis nếu cần cache, rate limit hoặc lưu trạng thái job.
- Object Storage để lưu PDF/DOCX.

### Các module chính

```text
API Controller
    |
Application Service
    |
Domain Model / Business Rules
    |
Repository
    |
PostgreSQL

Application Service ---> AI Provider Adapter ---> LLM
Application Service ---> Document Renderer ---> PDF/DOCX
```

Nên tạo interface `AiContractGenerator` thay vì gọi trực tiếp SDK AI trong
controller. Nhờ vậy có thể đổi giữa OpenAI, Azure OpenAI hoặc model local mà
không phải sửa toàn bộ business logic.

### Entity dự kiến

- `User`: tài khoản.
- `ContractTemplate`: mẫu hợp đồng.
- `Contract`: hợp đồng.
- `ContractVersion`: phiên bản.
- `ContractParty`: các bên trong hợp đồng.
- `ProductSpecification`: tên hàng, mã HS tham chiếu, xuất xứ, tiêu chuẩn,
  quy cách, hạn sử dụng và điều kiện bảo quản.
- `Shipment`: số lượng, đơn vị, cảng đi, cảng đến, lịch giao, Incoterms và
  phương thức vận chuyển.
- `RequiredDocument`: commercial invoice, packing list, bill of lading,
  certificate of origin, phytosanitary/health certificate và chứng từ khác.
- `GenerationRequest`: lịch sử yêu cầu AI.
- `AuditLog`: lịch sử thao tác.

## 7. Chọn mô hình AI nào?

### Khuyến nghị cho MVP: dùng API model có sẵn

Giai đoạn đầu nên dùng model qua API thay vì tự train hoặc tự host. Lý do:

- Ra MVP nhanh.
- Chất lượng tiếng Việt tốt hơn so với tự xây từ đầu.
- Có thể tập trung học backend, database, security và workflow.
- Dễ thay model nhờ adapter.

### Lựa chọn thực tế

| Lựa chọn | Khi nên dùng | Điểm mạnh | Điểm cần lưu ý |
|---|---|---|---|
| OpenAI GPT-4o hoặc model tương đương mới | Prototype/MVP nhanh | Khả năng hiểu yêu cầu và viết tốt | Chi phí, dữ liệu gửi ra ngoài |
| Azure OpenAI | Cần triển khai trong Azure/doanh nghiệp | Quản trị, bảo mật và compliance tốt hơn | Cấu hình Azure và chi phí |
| Gemini API | Muốn thử thêm một provider | Context lớn, dễ thử nghiệm | Cần đánh giá riêng chất lượng tiếng Việt |
| Qwen/Llama qua Ollama hoặc vLLM | Muốn self-host, dữ liệu không rời hệ thống | Kiểm soát dữ liệu | Cần máy mạnh, vận hành và đánh giá chất lượng |

### Có cần fine-tune không?

Chưa cần ở MVP. Nên làm theo thứ tự:

1. Prompt có cấu trúc.
2. Template hợp đồng được kiểm soát.
3. Structured output bằng JSON schema.
4. RAG với kho mẫu/điều khoản đã được chuyên gia duyệt.
5. Đánh giá trên bộ test thực tế.
6. Chỉ fine-tune khi đã có dữ liệu đủ lớn, sạch và được phép sử dụng.

### Khuyến nghị cụ thể cho bài toán này

- **MVP**: dùng một model mạnh có khả năng tiếng Việt/tiếng Anh và structured
  output; ưu tiên Azure OpenAI nếu hợp đồng chứa thông tin thương mại nhạy cảm
  và bạn dự định triển khai trên Azure.
- **RAG**: lưu các mẫu hợp đồng, bộ điều khoản nội bộ, hướng dẫn Incoterms và
  checklist chứng từ đã được duyệt. Chỉ đưa tài liệu phù hợp với nước nhập khẩu
  vào context.
- **Không dùng RAG để khẳng định pháp lý**: tài liệu cần có nguồn, ngày hiệu lực
  và người duyệt. Nếu không có tài liệu đáng tin, hệ thống phải tạo cảnh báo.
- **Model nhỏ/local** có thể dùng cho việc trích xuất trường hoặc phân loại,
  nhưng nên dùng model mạnh hơn cho bản nháp cuối và review.
- Hãy lưu `provider`, `model`, `promptVersion`, `knowledgeVersion` và kết quả
  review để so sánh chất lượng sau này.

### Kiến trúc AI nên dùng

Không nên để model tự viết toàn bộ hợp đồng từ trang trắng. Nên dùng:

```text
User input
    -> Extractor: chuyển yêu cầu thành JSON
    -> Validator: kiểm tra trường thiếu/mâu thuẫn
    -> Template selector: chọn mẫu
    -> Clause retriever: lấy điều khoản phù hợp
    -> Generator: điền và viết bản nháp
    -> Reviewer: kiểm tra lỗi/cảnh báo
    -> Human review: người dùng xác nhận
```

Cách này giảm hallucination và dễ kiểm thử hơn.

## 8. Prompt và dữ liệu đầu vào

Model nên nhận dữ liệu đã được backend chuẩn hóa, ví dụ:

```json
{
  "contractType": "food_export_sales",
  "exportCountry": "VN",
  "importCountry": "DE",
  "languages": ["vi", "en"],
  "incoterms": {
    "rule": "CIF",
    "version": "2020",
    "departurePort": "Cat Lai",
    "arrivalPort": "Hamburg"
  },
  "parties": [
    {"role": "exporter", "name": "Công ty ABC"},
    {"role": "importer", "name": "XYZ Foods"}
  ],
  "product": {
    "name": "Hạt điều",
    "quantity": 10000,
    "unit": "kg",
    "packaging": "25 kg/túi",
    "origin": "Vietnam",
    "shelfLife": "12 tháng",
    "storage": "Nơi khô, mát"
  },
  "terms": {
    "unitPrice": 8,
    "currency": "USD",
    "paymentMethod": "L/C",
    "deliveryWindow": "2027-01-01 đến 2027-01-31"
  },
  "qualityRequirements": [
    "Kiểm tra aflatoxin",
    "Có chứng nhận xuất xứ"
  ],
  "requiredDocuments": [
    "commercial_invoice",
    "packing_list",
    "bill_of_lading",
    "certificate_of_origin"
  ]
}
```

Prompt nên yêu cầu:

- Chỉ dùng dữ liệu đã cung cấp.
- Không tự bịa thông tin còn thiếu.
- Trả về đúng schema.
- Đánh dấu nội dung cần xác nhận.
- Tách `contractContent`, `missingFields`, `warnings`.
- Không tự sinh mã HS, số giấy phép, số chứng nhận hoặc kết quả kiểm nghiệm.
- Nêu nguồn/phiên bản của tài liệu RAG khi tạo cảnh báo về compliance.

## 9. Flow chính của hệ thống

### Flow tạo hợp đồng

```text
1. User chọn hợp đồng xuất khẩu thực phẩm và nước nhập khẩu
2. Backend trả về form/schema tương ứng với thị trường và loại sản phẩm
3. User nhập thông tin hoặc mô tả tự nhiên
4. Backend validate dữ liệu cơ bản, đơn vị và định dạng
5. Extractor AI chuyển mô tả thành dữ liệu có cấu trúc
6. Backend hiển thị dữ liệu đã hiểu để user xác nhận
7. Backend kiểm tra trường bắt buộc: Incoterms, cảng, thanh toán, sản phẩm,
   chất lượng và chứng từ
8. Chọn template, điều khoản và tài liệu RAG phù hợp
9. Generator AI tạo bản nháp song ngữ nếu được yêu cầu
10. Reviewer kiểm tra mâu thuẫn về giá, số lượng, giao hàng, chất lượng và chứng từ
11. Trả bản nháp + cảnh báo + missing fields + checklist chứng từ
12. User chỉnh sửa và xác nhận
13. Backend lưu version và audit log
14. Người dùng hoặc chuyên gia kiểm tra trước khi phát hành
15. User xuất PDF/DOCX
```

### Flow chỉnh sửa một điều khoản

```text
1. User chọn điều khoản
2. User nhập yêu cầu thay đổi
3. Backend gửi riêng điều khoản và context cần thiết
4. AI trả nội dung mới cùng lý do thay đổi
5. User accept/reject
6. Backend tạo version mới nếu accept
```

### Flow xử lý lỗi AI

```text
AI timeout/rate limit
    -> retry có giới hạn
    -> nếu vẫn lỗi: lưu trạng thái failed
    -> trả thông báo rõ ràng
    -> cho phép user thử lại
```

## 10. API dự kiến

```text
POST   /api/v1/auth/register
POST   /api/v1/auth/login

GET    /api/v1/templates
GET    /api/v1/templates/{id}

POST   /api/v1/contracts
GET    /api/v1/contracts
GET    /api/v1/contracts/{id}
PATCH  /api/v1/contracts/{id}
DELETE /api/v1/contracts/{id}

POST   /api/v1/contracts/{id}/generate
POST   /api/v1/contracts/{id}/review
POST   /api/v1/contracts/{id}/rewrite-clause
POST   /api/v1/contracts/{id}/check-compliance
GET    /api/v1/contracts/{id}/required-documents

GET    /api/v1/contracts/{id}/versions
POST   /api/v1/contracts/{id}/export
```

## 11. Lộ trình học backend gắn với sản phẩm

### Phase 1: Java/Spring cơ bản

- REST controller.
- DTO, validation.
- Service và dependency injection.
- Exception handling.
- Unit test.

### Phase 2: Database

- PostgreSQL.
- Entity và quan hệ.
- JPA repository.
- Transaction.
- Pagination, filtering.
- Flyway migration.

### Phase 3: Authentication và bảo mật

- Spring Security.
- JWT hoặc session.
- Role/permission.
- Password hashing.
- Audit log.

### Phase 4: Tích hợp AI

- HTTP client.
- Provider adapter.
- Prompt versioning.
- Structured output.
- Timeout/retry/rate limit.
- Token/cost tracking.
- RAG theo thị trường nhập khẩu và loại thực phẩm.
- Evaluation set cho các trường hợp thiếu Incoterms, chứng từ hoặc tiêu chuẩn
  chất lượng.

### Phase 5: Tài liệu và workflow

- Render DOCX/PDF.
- Background job.
- Versioning.
- Object storage.
- Email hoặc chia sẻ link có hạn sử dụng.

### Phase 6: Chất lượng sản phẩm

- Bộ test prompt.
- Test hallucination và thiếu dữ liệu.
- Logging/monitoring.
- Security review.
- Đánh giá với người có kiến thức pháp lý.
- Đánh giá với người làm xuất nhập khẩu/logistics.
- Kiểm thử các ca sai đơn vị, tiền tệ, cảng, ngày giao và hạn sử dụng.

## 12. Tiêu chí MVP hoàn thành

- User tạo được hợp đồng xuất khẩu cho ít nhất 3 nhóm thực phẩm.
- User chọn được ít nhất một thị trường nhập khẩu có bộ form/checklist riêng.
- Input thiếu được phát hiện, không bị AI tự bịa im lặng.
- Kết quả có cấu trúc, có cảnh báo về chất lượng/compliance và checklist chứng từ.
- User chỉnh sửa và lưu được nhiều phiên bản.
- Xuất được PDF hoặc DOCX.
- Có đăng nhập và mỗi user chỉ thấy dữ liệu của mình.
- Có log lỗi và retry khi AI provider lỗi.
- Có disclaimer rõ ràng.
- Có bộ test tối thiểu cho API, service và các case AI quan trọng.

## 13. Quyết định khởi đầu được đề xuất

Để bắt đầu đơn giản và phù hợp mục tiêu ôn backend:

1. Java 21 + Spring Boot.
2. PostgreSQL + JPA.
3. Azure OpenAI hoặc OpenAI qua một interface adapter.
4. Chỉ hỗ trợ hợp đồng xuất khẩu thực phẩm, bắt đầu với một tuyến xuất khẩu cụ thể.
5. Dùng template + structured output + RAG có nguồn, chưa fine-tune.
6. Có bước user xác nhận dữ liệu trước và sau khi AI tạo.
7. Làm REST API trước, frontend làm sau.
8. Có người am hiểu xuất nhập khẩu/pháp lý duyệt template và checklist trước khi
   đưa vào sản phẩm.

Điểm quan trọng nhất của sản phẩm không phải chỉ là gọi một model để viết văn
bản. Giá trị nằm ở việc chuẩn hóa dữ liệu, chọn đúng mẫu, kiểm tra thiếu/mâu
thuẫn, lưu phiên bản và yêu cầu con người review trước khi sử dụng.

## 14. Phạm vi MVP đã chốt

### Tuyến thương mại

- Nước xuất khẩu: Việt Nam.
- Nước nhập khẩu: Đức, trong phạm vi quy định áp dụng tại EU.
- Ngôn ngữ hợp đồng: tiếng Việt và tiếng Anh ở phiên bản đầu.
- Hợp đồng: hợp đồng mua bán/xuất khẩu thủy sản đông lạnh.

Đây là phạm vi sản phẩm để thiết kế và kiểm thử ban đầu, không có nghĩa là hệ
thống tự xác nhận lô hàng đủ điều kiện nhập khẩu vào Đức/EU.

### Actor trong domain

| Actor | Trách nhiệm |
|---|---|
| Exporter | Cung cấp thông tin doanh nghiệp, sản phẩm, chứng từ và giao hàng |
| Importer | Cung cấp thông tin nhận hàng, yêu cầu nhập khẩu và thanh toán |
| Quality/Compliance Officer | Xác nhận tiêu chuẩn chất lượng, kiểm nghiệm và cảnh báo |
| Logistics Officer | Xác nhận container lạnh, cảng, lịch giao và chứng từ vận tải |
| Admin | Quản lý template, checklist, nguồn tài liệu và phiên bản |
| Legal Reviewer | Duyệt mẫu điều khoản trước khi đưa vào sử dụng |

### Dữ liệu domain bắt buộc

#### Thông tin các bên

- Tên pháp lý, địa chỉ, quốc gia.
- Mã số doanh nghiệp hoặc mã số thuế nếu cần.
- Người đại diện và thông tin liên hệ.
- Vai trò: exporter, importer, consignee, notify party.

#### Thông tin sản phẩm

- Tên thương mại và tên khoa học nếu có.
- Loài thủy sản, dạng sản phẩm: nguyên con, fillet, cắt khúc hoặc chế biến.
- Trạng thái: đông lạnh, nhiệt độ bảo quản và nhiệt độ vận chuyển.
- Khối lượng tịnh, khối lượng cả bì, số kiện và quy cách đóng gói.
- Xuất xứ, vùng nuôi/khai thác nếu có.
- Mã HS chỉ được lưu là giá trị do người dùng cung cấp hoặc giá trị tham khảo
  cần xác nhận; AI không được tự khẳng định mã chính xác.
- Hạn sử dụng, ngày sản xuất và yêu cầu truy xuất lô.

#### Thương mại và giao hàng

- Đơn giá, tổng giá trị, tiền tệ và dung sai số lượng.
- Incoterms và phiên bản, ví dụ CIF Incoterms 2020.
- Cảng đi, cảng đến, địa điểm giao nhận và thời hạn giao.
- Phương thức vận tải, container lạnh và điều kiện nhiệt độ.
- Phương thức thanh toán: T/T, L/C hoặc phương án khác.
- Phân bổ chi phí, rủi ro, bảo hiểm và trách nhiệm thông quan.

#### Chất lượng và chứng từ

- Tiêu chuẩn chất lượng được hai bên thống nhất.
- Yêu cầu kiểm nghiệm, lấy mẫu và thời điểm kiểm tra.
- Điều kiện chấp nhận/từ chối hàng.
- Quy trình xử lý hàng không phù hợp, khiếu nại và bồi thường.
- Checklist chứng từ dự kiến: invoice, packing list, bill of lading, certificate
  of origin, health/veterinary certificate và chứng từ khác theo lô hàng.
- Mỗi chứng từ phải có trạng thái `required`, `optional`, `provided` hoặc
  `needs_confirmation`.

### Business rules đầu tiên

1. Không thể tạo bản nháp hoàn chỉnh nếu thiếu exporter, importer, sản phẩm,
   số lượng, giá, tiền tệ, Incoterms, cảng đi hoặc cảng đến.
2. Nếu chọn `CIF`, hệ thống phải yêu cầu user xác nhận phạm vi bảo hiểm và bên
   chịu trách nhiệm mua bảo hiểm; không tự suy diễn chi tiết ngoài dữ liệu nguồn.
3. Nếu sản phẩm là thủy sản đông lạnh, phải có nhiệt độ bảo quản/vận chuyển và
   quy cách đóng gói hoặc hệ thống phải cảnh báo thiếu dữ liệu.
4. Không cho phép hợp đồng có ngày giao trước ngày ký mà không có cảnh báo xác
   nhận.
5. Tổng giá trị phải nhất quán với số lượng, đơn giá và đơn vị.
6. Đơn vị tiền tệ trong giá, thanh toán và tổng giá trị phải nhất quán.
7. Các điều khoản về chất lượng, kiểm nghiệm và chứng từ phải được đánh dấu là
   do user cung cấp, từ template, hoặc tham khảo từ tài liệu có nguồn.
8. Nếu thiếu thông tin quy định EU/Đức phù hợp với loại thủy sản, hệ thống phải
   trả `needs_confirmation`, không trả kết luận đạt/không đạt.

### Kết quả của bước phân tích domain

Sau bước này, MVP cần có bốn nhóm đầu ra trước khi bắt đầu code:

1. **Contract input schema**: toàn bộ trường dữ liệu và trường bắt buộc.
2. **Clause map**: mỗi dữ liệu sẽ đi vào điều khoản nào.
3. **Document checklist**: chứng từ, trạng thái và người chịu trách nhiệm.
4. **Validation rules**: lỗi chặn tạo bản nháp và cảnh báo cần người dùng xác nhận.

### Các điểm cần chuyên gia xác nhận trước khi phát hành

- Bộ chứng từ chính xác cho từng loại thủy sản và từng tuyến vận chuyển.
- Tiêu chuẩn chất lượng, kiểm nghiệm và yêu cầu truy xuất lô.
- Điều kiện bảo hiểm và trách nhiệm theo Incoterms được chọn.
- Quy định nhãn, kiểm dịch, an toàn thực phẩm và điều kiện nhập khẩu hiện hành.
- Mẫu điều khoản xử lý hàng bị từ chối, chậm giao và tranh chấp.

Các nội dung trên nên được lưu kèm nguồn, ngày hiệu lực và người duyệt. AI chỉ
được sử dụng chúng để hỗ trợ soạn thảo và cảnh báo, không thay thế việc xác nhận
của chuyên gia.

## 15. Quyết định nền tảng: Web App trước

### Quyết định

Phiên bản đầu tiên sẽ là **Web App**, chưa làm mobile app song song.

### Lý do chọn Web App

- Người dùng doanh nghiệp thường tạo và kiểm tra hợp đồng trên laptop/desktop.
- Form hợp đồng có nhiều trường dữ liệu, bảng sản phẩm, chứng từ và điều khoản.
- Dễ xem song song dữ liệu đầu vào, bản nháp AI và checklist chứng từ.
- Dễ tải lên/tải xuống PDF, DOCX và tài liệu kiểm nghiệm.
- Dễ triển khai, cập nhật và sửa lỗi mà không cần phát hành qua app store.
- Phù hợp với mục tiêu ôn backend Java bằng REST API và database.

### Kiến trúc nền tảng

```text
Web Browser
    |
Frontend Web App
    |
REST API - Spring Boot
    |
Business Domain + Validation
    |             |             |
PostgreSQL      AI Provider    File/Object Storage
```

Frontend sẽ dùng React. Backend sẽ cung cấp REST API độc lập bằng Spring Boot
Java để sau này có thể dùng lại cho mobile app hoặc các client khác.

### Stack công nghệ đã chốt

#### Frontend

- React.
- React Router cho điều hướng màn hình.
- TypeScript được khuyến nghị để kiểm soát kiểu dữ liệu form và API response.
- React Hook Form cho form nhiều trường.
- Một thư viện component UI để tạo bảng, modal, stepper và form.
- Axios hoặc Fetch để gọi REST API.

#### Backend

- Java 21.
- Spring Boot.
- Spring Web cho REST API.
- Spring Validation cho validation request.
- Spring Security cho authentication và authorization.
- Spring Data JPA/Hibernate.
- PostgreSQL.
- Flyway cho database migration.
- OpenAI hoặc Azure OpenAI thông qua một AI provider adapter.

#### Tích hợp và vận hành

- Object Storage cho PDF, DOCX và chứng từ upload.
- Redis chỉ thêm khi cần rate limit, cache hoặc xử lý job.
- Docker để chạy local các service như PostgreSQL.
- OpenAPI/Swagger để mô tả và thử REST API.

### Nguyên tắc giao tiếp Frontend - Backend

```text
React UI
    -> HTTP/JSON
REST API - Spring Boot
    -> DTO validation
    -> Business service
    -> Database / AI provider / file storage
```

- React không gọi trực tiếp AI provider.
- React không chứa secret hoặc API key.
- Backend chịu trách nhiệm xác thực, phân quyền và kiểm tra dữ liệu.
- API response nên có format thống nhất cho thành công và lỗi.
- Các DTO request/response phải được định nghĩa rõ trước khi làm UI.
- Những tác vụ AI hoặc export file lâu nên có trạng thái job để frontend theo dõi.

### Các màn hình Web MVP

1. Đăng nhập.
2. Dashboard danh sách hợp đồng.
3. Chọn tuyến Việt Nam -> Đức/EU và loại thủy sản.
4. Form thông tin các bên.
5. Form sản phẩm, số lượng, đóng gói và nhiệt độ.
6. Form giá, Incoterms, cảng và thanh toán.
7. Form chứng từ và tiêu chuẩn chất lượng.
8. Màn hình xác nhận dữ liệu trước khi gọi AI.
9. Màn hình xem bản nháp và cảnh báo.
10. Màn hình chỉnh sửa/version history.
11. Màn hình checklist chứng từ.
12. Màn hình xuất PDF/DOCX.

### Mobile app sẽ làm sau

Mobile app có thể được thêm ở giai đoạn sau cho các thao tác nhanh:

- Xem trạng thái hợp đồng.
- Nhận thông báo.
- Duyệt hoặc từ chối bản nháp.
- Chụp/tải lên chứng từ.
- Tra cứu checklist.

Không nên bắt đầu bằng mobile vì việc nhập nhiều thông tin thương mại và chỉnh
điều khoản dài sẽ kém thuận tiện hơn trên màn hình nhỏ.

## 16. Quyết định kiến trúc: Modular Monolith

### Quyết định

AquaLex sẽ bắt đầu bằng kiến trúc **Modular Monolith**:

```text
React Web
    |
    | REST/JSON
    v
Spring Boot Application
    |
    +-- Auth Module
    +-- Contract Module
    +-- Template Module
    +-- Product Module
    +-- Shipment Module
    +-- Compliance Module
    +-- AI Module
    +-- Document Module
    +-- Audit Module
    |
    +-- PostgreSQL
    +-- Object Storage
```

Các module chạy trong cùng một ứng dụng Spring Boot và dùng chung database ở
giai đoạn MVP, nhưng phải được tách biệt rõ về package, trách nhiệm và interface.

### Nguyên tắc module

- Mỗi module sở hữu domain logic của mình.
- Controller chỉ nhận request, validate DTO và gọi application service.
- Không gọi trực tiếp repository của module khác.
- Không gọi trực tiếp AI SDK từ controller hoặc contract domain.
- Module giao tiếp qua service/interface rõ ràng.
- Không chia sẻ entity JPA giữa các module nếu không cần thiết.
- Các thay đổi dữ liệu liên quan nhiều module phải có transaction boundary rõ ràng.
- Dùng domain event hoặc application event cho các hành động cần xử lý sau.

### Package dự kiến

```text
com.aqualex
├── common
├── auth
├── contract
├── template
├── product
├── shipment
├── compliance
├── ai
├── document
└── audit
```

Mỗi module có thể tổ chức theo cấu trúc:

```text
contract/
├── controller/
├── dto/
├── application/
├── domain/
└── infrastructure/
```

### Ranh giới module MVP

| Module | Trách nhiệm chính |
|---|---|
| `auth` | User, đăng nhập, role và permission |
| `contract` | Hợp đồng, trạng thái, version và workflow review |
| `template` | Mẫu hợp đồng, clause và phiên bản template |
| `product` | Thông tin thủy sản đông lạnh và yêu cầu sản phẩm |
| `shipment` | Số lượng, đóng gói, cảng, Incoterms và vận chuyển |
| `compliance` | Checklist chứng từ, validation và cảnh báo |
| `ai` | Extract, generate, rewrite, review và provider adapter |
| `document` | Render PDF/DOCX và quản lý file |
| `audit` | Lịch sử thao tác và tracking thay đổi |

### Lộ trình tách thành Microservice sau này

Chỉ tách module thành service riêng khi có nhu cầu thực tế về scale, deploy độc
lập, bảo mật hoặc ownership. Module ưu tiên tách đầu tiên là `ai`, tiếp theo có
thể là `document` hoặc `compliance`.

Khi tách service, cần thay lời gọi nội bộ bằng REST hoặc message broker, đồng
thời đánh giá lại transaction, database ownership, retry, idempotency và
observability. Không tách service chỉ vì muốn có nhiều service.
