# Banner API

Base URL example: `http://localhost:8080`

## Auth

- Admin APIs require `Authorization: Bearer <token>`
- Public APIs do not require authentication

## Enums

`position`

- `HOME_TOP`
- `EVENT_POPUP`
- `SIDEBAR`

`status`

- `1`: enabled
- `0`: disabled

## Admin APIs

### Create banner

`POST /api/admin/banners`

```json
{
  "title": "Home hero banner",
  "positionCode": "HOME_TOP",
  "status": 1,
  "durationMs": 3000,
  "images": [
    {
      "imageUrl": "/uploads/banner_home/example-1.png",
      "linkUrl": "https://wingstars.com/events/summer",
      "displayOrder": 1,
      "status": 1,
      "startTime": "2026-04-14T00:00:00",
      "endTime": "2026-12-31T23:59:59"
    },
    {
      "imageUrl": "/uploads/banner_home/example-2.png",
      "linkUrl": null,
      "displayOrder": 2,
      "status": 1,
      "startTime": null,
      "endTime": null
    }
  ]
}
```

### Update banner

`PUT /api/admin/banners/{id}`

Updates full banner information.

### Update status

`PATCH /api/admin/banners/{id}/status`

Request body:
```json
{
  "status": 0
}
```
Returns: `BannerResponse`

### Restore banner

`PATCH /api/admin/banners/{id}/restore`

Restores a soft-deleted banner from trash.
Returns: `BannerResponse`

### Soft delete

`DELETE /api/admin/banners/{id}/soft`

Marks banner as deleted (`is_deleted = true`).

### Hard delete

`DELETE /api/admin/banners/{id}/hard`

Permanently removes banner and all its images from database.

### Get banner detail

`GET /api/admin/banners/{id}`

Returns: `BannerResponse`

### Get active admin list

`GET /api/admin/banners?page=0&pageSize=10`

Returns a paginated list of non-deleted banners.

### Get trash list

`GET /api/admin/banners/trash?page=0&pageSize=10`

Returns a paginated list of soft-deleted banners.

## Public API

### Get active banner by position

`GET /api/public/banners?positionCode=HOME_TOP`

Returns the single active banner for the requested position.

Conditions for appearing in Public API:
- Banner `status = 1`
- Banner `is_deleted = 0`
- Position matches `positionCode`
- Images inside banner are filtered by:
    - Image `status = 1` (internally `is_deleted = false`)
    - Current time is between `startTime` and `endTime` (if they are set)

Images are ordered by `displayOrder` ascending.

## Business behavior

- **Single Active Banner:** Only one banner can be active (`status = 1`) per position. Activating a banner will automatically deactivate others in the same position.
- **Image Time Window:** If `startTime` and `endTime` are equal, the image will only be visible for that exact second. To make it always visible, leave them as `null`.
- **Duration:** `durationMs` is set at the banner level and applies to all images within that banner (used for slideshow speed).
- **Cascade Delete:** Hard deleting a banner removes all its associated images.
