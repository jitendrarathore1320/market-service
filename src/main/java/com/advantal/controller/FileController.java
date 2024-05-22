package com.advantal.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.advantal.model.Crypto;
import com.advantal.model.Exchange;
import com.advantal.model.Indices;
import com.advantal.model.ShariaCompliance;
import com.advantal.model.Stock;
import com.advantal.repository.CountryRepository;
import com.advantal.requestpayload.PaginationPayLoad;
import com.advantal.requestpayload.RequestPage;
import com.advantal.requestpayload.ShariyaComplianceRequest;
import com.advantal.requestpayload.StockRequestPage;
import com.advantal.service.CryptoCurrencyService;
import com.advantal.service.StockService;
import com.advantal.utils.Constant;
import com.advantal.utils.CsvExportFileResponse;
import com.advantal.utils.ExportAlRAjhiCsvToPdf;
import com.advantal.utils.ExportCryptoToPdf;
import com.advantal.utils.ExportIndicesToPdf;
import com.advantal.utils.ExportSaudiCsvToPdf;
import com.advantal.utils.ExportShariaComplianceDetailsToExcel;
import com.advantal.utils.ExportShariaComplianceToPdf;
import com.advantal.utils.ExportStockExchangeToPdf;
import com.advantal.utils.ExportStockToPdf;
import com.advantal.utils.ExportUsCsvToPdf;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.DocumentException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/market_file")
public class FileController {

	@Autowired
	StockService stockService;

	@Autowired
	CryptoCurrencyService cryptoCurrencyService;

	@Autowired
	CountryRepository countryRepository;

	@GetMapping("/download_stock_exchange_details_pdf")
	public Map<String, String> stockExchangeDetailsExportToPDF(@RequestParam(required = true) String country,
			HttpServletResponse response) throws DocumentException, IOException {
		Map<String, String> map = new HashMap<>();

		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=stock_exchange_details_" + currentDateTime + ".pdf";
		List<Exchange> exchangeList = stockService.getAllExchangeDetails(country);

		if (!exchangeList.isEmpty()) {
			log.info(" record found successfully ! status - {}", Constant.OK + exchangeList);
			response.setHeader(headerKey, headerValue);
			ExportStockExchangeToPdf exportToPdf = new ExportStockExchangeToPdf(exchangeList);
			exportToPdf.export(response);
		} else {
			map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
			map.put(Constant.MESSAGE, Constant.NOT_DOWNLOAD_FILE_MESSAGE);
			log.info("Not able to download the file, because no record found on the database! status - {}",
					Constant.BAD_REQUEST);
		}
		return map;
	}

	@PostMapping("/download_indices_details_pdf")
	public Map<String, String> indicesDetailsExportToPDF(@RequestBody @Valid RequestPage requestPage,
			HttpServletResponse response) throws DocumentException, IOException {
		Map<String, String> map = new HashMap<>();

		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=indices_details_" + currentDateTime + ".pdf";
		List<Indices> indicesList = stockService.getAllIndicesDetails(requestPage);

		if (!indicesList.isEmpty()) {
			log.info(" record found successfully ! status - {}", Constant.OK + indicesList);
			response.setHeader(headerKey, headerValue);
			ExportIndicesToPdf exportToPdf = new ExportIndicesToPdf(indicesList);
			exportToPdf.export(response);
		} else {
			map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
			map.put(Constant.MESSAGE, Constant.NOT_DOWNLOAD_FILE_MESSAGE);
			log.info("Not able to download the file, because no record found on the database! status - {}",
					Constant.BAD_REQUEST);
		}
		return map;
	}

	@PostMapping("/download_stock_details_pdf")
//	@ApiOperation(value = "Download pdf file for sms details !!")
	public Map<String, String> stockDetailsExportToPDF(@RequestBody @Valid StockRequestPage stockRequestPage,
			HttpServletResponse response) throws DocumentException, IOException {
		Map<String, String> map = new HashMap<>();

		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=stock_details_" + currentDateTime + ".pdf";
		List<Stock> stockList = stockService.getAllStocksDetails(stockRequestPage);

		if (!stockList.isEmpty()) {
			log.info(" record found successfully ! status - {}", Constant.OK + stockList);
			response.setHeader(headerKey, headerValue);
			ExportStockToPdf exportToPdf = new ExportStockToPdf(stockList);
			exportToPdf.export(response);
		} else {
			map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
			map.put(Constant.MESSAGE, Constant.NOT_DOWNLOAD_FILE_MESSAGE);
			log.info("Not able to download the file, because no record found on the database! status - {}",
					Constant.BAD_REQUEST);
		}
		return map;
	}

	@PostMapping("/download_stock_details_excel")
	public Map<String, Object> stockDetailsExportToExcel(@RequestBody @Valid StockRequestPage stockRequestPage)
			throws IOException {
		Map<String, Object> map = new HashMap<>();
		List<Stock> stockList = stockService.getAllStocksDetails(stockRequestPage);
		log.info(" record found successfully ! status - {}", Constant.OK + stockList);
		if (stockList != null && !stockList.isEmpty()) {
			map.put(Constant.MESSAGE, Constant.DATA_FOUND_MESSAGE);
		} else {
			map.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
		}
		map.put(Constant.RESPONSE_CODE, Constant.OK);
		map.put(Constant.DATA, stockList);
		return map;
	}

	@PostMapping("/download_sharia_compliance_details_pdf")
	public Map<String, String> shariaComplianceDetailsExportToPDF(
			@RequestBody @Valid ShariyaComplianceRequest shariyaComplianceRequest, HttpServletResponse response)
			throws DocumentException, IOException {
		Map<String, String> map = new HashMap<>();

		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=shariya_compliance_details_" + currentDateTime + ".pdf";
		ShariaCompliance ShariaCompliance = stockService.getAllShariyaComplianceDetails(shariyaComplianceRequest);

		if (ShariaCompliance != null) {
			log.info(" record found successfully ! status - {}", Constant.OK + ShariaCompliance);
			response.setHeader(headerKey, headerValue);
			ExportShariaComplianceToPdf exportToPdf = new ExportShariaComplianceToPdf(ShariaCompliance);
			exportToPdf.export(response);
		} else {
			map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
			map.put(Constant.MESSAGE, Constant.NOT_DOWNLOAD_FILE_MESSAGE);
			log.info("Not able to download the file, because no record found on the database! status - {}",
					Constant.BAD_REQUEST);
		}
		return map;
	}

	@PostMapping("/download_sharia_compliance_details_excel")
	public Map<String, String> shariaComplianceDetailsExportToExcel(
			@RequestBody @Valid ShariyaComplianceRequest shariyaComplianceRequest, HttpServletResponse response)
			throws IOException {
		Map<String, String> map = new HashMap<>();
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=shariya_compliance_details_" + currentDateTime + ".xlsx";

		ShariaCompliance shariaCompliance = stockService.getAllShariyaComplianceDetails(shariyaComplianceRequest);
		if (shariaCompliance != null) {
			log.info(" record found successfully ! status - {}", Constant.OK + shariaCompliance);
			response.setHeader(headerKey, headerValue);
			ExportShariaComplianceDetailsToExcel exportModuleDetailsToExcel = new ExportShariaComplianceDetailsToExcel(
					shariaCompliance);
			exportModuleDetailsToExcel.export(response);
		} else {
			map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
			map.put(Constant.MESSAGE, Constant.NOT_DOWNLOAD_FILE_MESSAGE);
			log.info("Not able to download the file, because no record found on the database! status - {}",
					Constant.BAD_REQUEST);
		}
		return map;
	}

	@PostMapping("/download_crypto_details_pdf")
//	@ApiOperation(value = "Download pdf file for sms details !!")
	public Map<String, String> cryptoDetailsExportToPDF(@RequestBody @Valid PaginationPayLoad paginationPayLoad,
			HttpServletResponse response) throws DocumentException, IOException {
		Map<String, String> map = new HashMap<>();

		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=crypto_details_" + currentDateTime + ".pdf";
		List<Crypto> cryptoList = cryptoCurrencyService.getAllCryptoDetails(paginationPayLoad);

		if (cryptoList != null) {
			log.info(" record found successfully ! status - {}", Constant.OK + cryptoList);
			response.setHeader(headerKey, headerValue);
			ExportCryptoToPdf exportToPdf = new ExportCryptoToPdf(cryptoList);
			exportToPdf.export(response);
		} else {
			map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
			map.put(Constant.MESSAGE, Constant.NOT_DOWNLOAD_FILE_MESSAGE);
			log.info("Not able to download the file, because no record found on the database! status - {}",
					Constant.BAD_REQUEST);
		}
		return map;
	}

	@PostMapping("/download_newly_Listed_crypto_details_pdf")
	public Map<String, String> newlyListedCryptoDetailsExportToPDF(
			@RequestBody @Valid PaginationPayLoad paginationPayLoad, HttpServletResponse response)
			throws DocumentException, IOException {
		Map<String, String> map = new HashMap<>();

		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=newly_listed_crypto_details_" + currentDateTime + ".pdf";
		List<Crypto> newlyListedcryptoList = cryptoCurrencyService.getAllNewlyListedCryptoDetails(paginationPayLoad);

		if (newlyListedcryptoList != null && !newlyListedcryptoList.isEmpty()) {
			log.info(" record found successfully ! status - {}", Constant.OK + newlyListedcryptoList);
			response.setHeader(headerKey, headerValue);
			ExportCryptoToPdf exportToPdf = new ExportCryptoToPdf(newlyListedcryptoList);
			exportToPdf.export(response);
		} else {
			map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
			map.put(Constant.MESSAGE, Constant.NOT_DOWNLOAD_FILE_MESSAGE);
			log.info("Not able to download the file, because no record found on the database! status - {}",
					Constant.BAD_REQUEST);
		}
		return map;
	}

	@GetMapping("/download_export_csv_details_pdf")
//	@ApiOperation(value = "Download pdf file for sms details !!")
	public Map<String, String> csvFileDetailsExportToPDF(@RequestParam(required = true) String country,
			HttpServletResponse response) throws DocumentException, IOException {
		Map<String, String> map = new HashMap<>();

		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=export_csv_details_" + currentDateTime + ".pdf";
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		Map<String, Object> shariaComplianceList = stockService.getCsvFileExcelDetails(country);
		CsvExportFileResponse csvExportFileResponse = mapper.convertValue(shariaComplianceList.get("data"),
				CsvExportFileResponse.class);
		if (csvExportFileResponse != null) {
			if (csvExportFileResponse.getCsvSaudiExportFileRespnse() != null
					&& !csvExportFileResponse.getCsvSaudiExportFileRespnse().isEmpty()) {
				log.info(" record found successfully ! status - {}",
						Constant.OK + csvExportFileResponse.getCsvSaudiExportFileRespnse());
				response.setHeader(headerKey, headerValue);
				ExportSaudiCsvToPdf exportSaudiCsvToPdf = new ExportSaudiCsvToPdf(
						csvExportFileResponse.getCsvSaudiExportFileRespnse());
				exportSaudiCsvToPdf.export(response);
			} else if (csvExportFileResponse.getCsvUsaExportFileRespnse() != null
					&& !csvExportFileResponse.getCsvUsaExportFileRespnse().isEmpty()) {
				log.info(" record found successfully ! status - {}",
						Constant.OK + csvExportFileResponse.getCsvUsaExportFileRespnse());
				response.setHeader(headerKey, headerValue);
				ExportUsCsvToPdf exportToPdf = new ExportUsCsvToPdf(csvExportFileResponse.getCsvUsaExportFileRespnse());
				exportToPdf.export(response);
			} else if (csvExportFileResponse.getCsvAlRajhiComplianceList() != null
					&& !csvExportFileResponse.getCsvAlRajhiComplianceList().isEmpty()) {
				log.info(" record found successfully ! status - {}",
						Constant.OK + csvExportFileResponse.getCsvAlRajhiComplianceList());
				response.setHeader(headerKey, headerValue);
				ExportAlRAjhiCsvToPdf exportToPdf = new ExportAlRAjhiCsvToPdf(
						csvExportFileResponse.getCsvAlRajhiComplianceList());
				exportToPdf.export(response);
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
				map.put(Constant.MESSAGE, Constant.NOT_DOWNLOAD_FILE_MESSAGE);
				log.info("Not able to download the file, because no record found on the database! status - {}",
						Constant.BAD_REQUEST);
			}
		} else {
			map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
			map.put(Constant.MESSAGE, Constant.NOT_DOWNLOAD_FILE_MESSAGE);
			log.info("Not able to download the file, because no record found on the database! status - {}",
					Constant.BAD_REQUEST);
		}
		return map;
	}

	@GetMapping("/download_export_csv_details_excel")
	public Map<String, Object> csvFileDetailsExportToExcel(@RequestParam(required = true) String country) {
		return stockService.getCsvFileExcelDetails(country);
	}
}
