package com.example.userservice.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.example.userservice.exception.UserException;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
	private final UserRepository userRepository;

	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
		log.info("User service implementation initialized with repository");
	}

	@Override
	@Cacheable(value = "users")
	@Transactional(readOnly = true)
	public List<User> getAllUsers() {
		log.info("Retrieving all users");
		try {
			List<User> users = userRepository.findAll();
			log.debug("Successfully retrieved {} users", users.size());
			return users;
		} catch (Exception e) {
			log.error("An error occurred while retrieving all users", e);
			throw new UserException("Failed to fetch users: " + e.getMessage(), e);
		}
	}

	@Override
	@Cacheable(value = "users", key = "#id")
	@Transactional(readOnly = true)
	public User getUserById(String id) {
		log.info("Retrieving user with id: {}", id);
		try {
			return userRepository.findById(id)
					.orElseThrow(() -> {
						log.warn("User not found with id: {}", id);
						return new UserException("User not found with id: " + id);
					});
		} catch (Exception e) {
			log.error("An error occurred while retrieving user with id: {}", id, e);
			throw new UserException("Failed to fetch user: " + e.getMessage(), e);
		}
	}

	@Override
	@CacheEvict(value = "users", allEntries = true)
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public User createUser(User user) {
		log.info("Creating a new user with email: {}", user.getEmail());
		try {
			user.setCreatedAt(LocalDateTime.now().format(dateFormatter));
			user.setUpdatedAt(LocalDateTime.now().format(dateFormatter));
			if (user.getStatus() == null) {
				user.setStatus("ACTIVE");
			}
			if (user.getRole() == null) {
				user.setRole("USER");
			}
			User savedUser = userRepository.save(user);
			log.info("Successfully created user with id: {}", savedUser.getUserId());
			return savedUser;
		} catch (Exception e) {
			log.error("An error occurred while creating user with email: {}", user.getEmail(), e);
			throw new UserException("Failed to create user: " + e.getMessage(), e);
		}
	}

	@Override
	@CacheEvict(value = "users", allEntries = true)
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void deleteUser(String id) {
		log.info("Attempting to delete user with id: {}", id);
		try {
			if (!userRepository.existsById(id)) {
				log.warn("No user found with id {} for deletion", id);
				throw new UserException("User not found with id: " + id);
			}
			userRepository.deleteById(id);
			log.info("Successfully deleted user with id: {}", id);
		} catch (DataAccessException e) {
			log.error("An error occurred while deleting user with id: {}", id, e);
			throw new UserException("Failed to delete user: " + e.getMessage(), e);
		}
	}

	@Override
	@CachePut(value = "users", key = "#id")
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public User updateUser(String id, User user) {
		log.info("Updating user with id: {}", id);
		try {
			User existingUser = userRepository.findById(id)
					.orElseThrow(() -> {
						log.warn("No user found with id: {}", id);
						return new UserException("User not found with id: " + id);
					});
			existingUser.setEmail(user.getEmail());
			existingUser.setName(user.getName());
			existingUser.setStatus(user.getStatus());
			existingUser.setRole(user.getRole());
			existingUser.setUpdatedAt(LocalDateTime.now().format(dateFormatter));
			User updatedUser = userRepository.save(existingUser);
			log.info("Successfully updated user with id: {}", updatedUser.getUserId());
			return updatedUser;
		} catch (Exception e) {
			log.error("An error occurred while updating user with id: {}", id, e);
			throw new UserException("Failed to update user: " + e.getMessage(), e);
		}
	}
}