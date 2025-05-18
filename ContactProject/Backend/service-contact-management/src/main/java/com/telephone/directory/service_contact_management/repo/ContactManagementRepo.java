package com.telephone.directory.service_contact_management.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.telephone.directory.service_contact_management.domain.ContactManagement;

public interface ContactManagementRepo extends JpaRepository<ContactManagement, Long> {

	ContactManagement findByName(String name);

}
