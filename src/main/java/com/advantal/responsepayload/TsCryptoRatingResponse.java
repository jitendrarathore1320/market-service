package com.advantal.responsepayload;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TsCryptoRatingResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private ErrorResponse status;
	private List<TsCryptoRatingData> data;
}