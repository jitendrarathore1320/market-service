package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiUsage {
	
	private String timestamp;
	
	private Integer current_usage;
		
	private Integer plan_limit;
			
}
