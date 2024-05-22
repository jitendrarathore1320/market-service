package com.advantal.responsepayload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaCapturedLists {

	@JsonProperty("saudinewsenglish")
	  public List<SaudiMarketNews> saudinewsenglish;
}
