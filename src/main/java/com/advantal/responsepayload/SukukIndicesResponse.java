package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SukukIndicesResponse {
	/**
	 * browse AI
	 */
	private String statusCode;

	private String messageCode;

	private SukukIndicesRobotsResult result;
}
