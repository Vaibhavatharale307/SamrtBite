# SmartBite

SmartBite is a campus dining and digital-wallet ecosystem built around a React frontend and two Spring Boot services.

## Architecture

```text
React Frontend
      |
      | JWT
      v
SmartBite Main Service :8080
      |
      | REST + X-Internal-Api-Key
      v
SmartBite Wallet Service :8082

Main DB:   smartbite_db
Wallet DB: wallet_db
```

### Main Service
Responsible for:
- Authentication and JWT
- Users and roles
- Canteens
- Menus and food availability
- Orders
- Pickup slots
- Reviews
- Wallet-service orchestration

### Wallet Service
Responsible for:
- Wallets
- Balance
- Add money
- Deduction
- Credit/debit transactions
- Refund operations

### Frontend
React source for:
- Student portal
- Canteen manager portal
- Admin portal
- Login/registration
- Menu browsing/management
- Orders
- Wallet
- Transactions
- Pickup-slot selection

## Tech Stack

- Java 21
- Spring Boot 3.3.x
- Spring Security
- Spring Data JPA
- MySQL
- JWT
- React
- Vite-style frontend source
- Axios
- Bootstrap/CSS

## Local Backend Setup

Create the two MySQL databases:

```sql
CREATE DATABASE smartbite_db;
CREATE DATABASE wallet_db;
```

Set the environment variables described in:

- `backend/main-service/.env.example`
- `backend/wallet-service/.env.example`

Use the same `INTERNAL_API_KEY` value in both services.

Start Wallet Service first:

```bash
cd backend/wallet-service
./mvnw spring-boot:run
```

Then start Main Service:

```bash
cd backend/main-service
./mvnw spring-boot:run
```

Expected default ports:

- Main Service: `8080`
- Wallet Service: `8082`

## Important Repository Note

The supplied frontend ZIP contains only `src/`. It does not contain `package.json`, Vite configuration, or `index.html`. Those files should be restored from the original frontend development project before attempting a fresh npm/Vite build.

## Security

Do not commit:
- `.env` files
- database passwords
- JWT secrets
- internal service API keys
- production credentials

Use the provided `.env.example` files as templates.

## Current Project Audit Notes

The codebase contains the core SmartBite functionality, but there are known integration/readiness issues documented during the audit, including frontend build metadata being absent from the supplied frontend archive and a port/configuration mismatch in the original development files.

This repository preparation intentionally does not silently invent missing frontend build configuration or change business logic.
