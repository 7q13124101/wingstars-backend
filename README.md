## Wingstars Backend

Multi-module Spring Boot backend.

### Requirements

- **JDK**: 17
- **Database**: MySQL 8+
- **OS**: Windows/macOS/Linux

### Quick start

1. Start MySQL and create a database (default: `wingstars`).
2. Set environment variables (see `.env.example`).
3. Build and run:

```bash
./mvnw -DskipTests package
./mvnw -pl app spring-boot:run
```

On Windows (PowerShell):

```powershell
.\mvnw.cmd -DskipTests package
.\mvnw.cmd -pl app spring-boot:run
```

### Environment variables

The app reads DB config from `app/src/main/resources/application.properties`.

- **DB_HOST**: MySQL host (default: `localhost`)
- **DB_PORT**: MySQL port (default: `3306`)
- **DB_NAME**: database name (default: `wingstars`)
- **DB_USER**: MySQL username (default: `root`)
- **DB_PASS**: MySQL password (default: `2001`)

JWT configuration (optional; defaults exist for local dev):

- **SECURITY_JWT_SECRET**: secret key for signing JWTs
- **SECURITY_JWT_ACCESS_TOKEN_EXPIRATION_MS**: access token TTL (ms)
- **SECURITY_JWT_REFRESH_TOKEN_EXPIRATION_MS**: refresh token TTL (ms)

### File uploads

- **file.upload-dir**: defaults to `uploads/`
- The backend will **auto-create** the upload directory on startup if missing.

### Common clone-and-run issues

- **MySQL not running / wrong credentials**: check `DB_*` env vars.
- **`uploads/` missing**: handled automatically on startup.

