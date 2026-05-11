# Personal Rescue Backend

## API Documentation

Swagger UI is available after the backend starts:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- Swagger UI direct page: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

Main API groups:

- `Regions`: regions used by regional memos, notifications, and SOS emergency phone routing.
- `SOS`: creates SOS events and returns delivery diagnostics. The backend does not know the mobile user and does not store the user's contacts.
- `SMS`: diagnostic SMS delivery status for SOS events.
- `Memo Categories`: categories for grouping safety memos in the Flutter UI.
- `Memos`: memo catalog, offline sync updates, and WebView HTML content.
- `Regional Notifications`: region-bound alerts and latest notification feed.
- `Health`: simple backend smoke-check endpoint.

Create SOS for an emergency contact:

```bash
curl -X POST http://localhost:8080/api/sos \
  -H "Content-Type: application/json" \
  -d '{
    "targetType": "EMERGENCY_CONTACT",
    "regionId": 1,
    "contactPhone": "+79991234567",
    "message": "I need help. My location is attached.",
    "latitude": 55.7558,
    "longitude": 37.6173
  }'
```

Load memos for a region:

```bash
curl "http://localhost:8080/api/memos?regionId=1"
```

Load regional notifications:

```bash
curl "http://localhost:8080/api/notifications?regionId=1"
```
