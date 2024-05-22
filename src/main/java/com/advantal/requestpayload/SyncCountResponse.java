package com.advantal.requestpayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SyncCountResponse {

//	private String lastDate;
	
	private Long totalCount;
	
	private Long totalSynced;
	
	private Long remainsForSync;
}
