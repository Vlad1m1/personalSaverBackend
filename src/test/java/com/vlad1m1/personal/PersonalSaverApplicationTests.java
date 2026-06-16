package com.vlad1m1.personal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
    }

    @Test
    void seedDataIsExposedThroughApi() throws Exception {
        mockMvc.perform(get("/api/regions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(6)));

        mockMvc.perform(get("/api/memos"))
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
                .andExpect(jsonPath("$[0].steps").isArray());

    }

    @Test
    void createsSosEvent() throws Exception {
        mockMvc.perform(post("/api/sos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "targetType": "EMERGENCY_CONTACT",
                                  "contactPhone": "+79990000000",
                                  "message": "Test SOS"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("SENT"))
                .andExpect(jsonPath("$.sms.status").value("SENT"));
    }
}
