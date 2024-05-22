package com.advantal.responsepayload;

import java.util.List;

import com.advantal.model.Indices;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class IndicesResponse {

	private List<Indices> data;
	
	private String status;

}
