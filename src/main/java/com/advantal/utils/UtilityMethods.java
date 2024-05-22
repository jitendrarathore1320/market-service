package com.advantal.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.advantal.model.SaudiShariahCompliance;
import com.advantal.model.ShariaCompliance;
import com.advantal.responsepayload.ComplianceResponse;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UtilityMethods {

	public static Integer randomNumber() {
		Random random = new Random();
		return random.nextInt(1000000);
	}

	public static File createFileDirectory() {
		File file = null;
		try {
			File fileDir = new File("C:\\Users\\hp\\Downloads");
			String fileName = "language_file";
			file = new File(fileDir + "\\" + fileName + ".xlsx");
			if (!fileDir.exists()) {
				if (fileDir.mkdir()) {
					log.info("New directory created into your local system! status - {}", fileDir);
				}
				if (!file.exists()) {
					file.createNewFile();
					log.info("New file created into your local system! status - {}", file);
				} else
					log.info("File already exist into your local system! status - {}", file);
			} else {
				log.info("Directory found into your local system! status - {}", fileDir);
				if (!file.exists()) {
					file.createNewFile();
					log.info("New file created into your local system! status - {}", file);
				} else
					log.info("File already exist into your local system! status - {}", file);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

	public static Integer sendOtp(Integer oldOtp) {
		Integer otp = oldOtp;
		return otp;
	}

//	public static List<SariaCompliance> getDataFromExcelFile(String serverFilePath) {
//		List<SariaCompliance> complianceList = new ArrayList<>();
//		try {
//			/* reading data from a file in the form of bytes */
//			FileInputStream fis = new FileInputStream(new File(serverFilePath));
//			XSSFWorkbook serverWorkbook = new XSSFWorkbook(fis);
//			XSSFSheet serverSheet = serverWorkbook.getSheetAt(0);
//
//			Row headerRow = serverSheet.getRow(0);
//			Iterator<Row> rows = serverSheet.iterator();
//			int rowNumber = 0;
//			while (rows.hasNext()) {
//				Row currentRow = rows.next();
//				/* skip header */
//				if (rowNumber == 0) {
//					rowNumber++;
//					continue;
//				}
//
//				int cellIdx = 0, totalcell = 0;
//				totalcell = currentRow.getLastCellNum();
//				SariaCompliance sariaCompliance = new SariaCompliance();
//				while (cellIdx <= totalcell) {
//					Cell currentCell = currentRow.getCell(cellIdx);
//					Cell hdrCell = headerRow.getCell(cellIdx);
//					Integer cellValue;
//					switch (cellIdx) {
//					case 0:
//						if (currentCell != null) {
//							if (!hdrCell.getStringCellValue().equals(currentCell.getStringCellValue())) {
//								sariaCompliance.setTickerNames(currentCell.getStringCellValue());
//							}
//						} else {
//							sariaCompliance.setTickerNames("");
//						}
//						break;
//					case 1:
//						if (currentCell != null) {
//							if (!hdrCell.getStringCellValue().equals(currentCell.getStringCellValue())) {
//								sariaCompliance.setTickerSymbol(currentCell.getStringCellValue());
//							}
//						} else {
//							sariaCompliance.setTickerSymbol("");
//						}
//						break;
//					case 2:
//						if (currentCell != null) {
//							if (!hdrCell.getStringCellValue().equals(currentCell.getStringCellValue())) {
//								sariaCompliance.setComplianceStatus(currentCell.getStringCellValue());
//							}
//						} else {
//							sariaCompliance.setComplianceStatus("");
//						}
//						break;
//					case 3:
//						if (currentCell != null) {
//							if (currentCell.getCellType() == CellType.NUMERIC) {
//								cellValue = (int) currentCell.getNumericCellValue();
//								if (!hdrCell.getStringCellValue().equals(cellValue.toString())) {
//									sariaCompliance.setComplainceDegree((short) currentCell.getNumericCellValue());
//								}
//							}
//						} else {
//							sariaCompliance.setComplainceDegree(null);
//						}
//						break;
//					case 4:
//						if (currentCell != null) {
//							if (currentCell.getCellType() == CellType.NUMERIC) {
//								cellValue = (int) currentCell.getNumericCellValue();
//								if (!hdrCell.getStringCellValue().equals(cellValue.toString())) {
//									sariaCompliance.setRevenueBreakdownHalal((float) currentCell.getNumericCellValue());
//								}
//							}
//						} else {
//							sariaCompliance.setRevenueBreakdownHalal(null);
//						}
//						break;
//					case 5:
//						if (currentCell != null) {
//							if (currentCell.getCellType() == CellType.NUMERIC) {
//								cellValue = (int) currentCell.getNumericCellValue();
//								if (!hdrCell.getStringCellValue().equals(cellValue.toString())) {
//									sariaCompliance
//											.setRevenueBreakdownDoubtful((float) currentCell.getNumericCellValue());
//								}
//							}
//						} else {
//							sariaCompliance.setRevenueBreakdownDoubtful(null);
//						}
//						break;
//					case 6:
//						if (currentCell != null) {
//							if (currentCell.getCellType() == CellType.NUMERIC) {
//								cellValue = (int) currentCell.getNumericCellValue();
//								if (!hdrCell.getStringCellValue().equals(cellValue.toString())) {
//									sariaCompliance
//											.setRevenueBreakdownNotHalal((float) currentCell.getNumericCellValue());
//								}
//							}
//						} else {
//							sariaCompliance.setRevenueBreakdownNotHalal(null);
//						}
//						break;
//					case 7:
//						if (currentCell != null) {
//							if (!hdrCell.getStringCellValue().equals(currentCell.getStringCellValue())) {
//								sariaCompliance.setInterestBearingSecuritiesAndAssets(currentCell.getStringCellValue());
//							}
//						} else {
//							sariaCompliance.setInterestBearingSecuritiesAndAssets("");
//						}
//						break;
//					case 8:
//						if (currentCell != null) {
//							if (!hdrCell.getStringCellValue().equals(currentCell.getStringCellValue())) {
//								sariaCompliance.setInterestBearingDebt(currentCell.getStringCellValue());
//							}
//						} else {
//							sariaCompliance.setInterestBearingDebt("");
//						}
//						break;
//					case 9:
//						if (currentCell != null) {
//							if (currentCell.getCellType() == CellType.NUMERIC) {
//								String strCellValue= currentCell.getDateCellValue().toString();
//								if (!hdrCell.getStringCellValue().equals(currentCell.getDateCellValue().toString())) {
//									sariaCompliance.setLastUpdated(strCellValue);
//								}
//							} else if (currentCell.getCellType() == CellType.STRING) {
//								if (!hdrCell.getStringCellValue().equals(currentCell.getStringCellValue())) {
//									sariaCompliance.setLastUpdated(currentCell.getStringCellValue());
//								}
//							}
//						} else {
//							sariaCompliance.setLastUpdated(null);
//						}
//						break;
//					case 10:
//						if (currentCell != null) {
//							if (currentCell.getCellType() == CellType.STRING) {
//								String strCellValue = currentCell.getStringCellValue();
//								if (!hdrCell.getStringCellValue().equals(strCellValue)) {
//									sariaCompliance.setSource(strCellValue);
//								}
//							}
//						} else {
//							sariaCompliance.setSource("");
//						}
//						break;
//					case 11:
//						if (currentCell != null) {
//							if (!hdrCell.getStringCellValue().equals(currentCell.getStringCellValue())) {
//								sariaCompliance.setLink(currentCell.getStringCellValue());
//							}
//						} else {
//							sariaCompliance.setLink("");
//						}
//						break;
//					default:
//						break;
//					}
//					cellIdx++;
//				}
//				if ((sariaCompliance.getTickerNames() != null && !sariaCompliance.getTickerNames().isBlank())
//						|| (sariaCompliance.getTickerSymbol() != null && !sariaCompliance.getTickerSymbol().isBlank())
//						|| (sariaCompliance.getComplianceStatus() != null
//								&& !sariaCompliance.getComplianceStatus().isBlank())
//						|| (sariaCompliance.getComplainceDegree() != null)
//						|| (sariaCompliance.getRevenueBreakdownHalal() != null)
//						|| (sariaCompliance.getRevenueBreakdownDoubtful() != null)
//						|| (sariaCompliance.getRevenueBreakdownNotHalal() != null)
//						|| (sariaCompliance.getInterestBearingSecuritiesAndAssets() != null
//								&& !sariaCompliance.getInterestBearingSecuritiesAndAssets().isBlank())
//						|| (sariaCompliance.getInterestBearingDebt() != null
//								&& !sariaCompliance.getInterestBearingDebt().isBlank())
//						|| (sariaCompliance.getLastUpdated() != null && !sariaCompliance.getLastUpdated().isBlank())
//						|| (sariaCompliance.getSource() != null && !sariaCompliance.getSource().isBlank())
//						|| (sariaCompliance.getLink() != null && !sariaCompliance.getLink().isBlank())) {
//					sariaCompliance.setStatus(Constant.ONE);
//					complianceList.add(sariaCompliance);
//				} else
//					continue;
//			}
//			serverWorkbook.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//			log.info(e.getMessage());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return complianceList;
//	}

	public static void isFieldNameEquels(MultipartFile docfile, String fileFormate) throws FileNotFoundException {
		try {
			XSSFWorkbook uploadableWorkbook = new XSSFWorkbook(docfile.getInputStream());
			XSSFSheet uploadableSheet = uploadableWorkbook.getSheetAt(0);
			Row uploadableSheetHeaderRow = uploadableSheet.getRow(0);

			FileInputStream fis = new FileInputStream(new File(fileFormate));
			XSSFWorkbook fileFormateWorkbook = new XSSFWorkbook(fis);
			XSSFSheet fileFormateSheet = fileFormateWorkbook.getSheetAt(0);
			Row headerRow = fileFormateSheet.getRow(0);

			int cellIdx = 0, totalcell = 0;
			totalcell = headerRow.getLastCellNum();
			String serverfields = "", localfields = "";
			while (cellIdx <= totalcell) {
				Cell hdrCell = headerRow.getCell(cellIdx);
				serverfields = serverfields + hdrCell.getStringCellValue() + ",";
			}

			int localCellIdx = 0, localTotalCell = 0;
			totalcell = uploadableSheetHeaderRow.getLastCellNum();
			while (localCellIdx <= localTotalCell) {
				Cell hdrCell = uploadableSheetHeaderRow.getCell(cellIdx);
				localfields = localfields + hdrCell.getStringCellValue() + ",";
			}

			String localFieldList[], serverFieldList[];
			localFieldList = localfields.split(",");
			serverFieldList = serverfields.split(",");

			uploadableWorkbook.close();
			fileFormateWorkbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

//	public static List<ShariaCompliance> getDataFromCSVFile(MultipartFile file, String csvFileFormatePath,
//			String uploadDir, String country) {
	public static ComplianceResponse getDataFromCSVFile(MultipartFile file, String csvFileFormatePath,
			String fileType) {
		ComplianceResponse complianceResponse = new ComplianceResponse();
		List<ShariaCompliance> sariaComplianceList = new ArrayList<>();
		List<SaudiShariahCompliance> saudiShariahCompliancesList = new ArrayList<>();
		try {
			BufferedReader localFileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
			CSVReader localCSVReader = new CSVReader(localFileReader);
			String[] localCSVFieldList = localCSVReader.readNext();

			FileReader serverCSVReader = new FileReader(csvFileFormatePath);
			CSVReader serverCSVFormateReader = new CSVReader(serverCSVReader);
			String[] serverCSVFieldList = null;

			Integer fieldCount = 1;
			if (fileType.equalsIgnoreCase(Constant.SAUDI_AAOIFI_COMPLIANCE)) {
				serverCSVFieldList = serverCSVFormateReader.readNext();
				fieldCount = serverCSVFieldList.length;
			} else if (fileType.equalsIgnoreCase(Constant.USA_AAOIFI_COMPLIANCE)) {
				for (int i = 0; i <= 1; i++) {
					Integer count = 1;
					serverCSVFieldList = serverCSVFormateReader.readNext();
					for (String field : serverCSVFieldList) {
						if (!field.isBlank()) {
							fieldCount = count++;
						}
					}
				}
			} else if (fileType.equalsIgnoreCase(Constant.AL_RAJHI_COMPLIANCE)) {
				for (int i = 0; i <= 2; i++) {
					Integer count = 1;
					serverCSVFieldList = serverCSVFormateReader.readNext();
					for (String field : serverCSVFieldList) {
						if (!field.isBlank()) {
							fieldCount = count++;
						}
					}
				}
			}

			Boolean isFileFormated = false;
			if (localCSVFieldList.length == fieldCount) {
				for (int i = 0; i < fieldCount; i++) {
					if (serverCSVFieldList[i].equalsIgnoreCase(localCSVFieldList[i])) {
						isFileFormated = true;
					} else {
						isFileFormated = false;
						break;
					}
				}
			}

			if (isFileFormated == true) {
//				log.info("File is uploading! status - {}", Constant.OK);
//				String fileName = FileUploader.uploadFile(file, uploadDir);
//				if (fileName != null) {
//					log.info("New file is : " + fileName + " - uploaded successfully! status - {}", Constant.OK);
				log.info("Started, Reading file ! status - {}", Constant.OK);
				CSVReader csvReader = new CSVReaderBuilder(localFileReader).withSkipLines(0).build();
				List<String[]> allData = csvReader.readAll();
				if (!allData.isEmpty()) {
					if (fileType.equalsIgnoreCase(Constant.SAUDI_AAOIFI_COMPLIANCE)) {
						for (String[] row : allData) {
							ShariaCompliance sariaCompliance = new ShariaCompliance();
							Integer total = 0, index = 0;
							total = row.length - 1;
							while (index <= total) {
								String cell = row[index];
								switch (index) {
								case 0:
									if (!cell.isBlank()) {
										sariaCompliance.setTickerNames(cell);
									} else {
										sariaCompliance.setTickerNames("");
									}
									break;
								case 1:
									if (!cell.isBlank()) {
										sariaCompliance.setTickerSymbol(cell.substring(0, cell.length() - 3));
									} else {
										sariaCompliance.setTickerSymbol("");
									}
									break;
								case 2:
									if (!cell.isBlank()) {
										sariaCompliance.setComplianceStatus(cell);
									} else {
										sariaCompliance.setComplianceStatus("");
									}
									break;
								case 3:
									if (!cell.isBlank()) {
										sariaCompliance.setComplainceDegree(Short.parseShort(cell));
									} else {
										sariaCompliance.setComplainceDegree((short) 0);
									}
									break;
								case 4:
									if (!cell.isBlank()) {
										sariaCompliance
												.setRevenueBreakdownHalal(Float.parseFloat(cell.replace("%", "")));
									} else {
										sariaCompliance.setRevenueBreakdownHalal(0.0f);
									}
									break;
								case 5:
									if (!cell.isBlank()) {
										sariaCompliance
												.setRevenueBreakdownDoubtful(Float.parseFloat(cell.replace("%", "")));
									} else {
										sariaCompliance.setRevenueBreakdownDoubtful(0.0f);
									}
									break;
								case 6:
									if (!cell.isBlank()) {
										sariaCompliance
												.setRevenueBreakdownNotHalal(Float.parseFloat(cell.replace("%", "")));
									} else {
										sariaCompliance.setRevenueBreakdownNotHalal(0.0f);
									}
									break;
								case 7:
									if (!cell.isBlank()) {
										sariaCompliance.setInterestBearingSecuritiesAndAssetsStatus(cell);
									} else {
										sariaCompliance.setInterestBearingSecuritiesAndAssetsStatus("");
									}
									break;
								case 8:
									if (!cell.isBlank()) {
										sariaCompliance.setInterestBearingSecuritiesAndAssetsPercentage(
												Float.parseFloat(cell.replace("%", "")));
									} else {
										sariaCompliance.setInterestBearingSecuritiesAndAssetsPercentage(0.0f);
									}
									break;
								case 9:
									if (!cell.isBlank()) {
										sariaCompliance.setInterestBearingDebtStatus(cell);
									} else {
										sariaCompliance.setInterestBearingDebtStatus("");
									}
									break;
								case 10:
									if (!cell.isBlank()) {
										sariaCompliance.setInterestBearingDebtPercentage(
												Float.parseFloat(cell.replace("%", "")));
									} else {
										sariaCompliance.setInterestBearingDebtPercentage(0.0f);
									}
									break;
								case 11:
									if (!cell.isBlank()) {
										sariaCompliance.setLastUpdated(cell);
									} else {
										sariaCompliance.setLastUpdated("");
									}
									break;
								case 12:
									if (!cell.isBlank()) {
										sariaCompliance.setSource(cell);
									} else {
										sariaCompliance.setSource("");
									}
									break;
								case 13:
									if (!cell.isBlank()) {
										sariaCompliance.setUrl(cell);
									} else {
										sariaCompliance.setUrl("");
									}
									break;
								default:
									break;
								}
								index++;
							}
							sariaCompliance.setCountry(Constant.SAUDI_ARABIA);
							sariaCompliance.setCreationDate(new Date());
							sariaComplianceList.add(sariaCompliance);
							complianceResponse.setSariaComplianceList(sariaComplianceList);
							complianceResponse.setSaudiShariahCompliancesList(saudiShariahCompliancesList);
						}
					} else if (fileType.equalsIgnoreCase(Constant.USA_AAOIFI_COMPLIANCE)) {
						for (String[] row : allData) {
							ShariaCompliance sariaCompliance = new ShariaCompliance();
							Integer total = 0, index = 0;
							total = row.length - 1;
							while (index <= total) {
								String cell = row[index];
								switch (index) {
								case 0:
									if (!cell.isBlank()) {
										sariaCompliance.setTickerNames(cell);
									} else {
										sariaCompliance.setTickerNames("");
									}
									break;
								case 1:
									if (!cell.isBlank()) {
										sariaCompliance.setTickerSymbol(cell);
									} else {
										sariaCompliance.setTickerSymbol("");
									}
									break;
								case 2:
									if (!cell.isBlank()) {
										sariaCompliance.setComplianceStatus(cell);
									} else {
										sariaCompliance.setComplianceStatus("");
									}
									break;
								case 3:
									if (!cell.isBlank()) {
										sariaCompliance.setComplainceDegree(Short.parseShort(cell));
									} else {
										sariaCompliance.setComplainceDegree((short) 0);
									}
									break;
								case 4:
									if (!cell.isBlank()) {
										sariaCompliance
												.setRevenueBreakdownHalal(Float.parseFloat(cell.replace("%", "")));
									} else {
										sariaCompliance.setRevenueBreakdownHalal(0.0f);
									}
									break;
								case 5:
									if (!cell.isBlank()) {
										sariaCompliance
												.setRevenueBreakdownDoubtful(Float.parseFloat(cell.replace("%", "")));
									} else {
										sariaCompliance.setRevenueBreakdownDoubtful(0.0f);
									}
									break;
								case 6:
									if (!cell.isBlank()) {
										sariaCompliance
												.setRevenueBreakdownNotHalal(Float.parseFloat(cell.replace("%", "")));
									} else {
										sariaCompliance.setRevenueBreakdownNotHalal(0.0f);
									}
									break;

//								case 7:
//									if (!cell.isBlank()) {
//										sariaCompliance.setInterestBearingSecuritiesAndAssets(cell);
//									} else {
//										sariaCompliance.setInterestBearingSecuritiesAndAssets("");
//									}
//									break;
//								case 8:
//									if (!cell.isBlank()) {
//										sariaCompliance.setInterestBearingDebt(cell);
//									} else {
//										sariaCompliance.setInterestBearingDebt("");
//									}
//									break;
								// new chnges 18 march
								case 7:
									if (!cell.isBlank()) {
										sariaCompliance.setInterestBearingSecuritiesAndAssetsStatus(cell);
									} else {
										sariaCompliance.setInterestBearingSecuritiesAndAssetsStatus("");
									}
									break;
								case 8:
									if (!cell.isBlank()) {
										sariaCompliance.setInterestBearingSecuritiesAndAssetsPercentage(
												Float.parseFloat(cell.replace("%", "")));
									} else {
										sariaCompliance.setInterestBearingSecuritiesAndAssetsPercentage(0.0f);
									}
									break;
								case 9:
									if (!cell.isBlank()) {
										sariaCompliance.setInterestBearingDebtStatus(cell);
									} else {
										sariaCompliance.setInterestBearingDebtStatus("");
									}
									break;
								case 10:
									if (!cell.isBlank()) {
										sariaCompliance.setInterestBearingDebtPercentage(
												Float.parseFloat(cell.replace("%", "")));
									} else {
										sariaCompliance.setInterestBearingDebtPercentage(0.0f);
									}
									break;
								case 11:
									if (!cell.isBlank()) {
										sariaCompliance.setLastUpdated(cell);
									} else {
										sariaCompliance.setLastUpdated("");
									}
									break;
								case 12:
									if (!cell.isBlank()) {
										sariaCompliance.setSource(cell);
									} else {
										sariaCompliance.setSource("");
									}
									break;
								case 13:
									if (!cell.isBlank()) {
										sariaCompliance.setUrl(cell);
									} else {
										sariaCompliance.setUrl("");
									}
									break;
								default:
									break;
								}
								index++;
							}
							sariaCompliance.setCountry(Constant.UNITED_STATES);
							sariaCompliance.setCreationDate(new Date());
							sariaComplianceList.add(sariaCompliance);
							complianceResponse.setSariaComplianceList(sariaComplianceList);
							complianceResponse.setSaudiShariahCompliancesList(saudiShariahCompliancesList);
						}
					} else if (fileType.equalsIgnoreCase(Constant.AL_RAJHI_COMPLIANCE)) {
						for (String[] row : allData) {
							SaudiShariahCompliance saudiShariahCompliance = new SaudiShariahCompliance();
							Integer total = 0, index = 0;
							total = row.length - 1;
							while (index <= total) {
								String cell = row[index];
								switch (index) {
								case 0:
									if (!cell.isBlank()) {
										saudiShariahCompliance.setCompliance_status(cell);
									} else {
										saudiShariahCompliance.setCompliance_status("");
									}
								case 1:
									if (!cell.isBlank()) {
										saudiShariahCompliance.setSymbol(cell);
									} else {
										saudiShariahCompliance.setSymbol("");
									}
									break;
								case 2:
									if (!cell.isBlank()) {
										saudiShariahCompliance.setLastUpdated(cell);
									} else {
										saudiShariahCompliance.setLastUpdated("");
									}
									break;
								case 3:
									if (!cell.isBlank()) {
										saudiShariahCompliance.setSource(cell);
									} else {
										saudiShariahCompliance.setSource("");
									}
									break;
								default:
									break;
								}
								index++;
							}
//							sariaCompliance.setCountry(Constant.UNITED_STATES);
							saudiShariahCompliance.setCreationDate(new Date());
							saudiShariahCompliancesList.add(saudiShariahCompliance);
							complianceResponse.setSariaComplianceList(sariaComplianceList);
							complianceResponse.setSaudiShariahCompliancesList(saudiShariahCompliancesList);
						}
					} else {
						complianceResponse.setSariaComplianceList(sariaComplianceList);
						complianceResponse.setSaudiShariahCompliancesList(saudiShariahCompliancesList);
						return complianceResponse;
					}
				} else {
					complianceResponse.setSariaComplianceList(sariaComplianceList);
					complianceResponse.setSaudiShariahCompliancesList(saudiShariahCompliancesList);
					return complianceResponse;
				}
//				} else {
//					return sariaComplianceList;
//				}
			} else {
				complianceResponse.setSariaComplianceList(sariaComplianceList);
				complianceResponse.setSaudiShariahCompliancesList(saudiShariahCompliancesList);
				return complianceResponse;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return complianceResponse;
	}

}
