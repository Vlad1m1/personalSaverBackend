package com.vlad1m1.personal.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API Personal Saver",
                description = "Бэкенд API для мобильного приложения Personal Saver",
                version = "2.0.0"
        ),
        servers = {
                @Server(url = "https://personal-saver.ru", description = "Production HTTPS"),
                @Server(url = "http://localhost:8080", description = "Локальный запуск")
        },
        tags = {
                @Tag(name = "Public Regions", description = "Публичный справочник регионов для экранов приложения, памяток и SOS-маршрутизации."),
                @Tag(name = "Public SOS", description = "Создание SOS-событий и просмотр диагностического статуса. Backend не хранит список контактов пользователя."),
                @Tag(name = "Public Memos", description = "Каталог опубликованных памяток, лента обновлений для offline sync и HTML-контент для Flutter WebView."),
                @Tag(name = "Public Alarms", description = "Публичные тревожные уведомления по регионам."),
                @Tag(name = "Public Regional Notifications", description = "Активные региональные уведомления для списка и виджетов главного экрана."),
                @Tag(name = "Admin Regions", description = "Техническое управление регионами, защищенное X-Admin-Api-Key."),
                @Tag(name = "Admin Memo Categories", description = "Техническое управление категориями памяток, защищенное X-Admin-Api-Key."),
                @Tag(name = "Admin Memos", description = "Техническое управление памятками и публикацией, защищенное X-Admin-Api-Key."),
                @Tag(name = "Admin Alarms", description = "Техническое управление тревожными уведомлениями, защищенное X-Admin-Api-Key."),
                @Tag(name = "Admin Notifications", description = "Импорт уведомлений и диагностика парсинга, защищенные X-Admin-Api-Key."),
                @Tag(name = "Health", description = "Проверка доступности backend.")
        }
)
@SecurityScheme(
        name = "AdminApiKey",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = "X-Admin-Api-Key",
        description = "Технический API-ключ только для /api/admin/**. Это не пользовательская авторизация и не JWT."
)
public class OpenApiConfig {
}
