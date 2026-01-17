package com.interview.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.dto.PagedResponse;
import com.interview.dto.PaginationMeta;
import com.interview.dto.UserRequest;
import com.interview.dto.UserResponse;
import com.interview.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserResponse testUserResponse;

    @BeforeEach
    void setUp() {
        testUserResponse = new UserResponse();
        testUserResponse.setId(1L);
        testUserResponse.setName("John Doe");
        testUserResponse.setUsername("johndoe");
        testUserResponse.setEmail("john@example.com");
        testUserResponse.setPhone("1234567890");
        testUserResponse.setWebsite("johndoe.com");
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() throws Exception {
        // Arrange
        List<UserResponse> users = Arrays.asList(testUserResponse);
        PaginationMeta pagination = new PaginationMeta(0, 10, 1, 1);
        PagedResponse<UserResponse> pagedResponse = new PagedResponse<>(users, pagination);
        when(userService.getAllUsers(0, 10)).thenReturn(pagedResponse);

        // Act & Assert
        mockMvc.perform(get("/users")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].name").value("John Doe"))
                .andExpect(jsonPath("$.data.content[0].username").value("johndoe"))
                .andExpect(jsonPath("$.data.pagination.page").value(0))
                .andExpect(jsonPath("$.data.pagination.size").value(10))
                .andExpect(jsonPath("$.data.pagination.totalElements").value(1))
                .andExpect(jsonPath("$.data.pagination.totalPages").value(1));
    }

    @Test
    void getUserById_ShouldReturnUser() throws Exception {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(testUserResponse);

        // Act & Assert
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("John Doe"))
                .andExpect(jsonPath("$.data.email").value("john@example.com"));
    }

    @Test
    void createUser_WithValidData_ShouldReturnCreatedUser() throws Exception {
        // Arrange
        UserRequest request = new UserRequest("John Doe", "johndoe", "john@example.com", "1234567890", "johndoe.com");
        when(userService.createUser(any(UserRequest.class))).thenReturn(testUserResponse);

        // Act & Assert
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User created successfully"))
                .andExpect(jsonPath("$.data.name").value("John Doe"));
    }

    @Test
    void createUser_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Arrange - Empty name
        UserRequest request = new UserRequest("", "johndoe", "john@example.com", "1234567890", "johndoe.com");

        // Act & Assert
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void updateUser_WithValidData_ShouldReturnUpdatedUser() throws Exception {
        // Arrange
        UserRequest request = new UserRequest("John Updated", "johndoe", "john@example.com", "1234567890", "johndoe.com");
        when(userService.updateUser(eq(1L), any(UserRequest.class))).thenReturn(testUserResponse);

        // Act & Assert
        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User updated successfully"));
    }

    @Test
    void deleteUser_ShouldReturnSuccess() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User deleted successfully"));
    }
}
