# Ranking API

Base URL example: `http://localhost:8080`

## Auth

- Admin APIs require `Authorization: Bearer <token>`
- Public APIs do not require authentication

## Admin APIs

### List all categories

`GET /api/admin/ranking/categories`

Returns all ranking categories.

### Get category detail

`GET /api/admin/ranking/categories/{id}`

### Create category

`POST /api/admin/ranking/categories`

```json
{
  "name": "Most Popular 2026",
  "typeCode": "POPULAR_2026",
  "status": true
}
```

### Update category

`PUT /api/admin/ranking/categories/{id}`

### Soft delete category

`DELETE /api/admin/ranking/categories/{id}/soft`

### Hard delete category

`DELETE /api/admin/ranking/categories/{id}/hard`

### Add cheerleader to rank

`POST /api/admin/ranking/categories/{id}/entries`

```json
{
  "cheerleaderId": 1,
  "rankPosition": 1,
  "score": 1500,
  "cheerleaderImageUrl": "/uploads/ranking/idol-1.png"
}
```

### Update ranking entry

`PUT /api/admin/ranking/entries/{id}`

### Remove cheerleader from rank

`DELETE /api/admin/ranking/entries/{id}`

## Public APIs

### List active categories

`GET /api/public/ranking/categories`

### Get category detail

`GET /api/public/ranking/categories/{id}`

### List entries in category

`GET /api/public/ranking/categories/{id}/entries`

Returns the list of cheerleaders in the ranking, ordered by `rankPosition` ascending.

## Response Structure

Standard `ApiResponse` wrapper:

```json
{
  "status": 200,
  "message": "Success",
  "data": { ... },
  "timestamp": "..."
}
```
