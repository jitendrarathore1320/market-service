package com.advantal.utils;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.advantal.model.Stock;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class ExportStockToPdf {

	private List<Stock> stockList;

	public ExportStockToPdf(List<Stock> stockList) {
		this.stockList = stockList;
	}

	private void writeTableHeader(PdfPTable table) {

		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.lightGray);
		cell.setPadding(5);

		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setColor(Color.black);
		font.setSize(9);

		cell.setPhrase(new Phrase("Sr.", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Symbol", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Name", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Country", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Currency", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Exchange", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("InstrumentType", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Sector", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("IsActivelyTrading", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("LastSyncDate", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("UpdationDate", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Status", font));
		table.addCell(cell);

	}

	private void writeTableData(PdfPTable table) {
		int i = 0;
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.black);
		font.setSize(9);

		PdfPCell cell = new PdfPCell();
		for (Stock stock : stockList) {
			i = i + 1;
			cell.setPhrase(new Phrase(String.valueOf(i), font));
			table.addCell(cell);

			cell.setPhrase(new Phrase(stock.getSymbol()!=null && !stock.getSymbol().isBlank() ? stock.getSymbol() : "Nill", font));
			table.addCell(cell);

			cell.setPhrase(
					new Phrase(stock.getName() != null && !stock.getName().isBlank() ? stock.getName() : "Nill", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase(stock.getCountry()!=null && !stock.getCountry().isBlank() ? stock.getCountry() : "Nill", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase(stock.getCurrency()!=null && !stock.getCurrency().isBlank() ? stock.getCurrency() : "Nill", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase(stock.getExchange()!=null && !stock.getExchange().isBlank() ? stock.getExchange() : "Nill", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase(stock.getInstrumentType()!=null && !stock.getInstrumentType().isBlank() ? stock.getInstrumentType() : "Nill", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase(stock.getSector()!=null && !stock.getSector().isBlank() ? stock.getSector() : "Nill", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase(stock.getIsActivelyTrading()!=null ? stock.getIsActivelyTrading().toString() : "Nill", font));
			table.addCell(cell);

			if (stock.getLastSyncDate() != null) {
				cell.setPhrase(new Phrase(DateUtil.convertDateToStringDateTime(stock.getLastSyncDate()) , font));
				table.addCell(cell);
			} else {
				cell.setPhrase(new Phrase("0000-00-00 00:00:00", font));
				table.addCell(cell);
			}

			if (stock.getUpdationDate() != null) {
				cell.setPhrase(new Phrase(DateUtil.convertDateToStringDateTime(stock.getUpdationDate()) , font));
				table.addCell(cell);
			} else {
				cell.setPhrase(new Phrase("0000-00-00 00:00:00", font));
				table.addCell(cell);
			}

			if (stock.getStatus().equals(Constant.ONE)) {
				cell.setPhrase(new Phrase("Active", font));
				table.addCell(cell);
			} else {
				cell.setPhrase(new Phrase("Inactive", font));
				table.addCell(cell);
			}
		}
	}

	public void export(HttpServletResponse response) throws DocumentException, IOException {
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());
		document.open();

//		String filename;
//		Image img = null;
//		if (DiskPathStoreServiceImpl.isUnix()) {
//			filename = "/home/shieldcrypt/packages/tomcat/webapps/smsc-api-gui/WEB-INF/classes/logo/iaf-logo.png";
//			img = Image.getInstance(filename);
//			log.info("<<<<<<<<<< Image >>>> is set on linux os >>>>>> : " + filename);
//		} else if (DiskPathStoreServiceImpl.isWindows()) {
//			filename = ResourceUtils.getFile("src\\main\\resources\\logo\\amwalLogo.PNG").getPath();
//			img = Image.getInstance(filename);
//			log.info("<<<<<<<<<< Image >>>> is set on windows os >>>>>> : " + filename);
//		}
//		img.scaleToFit(100, 100);
//		img.setAlignment(Image.MIDDLE);
//		img.setAlignment(Image.TOP);
//		document.add(img);
//		log.info("<<<<<<<<<< Added >>>> image in document  >>>>>> : " + document);

		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(12);
		font.setColor(Color.BLACK);

		Paragraph p = new Paragraph("List of Stock Details", font);
		p.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(p);

		PdfPTable table = new PdfPTable(12);
		table.setWidthPercentage(100f);
//		table.setWidths(new float[] { 2.5f, 4.1f, 4.1f, 4.1f, 3.5f, 4.5f, 3.7f, 5.1f, 5.2f, 4.9f,4.9f,4.9f,4.1f });
		table.setSpacingBefore(10);
		writeTableHeader(table);
		writeTableData(table);
		document.add(table);
		document.close();
	}

}
