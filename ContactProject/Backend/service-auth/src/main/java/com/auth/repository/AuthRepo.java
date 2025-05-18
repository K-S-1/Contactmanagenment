package com.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.auth.domain.Registration;

@Repository
public interface AuthRepo extends JpaRepository<Registration, Long> {
	@Query("SELECT r FROM Registration r WHERE r.userName = ?1")
	public Optional<Registration> findByUserName(String userName);

	public Registration findByResetPasswordToken(String token);

}
