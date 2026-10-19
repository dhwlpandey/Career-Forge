package com.dhwl.careerforge.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.springframework.http.MediaType;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest 
@AutoConfigureMockMvc 
public class SecurityConfigTest {

    @Autowired 
    private MockMvc mockMvc;

    @Test 
    void unauthenticatedUserCannotAccessMyApplications() throws Exception {
        mockMvc.perform(get("/api/applications/me")).andExpect(status().isUnauthorized());
    }

    @Test
    void regularUserCannotAccessAdminApplications() throws Exception {
        mockMvc.perform(
                get("/api/applications/admin")
                        .with(user("testuser").roles("USER"))
        )
        .andExpect(status().isForbidden());
    }

    @Test
    void adminCanAccessAdminApplications() throws Exception {
        mockMvc.perform(
                get("/api/applications/admin")
                        .with(user("admin").roles("ADMIN"))
        )
        .andExpect(status().isOk());
    }

    @Test
    void regularUserCannotCreateJob() throws Exception {
        mockMvc.perform(
                post("/api/jobs")
                        .with(user("testuser").roles("USER"))
        ).andExpect(status().isForbidden());
    }

    @Test
    void regularUserCannotUpdateJob() throws Exception {
        mockMvc.perform(
                put("/api/jobs/1")
                        .with(user("testuser").roles("USER"))
        ).andExpect(status().isForbidden());
    }

    @Test
    void regularUserCannotDeleteJob() throws Exception {
        mockMvc.perform(
                delete("/api/jobs/1")
                        .with(user("testuser").roles("USER"))
        ).andExpect(status().isForbidden());
    }

    @Test
    void applyToJob_shouldReturnBadRequest_whenCoverLetterIsTooLong()
            throws Exception {

        String longCoverLetter = "a".repeat(5001);

        String requestBody = """
                {
                "coverLetter": "%s",
                "resumeUrl": "https://example.com/resume.pdf",
                "notes": "Test application"
                }
                """.formatted(longCoverLetter);

        mockMvc.perform(
                post("/api/applications/jobs/1")
                        .with(user("testuser").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.message").value("Validation failed"))
        .andExpect(jsonPath("$.errors.coverLetter")
                .value("Cover letter is too long"));
    }

    @Test
    void updateApplicationStatus_shouldReturnBadRequest_whenStatusIsMissing()
            throws Exception {

        mockMvc.perform(
                patch("/api/applications/admin/1/status")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.message").value("Validation failed"))
        .andExpect(jsonPath("$.errors.status")
                .value("Application status is required"));
    }
}
