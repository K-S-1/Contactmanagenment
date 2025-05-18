package com.auth.service;

import com.auth.domain.LoginRequest;
import com.auth.domain.LoginResponse;
import com.auth.domain.Registration;
import com.auth.domain.ValidationRequest;

public interface AuthService {
	
	public Registration registration(Registration registration);
	public LoginResponse login(LoginRequest loginRequest);
	public boolean isTokenValidate(ValidationRequest request);
	
	public void updateResetPasswordToken(String token, String email);
	public Registration getByResetPasswordToken(String token);
	public void updatePassword(Registration registration, String newPassword);
}
