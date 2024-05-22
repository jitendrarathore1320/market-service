package com.advantal.responsepayload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyExecutiveResponse {
	
	private List<KeyExecutiveRes> key_executives;
	
	private Integer maleCount;
	
	private Integer femaleCount;
	
	private Float femaleRatio;
	
}
