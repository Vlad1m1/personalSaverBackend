package com.vlad1m1.personal.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Personal Rescue API",
                description = "Backend API для мобильного приложения Personal Rescue",
                version = "1.0.0-MVP"
        ),
        tags = {
                @Tag(name = "Regions", description = "Регионы для памяток, уведомлений и выбора номера экстренной службы."),
                @Tag(name = "SOS", description = "Создание SOS-событий и диагностика результата отправки."),
                @Tag(name = "SMS", description = "Статусы SMS-доставки, связанные с SOS-сценариями."),
                @Tag(name = "Memos", description = "Список памяток, офлайн-синхронизация и HTML-контент для WebView."),
                @Tag(name = "Memo Categories", description = "Категории для группировки памяток в мобильном приложении."),
                @Tag(name = "Regional Notifications", description = "Региональные предупреждения и информационные уведомления."),
                @Tag(name = "Health", description = "Базовая проверка доступности backend-сервиса.")
        }
)
public class OpenApiConfig {
}
