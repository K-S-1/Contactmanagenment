package com.telephone.directory.service_contact_management.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.telephone.directory.service_contact_management.proxy.ContactManagementProxy;
import com.telephone.directory.service_contact_management.service.ContactManagementService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/contactManagement")
public class ContactManagementController {
	@Autowired
	private ContactManagementService contactManagementService;

	@Autowired
	private Environment environment;

	@GetMapping("/getAllContact")
//	public ResponseEntity<List<ContactManagementProxy>> getAllContact() {
//		System.err.println("port no: "+environment.getProperty("local.server.port"));
//		return new ResponseEntity<List<ContactManagementProxy>>(contactManagementService.getAllContacts(),
//				HttpStatus.OK);
//	}

	public ResponseEntity<Map<String, Object>> getAllContact(@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "ASC") String direction) {

		return contactManagementService.getUsers(page, size, sortBy, direction);
	}

	@GetMapping("/getContact/{name}")
	public ResponseEntity<ContactManagementProxy> getContact(@PathVariable() String name) {
		return new ResponseEntity<ContactManagementProxy>(contactManagementService.getContactByName(name),
				HttpStatus.OK);
	}

	@GetMapping("/getContactByID/{id}")
	public ResponseEntity<ContactManagementProxy> getContactByID(@PathVariable() Long id) {
		return new ResponseEntity<ContactManagementProxy>(contactManagementService.getContactById(id), HttpStatus.OK);
	}

	@PostMapping("/addContact")
	public ResponseEntity<ContactManagementProxy> addContact(
			@RequestBody ContactManagementProxy contactManagementProxy) {
		return new ResponseEntity<>(contactManagementService.addContact(contactManagementProxy), HttpStatus.CREATED);
	}

	@DeleteMapping("/deleteContact/{id}")
	public ResponseEntity<String> deleteContact(@PathVariable() Long id) {
		return new ResponseEntity<String>(contactManagementService.deleteContactById(id), HttpStatus.OK);
	}

	@PutMapping("/updateContact/{id}")
	public ResponseEntity<ContactManagementProxy> updateContact(
			@RequestBody ContactManagementProxy contactManagementProxy, @PathVariable() Long id) {
		return new ResponseEntity<ContactManagementProxy>(
				contactManagementService.updateContactById(contactManagementProxy, id), HttpStatus.OK);
	}

}
