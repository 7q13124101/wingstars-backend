# Auth & Account Management API

Base URL example: `http://localhost:8080`

## Overview

- **Auth APIs**: Public APIs for user registration, login, and token refresh.
- **Account Management APIs**: Admin APIs for Super Admin to manage user accounts. Requires `ROLE_SUPER_ADMIN`.

## Auth APIs (Public)

### Register

`POST /api/auth/register`

Registers a new user account with default role `USER`.

**Request Body:**
```json
{
  "username": "johndoe",
  "password": "password123",
  "email": "john@example.com",
  "fullName": "John Doe",
  "phone": "0123456789",
  "gender": "MALE",
  "address": "123 Street, City",
  "imageUrl": null
}
```

### Login

`POST /api/auth/login`

Authenticates a user and returns access and refresh tokens.

**Request Body:**
```json
{
  "username": "johndoe",
  "password": "password123"
}
```

**Response Data:**
```json
{
  "accessToken": "eyJhbG...",
  "refreshToken": "abc123...",
  "tokenType": "Bearer"
}
```

### Refresh Token

`POST /api/auth/refresh-token`

Obtains a new access token using a valid refresh token.

**Request Body:**
```json
{
  "refreshToken": "abc123..."
}
```

## Account Management APIs (Super Admin)

Requires `Authorization: Bearer <super_admin_token>`

### Get all accounts

`GET /api/admin/accounts?page=0&size=10&sortBy=id&direction=desc&includeDeleted=true`

Returns a paginated list of all user accounts.
- `includeDeleted` (optional, default `true`): Whether to include locked/soft-deleted accounts.

### Get account detail

`GET /api/admin/accounts/{id}`

Returns full information of a specific user account.

### Create account (Internal)

`POST /api/admin/accounts`

Creates a new account with a specific role (e.g., ADMIN, SUPER_ADMIN).

**Request Body:**
```json
{
  "username": "admin_user",
  "password": "admin_password",
  "email": "admin@wingstars.com",
  "fullName": "System Admin",
  "phone": "0987654321",
  "gender": "FEMALE",
  "address": "HQ Office",
  "roleCode": "ADMIN"
}
```

### Update account

`PUT /api/admin/accounts/{id}`

Updates an existing user's information and role.

**Request Body:**
```json
{
  "email": "updated@example.com",
  "fullName": "Updated Name",
  "phone": "0111222333",
  "gender": "OTHER",
  "address": "New Address",
  "roleCode": "USER",
  "deleted": false
}
```

### Lock account

`DELETE /api/admin/accounts/{id}/lock`

Soft deletes (locks) a user account. This will also revoke all active refresh tokens for this user, forcing them to log out.

### Unlock account

`POST /api/admin/accounts/{id}/unlock`

Restores a locked user account.

## Response Structure

All APIs return the following structure:

```json
{
  "status": 200,
  "message": "Success message",
  "data": { ... },
  "timestamp": "2026-04-14T..."
}
```
