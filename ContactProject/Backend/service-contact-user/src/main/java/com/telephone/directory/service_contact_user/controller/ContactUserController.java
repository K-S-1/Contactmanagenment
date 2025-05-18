package com.telephone.directory.service_contact_user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.telephone.directory.service_contact_user.proxy.ContactUserProxy;
import com.telephone.directory.service_contact_user.service.ContactUserService;

@RestController
@RequestMapping("/contactUser")
public class ContactUserController {
	@Autowired
	private ContactUserService contactUserService;

	@Value("${message}")
    private String message;

    @GetMapping("/message")
    public String getMessage() {
        return message;
    }
    
	@GetMapping("/findAllContact")
	public ResponseEntity<List<ContactUserProxy>> findAllContact() {
		return new ResponseEntity<List<ContactUserProxy>>(contactUserService.findAllContacts(), HttpStatus.OK);
	}

	@GetMapping("/findContactUsingFeign/{name}")
	public ResponseEntity<ContactUserProxy> findContact(@PathVariable() String name) {
		return contactUserService.findContactByName(name);
	}

	@GetMapping("/findContactById/{id}")
	public ResponseEntity<ContactUserProxy> findContactById(@PathVariable() Long id) {
		return new ResponseEntity<ContactUserProxy>(contactUserService.findContactById(id), HttpStatus.OK);
	}

}
