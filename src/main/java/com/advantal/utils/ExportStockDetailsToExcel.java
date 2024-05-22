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

import com.advantal.model.Stock;

public class ExportStockDetailsToExcel {

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private List<Stock> stockList;

	public ExportStockDetailsToExcel(List<Stock> stockList) {
		this.stockList = stockList;
		workbook = new XSSFWorkbook();
	}

	private void writeHeaderLine() {
		sheet = workbook.createSheet("Stocks");

		Row row = sheet.createRow(0);

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		style.setFont(font);

		createCell(row, 0, "Sr.", style);
		createCell(row, 1, "Symbol", style);
		createCell(row, 2, "Name", style);
		createCell(row, 3, "Country", style);
		createCell(row, 4, "Currency", style);
		createCell(row, 5, "Exchange", style);
		createCell(row, 6, "InstrumentType", style);
		createCell(row, 7, "Sector", style);
		createCell(row, 8, "IsActivelyTrading", style);
		createCell(row, 9, "LastSyncDate", style);
		createCell(row, 10, "UpdationDate", style);
		createCell(row, 11, "Status", style);
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
		for (Stock stock : stockList) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;
			i = i + 1;
			createCell(row, columnCount++, i, style);
			createCell(row, columnCount++,
					stock.getSymbol() != null && !stock.getSymbol().isBlank() ? stock.getSymbol() : "Nill", style);
			createCell(row, columnCount++,
					stock.getName() != null && !stock.getName().isBlank() ? stock.getName() : "Nill", style);
			createCell(row, columnCount++,
					stock.getCountry() != null && !stock.getCountry().isBlank() ? stock.getCountry() : "Nill", style);
			createCell(row, columnCount++,
					stock.getCurrency() != null && !stock.getCurrency().isBlank() ? stock.getCurrency() : "Nill",
					style);
			createCell(row, columnCount++,
					stock.getExchange() != null && !stock.getExchange().isBlank() ? stock.getExchange() : "Nill",
					style);
			createCell(row, columnCount++,
					stock.getInstrumentType() != null && !stock.getInstrumentType().isBlank()
							? stock.getInstrumentType()
							: "Nill",
					style);
			createCell(row, columnCount++,
					stock.getSector() != null && !stock.getSector().isBlank() ? stock.getSector() : "Nill", style);
			createCell(row, columnCount++, stock.getIsActivelyTrading() != null ? stock.getIsActivelyTrading() : "Nill",
					style);
			if (stock.getLastSyncDate() != null) {
				createCell(row, columnCount++, DateUtil.convertDateToStringDateTime(stock.getLastSyncDate()), style);
			} else {
				createCell(row, columnCount++, "0000-00-00 00:00:00", style);
			}
			if (stock.getUpdationDate() != null) {
				createCell(row, columnCount++, DateUtil.convertDateToStringDateTime(stock.getUpdationDate()), style);
			} else {
				createCell(row, columnCount++, "0000-00-00 00:00:00", style);
			}
			if (stock.getStatus().equals(Constant.ONE)) {
				createCell(row, columnCount++, "Active", style);
			} else {
				createCell(row, columnCount++, "Inactive", style);
			}
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
