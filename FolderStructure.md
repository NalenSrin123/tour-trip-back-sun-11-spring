Here is a detailed breakdown of the **Tour Trip API** project structure. The project follows a **Feature-Based (Modular) Architecture** in Spring Boot, where code is grouped by domain features alongside shared core infrastructure.

---

### 📁 Root Directory Layout

| Directory / File | Description |
| :--- | :--- |
| [`pom.xml`](file:///d:/Document/CODES%20DEV/SV2-Y3/ETEC_Intern/ETEC_Intern_SpringBoot/tour-trip-back-sun-11-spring/pom.xml) | **Maven Configuration**: Manages Spring Boot dependencies (JPA, Web MVC, Validation, Lombok, MySQL/PostgreSQL drivers) and build plugins. |
| [`.mvn/`](file:///d:/Document/CODES%20DEV/SV2-Y3/ETEC_Intern/ETEC_Intern_SpringBoot/tour-trip-back-sun-11-spring/.mvn), `mvnw`, `mvnw.cmd` | **Maven Wrapper**: Allows building and running the project without requiring a pre-installed local Maven executable. |
| [`uploads/`](file:///d:/Document/CODES%20DEV/SV2-Y3/ETEC_Intern/ETEC_Intern_SpringBoot/tour-trip-back-sun-11-spring/uploads) | **Media Upload Storage**: Stores uploaded file assets locally (e.g., tour images, user avatars, receipts). |
| [`docs/`](file:///d:/Document/CODES%20DEV/SV2-Y3/ETEC_Intern/ETEC_Intern_SpringBoot/tour-trip-back-sun-11-spring/docs) | **Documentation**: Holds API specs (`docs/api`), Entity-Relationship Diagrams (`docs/erd`), Postman collections (`docs/postman`), and OpenAPI/Swagger specs (`docs/swagger`). |

---

### 📁 `src/main/resources/` (Configuration & Resources)

* [`application.properties`](file:///d:/Document/CODES%20DEV/SV2-Y3/ETEC_Intern/ETEC_Intern_SpringBoot/tour-trip-back-sun-11-spring/src/main/resources/application.properties): Central application configuration (server port `3000`, database credentials, JPA/Hibernate settings, Jackson settings).
* **`db/`**:
  * `db/migration/`: Database schema versioning & DDL migration scripts (e.g., Flyway or Liquibase scripts).
  * `db/seed/`: Initial database seeding scripts (default roles, admin users, initial categories).
* **`messages/`**: Internationalization (i18n) and localized error/validation message bundles.
* **`static/` & `templates/`**: Web static assets (CSS/JS) and server-rendered templates (if applicable).

---

### 📁 `src/main/java/com/etec/tourtripapi/` (Java Source Code)

#### 1. Main Entry Point

* [`TourTripApiApplication.java`](file:///d:/Document/CODES%20DEV/SV2-Y3/ETEC_Intern/ETEC_Intern_SpringBoot/tour-trip-back-sun-11-spring/src/main/java/com/etec/tourtripapi/TourTripApiApplication.java): Spring Boot `@SpringBootApplication` main class containing `main()`.

---

#### 2. Cross-Cutting & Core Infrastructure

* **[`common/`](file:///d:/Document/CODES%20DEV/SV2-Y3/ETEC_Intern/ETEC_Intern_SpringBoot/tour-trip-back-sun-11-spring/src/main/java/com/etec/tourtripapi/common)**: Shared utilities and reusable building blocks across all modules.
  * `common/response/`: Standardized REST API envelopes (`ApiResponse<T>`, `PaginationResponse<T>`).
  * `common/exception/`: Custom exceptions (`ResourceNotFoundException`, `BadRequestException`) and `@RestControllerAdvice` (`GlobalExceptionHandler`).
  * `common/model/`: Shared base entity (`BaseEntity` with JPA audit timestamps `createdAt` & `updatedAt`).
  * `common/constants/`: Application-wide constant definitions (e.g., Security, Pagination defaults).
  * `common/enums/`: Shared enums (e.g., `Status`, `Gender`, `SortDirection`).
  * `common/mapper/` & `common/utils/` & `common/helper/`: MapStruct/DTO mappers, date/string helper utilities.
  * `common/validator/`: Custom Bean Validation annotations and validators.
* **[`config/`](file:///d:/Document/CODES%20DEV/SV2-Y3/ETEC_Intern/ETEC_Intern_SpringBoot/tour-trip-back-sun-11-spring/src/main/java/com/etec/tourtripapi/config)**: Framework configuration classes (CORS mappings, JPA Auditing, OpenAPI/Swagger bean setup).
* **[`security/`](file:///d:/Document/CODES%20DEV/SV2-Y3/ETEC_Intern/ETEC_Intern_SpringBoot/tour-trip-back-sun-11-spring/src/main/java/com/etec/tourtripapi/security)**: Authentication & authorization logic (Spring Security, JWT filters, token providers, user details services).

---

#### 3. Domain / Feature Modules

Each module standardly encapsulates its own **Entity**, **DTOs** (Request/Response), **Repository**, **Service**, and **Controller**:

| Feature Module | Business Scope & Responsibilities |
| :--- | :--- |
| **`auth/`** | Authentication flow (Login, Register, Refresh Token, Password Reset). |
| **`user/`** | User management, user profiles, and account settings. |
| **`role/`** | User role definition and permission management (e.g., ADMIN, USER, GUIDE). |
| **`tour/`** | Tour listings, tour details, pricing, itineraries, and search/filtering. |
| **`category/`** | Tour categorization (e.g., Adventure, Cultural, Beach, Eco-tour). |
| **`destination/`** | Destination locations, cities, landmarks, and location metadata. |
| **`schedule/`** | Tour departure schedules, availability, and capacity planning. |
| **`guide/`** | Tour guide assignments, guide profiles, and availability. |
| **`booking/`** | Customer tour reservations, booking status, and guest details. |
| **`payment/`** | Payment transaction processing, payment gateways, and invoice generation. |
| **`review/`** | Customer ratings, reviews, and feedback for tours and guides. |
| **`notification/`** | Push notifications, email alerts, and system messages. |
| **`dashboard/`** | Admin & partner dashboard analytics and key performance indicators. |
| **`report/`** | Business reports, booking statistics, and revenue summary generation. |
| **`upload/`** | File upload service handling image & document processing. |
| **`setting/`** | Application system configurations and site metadata settings. |
