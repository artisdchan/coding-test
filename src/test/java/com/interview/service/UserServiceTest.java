package com.interview.service;

import com.interview.dto.PagedResponse;
import com.interview.dto.UserRequest;
import com.interview.dto.UserResponse;
import com.interview.exception.ResourceNotFoundException;
import com.interview.model.User;
import com.interview.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("John Doe", "johndoe", "john@example.com", "1234567890", "johndoe.com");
        testUser.setId(1L);
    }

    @Test
    void getAllUsers_ShouldReturnPagedUsers() {
        // Arrange
        User user2 = new User("Jane Doe", "janedoe", "jane@example.com", "0987654321", "janedoe.com");
        user2.setId(2L);
        when(userRepository.findAll()).thenReturn(Arrays.asList(testUser, user2));

        // Act
        PagedResponse<UserResponse> result = userService.getAllUsers(0, 10);

        // Assert
        assertEquals(2, result.getContent().size());
        assertEquals("John Doe", result.getContent().get(0).getName());
        assertEquals("Jane Doe", result.getContent().get(1).getName());
        assertEquals(0, result.getPagination().getPage());
        assertEquals(10, result.getPagination().getSize());
        assertEquals(2, result.getPagination().getTotalElements());
        assertEquals(1, result.getPagination().getTotalPages());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // Act
        UserResponse result = userService.getUserById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("johndoe", result.getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_WhenUserDoesNotExist_ShouldThrowException() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(999L));
        verify(userRepository, times(1)).findById(999L);
    }

    @Test
    void createUser_WithValidData_ShouldReturnCreatedUser() {
        // Arrange
        UserRequest request = new UserRequest("John Doe", "johndoe", "john@example.com", "1234567890", "johndoe.com");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        UserResponse result = userService.createUser(request);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_WithDuplicateUsername_ShouldThrowException() {
        // Arrange
        UserRequest request = new UserRequest("John Doe", "johndoe", "john@example.com", "1234567890", "johndoe.com");
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(testUser));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUser_WhenUserExists_ShouldReturnUpdatedUser() {
        // Arrange
        UserRequest request = new UserRequest("John Updated", "johndoe", "john@example.com", "1234567890", "johndoe.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        UserResponse result = userService.updateUser(1L, request);

        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_WhenUserDoesNotExist_ShouldThrowException() {
        // Arrange
        UserRequest request = new UserRequest("John Updated", "johndoe", "john@example.com", "1234567890", "johndoe.com");
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(999L, request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser_WhenUserExists_ShouldDeleteUser() {
        // Arrange
        when(userRepository.existsById(1L)).thenReturn(true);

        // Act
        userService.deleteUser(1L);

        // Assert
        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUser_WhenUserDoesNotExist_ShouldThrowException() {
        // Arrange
        when(userRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(999L));
        verify(userRepository, never()).deleteById(anyLong());
    }
}
