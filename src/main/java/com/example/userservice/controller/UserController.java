package com.example.userservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.userservice.exception.UserException;
import com.example.userservice.model.User;
import com.example.userservice.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
@Slf4j
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/users")
	public List<User> getAllUsers() {
		try {
			List<User> users = userService.getAllUsers();
			log.info("Successfully retrieved {} users", users.size());
			return users;
		} catch (Exception e) {
			log.error("An error occurred while fetching users", e);
			throw new UserException("Failed to fetch users: " + e.getMessage(), e);
		}
	}

	@PostMapping("/user")
	public User createUser(@RequestBody User user) {
		try {
			User result = userService.createUser(user);
			log.info("Successfully created user with id: {}", result.getUserId());
			return result;
		} catch (Exception e) {
			log.error("An error occurred while creating user: {}", user.getEmail(), e);
			throw new UserException("Failed to create user: " + e.getMessage(), e);
		}
	}

	@DeleteMapping("/user/{id}")
	public void deleteUser(@PathVariable String id) {
		try {
			userService.deleteUser(id);
			log.info("Successfully deleted user with id: {}", id);
		} catch (Exception e) {
			log.error("An error occurred while deleting user: {}", id, e);
			throw new UserException("Failed to delete user: " + e.getMessage(), e);
		}
	}

	@GetMapping("/user/{id}")
	public User getUserById(@PathVariable String id) {
		try {
			User user = userService.getUserById(id);
			log.info("Successfully retrieved user: {}", user.getEmail());
			return user;
		} catch (Exception e) {
			log.error("An error occurred while fetching user: {}", id, e);
			throw new UserException("Failed to fetch user: " + e.getMessage(), e);
		}
	}

	@PutMapping("/user/{id}")
	public User updateUser(@PathVariable String id, @RequestBody User user) {
		try {
			User updatedUser = userService.updateUser(id, user);
			log.info("Successfully updated user with id: {}", updatedUser.getUserId());
			return updatedUser;
		} catch (Exception e) {
			log.error("An error occurred while updating user: {}", id, e);
			throw new UserException("Failed to update user: " + e.getMessage(), e);
		}
	}
}