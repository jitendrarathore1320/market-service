package com.advantal.responsepayload;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TsCryptoResponse1 implements Serializable {

	private static final long serialVersionUID = 1L;

	List<TsData1> data;
}
