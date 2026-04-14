# Media API

Base URL example: `http://localhost:8080`

## Auth

- Admin APIs require `Authorization: Bearer <token>`
- Public APIs do not require authentication

## Enums

`moduleSource`
- `CHEERLEADER_AVATAR`
- `CHEERLEADER_AUDIO`
- `CHEERLEADER_GALLERY`
- `BANNER_HOME`
- `BANNER_PROMO`
- `USER_AVATAR`
- `SYSTEM_ASSET`
- `UNKNOWN`

## Admin APIs

### Upload file

`POST /api/admin/files/upload`

**Content-Type:** `multipart/form-data`

- `file`: (MultipartFile) The actual file to upload.
- `module_source`: (String) Destination module enum.
- `title` (optional): (String) Descriptive title.
- `jump_url` (optional): (String) Redirect link.
- `display_order` (optional): (Integer) Sort order.

### Update/Replace file

`PUT /api/admin/files/{id}`

**Content-Type:** `multipart/form-data`

- `file` (optional): (MultipartFile) New file to replace the old one.
- `title` (optional): (String)
- `jump_url` (optional): (String)
- `display_order` (optional): (Integer)
- `is_active` (optional): (Boolean)

### Delete file

`DELETE /api/admin/files/{id}`

Soft deletes the file asset.

### Get admin file list

`GET /api/admin/files?page=0&size=10&moduleSource=BANNER_HOME`

Returns a paginated list of non-deleted files.

## Public APIs

### Get public file list

`GET /api/public/files?page=0&size=10&moduleSource=BANNER_HOME`

Returns a paginated list of active files.

### Get file detail

`GET /api/public/files/{id}`

Returns information of a specific file.

## Response Structure (Pagination)

For list APIs, the response follows the `PageResponse` structure:

```json
{
  "status": 200,
  "message": "Success",
  "data": {
    "totalElements": 1,
    "totalPages": 1,
    "page": 0,
    "size": 10,
    "items": [
      {
        "mediaId": 1,
        "fileUrl": "/uploads/banner_home/uuid.png",
        "moduleSource": "BANNER_HOME",
        "title": "Home Banner",
        "jumpUrl": "https://example.com",
        "displayOrder": 0,
        "isActive": true
      }
    ],
    "first": true,
    "last": true,
    "empty": false,
    "hasNext": false,
    "hasPrevious": false
  },
  "timestamp": "2026-04-14T..."
}
```
