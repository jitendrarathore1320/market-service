package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CryptoGloalStatusResponse {
	String total_market_cap;
	String spot_volume_24H;
	String btc;
}
