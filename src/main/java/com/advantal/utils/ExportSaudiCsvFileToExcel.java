package com.advantal.utils;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.advantal.model.ShariaCompliance;

public class ExportSaudiCsvFileToExcel {

	List<ShariaCompliance> shariaComplianceList;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
//	private ShariaCompliance shariaCompliance;

	public ExportSaudiCsvFileToExcel(List<ShariaCompliance> shariaComplianceList) {
		this.shariaComplianceList = shariaComplianceList;
		workbook = new XSSFWorkbook();
	}

	private void writeHeaderLine() {
		sheet = workbook.createSheet("Sharia Compliance");

		Row row = sheet.createRow(0);

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		style.setFont(font);

//		createCell(row, 0, "Sr.", style);
		createCell(row, 0, "Ticker Names", style);
		createCell(row, 1, "Ticker Symbol", style);
		createCell(row, 2, "Compliance Status", style);
		createCell(row, 3, "Complaince Degree", style);
		createCell(row, 4, "Revenue Breakdown (Halal)", style);
		createCell(row, 5, "Revenue Breakdown (Doubtful)", style);
		createCell(row, 6, "Revenue Breakdown (NotHalal)", style);
		createCell(row, 7, "Interest-bearing securities and assets status", style);
		createCell(row, 8, "Interest-bearing securities and assets persentage", style);
		createCell(row, 9, "Interest-bearing debt status", style);
		createCell(row, 10, "Interest-bearing debt persentage", style);
		createCell(row, 11, "LastUpdated", style);
		createCell(row, 12, "Source", style);
		createCell(row, 13, "Url", style);

	}

	private void createCell(Row row, int columnCount, Object value, CellStyle style) {
		sheet.autoSizeColumn(columnCount);
		Cell cell = row.createCell(columnCount);
		if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else {
			cell.setCellValue((String) value);
		}
		cell.setCellStyle(style);
	}

	private void writeDataLines() {
		int rowCount = 1;

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		style.setFont(font);
//		int i = 0;
		for (ShariaCompliance shariaCompliance : shariaComplianceList) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;
//			i = i + 1;
//			createCell(row, columnCount++, i, style);
			createCell(row, columnCount++,
					shariaCompliance.getTickerNames() != null && !shariaCompliance.getTickerNames().isBlank()
							? shariaCompliance.getTickerNames()
							: "Nill",
					style);
			createCell(row, columnCount++,
					shariaCompliance.getTickerSymbol() != null && !shariaCompliance.getTickerSymbol().isBlank()
							? shariaCompliance.getTickerSymbol()
							: "Nill",
					style);
			createCell(row, columnCount++,
					shariaCompliance.getComplianceStatus() != null && !shariaCompliance.getComplianceStatus().isBlank()
							? shariaCompliance.getComplianceStatus()
							: "Nill",
					style);
			createCell(row, columnCount++,
					shariaCompliance.getComplainceDegree().toString() != null
							&& !shariaCompliance.getComplainceDegree().toString().isBlank()
									? shariaCompliance.getComplainceDegree().toString()
									: "Nill",
					style);
			if (shariaCompliance.getRevenueBreakdownHalal() != null) {
				createCell(row, columnCount++, shariaCompliance.getRevenueBreakdownHalal().toString(), style);
			} else {
				createCell(row, columnCount++, "0", style);
			}

			if (shariaCompliance.getRevenueBreakdownDoubtful() != null) {
				createCell(row, columnCount++, shariaCompliance.getRevenueBreakdownDoubtful().toString(), style);
			} else {
				createCell(row, columnCount++, "0", style);
			}

			if (shariaCompliance.getRevenueBreakdownNotHalal() != null) {
				createCell(row, columnCount++, shariaCompliance.getRevenueBreakdownNotHalal().toString(), style);
			} else {
				createCell(row, columnCount++, "0", style);
			}

			if (shariaCompliance.getInterestBearingSecuritiesAndAssetsStatus() != null
					&& !shariaCompliance.getInterestBearingSecuritiesAndAssetsStatus().isBlank()) {
				createCell(row, columnCount++, shariaCompliance.getInterestBearingSecuritiesAndAssetsStatus(), style);
			} else {
				createCell(row, columnCount++, "0", style);
			}

			if (shariaCompliance.getInterestBearingSecuritiesAndAssetsPercentage() != null) {
				createCell(row, columnCount++,
						shariaCompliance.getInterestBearingSecuritiesAndAssetsPercentage().toString(), style);
			} else {
				createCell(row, columnCount++, "0", style);
			}

//			if (shariaCompliance.getInterestBearingDebt() != null && !shariaCompliance.getInterestBearingDebt().isBlank()) {
//				createCell(row, columnCount++, shariaCompliance.getInterestBearingDebt(), style);
//			} else {
//				createCell(row, columnCount++, "0", style);
//			}

			if (shariaCompliance.getInterestBearingDebtStatus() != null
					&& !shariaCompliance.getInterestBearingDebtStatus().isBlank()) {
				createCell(row, columnCount++, shariaCompliance.getInterestBearingDebtStatus(), style);
			} else {
				createCell(row, columnCount++, "0", style);
			}

			if (shariaCompliance.getInterestBearingDebtPercentage() != null) {
				createCell(row, columnCount++, shariaCompliance.getInterestBearingDebtPercentage().toString(), style);
			} else {
				createCell(row, columnCount++, "0", style);
			}

			if (shariaCompliance.getLastUpdated() != null && !shariaCompliance.getLastUpdated().isBlank()) {
				createCell(row, columnCount++, shariaCompliance.getLastUpdated(), style);
			} else {
				createCell(row, columnCount++, "0000-00-00 00:00:00", style);
			}
			createCell(row, columnCount++,
					shariaCompliance.getSource() != null && !shariaCompliance.getSource().isBlank()
							? shariaCompliance.getSource()
							: "Nill",
					style);
			createCell(row, columnCount++,
					shariaCompliance.getUrl() != null && !shariaCompliance.getUrl().isBlank()
							? shariaCompliance.getUrl()
							: "Nill",
					style);

		}

	}

	public void export(HttpServletResponse response) throws IOException {
		writeHeaderLine();
		writeDataLines();

		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}

}
