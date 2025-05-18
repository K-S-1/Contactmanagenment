package com.telephone.directory.service_contact_management.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.telephone.directory.service_contact_management.domain.ContactManagement;
import com.telephone.directory.service_contact_management.mapperUtil.ContactManagementMapperUtil;
import com.telephone.directory.service_contact_management.proxy.ContactManagementProxy;
import com.telephone.directory.service_contact_management.repo.ContactManagementRepo;
import com.telephone.directory.service_contact_management.service.ContactManagementService;

@Service
public class ContactManagementServiceImpl implements ContactManagementService {
	@Autowired
	private ContactManagementRepo contactManagementRepo;

	@Override
	public ResponseEntity<Map<String, Object>> getUsers(Integer page, Integer size, String sortBy, String direction) {

		// Input validation
		if (page < 0)
			page = 0;
		if (size <= 0)
			size = 10;
		if (size > 100)
			size = 100; // Limit max page size

		// Create sort object based on direction parameter
		Sort.Direction sortDirection = direction.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
		Sort sort = Sort.by(sortDirection, sortBy);

		// Create pageable object with sorting
		Pageable pageable = PageRequest.of(page, size, sort);

		// Get page result
		Page<ContactManagement> pageResult = contactManagementRepo.findAll(pageable);

		// Create response with more metadata
		Map<String, Object> response = new HashMap<>();
		response.put("content", pageResult.getContent());
		response.put("totalElements", pageResult.getTotalElements());
		response.put("totalPages", pageResult.getTotalPages());
		response.put("currentPage", pageResult.getNumber());
		response.put("size", pageResult.getSize());
		response.put("first", pageResult.isFirst());
		response.put("last", pageResult.isLast());
		response.put("empty", pageResult.isEmpty());

		return ResponseEntity.ok(response);
	}

	@Override
	public List<ContactManagementProxy> getAllContacts() {
		List<ContactManagement> all = contactManagementRepo.findAll();
		return ContactManagementMapperUtil.convertListOfValue(all, ContactManagementProxy.class);
	}

	@Override
	public ContactManagementProxy getContactByName(String name) {
		ContactManagement contactManagement = contactManagementRepo.findByName(name);
		return ContactManagementMapperUtil.convertValue(contactManagement, ContactManagementProxy.class);
	}

	@Override
	public ContactManagementProxy getContactById(Long id) {
		ContactManagement contactManagement = contactManagementRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("ContactID not found"));
		return ContactManagementMapperUtil.convertValue(contactManagement, ContactManagementProxy.class);
	}

	@Override
	public ContactManagementProxy addContact(ContactManagementProxy contactManagementProxy) {
		ContactManagement save = contactManagementRepo
				.save(ContactManagementMapperUtil.convertValue(contactManagementProxy, ContactManagement.class));
		return ContactManagementMapperUtil.convertValue(save, ContactManagementProxy.class);
	}

	@Override
	public String deleteContactById(Long id) {
		contactManagementRepo.deleteById(id);
		return "Contact Deleted Successfully";
	}

	@Override
	public ContactManagementProxy updateContactById(ContactManagementProxy contactManagementProxy, Long id) {
		ContactManagement contact = contactManagementRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Contact not found"));
		ContactManagement convertValue = ContactManagementMapperUtil.convertValue(contactManagementProxy,
				ContactManagement.class);
		contact.setAddress(convertValue.getAddress());
		contact.setLandLineNo(convertValue.getLandLineNo());
		contact.setMobileNo(convertValue.getMobileNo());
		contact.setName(convertValue.getName());
		contact.setPincode(convertValue.getPincode());
		contactManagementRepo.save(contact);
		return ContactManagementMapperUtil.convertValue(contact, ContactManagementProxy.class);
	}

}
