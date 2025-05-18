package com.telephone.directory.service_contact_user.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.telephone.directory.service_contact_user.proxy.ContactUserProxy;

public interface ContactUserService {
	public List<ContactUserProxy> findAllContacts();

	public ResponseEntity<ContactUserProxy> findContactByName(String name);

	public ContactUserProxy findContactById(Long id);
}
