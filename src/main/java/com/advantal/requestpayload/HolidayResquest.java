package com.advantal.requestpayload;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HolidayResquest {

	@NotNull(message = "Id can't be null")
	private Long id;
	
	@NotNull(message = "countryId can't be null")
	private Long countryId;

	@NotEmpty(message = "Holiday name can't be empty")
	@NotNull(message = "Holiday name can't be null")
	private String holidayName;

	@NotEmpty(message = "Holiday date can't be empty")
	@NotNull(message = "Holiday date can't be null")
	private String date;
	
}
