package com.advantal.responsepayload;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SecFilingResponse {

	private String symbol;

	private String country;

	private String type;

	private String link;

	private String finalLink;
	
	private String fillingDate;

	private String acceptedDate;
				
}
