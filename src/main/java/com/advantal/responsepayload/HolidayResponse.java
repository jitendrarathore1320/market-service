package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HolidayResponse {

	private Long id;
	
	private String holidayName;
	
	private String country;
	
	private String date;
	
	private Short status;
	
	private String creationDate;

	private String updationDate;
	
	private Long countryId;
	
}
