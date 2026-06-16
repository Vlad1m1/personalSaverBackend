package com.vlad1m1.personal;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:personalsaver;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"
})
@AutoConfigureMockMvc
class PersonalSaverApplicationTests {
    private static final String ADMIN_API_KEY = "dev-admin-api-key";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
    }

    @Test
    void openApiUsesHttpsProductionServerByDefault() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.servers[0].url").value("https://personal-saver.ru"));
    }

    @Test
    void seedDataIsExposedThroughApi() throws Exception {
        mockMvc.perform(get("/api/regions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(6)));

        MvcResult memosResult = mockMvc.perform(get("/api/memos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(14)))
                .andExpect(jsonPath("$[*].title", hasItems(
                        "Первая помощь при ожоге",
                        "Первая помощь при кровотечении",
                        "Первая помощь при потере сознания",
                        "Действия при пожаре",
                        "Действия при угрозе наводнения",
                        "Действия при сильном ветре",
                        "Первая помощь при переломе",
                        "Первая помощь при вывихе или растяжении",
                        "Первая помощь при отравлении",
                        "Первая помощь при тепловом ударе",
                        "Первая помощь при переохлаждении",
                        "Первая помощь при укусе животного",
                        "Действия при землетрясении",
                        "Действия при утечке газа"
                )))
                .andExpect(jsonPath("$[0].contentHash").exists())
                .andExpect(jsonPath("$[*].iconName", hasItems("favorite", "local_fire_department", "gas_meter")))
                .andExpect(jsonPath("$[*].accentColor", hasItems("#C62828", "#D84315", "#6D4C41")))
                .andExpect(jsonPath("$[0].imageUrl").exists())
                .andReturn();

        String memoId = JsonPath.read(
                memosResult.getResponse().getContentAsString(StandardCharsets.UTF_8),
                "$[0].id"
        );

        mockMvc.perform(get("/api/memos/{id}", memoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.steps").isArray())
                .andExpect(jsonPath("$.htmlContent").exists());

    }

    @Test
    void createsSosEvent() throws Exception {
        mockMvc.perform(post("/api/sos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "targetType": "EMERGENCY_CONTACT",
                                  "latitude": 55.755864,
                                  "longitude": 37.617698,
                                  "accuracyMeters": 15,
                                  "contactPhone": "+79990000000",
                                  "message": "Test SOS"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("SENT"))
                .andExpect(jsonPath("$.sms.status").value("SENT"));
    }

    @Test
    void filtersMemosByRegionCategoryActiveAndCritical() throws Exception {
        Long regionId = firstRegionId();
        Long categoryId = firstCategoryId();

        String memoTitle = "Inactive regional test memo";
        String createdMemo = mockMvc.perform(post("/api/admin/memos")
                        .header("X-Admin-Api-Key", ADMIN_API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "categoryId": %d,
                                  "regionId": %d,
                                  "title": "%s",
                                  "shortDescription": "Short test description",
                                  "htmlContent": "<h1>Test memo</h1><p>Body</p>",
                                  "steps": ["Check the region", "Call 112 if needed"],
                                  "version": 1,
                                  "critical": true,
                                  "active": false
                                }
                                """.formatted(categoryId, regionId, memoTitle)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.categoryId").value(categoryId))
                .andExpect(jsonPath("$.regionId").value(regionId))
                .andExpect(jsonPath("$.steps").isArray())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        String memoId = JsonPath.read(createdMemo, "$.id");
        mockMvc.perform(get("/api/memos")
                        .param("region_id", regionId.toString())
                        .param("category_id", categoryId.toString())
                        .param("is_active", "false")
                        .param("is_critical", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", hasItems(memoId)))
                .andExpect(jsonPath("$[*].title", hasItems(memoTitle)));
    }

    @Test
    void returnsAlarmsByRegion() throws Exception {
        Long regionId = firstRegionId();

        mockMvc.perform(get("/api/alarms").param("regionId", regionId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$[0].regionId").value(regionId))
                .andExpect(jsonPath("$[0].text").exists());
    }

    @Test
    void notificationsIncludeDetailedDescriptions() throws Exception {
        Long regionId = firstRegionId();

        String response = mockMvc.perform(get("/api/notifications").param("regionId", regionId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$[0].text").exists())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        String text = JsonPath.read(response, "$[0].text");
        assertTrue(text.length() > 120, "notification text should include actionable details");
    }

    @Test
    void createsAndListsSosEventsByRegion() throws Exception {
        Long regionId = firstRegionId();

        String createdSos = mockMvc.perform(post("/api/sos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "targetType": "EMERGENCY_CONTACT",
                                  "regionId": %d,
                                  "contactPhone": "+79991112233",
                                  "message": "Region SOS without coordinates"
                                }
                                """.formatted(regionId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.regionId").value(regionId))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        String sosId = JsonPath.read(createdSos, "$.id");
        mockMvc.perform(get("/api/sos").param("region_id", regionId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", hasItems(sosId)));
    }

    private Long firstRegionId() throws Exception {
        Number regionId = JsonPath.read(
                mockMvc.perform(get("/api/regions"))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8),
                "$[0].id"
        );
        return regionId.longValue();
    }

    private Long firstCategoryId() throws Exception {
        Number categoryId = JsonPath.read(
                mockMvc.perform(get("/api/memo-categories"))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8),
                "$[0].id"
        );
        return categoryId.longValue();
    }
}
