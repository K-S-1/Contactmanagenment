package com.telephone.directory.service_contact_user.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.telephone.directory.service_contact_user.domain.ContactUser;

public interface ContactUserRepo extends JpaRepository<ContactUser, Long> {

	Optional<ContactUser> findByName(String name);

}
