package com.advantal.utils;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.Crypt;

import com.advantal.model.Crypto;
import com.advantal.model.ShariaCompliance;
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

public class ExportCryptoToPdf {

	List<Crypto> cryptoList;

	public ExportCryptoToPdf(List<Crypto> cryptoList) {
		this.cryptoList = cryptoList;
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

		cell.setPhrase(new Phrase("CryptoId", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Symbol", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Name", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Currency", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("InstrumentType", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Listing_time", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("CreationDate", font));
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
		for (Crypto crypto : cryptoList) {
			PdfPCell cell = new PdfPCell();
			i = i + 1;
			cell.setPhrase(new Phrase(String.valueOf(i), font));
			table.addCell(cell);

			cell.setPhrase(new Phrase(crypto.getCryptoId()!=null && !crypto.getCryptoId().isBlank() ? crypto.getCryptoId() : "Nill", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase(crypto.getSymbol()!=null && !crypto.getSymbol().isBlank() ? crypto.getSymbol() : "Nill", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase(crypto.getName()!=null && !crypto.getName().isBlank() ? crypto.getName() : "Nill", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase(crypto.getCurrency()!=null && !crypto.getCurrency().isBlank() ? crypto.getCurrency() : "Nill", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase(crypto.getInstrumentType()!=null && !crypto.getInstrumentType().isBlank() ? crypto.getInstrumentType() : "Nill", font));
			table.addCell(cell);

			if (crypto.getListing_time() != null && !crypto.getListing_time().isBlank()) {
				cell.setPhrase(new Phrase(crypto.getListing_time(), font));
				table.addCell(cell);
			} else {
				cell.setPhrase(new Phrase("0000-00-00 00:00:00", font));
				table.addCell(cell);
			}

			if (crypto.getCreationDate() != null) {
				cell.setPhrase(new Phrase(crypto.getCreationDate().toString(), font));
				table.addCell(cell);
			} else {
				cell.setPhrase(new Phrase("0000-00-00 00:00:00", font));
				table.addCell(cell);
			}

			if (crypto.getUpdationDate() != null) {
				cell.setPhrase(new Phrase(crypto.getUpdationDate().toString(), font));
				table.addCell(cell);
			} else {
				cell.setPhrase(new Phrase("0000-00-00 00:00:00", font));
				table.addCell(cell);
			}
			
			if(crypto.getStatus().equals(Constant.ONE)) {
				cell.setPhrase(new Phrase("Active", font));
				table.addCell(cell);
			}else {
				cell.setPhrase(new Phrase("Incative", font));
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

		Paragraph p = new Paragraph("List of Crypto Details", font);
		p.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(p);

		PdfPTable table = new PdfPTable(10);
		table.setWidthPercentage(100f);
//		table.setWidths(new float[] { 2.5f, 4.1f, 4.1f, 4.1f, 3.5f, 4.5f, 3.7f, 5.1f, 5.2f, 4.9f,4.9f,4.9f,4.1f });
		table.setSpacingBefore(10);
		writeTableHeader(table);
		writeTableData(table);
		document.add(table);
		document.close();
	}

}
