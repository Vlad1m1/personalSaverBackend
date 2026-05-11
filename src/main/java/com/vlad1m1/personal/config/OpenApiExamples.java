package com.vlad1m1.personal.config;

public final class OpenApiExamples {
    public static final String VALIDATION_ERROR = """
            {
              "timestamp": "2026-05-12T10:15:30+03:00",
              "status": 400,
              "error": "Bad Request",
              "message": "Ошибка валидации",
              "path": "/api/sos",
              "requestId": "7a2d7f2c-5c59-49a0-b0f2-2f7b2a532c4a",
              "details": {
                "contactPhone": "contactPhone обязателен для EMERGENCY_CONTACT"
              }
            }
            """;

    public static final String NOT_FOUND_ERROR = """
            {
              "timestamp": "2026-05-12T10:15:30+03:00",
              "status": 404,
              "error": "Not Found",
              "message": "Регион не найден: 999",
              "path": "/api/regions/999",
              "requestId": "1c0f5ed0-7a95-4e45-9c64-7fdcc6b05ad3",
              "details": {}
            }
            """;

    public static final String INTERNAL_ERROR = """
            {
              "timestamp": "2026-05-12T10:15:30+03:00",
              "status": 500,
              "error": "Internal Server Error",
              "message": "Непредвиденная ошибка сервера",
              "path": "/api/memos",
              "requestId": "a6a0e1c8-a3d7-4322-9f9b-43e2fddfb982",
              "details": {}
            }
            """;

    public static final String REGIONS_RESPONSE = """
            [
              {
                "id": 1,
                "name": "Москва",
                "emergencyPhone": "112"
              },
              {
                "id": 2,
                "name": "Санкт-Петербург",
                "emergencyPhone": "112"
              }
            ]
            """;

    public static final String REGION_RESPONSE = """
            {
              "id": 1,
              "name": "Москва",
              "emergencyPhone": "112"
            }
            """;

    public static final String CATEGORIES_RESPONSE = """
            [
              {
                "id": 10,
                "name": "Первая помощь",
                "iconName": "medical_services",
                "accentColor": "#D32F2F",
                "displayOrder": 1,
                "updatedAt": "2026-05-12T08:30:00"
              }
            ]
            """;

    public static final String MEMOS_RESPONSE = """
            [
              {
                "id": "8c7a15f8-2f68-4d8d-8cbb-2b26f1c4b4d3",
                "categoryId": 10,
                "regionId": null,
                "title": "Что делать при пожаре",
                "shortDescription": "Основные шаги, чтобы защитить себя и вызвать экстренные службы.",
                "version": 3,
                "critical": true,
                "imageUrl": "https://cdn.example.com/memos/fire.png",
                "updatedAt": "2026-05-12T08:30:00"
              }
            ]
            """;

    public static final String MEMO_UPDATES_RESPONSE = """
            {
              "generatedAt": "2026-05-12T10:15:30+03:00",
              "memos": [
                {
                  "id": "8c7a15f8-2f68-4d8d-8cbb-2b26f1c4b4d3",
                  "categoryId": 10,
                  "regionId": 1,
                  "title": "Памятка при угрозе паводка",
                  "shortDescription": "Подготовьте документы и избегайте затопленных дорог.",
                  "version": 4,
                  "critical": true,
                  "imageUrl": "https://cdn.example.com/memos/flood.png",
                  "updatedAt": "2026-05-12T09:20:00"
                }
              ]
            }
            """;

    public static final String MEMO_DETAIL_RESPONSE = """
            {
              "id": "8c7a15f8-2f68-4d8d-8cbb-2b26f1c4b4d3",
              "categoryId": 10,
              "regionId": null,
              "title": "Что делать при пожаре",
              "shortDescription": "Основные шаги, чтобы защитить себя и вызвать экстренные службы.",
              "htmlContent": "<h1>Что делать при пожаре</h1><ol><li>Покиньте здание.</li><li>Позвоните 112.</li></ol>",
              "steps": ["Покиньте здание", "Позвоните 112", "Помогайте другим только если это безопасно"],
              "version": 3,
              "critical": true,
              "imageUrl": "https://cdn.example.com/memos/fire.png",
              "updatedAt": "2026-05-12T08:30:00"
            }
            """;

    public static final String SOS_CONTACT_REQUEST = """
            {
              "targetType": "EMERGENCY_CONTACT",
              "regionId": 1,
              "contactPhone": "+79991234567",
              "message": "Мне нужна помощь. Моя геопозиция приложена.",
              "latitude": 55.7558,
              "longitude": 37.6173
            }
            """;

    public static final String SOS_SERVICE_REQUEST = """
            {
              "targetType": "EMERGENCY_SERVICE",
              "regionId": 1,
              "contactPhone": "+79991234567",
              "message": "Требуется экстренная помощь.",
              "latitude": 55.7558,
              "longitude": 37.6173
            }
            """;

    public static final String SOS_RESPONSE = """
            {
              "id": "3b06b36f-8077-4f03-b8cf-bb7a7b9b6f6f",
              "targetType": "EMERGENCY_CONTACT",
              "regionId": 1,
              "status": "SENT",
              "targetPhone": "+79991234567",
              "message": "Мне нужна помощь. Моя геопозиция приложена.",
              "latitude": 55.7558,
              "longitude": 37.6173,
              "sms": {
                "recipientPhone": "+79991234567",
                "status": "SENT",
                "providerMessage": "Принято SMS-провайдером"
              },
              "createdAt": "2026-05-12T10:15:30",
              "updatedAt": "2026-05-12T10:15:31"
            }
            """;

    public static final String NOTIFICATIONS_RESPONSE = """
            [
              {
                "id": "d8df5b89-6845-4b2f-ae58-b3c6c8edc7fb",
                "regionId": 1,
                "title": "Предупреждение о сильном дожде",
                "message": "Избегайте низин и следуйте рекомендациям местных экстренных служб.",
                "severity": "WARNING",
                "publishedAt": "2026-05-12T08:30:00"
              }
            ]
            """;

    public static final String NOTIFICATION_RESPONSE = """
            {
              "id": "d8df5b89-6845-4b2f-ae58-b3c6c8edc7fb",
              "regionId": 1,
              "title": "Предупреждение о сильном дожде",
              "message": "Избегайте низин и следуйте рекомендациям местных экстренных служб.",
              "severity": "WARNING",
              "publishedAt": "2026-05-12T08:30:00"
            }
            """;

    public static final String HEALTH_RESPONSE = """
            {
              "status": "UP",
              "service": "personal-rescue-backend",
              "timestamp": "2026-05-12T10:15:30+03:00"
            }
            """;

    private OpenApiExamples() {
    }
}
