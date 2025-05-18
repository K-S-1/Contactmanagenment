package com.telephone.directory.service_contact_management.proxy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactManagementProxy {
	private Long id;
	private String name;
	private String mobileNo;
	private String landLineNo;
	private String address;
	private String pincode;
}
