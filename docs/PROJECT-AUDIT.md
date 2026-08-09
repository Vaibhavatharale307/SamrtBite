# SmartBite Project Audit

## Services

1. Main Service
2. Wallet Service
3. React Frontend

## Main Service Responsibilities

Authentication, JWT, roles, users, canteens, menus, orders, pickup slots, reviews and wallet-service calls.

## Wallet Service Responsibilities

Wallet creation, balance, add money, deduction, transactions and refunds.

## Security

Main Service uses JWT and role-based authorization.

Main Service communicates with Wallet Service using an internal API key in the `X-Internal-Api-Key` header.

## Databases

Main Service:
- `smartbite_db`

Wallet Service:
- `wallet_db`

The wallet's `userId` is a logical cross-service reference rather than a relational foreign key.

## Known Issues From Source Audit

- Original Main Service configuration had a 8080/8081 inconsistency.
- Supplied frontend archive lacks package/Vite project metadata.
- Forgot-password frontend routes exist, but corresponding backend endpoints were not found.
- Pickup-slot capacity is a basic count-based implementation and is not atomic/per-canteen/date-specific.
- Review authorization should be tightened to verify order ownership.
- Some demo seeders contain hardcoded demo credentials.
- Main and Wallet services use separate databases, so order/payment consistency is distributed rather than transactional across both databases.
