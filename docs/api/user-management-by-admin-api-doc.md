# User Management and Admin Profile API Documentation

- **Base URL**: `http://localhost:3000/api/v1`
- **Content-Type**: `application/json`
- **Authorization**: `Bearer <token>`

---

## IMPORTANT NOTE: REQUIRED ADMIN AUTHENTICATION

All User Management, Admin Profile, and Customer Management endpoints require administrative privileges.
You **MUST** log in as an administrator to obtain a Bearer JWT Token.

### How to obtain your Admin Bearer Token

1. **Step 1: Admin Login Request**
   - **Method**: `POST`
   - **Endpoint**: `/auth/login`
   - **Body**:

     ```json
     {
       "email": "thoeungsereymongkol@gmail.com", // or hengheng513.513@gmail.com, rithyysak7777@gmail.com, admin@gmail.com
       "password": "123"
     }
     ```

   - **Response**: Returns `{"requiresOtp": true, ...}` and dispatches a 6-digit OTP code to the email / server console logs.

2. **Step 2: Verify OTP Code**
   - **Method**: `POST`
   - **Endpoint**: `/auth/verify-otp`
   - **Body**:

     ```json
     {
       "email": "admin@gmail.com",
       "otpCode": "123456"
     }
     ```

   - **Response**: Returns `{"token": "eyJhbGciOiJIUzM4NCJ9...", "role": "ADMIN"}`.

3. **Step 3: Attach Header to Protected Endpoints**
   - **Header**: `Authorization: Bearer <token>`

---

## Authentication Endpoints

### 1. Logout

- **Method**: `POST`
- **Endpoint**: `/auth/logout` (also `/api/v2/auth/logout`)
- **Headers**: Optional `Authorization: Bearer <token>`
- **Description**: Clears the security context and invalidates the client session.
- **Response** (`200 OK`):

  ```json
  {
    "message": "Logout successful"
  }
  ```

---

## Admin Profile Endpoints

### 1. Get Admin Profile

- **Method**: `GET`
- **Endpoint**: `/admin/profile`
- **Headers**: `Authorization: Bearer <token>`
- **Description**: Retrieves the profile information of the currently authenticated administrator.
- **Response** (`200 OK`):

  ```json
  {
    "success": true,
    "status": 200,
    "message": "Admin profile retrieved successfully",
    "data": {
      "id": 1,
      "fullName": "System Administrator",
      "email": "admin@gmail.com",
      "role": "ADMIN",
      "userProfile": "Platform Administrator",
      "status": "ACTIVE",
      "avatarUrl": "https://example.com/admin-avatar.png",
      "createdAt": "2026-09-13T12:00:00",
      "updatedAt": "2026-09-20T11:00:00"
    },
    "timestamp": "2026-09-20T12:00:00"
  }
  ```

### 2. Update Admin Profile

- **Method**: `PUT`
- **Endpoint**: `/admin/profile`
- **Headers**: `Authorization: Bearer <token>`
- **Description**: Updates personal profile information and/or credentials for the currently authenticated administrator.
- **Request Body Parameters**:

| Field | Type | Required | Description |
| :--- | :--- | :--- | :--- |
| `fullName` | string | Yes | Full name of the admin (max 150 characters) |
| `userProfile` | string | Optional | Profile bio, notes, or role description |
| `avatarUrl` | string | Optional | URL link to avatar image |
| `password` | string | Optional | New password if changing credentials (min 6 characters) |

- **Example Request**:

  ```json
  {
    "fullName": "Super Administrator",
    "userProfile": "Lead administrator for Tour Trip platform",
    "avatarUrl": "https://example.com/avatar.png",
    "password": "newSecurePassword123"
  }
  ```

- **Response** (`200 OK`):

  ```json
  {
    "success": true,
    "status": 200,
    "message": "Admin profile updated successfully",
    "data": {
      "id": 1,
      "fullName": "Super Administrator",
      "email": "admin@gmail.com",
      "role": "ADMIN",
      "userProfile": "Lead administrator for Tour Trip platform",
      "status": "ACTIVE",
      "avatarUrl": "https://example.com/avatar.png",
      "createdAt": "2026-09-13T12:00:00",
      "updatedAt": "2026-09-20T12:05:00"
    },
    "timestamp": "2026-09-20T12:05:00"
  }
  ```

---

## Admin User Management Endpoints

### 1. Get Admin Users

- **Method**: `GET`
- **Endpoint**: `/admin/users`
- **Query Parameters**:

| Field | Type | Required | Description |
| :--- | :--- | :--- | :--- |
| `role` | string | Optional | Filter by role: `ADMIN` or `STAFF` (Default: `ADMIN`) |
| `status` | string | Optional | Filter by status: `ACTIVE`, `INACTIVE`, `SUSPENDED` |
| `name` | string | Optional | Partial search by full name |
| `email` | string | Optional | Filter by exact or partial email |

### 2. Get Admin User by ID

- **Method**: `GET`
- **Endpoint**: `/admin/users/{id}`

### 3. Create Admin User

- **Method**: `POST`
- **Endpoint**: `/admin/users`
- **Request Body Parameters**:

| Field | Type | Required | Description |
| :--- | :--- | :--- | :--- |
| `fullName` | string | Yes | Full name of the user |
| `email` | string | Yes | Valid unique email address |
| `password` | string | Yes | Password (min 6 characters) |
| `role` | string | Optional | Role assigned: `ADMIN` or `STAFF` (Default: `ADMIN`) |
| `userProfile` | string | Optional | Biography or operational notes |
| `avatarUrl` | string | Optional | URL to avatar image |

### 4. Update Admin User by ID

- **Method**: `PUT`
- **Endpoint**: `/admin/users/{id}`
- **Request Body Parameters**:

| Field | Type | Required | Description |
| :--- | :--- | :--- | :--- |
| `fullName` | string | Yes | Updated full name |
| `userProfile` | string | Optional | Updated biography or operational notes |
| `avatarUrl` | string | Optional | Updated avatar URL |
| `status` | string | Optional | Updated status: `ACTIVE`, `INACTIVE`, `SUSPENDED` |
| `password` | string | Optional | New password if changing credentials (min 6 chars) |

---

## Customer Management Endpoints

### 1. Get Customers

- **Method**: `GET`
- **Endpoint**: `/customers`
- **Query Parameters**:

| Field | Type | Required | Description |
| :--- | :--- | :--- | :--- |
| `name` | string | Optional | Search customers by name |
| `email` | string | Optional | Search customers by email |
| `status` | string | Optional | Filter by status: `ACTIVE`, `INACTIVE`, `SUSPENDED` |

### 2. Get Customer by ID

- **Method**: `GET`
- **Endpoint**: `/customers/{id}`

### 3. Update Customer

- **Method**: `PUT`
- **Endpoint**: `/customers/{id}`
- **Request Body Parameters**:

| Field | Type | Required | Description |
| :--- | :--- | :--- | :--- |
| `fullName` | string | Yes | Updated full name |
| `userProfile` | string | Optional | Updated customer bio or preferences |
| `avatarUrl` | string | Optional | Updated avatar image URL |

### 4. Activate Customer

- **Method**: `PATCH`
- **Endpoint**: `/customers/{id}/activate`

### 5. Deactivate Customer

- **Method**: `PATCH`
- **Endpoint**: `/customers/{id}/deactivate`

### 6. Update Customer Status

- **Method**: `PATCH`
- **Endpoint**: `/customers/{id}/status`
- **Request Body Parameters**:

| Field | Type | Required | Description |
| :--- | :--- | :--- | :--- |
| `status` | string | Yes | New status: `ACTIVE`, `INACTIVE`, `SUSPENDED` |

### 7. Delete Customer

- **Method**: `DELETE`
- **Endpoint**: `/customers/{id}`

---

## HTTP Status Codes

| Code | Meaning |
| :--- | :--- |
| `200` | Success / OK |
| `201` | Created |
| `400` | Bad Request (Validation failure or invalid parameters) |
| `401` | Unauthorized (Missing or invalid Bearer token) |
| `403` | Forbidden (User lacks required permissions or ADMIN role) |
| `404` | Not Found (Resource does not exist) |
| `500` | Internal Server Error |
