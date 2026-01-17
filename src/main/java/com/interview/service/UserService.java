package com.interview.service;

import com.interview.dto.PagedResponse;
import com.interview.dto.PaginationMeta;
import com.interview.dto.UserRequest;
import com.interview.dto.UserResponse;
import com.interview.exception.ResourceNotFoundException;
import com.interview.model.User;
import com.interview.repository.UserRepository;
import com.interview.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public PagedResponse<UserResponse> getAllUsers(int page, int size) {
        List<User> allUsers = userRepository.findAll();
        int totalElements = allUsers.size();
        int totalPages = PaginationUtil.calculateTotalPages(totalElements, size);

        // Calculate start and end index for pagination
        int startIndex = PaginationUtil.calculateStartIndex(page, size);

        // Get paginated users
        List<UserResponse> paginatedUsers = allUsers.stream()
                .skip(startIndex)
                .limit(size)
                .map(UserResponse::new)
                .collect(Collectors.toList());

        // Create pagination metadata
        PaginationMeta pagination = new PaginationMeta(page, size, totalElements, totalPages);

        return new PagedResponse<>(paginatedUsers, pagination);
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return new UserResponse(user);
    }

    public UserResponse createUser(UserRequest request) {
        // Check if username already exists
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username '" + request.getUsername() + "' already exists");
        }

        // Check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email '" + request.getEmail() + "' already exists");
        }

        User user = new User(
                request.getName(),
                request.getUsername(),
                request.getEmail(),
                request.getPhone(),
                request.getWebsite()
        );

        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser);
    }

    public UserResponse updateUser(Long id, UserRequest request) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        // Check if username is being changed and if it already exists
        if (!existingUser.getUsername().equals(request.getUsername())) {
            if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                throw new IllegalArgumentException("Username '" + request.getUsername() + "' already exists");
            }
        }

        // Check if email is being changed and if it already exists
        if (!existingUser.getEmail().equals(request.getEmail())) {
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Email '" + request.getEmail() + "' already exists");
            }
        }

        existingUser.setName(request.getName());
        existingUser.setUsername(request.getUsername());
        existingUser.setEmail(request.getEmail());
        existingUser.setPhone(request.getPhone());
        existingUser.setWebsite(request.getWebsite());

        User updatedUser = userRepository.save(existingUser);
        return new UserResponse(updatedUser);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        userRepository.deleteById(id);
    }
}
