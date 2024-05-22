package com.advantal.responsepayload;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
public class RoleModuleMappingResponse{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
//	private Long role_id_fk;
	private Long roleIdFk;
	
//	private Long moduleId;
	
	private Short moduleAction;
		
	private String moduleName;
	
	private String parentModuleName;
	
	private String moduleCode;

	private Short addAction;

	private Short updateAction;

	private Short deleteAction;

	private Short viewAction;
	
	private Short downloadAction;
	
	private String entryDate;
	
	private String updationDate;
	
	private Short status;

}
