package com.auth.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth.domain.LoginRequest;
import com.auth.domain.LoginResponse;
import com.auth.domain.Registration;
import com.auth.domain.ValidationRequest;
import com.auth.repository.AuthRepo;
import com.auth.service.AuthService;
import com.auth.utils.JwtUtils;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

	@Autowired
	AuthRepo authRepo;

	@Autowired
	private AuthenticationManager authManeger;

	@Autowired
	private JwtUtils jwtU;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public Registration registration(Registration registration) {
		registration.setPassword(bCryptPasswordEncoder.encode(registration.getPassword()));
		System.out.println(registration);
		return authRepo.save(registration);
	}

	@Override
	public LoginResponse login(LoginRequest loginRequest) {
		Authentication unVerifiedAuth = new UsernamePasswordAuthenticationToken(loginRequest.getUserName(),
				loginRequest.getPassword());
		Authentication verifiedAuth = authManeger.authenticate(unVerifiedAuth);
//		System.out.println(jwtU.generateSecretKey());
		if (verifiedAuth.isAuthenticated()) {
			return new LoginResponse(jwtU.geneateToken(loginRequest.getUserName()));
		}
		return new LoginResponse("failed Token");
	}

	@Override
	public boolean isTokenValidate(ValidationRequest request) {

		try {
			String token = request.getToken();
			token = token.substring(7);
			String path = request.getPath();
			System.out.println(path);

			String userName = jwtU.extractUserName(token);
			boolean isTokenExpired = jwtU.isTokenExpired(token);

			Optional<Registration> uName = authRepo.findByUserName(userName);

			if (!uName.isPresent() || isTokenExpired) {
				return false;
			}

			return true;

		} catch (Exception e) {
			return false;
		}

	}

	public void updateResetPasswordToken(String token, String email) {
		Registration registration = authRepo.findByUserName(email)
				.orElseThrow(() -> new RuntimeException("Email not found"));

		if (registration != null) {
			registration.setResetPasswordToken(token);
			authRepo.save(registration);
		}
	}

	public Registration getByResetPasswordToken(String token) {
		return authRepo.findByResetPasswordToken(token);
	}

	public void updatePassword(Registration registration, String newPassword) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(newPassword);
		registration.setPassword(encodedPassword);

		registration.setResetPasswordToken(null);
		authRepo.save(registration);
	}
}
