package com.telephone.directory.service_contact_user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.telephone.directory.service_contact_user.domain.ContactUser;
import com.telephone.directory.service_contact_user.fiegn.client.UserManagementClient;
import com.telephone.directory.service_contact_user.mapperUtil.ContactUserMapperUtil;
import com.telephone.directory.service_contact_user.proxy.ContactUserProxy;
import com.telephone.directory.service_contact_user.repo.ContactUserRepo;
import com.telephone.directory.service_contact_user.service.ContactUserService;

@Service
public class ContactUserServiceImpl implements ContactUserService {

	@Autowired
	private ContactUserRepo contactUserRepo;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private UserManagementClient userManagementClient;

//	public final String url = "http://SERVICE-CONTACT-MANAGEMENT/contactManagement";
	@Value("${management.service.url}")
	public String url;

	@Override
	public List<ContactUserProxy> findAllContacts() {
		ParameterizedTypeReference<List<ContactUserProxy>> list=new ParameterizedTypeReference<List<ContactUserProxy>>() {};
		ResponseEntity<List<ContactUserProxy>> exchange = restTemplate.exchange(url+"/getAllContact",HttpMethod.GET,null,list);
		return exchange.getBody();
	}

	@Override
	public ResponseEntity<ContactUserProxy> findContactByName(String name) {
//		using openFeign
		return userManagementClient.findContactUsingFeign(name);
//		using restTemplate
		// return restTemplate.getForObject(url + "/getContact/{name}",
		// ContactUserProxy.class, name);
	}

	@Override
	public ContactUserProxy findContactById(Long id) {
//		********using getforobject*****

//		Map<String, Object> uriVars = new HashMap<>();
//		uriVars.put("a", id);
//		return restTemplate.getForObject(url + "/getContactById/{a}", ContactUserProxy.class, uriVars);

//		******using getforentity******

//		ResponseEntity<ContactUserProxy> forEntity = restTemplate.getForEntity(url + "/getContactById/" + id,
//				ContactUserProxy.class);
//		System.out.println("Response Body" + forEntity.getBody());
//		System.out.println("Response Headers" + forEntity.getHeaders());
//		System.out.println("Response Status" + forEntity.getStatusCode());
//
//		return forEntity.getBody();

//		******using exchange******
		ResponseEntity<ContactUserProxy> forexchange = restTemplate.exchange(url + "/getContactByID/{id}",
				HttpMethod.GET, null, ContactUserProxy.class, id);
		System.out.println("Response Body" + forexchange.getBody());
		System.out.println("Response Headers" + forexchange.getHeaders());
		System.out.println("Response Status" + forexchange.getStatusCode());

		return forexchange.getBody();
	}

//	@Override
//	public ResponseEntity<ContactUserProxy> findContactUsingFeign(String name) {
//		ResponseEntity<ContactUserProxy> findContactUsingFeign = userManagementClient.findContactUsingFeign(name);
//		
//		return null;
//	}

}
