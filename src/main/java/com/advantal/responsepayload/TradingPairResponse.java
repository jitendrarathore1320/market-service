package com.advantal.responsepayload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TradingPairResponse {

	private String baseQuote;

//	private String cryptoId;
//
//	private String exchangeName;

//	private String LatestPrice;

	public TradingPairResponse(String baseQuote) {
		super();
		this.baseQuote = baseQuote;
	}

	public String getBaseQuote() {
		return baseQuote;
	}

//	@Override
//	public int compareTo(TradingPairResponse o) {
//		return this.getBaseQuote().compareTo(o.getBaseQuote());
//	}

//	@Override
//	public int compareTo(TradingPairResponse o1, TradingPairResponse o2) {
//		// TODO Auto-generated method stub
//		//return o1.getBaseQuote().compareTo(o2.getBaseQuote());
//		return 0;
//	}
//	@Override
//    public boolean equals(Object o) {
//		TradingPairResponse t = (TradingPairResponse)o;
//		return this.getBaseQuote().equals(t.getBaseQuote());
//	}

}
