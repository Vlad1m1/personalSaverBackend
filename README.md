# Personal Rescue Backend

Spring Boot backend for the Personal Rescue mobile application.

## Run

Docker Compose with PostgreSQL:

```bash
docker compose up --build
```

Local run with an existing PostgreSQL:

```bash
./gradlew bootRun
```

Windows:

```powershell
.\gradlew.bat bootRun
```

## Environment

- `SPRING_DATASOURCE_URL` - PostgreSQL JDBC URL, default `jdbc:postgresql://localhost:5432/personalsaver`
- `SPRING_DATASOURCE_USERNAME` - database user, default `postgres`
- `SPRING_DATASOURCE_PASSWORD` - database password, default `postgres`
- `SPRING_JPA_HIBERNATE_DDL_AUTO` - schema mode, default `update`
- `ADMIN_API_KEY` - technical admin API key for `/api/admin/**`, default `dev-admin-api-key`
- `SOS_RATE_LIMIT_PER_MINUTE` - POST `/api/sos` requests allowed per IP per minute, default `5`

## Swagger / OpenAPI

Swagger UI:

- `http://localhost:8080/swagger-ui.html`
- `http://localhost:8080/swagger-ui/index.html`

OpenAPI JSON:

- `http://localhost:8080/v3/api-docs`

Public mobile endpoints do not require authentication:

- `GET /api/health`
- `GET /api/regions`
- `GET /api/regions/{id}`
- `GET /api/memo-categories`
- `GET /api/memos`
- `GET /api/memos/updates`
- `GET /api/memos/{id}`
- `GET /api/notifications`
- `GET /api/notifications/latest`
- `GET /api/notifications/{id}`
- `POST /api/sos`
- `GET /api/sos/{id}`
- `GET /api/sms/sos/{sosId}`

Admin endpoints under `/api/admin/**` require the technical `X-Admin-Api-Key` header. This is not user authorization and not a JWT.

Admin request example:

```bash
curl -X POST http://localhost:8080/api/admin/regions \
  -H "Content-Type: application/json" \
  -H "X-Admin-Api-Key: dev-admin-api-key" \
  -d '{"name":"Moscow","emergencyPhone":"112"}'
```

Public SOS example:

```bash
curl -X POST http://localhost:8080/api/sos \
  -H "Content-Type: application/json" \
  -d '{
    "targetType": "EMERGENCY_CONTACT",
    "latitude": 55.755864,
    "longitude": 37.617698,
    "accuracyMeters": 15,
    "address": "Moscow, Red Square",
    "message": "I need help",
    "contactName": "Mom",
    "contactPhone": "+79991234567"
  }'
```

Get memos:

```bash
curl "http://localhost:8080/api/memos?regionId=1"
```

Get notifications:

```bash
curl "http://localhost:8080/api/notifications?regionId=1&severity=WARNING&limit=20"
```

## Seed Data

For local development, Hibernate updates the schema through `ddl-auto=update`. On an empty database, `DataInitializer` seeds regions, memo categories, safety memos, and regional notifications.

## Checks

```bash
./gradlew test
```
