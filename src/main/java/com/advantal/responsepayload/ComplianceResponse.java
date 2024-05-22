package com.advantal.responsepayload;

import java.util.ArrayList;
import java.util.List;

import com.advantal.model.SaudiShariahCompliance;
import com.advantal.model.ShariaCompliance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComplianceResponse {
	private List<ShariaCompliance> sariaComplianceList = new ArrayList<>();
	private List<SaudiShariahCompliance> saudiShariahCompliancesList = new ArrayList<>();
}
