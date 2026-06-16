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
                title = "Personal Rescue API",
                description = "Production-like backend API for Personal Rescue mobile application",
                version = "2.0.0"
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "local"),
                @Server(url = "http://localhost:8080", description = "docker")
        },
        tags = {
                @Tag(name = "Public Regions", description = "Read-only region catalog for mobile screens and SOS routing."),
                @Tag(name = "Public SOS", description = "Mobile SOS creation and diagnostic lookup. The backend does not know app users and does not store user contact lists."),
                @Tag(name = "Public Memos", description = "Published memo catalog, offline update feed, and HTML content for Flutter WebView."),
                @Tag(name = "Public Regional Notifications", description = "Active regional notifications for mobile list and home screen widgets."),
                @Tag(name = "Admin Regions", description = "Technical region management endpoints protected by X-Admin-Api-Key."),
                @Tag(name = "Admin Memo Categories", description = "Technical memo category management endpoints protected by X-Admin-Api-Key."),
                @Tag(name = "Admin Memos", description = "Technical memo management and publish workflow endpoints protected by X-Admin-Api-Key."),
                @Tag(name = "Admin Notifications", description = "Technical notification import and parsing diagnostics protected by X-Admin-Api-Key."),
                @Tag(name = "Health", description = "Backend availability checks.")
        }
)
@SecurityScheme(
        name = "AdminApiKey",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = "X-Admin-Api-Key",
        description = "Technical API key for /api/admin/** only. This is not user authorization and not a JWT."
)
public class OpenApiConfig {
}
