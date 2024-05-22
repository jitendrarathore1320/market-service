package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyExecutiveRes {
	
	private String title;

	private String name;

//	private Long pay;
	
//	private String currencyPay;//

	private String gender;//

//	private Integer year_born;

	private Integer yearBorn;

	private Integer age;

	private Long titleSince;//

}
