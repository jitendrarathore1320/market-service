package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class csvAlRajhiComplianceExportFileResponse {

	private String symbol;

	private String compliance_status;

	private String lastUpdated;

	private String source;
}
