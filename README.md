# 🍽️ SmartBite

> **SmartBite is a campus dining and digital wallet ecosystem that lets students discover canteens, browse menus, place food orders, select pickup slots, and pay through a digital wallet, while giving canteen managers and administrators dedicated management interfaces.**

---

## 📌 Project Overview

SmartBite is designed to digitize the campus food-ordering experience.

The system separates the core application and wallet functionality into independent Spring Boot services:

- **Main Service** — authentication, users, roles, canteens, menus, orders, pickup slots and reviews.
- **Wallet Service** — wallet balance and financial transactions.
- **React Frontend** — student, canteen manager and administrator interfaces.

The services communicate over REST APIs, while the wallet service maintains its own database.

---

## 🎯 Objectives

- Provide a simple digital food-ordering experience for students.
- Reduce queues and manual order handling at campus canteens.
- Provide pickup-slot based ordering.
- Prevent orders when food is unavailable or wallet balance is insufficient.
- Maintain wallet transactions separately from the main application data.
- Give canteen managers tools to manage menus and order status.
- Provide role-based access for students, managers and administrators.

---

## 🏗️ Architecture

```text
                         ┌──────────────────────────┐
                         │      React Frontend      │
                         │                          │
                         │ Student / Manager / Admin│
                         └────────────┬─────────────┘
                                      │
                              JWT Bearer Token
                                      │
                                      ▼
                    ┌─────────────────────────────────┐
                    │      SmartBite Main Service     │
                    │            :8080                │
                    │                                 │
                    │ Auth & JWT                      │
                    │ Users & Roles                   │
                    │ Canteens & Menus                │
                    │ Orders & Pickup Slots           │
                    │ Reviews                         │
                    │ Wallet Orchestration            │
                    └───────────────┬─────────────────┘
                                    │
                         REST + Internal API Key
                                    │
                                    ▼
                    ┌─────────────────────────────────┐
                    │        Wallet Service           │
                    │            :8082                │
                    │                                 │
                    │ Wallets                         │
                    │ Add Money                       │
                    │ Deduction                       │
                    │ Refunds                         │
                    │ Transactions                   │
                    └───────────────┬─────────────────┘
                                    │
                         ┌──────────┴──────────┐
                         ▼                     ▼
                  smartbite_db             wallet_db
```

### Service Responsibilities

| Component | Responsibility | Default Port |
|---|---|---:|
| React Frontend | User interfaces and API integration | `5173`* |
| Main Service | Auth, users, canteens, menus, orders, reviews | `8080` |
| Wallet Service | Wallet and transactions | `8082` |
| MySQL | Main application data | `3306` |
| MySQL | Wallet data | `3306` |

\* The supplied frontend source uses Vite-style configuration, but the uploaded frontend archive did not include its project-level `package.json`/Vite metadata.

---

## ✨ Key Features

### 🔐 Authentication & Authorization

- JWT-based authentication
- BCrypt password hashing
- Role-based authorization
- Roles:
  - `STUDENT`
  - `CANTEEN_MANAGER`
  - `ADMIN`
- Protected frontend routes
- Backend method-level authorization

### 👨‍🎓 Student Features

- Student registration
- Login
- Browse canteens
- Browse available menu items
- Select quantity
- Select pickup slot
- Place orders
- View order history
- Track order status
- Cancel eligible orders
- View wallet balance
- Add money
- View wallet transactions

### 👨‍🍳 Canteen Manager Features

- Manager authentication
- View canteen orders
- Update order status
- Add menu items
- Edit menu items
- Delete menu items
- Enable/disable food availability
- Access restricted to the assigned canteen

### 🛠️ Admin Features

- Manage users
- Manage canteens
- Assign managers to canteens
- Manage canteen data
- Administrative access through role-based authorization

### 💳 Digital Wallet

- Wallet creation
- Balance management
- Add money
- Deduct money during order placement
- Refund during eligible cancellation
- Credit/debit transaction history
- Separate wallet database
- Optimistic locking using JPA `@Version`

### 🕐 Pickup Slots

The current implementation supports predefined pickup-slot strings and a basic capacity limit.

Current capacity:

> **Maximum 20 orders per pickup-slot value**

### 📦 Order Lifecycle

```text
PENDING_PAYMENT
       │
       ▼
    PLACED
       │
       ▼
  PREPARING
       │
       ▼
    READY
       │
       ▼
  COMPLETED
```

Cancellation/payment failure states are also supported:

```text
PENDING_PAYMENT → PAYMENT_FAILED

PLACED / PREPARING → CANCELLED
                       │
                       ▼
                    REFUND
```

---

## 🧩 Technology Stack

### Backend

- Java 21
- Spring Boot 3.3.x
- Spring Web
- Spring Data JPA
- Spring Security
- JWT
- Bean Validation
- MySQL
- Lombok
- Springdoc OpenAPI / Swagger
- Spring Boot Actuator
- JUnit / Mockito

### Frontend

- React
- React Router
- Axios
- Bootstrap
- CSS
- Vite-style environment configuration
- Browser `localStorage` for client-side authentication state

### Architecture

- Service-oriented / microservice-style backend
- REST API communication
- Independent databases
- JWT authentication
- Internal API-key protected service-to-service communication

---

## 🗄️ Database Design

SmartBite uses separate databases for the main application and wallet service.

### Main Database — `smartbite_db`

Important entities:

```text
User
Role
Canteen
Menu
Order
OrderReview
```

High-level relationships:

```text
Role
  │
  └── 1:N ── User
                 │
                 └── N:1 ── Canteen

Canteen
  │
  └── 1:N ── Menu
```

Orders and reviews store logical IDs such as `userId`, `canteenId`, `foodId` and `orderId`.

### Wallet Database — `wallet_db`

```text
Wallet
WalletTransaction
```

The wallet service keeps `userId` as a logical cross-service reference rather than a relational foreign key to the main database.

---

## 🔄 End-to-End Order Flow

```text
Student
   │
   ▼
Login
   │
   ▼
JWT issued
   │
   ▼
Browse Canteen
   │
   ▼
Select Food + Quantity
   │
   ▼
Select Pickup Slot
   │
   ▼
Place Order
   │
   ├── Validate user
   ├── Validate food
   ├── Check availability
   ├── Calculate total
   └── Check pickup-slot capacity
   │
   ▼
Wallet Service
   │
   ├── Check balance
   └── Deduct amount
   │
   ▼
Order → PLACED
   │
   ▼
Manager updates status
   │
   ├── PREPARING
   ├── READY
   └── COMPLETED
```

---

## 🔗 Important APIs

### Authentication

| Method | Endpoint | Purpose |
|---|---|---|
| `POST` | `/auth/register` | Register user |
| `POST` | `/auth/login` | Login and obtain JWT |
| `GET` | `/auth/test` | Authentication test |

### Canteen & Menu

| Method | Endpoint | Purpose |
|---|---|---|
| `GET` | `/canteen` | List canteens |
| `POST` | `/canteen` | Create canteen |
| `PUT` | `/canteen/{canteenId}` | Update canteen |
| `DELETE` | `/canteen/{canteenId}` | Delete canteen |
| `GET` | `/canteen/menu/{canteenId}` | Get canteen menu |
| `POST` | `/canteen/menu` | Add menu item |
| `PUT` | `/canteen/menu/{foodId}` | Update menu item |
| `DELETE` | `/canteen/menu/{foodId}` | Delete menu item |

### Orders

| Method | Endpoint | Purpose |
|---|---|---|
| `POST` | `/order` | Place order |
| `GET` | `/order/{id}` | Get order |
| `GET` | `/order/user/{userId}` | User order history |
| `GET` | `/order/canteen/{canteenId}` | Canteen orders |
| `PUT` | `/order/{orderId}/status` | Update order status |
| `PUT` | `/order/cancel/{orderId}` | Cancel order |

### Wallet

| Method | Endpoint | Purpose |
|---|---|---|
| `GET` | `/wallet/{userId}` | Get wallet |
| `GET` | `/wallet/{userId}/history` | Transaction history |
| `PUT` | `/wallet/addMoney` | Add money |

The Main Service internally communicates with the Wallet Service for wallet creation, deduction and refund operations.

---

## 🔒 Security Flow

### Frontend → Main Service

```text
Login
  │
  ▼
JWT Token
  │
  ▼
Axios Authorization Header
  │
  ▼
Authorization: Bearer <JWT>
```

### Main Service → Wallet Service

```text
Main Service
     │
     ▼
X-Internal-Api-Key
     │
     ▼
Wallet Service
```

The wallet service is protected from unauthenticated direct requests by an internal API-key filter.

---

## 📁 Repository Structure

```text
SmartBite/
│
├── backend/
│   ├── main-service/
│   │   ├── pom.xml
│   │   └── src/
│   │
│   └── wallet-service/
│       ├── pom.xml
│       └── src/
│
├── frontend/
│   ├── src/
│   └── README.md
│
├── docs/
│   ├── PROJECT-AUDIT.md
│   └── PREPARATION-NOTES.md
│
├── .gitignore
└── README.md
```

---

## 🚀 Backend Setup

### Prerequisites

Install:

- Java 21
- Maven or Maven Wrapper
- MySQL 8+
- Node.js/npm for the frontend
- Git

### 1. Create Databases

```sql
CREATE DATABASE smartbite_db;
CREATE DATABASE wallet_db;
```

### 2. Configure Main Service

Go to:

```text
backend/main-service/
```

Use `.env.example` as the configuration template.

Required values include:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
JWT_SECRET
WALLET_SERVICE_URL
INTERNAL_API_KEY
CORS_ALLOWED_ORIGIN
SERVER_PORT
```

Default service port:

```text
8080
```

### 3. Configure Wallet Service

Go to:

```text
backend/wallet-service/
```

Configure:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
INTERNAL_API_KEY
SERVER_PORT
```

Default service port:

```text
8082
```

The `INTERNAL_API_KEY` must match between Main Service and Wallet Service.

### 4. Start Wallet Service

Windows:

```powershell
cd backend/wallet-service
.\mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
cd backend/wallet-service
./mvnw spring-boot:run
```

### 5. Start Main Service

Windows:

```powershell
cd backend/main-service
.\mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
cd backend/main-service
./mvnw spring-boot:run
```

---

## 🖥️ Frontend Setup

The supplied frontend archive contains the React source under:

```text
frontend/src/
```

However, the uploaded frontend archive did not include the project-level npm/Vite files such as:

```text
package.json
vite.config.*
index.html
```

Therefore those files should be restored from the original frontend development project before running a fresh npm/Vite build.

Once the complete frontend project is available, the expected workflow is:

```bash
npm install
npm run dev
```

---

## ⚙️ Configuration & Environment Variables

Never commit real credentials.

Use:

```text
.env.example
```

as a template and create local environment configuration separately.

Sensitive values include:

- Database passwords
- JWT signing secret
- Internal service API key
- Production credentials

These are intentionally excluded through `.gitignore`.

---

## 🧪 Testing

The backend services contain unit/integration test infrastructure using:

- JUnit
- Mockito
- Spring Boot Test
- Spring Security Test

Run tests from each service:

```bash
./mvnw test
```

On Windows:

```powershell
.\mvnw.cmd test
```

---

## 📊 Current Implementation Status

| Feature | Status |
|---|---|
| JWT Authentication | ✅ Implemented |
| Role-Based Authorization | ✅ Implemented |
| Student Registration | ✅ Implemented |
| Manager Authentication | ✅ Implemented |
| Admin Authentication | ✅ Implemented |
| Canteen Management | ✅ Implemented |
| Menu Management | ✅ Implemented |
| Food Availability | ✅ Implemented |
| Order Placement | ✅ Implemented |
| Pickup Slot Selection | ✅ Implemented |
| Pickup Capacity | ⚠️ Basic implementation |
| Wallet Creation | ✅ Implemented |
| Add Money | ✅ Implemented |
| Wallet Deduction | ✅ Implemented |
| Refund | ⚠️ Implemented with limitations |
| Transaction History | ✅ Implemented |
| Order Status Transitions | ✅ Implemented |
| Student Dashboard | ✅ Implemented |
| Manager Dashboard | ✅ Implemented |
| Admin Dashboard | ✅ Implemented |
| Reviews | ⚠️ Backend implemented; frontend UI incomplete |
| Forgot Password | ❌ Backend endpoint not implemented |
| OTP Verification | ❌ Backend implementation not available |
| Dynamic Pickup Slot Management | ❌ Not implemented |

---

## ⚠️ Known Limitations

The current project is a working academic/project implementation, but several areas can be improved for production use:

1. **Pickup-slot capacity**
   - Current capacity is a simple count-based check.
   - It is not atomic under concurrent requests.
   - Capacity is not date-specific.
   - Capacity is not currently isolated per canteen.

2. **Distributed payment consistency**
   - Main Service and Wallet Service use separate databases.
   - A production system would need stronger distributed consistency/error-recovery patterns.

3. **Forgot password**
   - Frontend routes/UI exist, but matching backend endpoints were not found in the supplied Main Service.

4. **Reviews**
   - Backend review functionality exists, while the supplied frontend does not expose the complete review workflow.

5. **Frontend build metadata**
   - The supplied frontend ZIP contains the React source but not the project-level npm/Vite metadata.

6. **Production configuration**
   - Secrets should always be supplied through environment/secret-management infrastructure.

---

## 🔮 Future Enhancements

Potential next improvements:

- Dynamic pickup-slot management
- Date-specific pickup slots
- Per-canteen slot capacity
- Atomic slot reservation
- Payment gateway integration
- Email/SMS notifications
- Forgot-password + OTP flow
- Complete review UI
- Food image upload/storage
- Search and filtering
- Pagination
- Docker Compose deployment
- API Gateway
- Centralized configuration
- Distributed tracing
- Redis caching
- CI/CD pipeline
- Cloud deployment

---

## 🎓 Project Learning Outcomes

SmartBite demonstrates practical implementation of:

- Spring Boot REST APIs
- Spring Security
- JWT authentication
- Role-based authorization
- JPA/Hibernate
- MySQL database design
- Microservice/service separation
- REST communication between services
- Inter-service security
- React frontend development
- Axios API integration
- State and authentication management
- Order lifecycle management
- Wallet/transaction modelling
- Exception handling
- Backend validation
- Optimistic locking

---

## 👥 User Roles

### Student

```text
Register/Login
     ↓
Browse Canteens
     ↓
Browse Menu
     ↓
Order Food
     ↓
Select Pickup Slot
     ↓
Pay from Wallet
     ↓
Track Order
     ↓
Pickup Food
```

### Canteen Manager

```text
Login
  ↓
View Assigned Canteen
  ↓
Manage Menu
  ↓
Manage Availability
  ↓
View Orders
  ↓
Update Order Status
```

### Admin

```text
Login
  ↓
Manage Users
  ↓
Manage Canteens
  ↓
Assign Managers
  ↓
Manage Platform Data
```

---

## 📚 Documentation

Additional project documentation:

- [`docs/PROJECT-AUDIT.md`](docs/PROJECT-AUDIT.md)
- [`docs/PREPARATION-NOTES.md`](docs/PREPARATION-NOTES.md)

---

## 🛡️ GitHub Security

The repository intentionally excludes:

```text
.env
target/
node_modules/
IDE metadata
build artifacts
```

Do not commit real:

```text
database passwords
JWT secrets
API keys
production credentials
```

---

## 📜 License

This project was developed as an academic/project implementation.

If you intend to distribute or reuse it, add the appropriate license and attribution information here.

---

## 👨‍💻 Author

**Vaibhav Atharale**

SmartBite — Campus Dining & Digital Wallet Ecosystem

---

<p align="center">
  Built with ❤️ using React, Spring Boot, Spring Security, JPA and MySQL.
</p>
