//package com.advantal.utils;
//
//import java.awt.Color;
//import java.io.IOException;
//import java.util.List;
//
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.util.ResourceUtils;
//
//import com.advantal.model.Admin;
//import com.advantal.serviceimpl.DiskPathStoreServiceImpl;
//import com.lowagie.text.Document;
//import com.lowagie.text.DocumentException;
//import com.lowagie.text.Font;
//import com.lowagie.text.FontFactory;
//import com.lowagie.text.Image;
//import com.lowagie.text.PageSize;
//import com.lowagie.text.Paragraph;
//import com.lowagie.text.Phrase;
//import com.lowagie.text.pdf.PdfPCell;
//import com.lowagie.text.pdf.PdfPTable;
//import com.lowagie.text.pdf.PdfWriter;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//public class ExportDetailsToPdf {
//
//
//	private List<Admin> adminList;
//    
//    public ExportDetailsToPdf(List<Admin> adminList) {
//        this.adminList = adminList;
//    }
//     
//    private void writeTableHeader(PdfPTable table) {
//        PdfPCell cell = new PdfPCell();
//        cell.setBackgroundColor(Color.lightGray);
//        cell.setPadding(5);
//         
//        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
//        font.setColor(Color.black);
//        font.setSize(10);
//         
//        cell.setPhrase(new Phrase("Sr.", font));
//        table.addCell(cell);
//         
//        cell.setPhrase(new Phrase("From", font));
//        table.addCell(cell);
//         
//        cell.setPhrase(new Phrase("To", font));
//        table.addCell(cell);
//         
//        cell.setPhrase(new Phrase("Message", font));
//        table.addCell(cell);
//         
//        cell.setPhrase(new Phrase("Status", font));
//        table.addCell(cell);
//
//        cell.setPhrase(new Phrase("ReceivedDate", font));
//        table.addCell(cell);
//    }
//     
//    private void writeTableData(PdfPTable table) {
//    	int i=0;
//        Font font = FontFactory.getFont(FontFactory.HELVETICA);
//        font.setColor(Color.black);
//        font.setSize(10);
//
//        PdfPCell cell = new PdfPCell();
//        for (Admin admin : adminList) {
//        	 i=i+1;
//            cell.setPhrase(new Phrase(String.valueOf(i), font));
//            table.addCell(cell);
//
//            cell.setPhrase(new Phrase(sms.getSmsSenderDetail().getMobileNo(), font));
//            table.addCell(cell);
//
//            cell.setPhrase(new Phrase(sms.getSmsReceiverDetail().getMobileNo(), font));
//            table.addCell(cell);
//
//            cell.setPhrase(new Phrase(sms.getMessage(), font));
//            table.addCell(cell);
//
//            if(sms.getStatus().equals(Constant.ONE)) {
//                cell.setPhrase(new Phrase("Active", font));
//                table.addCell(cell);
//            }else {
//                cell.setPhrase(new Phrase("Deactive", font));
//                table.addCell(cell);
//            }
//
//            cell.setPhrase(new Phrase(DateUtil.convertTimeStampToStringDate(sms.getEntryDate()), font));
//            table.addCell(cell);
//        }
//    }
//     
//    public void export(HttpServletResponse response) throws DocumentException, IOException {
//        Document document = new Document(PageSize.A4);
//        PdfWriter.getInstance(document, response.getOutputStream());
//        document.open();
//
////        String filename = ResourceUtils.getFile("src\\main\\resources\\logo\\iaf-logo.PNG").getPath();
////        Image img = null;
////        img = Image.getInstance(filename);
////        log.info("<<<<<<<<<< Image >>>> is set on windows os >>>>>> : "+filename);
////        img.scaleToFit(275, 100);
////        img.setAlignment(Image.MIDDLE);
////        img.setAlignment(Image.TOP);
////        document.add(img);
////        log.info("<<<<<<<<<< Added >>>> image in document  >>>>>> : "+document);
//
//        String filename;
//        Image img = null;
//        if(DiskPathStoreServiceImpl.isUnix()){
//            filename = "/home/shieldcrypt/packages/tomcat/webapps/smsc-api-gui/WEB-INF/classes/logo/iaf-logo.png";
//            img = Image.getInstance(filename);
//            log.info("<<<<<<<<<< Image >>>> is set on linux os >>>>>> : "+filename);
//        }else if (DiskPathStoreServiceImpl.isWindows()){
//            filename = ResourceUtils.getFile("src\\main\\resources\\logo\\iaf-logo.PNG").getPath();
//            img = Image.getInstance(filename);
//            log.info("<<<<<<<<<< Image >>>> is set on windows os >>>>>> : "+filename);
//        }
//        img.scaleToFit(275, 100);
//        img.setAlignment(Image.MIDDLE);
//        img.setAlignment(Image.TOP);
//        document.add(img);
//        log.info("<<<<<<<<<< Added >>>> image in document  >>>>>> : "+document);
//
//        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
//        font.setSize(15);
//        font.setColor(Color.BLACK);
//         
//        Paragraph p = new Paragraph("Admin Detail Report", font);
//        p.setAlignment(Paragraph.ALIGN_CENTER);
//        document.add(p);
//         
//        PdfPTable table = new PdfPTable(6);
//        table.setWidthPercentage(100f);
//        table.setWidths(new float[] {1f, 1.6f, 1.6f, 3f,.9f, 2.4f});
//        table.setSpacingBefore(10);
//         
//        writeTableHeader(table);
//        writeTableData(table);
//        document.add(table);
//        document.close();
//    }
//
//}
