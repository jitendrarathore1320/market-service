package com.advantal.responsepayload;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TsCryptoResponse implements Serializable{

	private static final long serialVersionUID = 1L;

	TsData data;
}
