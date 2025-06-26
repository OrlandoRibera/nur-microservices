package controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class HealthControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private HealthController healthController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(healthController).build();
    }

    @Test
    void health_ShouldReturnHealthyStatus() throws Exception {
        mockMvc.perform(get("/api/health")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").value("catering"))
                .andExpect(jsonPath("$.version").value("1.0.0"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void readiness_ShouldReturnReadyStatus() throws Exception {
        mockMvc.perform(get("/api/health/ready")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("READY"))
                .andExpect(jsonPath("$.service").value("catering"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void liveness_ShouldReturnAliveStatus() throws Exception {
        mockMvc.perform(get("/api/health/live")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("ALIVE"))
                .andExpect(jsonPath("$.service").value("catering"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void testEndpoint_ShouldReturnTestResponse() throws Exception {
        mockMvc.perform(get("/api/health/test")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("API is working correctly"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.endpoint").value("/api/health/test"));
    }
} 