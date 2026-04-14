# Cheerleader API

Base URL example: `http://localhost:8080`

## Auth

- Admin APIs require `Authorization: Bearer <token>`
- Public APIs do not require authentication

## Admin APIs

### Get all cheerleaders

`GET /api/admin/cheerleaders?page=0&size=10&search=Linh`

Returns a paginated list of all cheerleaders.
- `search` (optional): Filter by full name (case-insensitive).

### Get cheerleader detail

`GET /api/admin/cheerleaders/{id}`

Returns full information of a cheerleader by ID.

### Create cheerleader

`POST /api/admin/cheerleaders`

```json
{
  "fullName": "Nguyen Thi A",
  "avatarUrl": "/uploads/cheerleader_avatar/example.png",
  "jerseyNumber": "12",
  "facebookUrl": "https://facebook.com/cheerleader.a",
  "instagramUrl": "https://instagram.com/cheerleader.a",
  "exclusiveAudioUrl": null,
  "photoFrameUrl": null,
  "aboutCheerleader": "Professional cheerleader with 5 years of experience.",
  "messageToFans": "Love you all!",
  "hobbies": "Dancing, Traveling",
  "heightCm": 165,
  "weightKg": 48,
  "birthDate": "2000-01-01",
  "zodiacSign": "Capricorn",
  "bloodType": "A"
}
```

### Update cheerleader

`PUT /api/admin/cheerleaders/{id}`

Updates existing cheerleader information. Supports partial updates for some fields (refer to implementation for specific logic).

### Delete cheerleader

`DELETE /api/admin/cheerleaders/{id}`

Soft deletes a cheerleader from the system.

## Public APIs

### Get cheerleader list

`GET /api/public/cheerleaders?page=0&size=10&search=Linh`

Returns a paginated list of active cheerleaders for the public view.

### Get cheerleader detail

`GET /api/public/cheerleaders/{id}`

Returns public details of a specific cheerleader.

## Response Structure (Pagination)

For list APIs, the response follows the `PageResponse` structure:

```json
{
  "status": 200,
  "message": "Success",
  "data": {
    "totalElements": 20,
    "totalPages": 2,
    "page": 0,
    "size": 10,
    "items": [
      {
        "id": 1,
        "name": "Nguyen Thi A",
        "number": "12",
        "avatarUrl": "/uploads/cheerleader_avatar/example.png",
        "social": {
          "facebook": "...",
          "instagram": "..."
        },
        "profile": {
          "birthdate": "2000-01-01",
          "sign": "Capricorn",
          "bloodType": "A",
          "height": "165",
          "weight": "48",
          "interest": "Dancing, Traveling"
        },
        "about": "...",
        "say": "..."
      }
    ],
    "first": true,
    "last": false,
    "empty": false,
    "hasNext": true,
    "hasPrevious": false
  },
  "timestamp": "2026-04-14T..."
}
```
