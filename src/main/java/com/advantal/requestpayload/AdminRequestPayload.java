package com.advantal.requestpayload;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class AdminRequestPayload {
	@NotNull(message = "Id can't be null !!")
	private Long id;
	
	@NotEmpty(message = "Name can't be empty !!")
	private String name;
	
	@NotEmpty(message = "mobile no can't be empty !!")
	private String mobile;
	
	@NotEmpty(message = "email can't be empty !!")
	private String email;
	
	@NotEmpty(message = "password can't be empty !!")
	private String password;
	
	@NotNull(message = "Role Id can't be empty !!")
	private Long roleId;
	
	@NotEmpty(message = "country no can't be empty !!")
	private String country;
	
	@NotEmpty(message = "city no can't be empty !!")
	private String city;
	
	@NotEmpty(message = "pinCode no can't be empty !!")
	private String pinCode;
	
	@NotEmpty(message = "address no can't be empty !!")
	private String address;
	

}
