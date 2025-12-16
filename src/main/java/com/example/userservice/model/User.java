package com.example.userservice.model;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "user_id", updatable = false, nullable = false, length = 30)
	private String userId;

	@Column(name = "name")
	private String name;

	@Column(name = "email", unique = true)
	private String email;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "password")
	private String password;

	@Column(name = "address")
	private String address;

	@Column(name = "role")
	private String role;

	@Column(name = "status")
	private String status;

	@Column(name = "created_at")
	private String createdAt;

	@Column(name = "updated_at")
	private String updatedAt;

	@Column(name = "last_login")
	private String lastLogin;

	@Column(name = "ratings")
	private List<Rating> ratings;

	@PrePersist
	protected void onCreate() {
		if (this.userId == null) {
			this.userId = UUID.randomUUID().toString().length() > 30 ? UUID.randomUUID().toString().substring(0, 30)
					: UUID.randomUUID().toString();
		}
	}
}