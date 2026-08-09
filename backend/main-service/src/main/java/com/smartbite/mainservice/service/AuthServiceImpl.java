package com.smartbite.mainservice.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.smartbite.mainservice.dto.AuthResponse;
import com.smartbite.mainservice.dto.LoginRequest;
import com.smartbite.mainservice.dto.LoginResponse;
import com.smartbite.mainservice.dto.RegisterRequest;
import com.smartbite.mainservice.entity.Role;
import com.smartbite.mainservice.entity.RoleType;
import com.smartbite.mainservice.entity.User;
import com.smartbite.mainservice.entity.Canteen;
import com.smartbite.mainservice.exception.BadRequestException;
import com.smartbite.mainservice.exception.ResourceNotFoundException;
import com.smartbite.mainservice.repository.CanteenRepository;
import com.smartbite.mainservice.repository.RoleRepository;
import com.smartbite.mainservice.repository.UserRepository;
import com.smartbite.mainservice.security.JwtService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class responsible for handling
 * user authentication related operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

	// Repository used to perform CRUD operations on User table
	private final UserRepository userRepository;

	// Repository used to fetch roles from Role table
	private final RoleRepository rolerepository;

	private final CanteenRepository canteenRepository;

	// Used to hash passwords before storing them in database
	private final BCryptPasswordEncoder passwordEncoder;

	//generating jwt tokens
	private final JwtService jwtservice;

	// Change 4: Wallet Service ko call karne ke liye
	private final RestClient walletRestClient;

	/*
	 * registration of new user
	 *
	 * @param request registration details
	 * @return response => success
	 */
	@Override
	public AuthResponse register(RegisterRequest request) {

		// Check if user already exists with given email
		if (userRepository.findByEmail(request.getEmail()).isPresent()) {
			throw new BadRequestException("Email already exists");
		}

		RoleType requestedRole = request.getRole() == null ? RoleType.STUDENT : request.getRole();
		if (requestedRole != RoleType.STUDENT && requestedRole != RoleType.CANTEEN_MANAGER) {
			throw new BadRequestException("Only STUDENT or CANTEEN_MANAGER registration is allowed");
		}

		Role role = rolerepository.findByRoleName(requestedRole)
				.orElseThrow(() -> new ResourceNotFoundException("Selected role was not found"));

		// Create new User entity object
		User user = new User();

		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPhone(request.getPhone());

		user.setPassword(passwordEncoder.encode(request.getPassword()));

		user.setRole(role);
		if (requestedRole == RoleType.CANTEEN_MANAGER) {
			if (request.getCanteenId() == null) {
				throw new BadRequestException("Canteen is required for manager registration");
			}
			Canteen canteen = canteenRepository.findById(request.getCanteenId())
					.orElseThrow(() -> new ResourceNotFoundException("Selected canteen was not found"));
			user.setCanteen(canteen);
		}
		User savedUser = userRepository.save(user);

		// Change 4: Register ke baad automatically wallet create karo
		if (requestedRole == RoleType.STUDENT) try {
			java.util.Map<String, Object> walletRequest = new java.util.HashMap<>();
			walletRequest.put("userId", savedUser.getUserId());

			walletRestClient.post().uri("/wallet/create").body(walletRequest).retrieve().toBodilessEntity();

		} catch (Exception e) {
			// Wallet creation fail ho toh bhi registration success rahe
			log.warn("Auto wallet creation failed for user {}: {}", savedUser.getUserId(), e.getMessage());
		}

		return new AuthResponse("User Registered Successfully");

	}

	/*
	 * user login system
	 *
	 * @param request user login details email and password
	 * @return response JWT token and user details if successful
	 */
	@Override
	public LoginResponse login(LoginRequest request) {

		// Fetch user by email from database
		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new BadRequestException("Invalid Credentials"));

		// Verify password using BCrypt password encoder
		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new BadRequestException("Invalid Credentials");
		}

		// Generate JWT token for authenticated user (passes User object)
		String token = jwtservice.generateToken(user);

		// Build and return login response containing token and user info
		return LoginResponse.builder()
				.token(token)
				.userId(user.getUserId())
				.name(user.getName())
				.email(user.getEmail())
				.phone(user.getPhone())
				.role(user.getRole().getRoleName().name())
				.canteenId(user.getCanteen() == null ? null : user.getCanteen().getCanteenId())
				.build();
	}

}
