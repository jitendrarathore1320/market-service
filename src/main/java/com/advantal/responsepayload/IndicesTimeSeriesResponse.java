package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class IndicesTimeSeriesResponse {
	
	private IndicesDetails indicesDetails;
	
	private TimeSeriesResponse timeSeriesResponse;

}
