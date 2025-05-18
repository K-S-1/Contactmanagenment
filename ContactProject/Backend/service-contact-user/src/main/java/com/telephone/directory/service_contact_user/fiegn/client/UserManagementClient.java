package com.telephone.directory.service_contact_user.fiegn.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.telephone.directory.service_contact_user.proxy.ContactUserProxy;

@FeignClient(name = "ContactManagementService", url = "http://localhost:9092", path = "/contactManagement")
public interface UserManagementClient {

	@GetMapping("/getContact/{name}")
	public ResponseEntity<ContactUserProxy> findContactUsingFeign(@PathVariable() String name);

}
