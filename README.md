# AquaLex Backend

Backend Java Spring Boot cho AquaLex - ứng dụng hỗ trợ tạo và quản lý hợp đồng xuất khẩu thực phẩm/thủy sản. Project hiện tập trung vào việc ôn tập và áp dụng các kiến thức backend thực tế như REST API, authentication, JWT, refresh token, Redis và quản lý migration database.

## Mục tiêu dự án

AquaLex hướng tới việc giúp doanh nghiệp:

- Quản lý thông tin hợp đồng xuất khẩu.
- Chuẩn hóa dữ liệu hợp đồng giữa bên xuất khẩu và bên nhập khẩu.
- Chuẩn bị nền tảng cho tính năng AI hỗ trợ tạo hợp đồng trong các giai đoạn tiếp theo.
- Xây dựng authentication flow có access token, refresh token và logout.

## Công nghệ sử dụng

- Java 21
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA / Hibernate
- Spring Security
- PostgreSQL
- Flyway
- Redis
- JJWT 0.12.6
- Springdoc OpenAPI / Swagger UI
- Maven

## Kiến trúc package

```text
src/main/java/com/aquaLex
├── common
│   ├── config
│   ├── exception
│   └── response
├── contract
│   ├── application
│   ├── controller
│   ├── domain
│   ├── dto
│   ├── infrastructure
│   └── security
└── health
```

Trách nhiệm chính:

- `controller`: nhận HTTP request và trả HTTP response.
- `application`: xử lý use case và nghiệp vụ.
- `domain`: entity và enum nghiệp vụ.
- `dto`: request/response object.
- `infrastructure`: repository truy cập database.
- `security`: tạo và kiểm tra JWT.
- `common.exception`: exception và global exception handler.
- `common.response`: format response thống nhất.

## Các tính năng hiện tại

### Authentication

- Đăng ký tài khoản.
- Đăng nhập bằng email và password.
- Hash password bằng BCrypt.
- Tạo access token JWT có thời hạn 15 phút.
- Tạo refresh token ngẫu nhiên có thời hạn 7 ngày.
- Lưu hash của refresh token vào database thay vì lưu raw token.
- Refresh access token.
- Logout:
  - Đưa access token vào Redis blacklist với TTL bằng thời gian còn lại của token.
  - Revoke refresh token trong database.
- Lấy profile của user hiện tại dựa trên access token.

### Contract

- Tạo hợp đồng.
- Lấy danh sách hợp đồng.
- Cập nhật hợp đồng.
- Xóa hợp đồng.

### Response và exception

API authentication sử dụng response wrapper thống nhất:

```json
{
  "success": true,
  "code": "USER_LOGGED_IN",
  "message": "User logged in successfully",
  "data": {},
  "timestamp": "2026-10-26T00:00:00Z",
  "path": "/api/v1/auth/login"
}
```

Một số mapping lỗi:

| Tình huống | HTTP status |
|---|---:|
| Email hoặc username đã tồn tại | `409 Conflict` |
| Sai thông tin đăng nhập | `401 Unauthorized` |
| Token không hợp lệ | `401 Unauthorized` |
| Request validation hoặc JSON không hợp lệ | `400 Bad Request` |
| Lỗi không mong đợi | `500 Internal Server Error` |

Service chịu trách nhiệm phát hiện lỗi nghiệp vụ. `GlobalExceptionHandler` chuyển exception thành HTTP response. Controller không cần tự bắt các exception nghiệp vụ.

## Cấu hình môi trường

Không commit password database, JWT secret, Redis password hoặc các credential khác lên Git.

Các cấu hình nên được truyền qua environment variables:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

jwt.secret=${JWT_SECRET}

spring.data.redis.host=${REDIS_HOST:localhost}
spring.data.redis.port=${REDIS_PORT:6379}
```

Nếu cần cấu hình local riêng, tạo file:

```text
src/main/resources/application-local.properties
```

và thêm file này vào `.gitignore`. Nếu secret đã từng được push lên remote, cần rotate secret ngay cả khi file đã được xóa khỏi commit mới.

## Yêu cầu cài đặt

Cần chuẩn bị:

- JDK 21
- PostgreSQL
- Redis
- Maven hoặc Maven Wrapper đi kèm project

Các service mặc định:

```text
PostgreSQL: localhost:5432
Redis:      localhost:6379
Application: localhost:8080
```

Địa chỉ database thực tế được cấu hình bằng `spring.datasource.*`.

## Chạy project

### Maven Wrapper trên Windows

```powershell
.\mvnw.cmd spring-boot:run
```

### Build

```powershell
.\mvnw.cmd clean package
```

### Compile không chạy test

```powershell
.\mvnw.cmd -DskipTests compile
```

Flyway sẽ tự chạy các migration trong:

```text
src/main/resources/db/migration
```

Các migration hiện có:

```text
V1__create_contracts_table.sql
V2__create_users_table.sql
V3__create_refresh_tokens_table.sql
```

## Swagger và OpenAPI

Sau khi ứng dụng khởi động:

```text
Swagger UI: http://localhost:8080/api/docs
OpenAPI:    http://localhost:8080/api-docs
```

Flow test authentication trên Swagger:

1. Gọi `POST /api/v1/auth/register`.
2. Gọi `POST /api/v1/auth/login`.
3. Copy `accessToken` và `refreshToken` từ response login.
4. Nhấn nút `Authorize` trên Swagger.
5. Dán access token vào scheme `bearerAuth`, không cần thêm tiền tố `Bearer`.
6. Gọi các API cần access token.
7. Với logout, nhập thêm raw refresh token vào header `Refresh-Token`.

## API hiện tại

### Health

| Method | Endpoint | Mô tả |
|---|---|---|
| `GET` | `/api/v1/health` | Kiểm tra trạng thái API |

### Authentication

| Method | Endpoint | Mô tả |
|---|---|---|
| `POST` | `/api/v1/auth/register` | Đăng ký user |
| `POST` | `/api/v1/auth/login` | Đăng nhập và nhận access/refresh token |
| `POST` | `/api/v1/auth/refresh` | Tạo access token mới từ refresh token |
| `POST` | `/api/v1/auth/logout` | Blacklist access token và revoke refresh token |
| `GET` | `/api/v1/auth/profile` | Lấy profile của user hiện tại |

Header refresh token:

```http
Refresh-Token: <raw-refresh-token>
```

Header access token:

```http
Authorization: Bearer <access-token>
```

### Contracts

| Method | Endpoint | Mô tả |
|---|---|---|
| `POST` | `/api/v1/contracts/create` | Tạo hợp đồng |
| `GET` | `/api/v1/contracts/list` | Lấy danh sách hợp đồng |
| `PATCH` | `/api/v1/contracts/update/{contractId}` | Cập nhật hợp đồng |
| `DELETE` | `/api/v1/contracts/delete/{contractId}` | Xóa hợp đồng |

## Authentication flow

```text
Register
   |
   v
Login
   |
   +--> access token, sống 15 phút
   |
   +--> refresh token, sống 7 ngày
          |
          v
Access token hết hạn
          |
          v
POST /api/v1/auth/refresh
          |
          v
Nhận access token mới
          |
          v
Logout
   |
   +--> access token vào Redis blacklist
   |
   +--> refresh token bị revoke trong database
```

Refresh token được hash bằng SHA-256 trước khi lưu vào database. Raw refresh token chỉ được trả về client tại thời điểm login hoặc refresh.

## Redis blacklist

Khi logout, access token được lưu vào Redis với key dạng:

```text
blacklist:access:<access-token>
```

TTL của key bằng thời gian còn lại của access token. Vì access token hiện sống 15 phút, key blacklist thường cũng tự hết hạn sau tối đa 15 phút.

## Phát triển tiếp theo

- Hoàn thiện JWT filter để mọi protected API thực sự xác thực access token.
- Đổi các route contract từ `permitAll` sang yêu cầu authentication.
- Hoàn thiện refresh token rotation và phát hiện reuse.
- Thêm phân quyền `USER` và `ADMIN`.
- Thêm kiểm tra user sở hữu contract.
- Thêm API đổi mật khẩu.
- Thêm flow quên mật khẩu và gửi email.
- Tích hợp AI để sinh nội dung hợp đồng từ dữ liệu nghiệp vụ.
- Bổ sung test cho authentication, refresh token, Redis blacklist và contract use case.

## Lưu ý bảo mật

- Không commit file cấu hình local chứa secret.
- Không sử dụng JWT secret mẫu trong môi trường production.
- Nếu secret đã bị push, hãy đổi secret/database password ngay.
- Không log access token, refresh token hoặc password.
- Không lưu raw refresh token vào database.
- Với production, nên lưu hash/JTI của access token trong Redis thay vì dùng toàn bộ JWT làm key.
