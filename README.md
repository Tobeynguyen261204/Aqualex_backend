# AquaLex

AquaLex là nền tảng hỗ trợ doanh nghiệp tạo và quản lý hợp đồng xuất khẩu thực phẩm, đặc biệt là các hợp đồng xuất khẩu thủy sản từ Việt Nam sang Đức và thị trường châu Âu.

Hệ thống được xây dựng nhằm giảm thời gian soạn thảo hợp đồng thủ công, chuẩn hóa thông tin giao dịch và tạo nền tảng cho các tính năng AI hỗ trợ sinh nội dung hợp đồng trong tương lai.

## Tổng quan sản phẩm

Trong hoạt động xuất khẩu, việc chuẩn bị hợp đồng thường cần nhiều thông tin như:

- Bên xuất khẩu và bên nhập khẩu.
- Sản phẩm và số lượng.
- Đơn vị tính, tiền tệ và đơn giá.
- Điều kiện thương mại quốc tế (Incoterms).
- Cảng đi và cảng đến.
- Trạng thái xử lý hợp đồng.

AquaLex tập trung quản lý các thông tin này trong một hệ thống thống nhất, giúp người dùng dễ dàng tạo, theo dõi và cập nhật hợp đồng.

## Người dùng mục tiêu

- Doanh nghiệp xuất khẩu thực phẩm và thủy sản.
- Nhân viên kinh doanh xuất nhập khẩu.
- Nhân viên phụ trách chứng từ và hợp đồng.
- Doanh nghiệp cần quản lý nhiều hợp đồng với các đối tác quốc tế.

## Chức năng chính

### Quản lý tài khoản

- Đăng ký tài khoản.
- Đăng nhập bằng email và mật khẩu.
- Mã hóa mật khẩu bằng BCrypt.
- Lấy thông tin hồ sơ người dùng.
- Đăng xuất và thu hồi phiên đăng nhập.

### Xác thực và bảo mật

- Xác thực người dùng bằng JWT access token.
- Refresh token giúp cấp access token mới khi access token hết hạn.
- Access token có thời hạn ngắn để tăng tính an toàn.
- Refresh token được lưu dưới dạng hash trong cơ sở dữ liệu.
- Access token sau khi đăng xuất được đưa vào Redis blacklist.
- Phân biệt quyền truy cập giữa người dùng chưa xác thực và người dùng đã đăng nhập.

### Quản lý hợp đồng

- Tạo hợp đồng xuất khẩu.
- Xem danh sách hợp đồng của người dùng hiện tại.
- Cập nhật thông tin hợp đồng.
- Xóa hợp đồng.
- Quản lý trạng thái hợp đồng.
- Bảo đảm người dùng chỉ truy cập các hợp đồng thuộc tài khoản của mình.

## Luồng sử dụng tổng quát

```text
Đăng ký tài khoản
        ↓
Đăng nhập
        ↓
Nhận access token và refresh token
        ↓
Tạo và quản lý hợp đồng
        ↓
Access token hết hạn
        ↓
Cấp access token mới bằng refresh token
        ↓
Đăng xuất và thu hồi phiên
```

## Công nghệ sử dụng

- Java 21
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Security
- Spring Data JPA
- PostgreSQL
- Flyway
- Redis
- JJWT
- Springdoc OpenAPI / Swagger UI
- Maven

## Kiến trúc backend

Backend được tổ chức theo các nhóm trách nhiệm chính:

```text
src/main/java/com/aquaLex
├── common
│   ├── config
│   ├── exception
│   ├── response
│   └── security
├── contract
│   ├── application
│   ├── controller
│   ├── domain
│   ├── dto
│   ├── infrastructure
│   └── security
└── health
```

- `controller`: cung cấp REST API.
- `application`: điều phối các use case và nghiệp vụ.
- `domain`: mô hình dữ liệu và quy tắc miền.
- `dto`: dữ liệu request/response.
- `infrastructure`: repository và truy cập cơ sở dữ liệu.
- `common`: cấu hình chung, response và xử lý exception.

## API chính

### Authentication

| Method | Endpoint | Mô tả |
|---|---|---|
| `POST` | `/api/v1/auth/register` | Đăng ký tài khoản |
| `POST` | `/api/v1/auth/login` | Đăng nhập |
| `POST` | `/api/v1/auth/refresh` | Cấp access token mới |
| `POST` | `/api/v1/auth/logout` | Đăng xuất và thu hồi token |
| `GET` | `/api/v1/auth/profile` | Xem hồ sơ người dùng |

### Contract

| Method | Endpoint | Mô tả |
|---|---|---|
| `POST` | `/api/v1/contracts/create` | Tạo hợp đồng |
| `GET` | `/api/v1/contracts/list` | Xem danh sách hợp đồng của người dùng |
| `PATCH` | `/api/v1/contracts/update/{contractId}` | Cập nhật hợp đồng |
| `DELETE` | `/api/v1/contracts/delete/{contractId}` | Xóa hợp đồng |

### Health check

```text
GET /api/v1/health
```

## Dữ liệu và lưu trữ

Hệ thống sử dụng:

- PostgreSQL để lưu người dùng, hợp đồng và refresh token.
- Redis để lưu danh sách access token đã bị thu hồi trong thời gian còn hiệu lực.
- Flyway để quản lý cấu trúc và phiên bản database.


## Trạng thái dự án

AquaLex Backend hiện cung cấp nền tảng REST API cho authentication và quản lý hợp đồng. Đây là backend core của hệ thống AquaLex, sẵn sàng làm nền tảng cho giao diện người dùng và các tính năng hỗ trợ tạo hợp đồng bằng AI.
