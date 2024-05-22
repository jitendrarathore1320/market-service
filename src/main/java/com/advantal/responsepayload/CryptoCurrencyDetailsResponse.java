package com.advantal.responsepayload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CryptoCurrencyDetailsResponse {

	private String CryptoId;
	private String name;
	private String symbol;
	private String logo;
//	private Double price;
	private String price;
	private String currency;
//	private Double change_value;
//	private Double percent_change;
	private String change_value;
	private String percent_change;
	private String description;
		/* market data */
//	String update_date;
	
	Long update_date;
	private List<KeyValueResponse> keyValueResponseList; // using for mobile
	/* high low 24H */
//	private Double high_24h;
//	private Double low_24h;
	private String high_24h;
	private String low_24h;
	/* high low 7D */
//	private Double high_7d;
//	private Double low_7d;
	private String high_7d;
	private String low_7d;
	/* analyst rating */
	String rating_level;
	Long last_Review_Date;
	String rating_report;
//	String last_Review_Date;

	String rating_score;
	Long data_updated;
	private List<KeyValueResponse> keyValueResponseRatingList;
	
	private Boolean favorite;
	
	private Long instrumentId;
	
	private List<KeyValueResponse> keyValueAdvancMatrixList;
	
}