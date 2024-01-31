package com.imdb.validations.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ValidationUserControllerTest {

    MockMvc mockMvc;

    @Autowired
    ValidationUserService validationUserService;

    @MockBean
    ValidationUserRepository validationUserRepository;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    ObjectMapper objectMapper;

    ValidationUsers validationUser1;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        validationUser1= new ValidationUsers("user1",true);

        Mockito.when(validationUserRepository.save(validationUser1)).thenReturn(validationUser1);

        Mockito.when(validationUserRepository.findById("user1")).thenReturn(Optional.ofNullable(validationUser1));
        Mockito.when(validationUserRepository.findById("user2")).thenReturn(Optional.empty());

    }

    @Test
    void register() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validationUser1)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void updateInfo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/info")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validationUser1)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}