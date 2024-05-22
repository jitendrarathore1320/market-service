package com.advantal.responsepayload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TimeSeriesResponse {

//	private TimeSeriesMeta meta;

	private List<TimeSeriesDetails> values;

	private String high;

	private String low;

	private String previous_close;
}
