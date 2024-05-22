package com.advantal.responsepayload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TsCryptoGloalStatusData {
	 Long total_market_cap;
	 
	 Double total_market_cap_change_percentage_24h;
	 
	 @JsonProperty("24h_spot_volume")
	 Long spot_volume_24h;
	 
	 @JsonProperty("24h_spot_volume_change_percentage_24h")
	 Double spot_volume_change_percentage_24h;
	 
	 TsMarketDominances market_dominances;
}
