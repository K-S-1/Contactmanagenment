package com.telephone.directory.service_contact_management.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.telephone.directory.service_contact_management.proxy.ContactManagementProxy;

public interface ContactManagementService {
	public List<ContactManagementProxy> getAllContacts();

	public ContactManagementProxy getContactByName(String name);

	public ContactManagementProxy getContactById(Long id);

	public ContactManagementProxy addContact(ContactManagementProxy contactManagementProxy);

	public String deleteContactById(Long id);

	public ContactManagementProxy updateContactById(ContactManagementProxy contactManagementProxy,Long id);

	public ResponseEntity<Map<String, Object>> getUsers(Integer page, Integer size, String sortBy, String direction);


}
