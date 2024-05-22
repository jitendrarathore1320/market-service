package com.advantal.utils;

import java.awt.Color;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

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

public class ExportShariaComplianceToPdf {

	ShariaCompliance shariaCompliance;

	public ExportShariaComplianceToPdf(ShariaCompliance shariaCompliance) {
		this.shariaCompliance = shariaCompliance;
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

		cell.setPhrase(new Phrase("TickerNames", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("TickerSymbol", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Country", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Source", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Url", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("complianceStatus", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("ComplainceDegree", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("RevenueBreakdownHalal", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("RevenueBreakdownDoubtful", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("RevenueBreakdownNotHalal", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("InterestBearingSecuritiesAndAssets", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("InterestBearingSecuritiesAndAssetsPersentage", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("InterestBearingSecuritiesAndAssetsStatus", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("InterestBearingDebt", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("InterestBearingDebtStatus", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("InterestBearingDebtPersentage", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("CreationDate", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("LastUpdated", font));
		table.addCell(cell);

	}

	private void writeTableData(PdfPTable table) {
		int i = 0;
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.black);
		font.setSize(9);

		PdfPCell cell = new PdfPCell();
		i = i + 1;
		cell.setPhrase(new Phrase(String.valueOf(i), font));
		table.addCell(cell);

		cell.setPhrase(new Phrase(shariaCompliance.getTickerNames()!=null && !shariaCompliance.getTickerNames().isBlank() ? shariaCompliance.getTickerNames() : "Nill", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase(shariaCompliance.getTickerSymbol()!=null && !shariaCompliance.getTickerSymbol().isBlank() ? shariaCompliance.getTickerSymbol() : "Nill", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase(shariaCompliance.getCountry()!=null && !shariaCompliance.getCountry().isBlank() ? shariaCompliance.getCountry() : "Nill", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase(shariaCompliance.getSource()!=null && !shariaCompliance.getSource().isBlank() ? shariaCompliance.getSource() : "Nill", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase(shariaCompliance.getUrl()!=null && !shariaCompliance.getUrl().isBlank() ? shariaCompliance.getUrl() : "Nill", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase(shariaCompliance.getComplianceStatus()!=null && !shariaCompliance.getComplianceStatus().isBlank() ? shariaCompliance.getComplianceStatus() : "Nill", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase(shariaCompliance.getComplainceDegree().toString()!=null && !shariaCompliance.getComplainceDegree().toString().isBlank() ? shariaCompliance.getComplainceDegree().toString() : "Nill", font));
		table.addCell(cell);

		if (shariaCompliance.getRevenueBreakdownHalal() != null) {
			cell.setPhrase(new Phrase(shariaCompliance.getRevenueBreakdownHalal().toString(), font));
			table.addCell(cell);
		} else {
			cell.setPhrase(new Phrase("0", font));
			table.addCell(cell);
		}

		if (shariaCompliance.getRevenueBreakdownDoubtful() != null) {
			cell.setPhrase(new Phrase(shariaCompliance.getRevenueBreakdownDoubtful().toString(), font));
			table.addCell(cell);
		} else {
			cell.setPhrase(new Phrase("0", font));
			table.addCell(cell);
		}

		if (shariaCompliance.getRevenueBreakdownNotHalal() != null) {
			cell.setPhrase(new Phrase(shariaCompliance.getRevenueBreakdownNotHalal().toString(), font));
			table.addCell(cell);
		} else {
			cell.setPhrase(new Phrase("0", font));
			table.addCell(cell);
		}

		if (shariaCompliance.getInterestBearingSecuritiesAndAssetsStatus()!=null && !shariaCompliance.getInterestBearingSecuritiesAndAssetsStatus().isBlank() ) {
			cell.setPhrase(new Phrase(shariaCompliance.getInterestBearingSecuritiesAndAssetsStatus().toString(), font));
			table.addCell(cell);
		} else {
			cell.setPhrase(new Phrase("0", font));
			table.addCell(cell);
		}
		
		if (shariaCompliance.getInterestBearingSecuritiesAndAssetsPercentage() != null) {
			cell.setPhrase(
					new Phrase(shariaCompliance.getInterestBearingSecuritiesAndAssetsPercentage().toString(), font));
			table.addCell(cell);
		} else {
			cell.setPhrase(new Phrase("0", font));
			table.addCell(cell);
		}
		
		if ( shariaCompliance.getInterestBearingDebtStatus()!=null && !shariaCompliance.getInterestBearingDebtStatus().isBlank()) {
			cell.setPhrase(new Phrase(shariaCompliance.getInterestBearingDebtStatus(), font));
			table.addCell(cell);
		} else {
			cell.setPhrase(new Phrase("0", font));
			table.addCell(cell);
		}
	
		if (shariaCompliance.getInterestBearingDebtPercentage() != null) {
			cell.setPhrase(new Phrase(shariaCompliance.getInterestBearingDebtPercentage().toString(), font));
			table.addCell(cell);
		} else {
			cell.setPhrase(new Phrase("0", font));
			table.addCell(cell);
		}

		if (shariaCompliance.getCreationDate() != null) {
			cell.setPhrase(new Phrase(DateUtil.convertDateToStringDateTime(shariaCompliance.getCreationDate()), font));
			table.addCell(cell);
		} else {
			cell.setPhrase(new Phrase("0000-00-00 00:00:00", font));
			table.addCell(cell);
		}

		if (shariaCompliance.getLastUpdated() != null) {
			cell.setPhrase(new Phrase(shariaCompliance.getLastUpdated().toString(), font));
			table.addCell(cell);
		} else {
			cell.setPhrase(new Phrase("0000-00-00 00:00:00", font));
			table.addCell(cell);
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

		Paragraph p = new Paragraph("List of Sharia Compliance Details", font);
		p.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(p);

		PdfPTable table = new PdfPTable(19);
		table.setWidthPercentage(100f);
//		table.setWidths(new float[] { 2.5f, 4.1f, 4.1f, 4.1f, 3.5f, 4.5f, 3.7f, 5.1f, 5.2f, 4.9f,4.9f,4.9f,4.1f });
		table.setSpacingBefore(10);
		writeTableHeader(table);
		writeTableData(table);
		document.add(table);
		document.close();
	}

}
