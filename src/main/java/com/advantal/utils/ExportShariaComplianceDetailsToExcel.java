package com.advantal.utils;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.advantal.model.ShariaCompliance;

public class ExportShariaComplianceDetailsToExcel {

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private ShariaCompliance shariaCompliance;

	public ExportShariaComplianceDetailsToExcel(ShariaCompliance shariaCompliance) {
		this.shariaCompliance = shariaCompliance;
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

		createCell(row, 0, "Sr.", style);
		createCell(row, 1, "TickerNames", style);
		createCell(row, 2, "TickerSymbol", style);
		createCell(row, 3, "Country", style);
		createCell(row, 4, "Source", style);
		createCell(row, 5, "Url", style);
		createCell(row, 6, "ComplianceStatus", style);
		createCell(row, 7, "ComplainceDegree", style);
		createCell(row, 8, "RevenueBreakdownHalal", style);
		createCell(row, 9, "RevenueBreakdownDoubtful", style);
		createCell(row, 10, "RevenueBreakdownNotHalal", style);
		createCell(row, 11, "InterestBearingSecuritiesAndAssets", style);
		createCell(row, 12, "InterestBearingSecuritiesAndAssetsPersentage", style);
		createCell(row, 13, "InterestBearingSecuritiesAndAssetsStatus", style);
		createCell(row, 14, "InterestBearingDebt", style);
		createCell(row, 15, "InterestBearingDebtStatus", style);
		createCell(row, 16, "InterestBearingDebtPersentage", style);
		createCell(row, 17, "CreationDate", style);
		createCell(row, 18, "LastUpdated", style);
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
		int i = 0;
		Row row = sheet.createRow(rowCount++);
		int columnCount = 0;
		i = i + 1;
		createCell(row, columnCount++, i, style);
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
				shariaCompliance.getCountry() != null && !shariaCompliance.getCountry().isBlank()
						? shariaCompliance.getCountry()
						: "Nill",
				style);
		createCell(row, columnCount++,
				shariaCompliance.getSource() != null && !shariaCompliance.getSource().isBlank()
						? shariaCompliance.getSource()
						: "Nill",
				style);
		createCell(row, columnCount++,
				shariaCompliance.getUrl() != null && !shariaCompliance.getUrl().isBlank() ? shariaCompliance.getUrl()
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

		if (shariaCompliance.getCreationDate() != null) {
			createCell(row, columnCount++, DateUtil.convertDateToStringDateTime(shariaCompliance.getCreationDate()),
					style);
		} else {
			createCell(row, columnCount++, "0000-00-00 00:00:00", style);
		}

		if (shariaCompliance.getLastUpdated() != null && shariaCompliance.getLastUpdated().isBlank()) {
			createCell(row, columnCount++, shariaCompliance.getLastUpdated(), style);
		} else {
			createCell(row, columnCount++, "0000-00-00 00:00:00", style);
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
