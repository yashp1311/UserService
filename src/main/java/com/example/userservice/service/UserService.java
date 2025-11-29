package com.example.userservice.service;

import java.util.List;

import com.example.userservice.model.User;

public interface UserService {

	public List<User> getAllUsers();

	public User getUserById(String id);

	public User createUser(User user);

	public void deleteUser(String id);

	public User updateUser(String id, User user);
}