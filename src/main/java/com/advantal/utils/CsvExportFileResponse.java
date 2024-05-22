package com.advantal.utils;

import java.util.List;

import com.advantal.responsepayload.CsvSaudiExportFileRespnse;
import com.advantal.responsepayload.CsvUsaExportFileRespnse;
import com.advantal.responsepayload.csvAlRajhiComplianceExportFileResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CsvExportFileResponse {

	List<csvAlRajhiComplianceExportFileResponse> csvAlRajhiComplianceList;

	List<CsvUsaExportFileRespnse> csvUsaExportFileRespnse;

	List<CsvSaudiExportFileRespnse> csvSaudiExportFileRespnse;
}
