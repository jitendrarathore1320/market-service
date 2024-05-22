package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalystResponse {
	private String	buy;
	private String hold;
	private String	sell;
	private Short rating;
}
