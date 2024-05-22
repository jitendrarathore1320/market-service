package com.advantal.responsepayload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SecFilingRes {
	
	private Integer totalReport;
	
	private Integer year;
	
	private List<SecFilingResponse> filingResponsesList;
			
}
