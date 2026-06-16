package com.vlad1m1.personal.config;

public final class OpenApiExamples {
    public static final String VALIDATION_ERROR = """
            {
              "timestamp": "2026-05-12T10:00:00Z",
              "status": 400,
              "error": "Bad Request",
              "message": "Validation error",
              "path": "/api/sos",
              "requestId": "7f3c2d8a",
              "details": [
                {
                  "field": "latitude",
                  "message": "must be greater than or equal to -90"
                }
              ]
            }
            """;

    public static final String ADMIN_KEY_ERROR = """
            {
              "timestamp": "2026-05-12T10:00:00Z",
              "status": 401,
              "error": "Unauthorized",
              "message": "Missing or invalid admin API key",
              "path": "/api/admin/memos",
              "requestId": "7f3c2d8a",
              "details": []
            }
            """;

    public static final String NOT_FOUND_ERROR = """
            {
              "timestamp": "2026-05-12T10:00:00Z",
              "status": 404,
              "error": "Not Found",
              "message": "Memo not found: 8c7a15f8-2f68-4d8d-8cbb-2b26f1c4b4d3",
              "path": "/api/memos/8c7a15f8-2f68-4d8d-8cbb-2b26f1c4b4d3",
              "requestId": "1c0f5ed0",
              "details": []
            }
            """;

    public static final String CONFLICT_ERROR = """
            {
              "timestamp": "2026-05-12T10:00:00Z",
              "status": 409,
              "error": "Conflict",
              "message": "Resource state conflicts with the requested operation",
              "path": "/api/admin/memos/8c7a15f8-2f68-4d8d-8cbb-2b26f1c4b4d3/publish",
              "requestId": "0d2b1f4a",
              "details": []
            }
            """;

    public static final String RATE_LIMIT_ERROR = """
            {
              "timestamp": "2026-05-12T10:00:00Z",
              "status": 429,
              "error": "Too Many Requests",
              "message": "Too many SOS requests from this IP address",
              "path": "/api/sos",
              "requestId": "2a71b8c9",
              "details": []
            }
            """;

    public static final String INTERNAL_ERROR = """
            {
              "timestamp": "2026-05-12T10:00:00Z",
              "status": 500,
              "error": "Internal Server Error",
              "message": "Internal server error",
              "path": "/api/memos",
              "requestId": "a6a0e1c8",
              "details": []
            }
            """;

    public static final String REGIONS_RESPONSE = """
            [
              {
                "id": 1,
                "name": "Moscow",
                "emergencyPhone": "112"
              }
            ]
            """;

    public static final String REGION_RESPONSE = """
            {
              "id": 1,
              "name": "Moscow",
              "emergencyPhone": "112"
            }
            """;

    public static final String ADMIN_REGION_CREATE_REQUEST = """
            {
              "name": "Moscow",
              "emergencyPhone": "112"
            }
            """;

    public static final String ADMIN_CATEGORY_REQUEST = """
            {
              "name": "First Aid",
              "iconName": "medical_services",
              "accentColor": "#D32F2F",
              "displayOrder": 1
            }
            """;

    public static final String CATEGORIES_RESPONSE = """
            [
              {
                "id": 1,
                "name": "First Aid",
                "iconName": "medical_services",
                "accentColor": "#D32F2F",
                "displayOrder": 1,
                "updatedAt": "2026-05-12T08:30:00"
              }
            ]
            """;

    public static final String MEMO_ITEM = """
            {
              "id": "8c7a15f8-2f68-4d8d-8cbb-2b26f1c4b4d3",
              "categoryId": 1,
              "regionId": null,
              "title": "What to do during a fire",
              "slug": "fire-safety",
              "shortDescription": "Short fire safety instruction",
              "contentHash": "sha256:4b4f3c0d7f9a",
              "version": 3,
              "updatedAt": "2026-05-12T10:00:00"
            }
            """;

    public static final String MEMOS_RESPONSE = """
            [
              {
                "id": "8c7a15f8-2f68-4d8d-8cbb-2b26f1c4b4d3",
                "categoryId": 1,
                "regionId": null,
                "title": "What to do during a fire",
                "slug": "fire-safety",
                "shortDescription": "Short fire safety instruction",
                "contentHash": "sha256:4b4f3c0d7f9a",
                "version": 3,
                "updatedAt": "2026-05-12T10:00:00"
              }
            ]
            """;

    public static final String MEMO_UPDATES_RESPONSE = """
            {
              "generatedAt": "2026-05-12T10:15:30Z",
              "memos": [
                {
                  "id": "8c7a15f8-2f68-4d8d-8cbb-2b26f1c4b4d3",
                  "categoryId": 1,
                  "regionId": 1,
                  "title": "Flood warning checklist",
                  "slug": "flood-warning-checklist",
                  "shortDescription": "Prepare documents and avoid flooded roads.",
                  "contentHash": "sha256:9a7c8e1d2f3b",
                  "version": 4,
                  "updatedAt": "2026-05-12T09:20:00"
                }
              ]
            }
            """;

    public static final String MEMO_DETAIL_RESPONSE = """
            {
              "id": "8c7a15f8-2f68-4d8d-8cbb-2b26f1c4b4d3",
              "categoryId": 1,
              "regionId": null,
              "title": "What to do during a fire",
              "slug": "fire-safety",
              "shortDescription": "Short fire safety instruction",
              "htmlContent": "<h1>What to do during a fire</h1><ol><li>Leave the building.</li><li>Call 112.</li></ol>",
              "contentHash": "sha256:4b4f3c0d7f9a",
              "version": 3,
              "critical": true,
              "updatedAt": "2026-05-12T10:00:00"
            }
            """;

    public static final String ADMIN_MEMO_REQUEST = """
            {
              "categoryId": 1,
              "regionId": null,
              "title": "What to do during a fire",
              "shortDescription": "Short fire safety instruction",
              "htmlContent": "<h1>What to do during a fire</h1><ol><li>Leave the building.</li><li>Call 112.</li></ol>",
              "steps": ["Leave the building", "Call 112"],
              "version": 3,
              "critical": true,
              "active": false
            }
            """;

    public static final String SOS_CONTACT_REQUEST = """
            {
              "targetType": "EMERGENCY_CONTACT",
              "latitude": 55.755864,
              "longitude": 37.617698,
              "accuracyMeters": 15,
              "address": "Moscow, Red Square",
              "message": "I need help",
              "contactName": "Mom",
              "contactPhone": "+79991234567"
            }
            """;

    public static final String SOS_SERVICE_REQUEST = """
            {
              "targetType": "EMERGENCY_SERVICE",
              "regionId": 1,
              "latitude": 55.755864,
              "longitude": 37.617698,
              "accuracyMeters": 15,
              "address": "Moscow, Red Square",
              "message": "A person needs urgent medical help"
            }
            """;

    public static final String SOS_RESPONSE = """
            {
              "id": "3b06b36f-8077-4f03-b8cf-bb7a7b9b6f6f",
              "targetType": "EMERGENCY_CONTACT",
              "regionId": null,
              "status": "SENT",
              "targetPhone": "+79991234567",
              "message": "I need help",
              "latitude": 55.755864,
              "longitude": 37.617698,
              "accuracyMeters": 15,
              "address": "Moscow, Red Square",
              "sms": {
                "recipientPhone": "+79991234567",
                "status": "SENT",
                "providerMessage": "Accepted by SMS provider"
              },
              "createdAt": "2026-05-12T10:15:30",
              "updatedAt": "2026-05-12T10:15:31"
            }
            """;

    public static final String NOTIFICATIONS_WARNING_RESPONSE = """
            [
              {
                "id": "d8df5b89-6845-4b2f-ae58-b3c6c8edc7fb",
                "regionId": 1,
                "title": "Storm warning",
                "text": "Strong wind up to 25 m/s is expected.",
                "severity": "WARNING",
                "publishedAt": "2026-05-12T08:30:00",
                "receivedAt": "2026-05-12T08:35:00"
              }
            ]
            """;

    public static final String NOTIFICATIONS_CRITICAL_RESPONSE = """
            [
              {
                "id": "f7f10c2f-8321-4d36-a49d-8b1f4124d40a",
                "regionId": 1,
                "title": "Evacuation required",
                "text": "Follow official evacuation instructions immediately.",
                "severity": "CRITICAL",
                "publishedAt": "2026-05-12T09:30:00",
                "receivedAt": "2026-05-12T09:31:00"
              }
            ]
            """;

    public static final String NOTIFICATION_RESPONSE = """
            {
              "id": "d8df5b89-6845-4b2f-ae58-b3c6c8edc7fb",
              "regionId": 1,
              "title": "Storm warning",
              "text": "Strong wind up to 25 m/s is expected.",
              "severity": "WARNING",
              "publishedAt": "2026-05-12T08:30:00",
              "receivedAt": "2026-05-12T08:35:00"
            }
            """;

    public static final String PARSING_LOG_RESPONSE = """
            [
              {
                "id": "2026-05-12T10:00:00Z",
                "startedAt": "2026-05-12T10:00:00Z",
                "finishedAt": "2026-05-12T10:00:03Z",
                "status": "SUCCESS",
                "source": "manual",
                "message": "Manual notification parsing completed",
                "createdCount": 2,
                "updatedCount": 1,
                "errorDetails": null
              }
            ]
            """;

    public static final String HEALTH_RESPONSE = """
            {
              "status": "UP",
              "service": "personal-rescue-backend",
              "timestamp": "2026-05-12T10:15:30Z"
            }
            """;

    private OpenApiExamples() {
    }
}
