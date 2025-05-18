package com.auth.controller;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.domain.ForgotPasswordRequest;
import com.auth.domain.LoginRequest;
import com.auth.domain.LoginResponse;
import com.auth.domain.Registration;
import com.auth.domain.ResetPasswordRequest;
import com.auth.domain.ValidationRequest;
import com.auth.service.AuthService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import net.bytebuddy.utility.RandomString;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	AuthService authService;

	@Autowired
	private JavaMailSender mailSender;

	public AuthController() {
		System.out.println("AuthController initialized!");
	}

	@GetMapping("/test")
	public String testEndpoint() {
		System.out.println("Test endpoint hit!");
		return "API is working";
	}

	@PostMapping("/registration")
	public Registration registration(@RequestBody Registration registration) {
		return authService.registration(registration);
	}

	@PostMapping("/login")
	public LoginResponse login(@RequestBody LoginRequest loginRequest) {
		return authService.login(loginRequest);
	}

	@PostMapping("/isTokenValidate")
	public boolean isTokenValidate(@RequestBody ValidationRequest request) {
		return authService.isTokenValidate(request);
	}

	@PostMapping("/forgotPassword")
	public ResponseEntity<String> processForgotPassword(@RequestBody ForgotPasswordRequest request) {
		String email = request.getUserName();
		if (email == null || email.trim().isEmpty()) {
			return ResponseEntity.badRequest().body("userName field is required in the request body.");
		}
		String token = RandomString.make(30);
		try {
			authService.updateResetPasswordToken(token, email);
			String resetPasswordLink = "http://localhost:4200/reset-password?token=" + token;
			sendEmail(email, resetPasswordLink);
			System.out.println("Reset link generated (would be sent via email): " + resetPasswordLink);
//			return ResponseEntity.ok("If an account with that email exists, a password reset link has been sent.");
			return ResponseEntity.ok().body("{\"resetLink\": \"" + resetPasswordLink + "\"}");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An unexpected internal error occurred.");
		}
	}

	public void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom("clgkrishnasharma@gmail.com", "Contact Project Support");
		helper.setTo(recipientEmail);

		String subject = "Here's the link to reset your password";

		String content = "<p>Hello,</p>" + "<p>You have requested to reset your password.</p>"
				+ "<p>Click the link below to change your password:</p>" + "<p><a href=\"" + link
				+ "\">Change my password</a></p>" + "<br>" + "<p>Ignore this email if you do remember your password, "
				+ "or you have not made the request.</p>";

		helper.setSubject(subject);

		helper.setText(content, true);

		mailSender.send(message);
	}

	@PostMapping("/resetPassword")
	public ResponseEntity<?> processResetPassword(@Valid @RequestBody ResetPasswordRequest request) {
		String token = request.getToken();
		String password = request.getNewPassword();
		try {
			Registration registration = authService.getByResetPasswordToken(token);
			if (registration == null) {
				return ResponseEntity.badRequest().body(Map.of("error", "Invalid or expired token.")); // Using Map.of for simple json
			} else {
				authService.updatePassword(registration, password);
				return ResponseEntity.ok().body(Map.of("message", "Password has been successfully reset."));
			}
		} catch (Exception e) {
			return ResponseEntity.internalServerError()
					.body(Map.of("error", "An unexpected error occurred while resetting the password."));
		}
	}

}
