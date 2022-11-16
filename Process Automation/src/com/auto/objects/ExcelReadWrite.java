package com.auto.objects;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.auto.Supports.WrapperClass;

public class ExcelReadWrite extends WrapperClass {

	Workbook workBook;
	Sheet sheet;
	Row row;
	FileInputStream fis;
	FileOutputStream fos;
	String filePath = null;
	WrapperClass wrap = new WrapperClass();

	public XSSFWorkbook outPutworkBook = new XSSFWorkbook();

	static XSSFRow outPutRow;
	static XSSFSheet outPutSheet;
	int totalNumberOfSheets;
	static int rowNumber;
	int cellNumber = 0;

	Map<String, Map<Integer, Map<String, String>>> dataFromExcelWorkBook = new HashMap<>();

	Map<Integer, Map<String, String>> childData;

	public Map<String, Map<Integer, Map<String, String>>> extractData(String fileName) throws IOException {

		fis = new FileInputStream(System.getProperty("user.dir") + "//" + fileName + ".xlsx");
		workBook = new XSSFWorkbook(fis);
		totalNumberOfSheets = workBook.getNumberOfSheets();
		DataFormatter dataFormatter = new DataFormatter();

		for (int i = 0; i < totalNumberOfSheets; i++) {
			sheet = workBook.getSheetAt(i);
			int lastRowInSheet = sheet.getLastRowNum();
			childData = new HashMap<>();
			for (int j = 1; j <= lastRowInSheet; j++) {
				Row row = sheet.getRow(j);
				Map<String, String> excelData = new HashMap<>();

				for (int k = 0; k < sheet.getRow(0).getLastCellNum(); k++) {
					if (row.getCell(k) != null) {
						excelData.put(sheet.getRow(0).getCell(k).toString(),
								dataFormatter.formatCellValue(row.getCell(k)));
					} else {
						excelData.put(dataFormatter.formatCellValue(sheet.getRow(0).getCell(k)), null);
					}
				}
				childData.put(j, excelData);
			}
			dataFromExcelWorkBook.put(sheet.getSheetName(), childData);
		}

		fis.close();
		return dataFromExcelWorkBook;
	}

	public void writeExcelForTagFinder(String fileName, Map<Integer, TagFinderWriteObject> excelWrite)
			throws IOException {

		sheet = workBook.getSheetAt(0);
		int lastRownum = sheet.getLastRowNum();
//		int lasColNum = sheet.getRow(0).getLastCellNum();
		sheet.getRow(0).createCell(1).setCellValue("Initial Tags");
		sheet.getRow(0).createCell(2).setCellValue("Tags Removed");
		sheet.getRow(0).createCell(3).setCellValue("Tags Added");
		sheet.getRow(0).createCell(4).setCellValue("Tag After Updation");

		for (int i = 1; i <= lastRownum; i++) {
			sheet.getRow(i).createCell(1).setCellValue(excelWrite.get(i).initialTags);
			sheet.getRow(i).createCell(2).setCellValue(excelWrite.get(i).removedTags);
			sheet.getRow(i).createCell(3).setCellValue(excelWrite.get(i).addedTags);
			sheet.getRow(i).createCell(4).setCellValue(excelWrite.get(i).tagsAfterUpdation);

		}
		fis.close();
		FileOutputStream fos = new FileOutputStream(System.getProperty("user.dir") + "//" + fileName + ".xlsx");
		workBook.write(fos);
		fos.close();
	}

	private void createOuputExcelSheet(String typeOfSheet) {
//		String fileName = new SimpleDateFormat("Health First_" + "yyyyMMddHHmm'.xlsx'").format(new Date());
//		String fileName = System.getProperty("user.dir") + "//Health First_" + fileNameCount;

		if (outPutworkBook.getSheet(typeOfSheet) == null) {
			try {
				outPutSheet = outPutworkBook.createSheet(typeOfSheet);
				createHeaders();
			} catch (IllegalArgumentException e) {
				outPutworkBook.getSheet(typeOfSheet);
			}

			rowNumber = outPutSheet.getLastRowNum() + 1;

		} else {
			outPutSheet = outPutworkBook.getSheet(typeOfSheet);
			rowNumber = outPutSheet.getLastRowNum() + 1;

		}
	}

	private void createOuputForEmptyRecords(String typeOfSheet) {
//		String fileName = new SimpleDateFormat("Health First_" + "yyyyMMddHHmm'.xlsx'").format(new Date());
//		String fileName = System.getProperty("user.dir") + "//Health First_" + fileNameCount;

		if (outPutworkBook.getSheet(typeOfSheet) == null) {
			try {
				outPutSheet = outPutworkBook.createSheet(typeOfSheet);
				createHeadersForEmptyRecords();
			} catch (IllegalArgumentException e) {
				outPutworkBook.getSheet(typeOfSheet);
			}

			rowNumber = outPutSheet.getLastRowNum() + 1;

		} else {
			outPutSheet = outPutworkBook.getSheet(typeOfSheet);
			rowNumber = outPutSheet.getLastRowNum() + 1;
		}
	}

	public void splitClaims(Map<Integer, List<Map<String, String>>> claimDetailsRowWise) {
//		for (Map<String, String> eachData : eachRowclaimDetails.getValue().s) {
//			eachData.size();
//		}
		String typeOfSheet = null;
		List<String> paidAmount = null;
		paidAmount = new ArrayList<>();
		int claimDetailsRowSize = claimDetailsRowWise.size();
		for (int i = 1; i <= claimDetailsRowSize; i++) {

			if (claimDetailsRowWise.get(i).get(15).get("Paid Amount").equals("$0.00")) {
				paidAmount.add(claimDetailsRowWise.get(i).get(15).get("Paid Amount"));
			}

		}

		if (paidAmount.size() == claimDetailsRowSize) {
			typeOfSheet = "Health First Fully Denied";
		} else if (paidAmount.size() < claimDetailsRowSize) {
			typeOfSheet = "Health First partially Denied";
		} else if (paidAmount.size() == 0) {
			typeOfSheet = "Health First Fully Paid";
		}
		createOuputExcelSheet(typeOfSheet);

	}

	public void OutputExcelCreation(Map<String, Map<String, String>> memberDetails,
			Map<String, Map<String, String>> overViewData, Map<Integer, List<Map<String, String>>> claimDetailsRowWise,
			String StringConvertedLegendData, boolean isClaimEmpty) throws IOException {

		if (isClaimEmpty) {
			createOuputForEmptyRecords("Empty Records");
			isClaimEmpty = false;
		} else {
			for (Entry<Integer, List<Map<String, String>>> EachRowclaimDetails : claimDetailsRowWise.entrySet()) {

				outPutRow = outPutSheet.createRow(rowNumber);

//				outPutRow.createCell(fileNameCount);
				outPutRow.createCell(0).setCellValue(memberDetails.get("Member Details").get("Member Details").trim());
				outPutRow.createCell(1).setCellValue(overViewData.get("OverView").get("Claim Number").trim());
				outPutRow.createCell(2).setCellValue(EachRowclaimDetails.getValue().get(1).get("Pro-CPT Code"));
				outPutRow.createCell(3).setCellValue(overViewData.get("OverView").get("Claim Received").trim());
				outPutRow.createCell(4).setCellValue(overViewData.get("OverView").get("Status").trim());
//				outPutRow.createCell(5).setCellValue(overViewData.get("OverView").get("Paid Amount").trim());
				outPutRow.createCell(5).setCellValue(overViewData.get("OverView").get("Paid Date").trim());
//				outPutRow.createCell(7).setCellValue(wrap.gettext("Health First Claim Number").toString().trim());

				outPutRow.createCell(6).setCellValue(EachRowclaimDetails.getValue().get(5).get("Total Charge"));
				outPutRow.createCell(7).setCellValue(EachRowclaimDetails.getValue().get(10).get("Remark Code"));
				outPutRow.createCell(8).setCellValue(EachRowclaimDetails.getValue().get(15).get("Paid Amount"));

				if (StringConvertedLegendData.length() > 1) {
					outPutSheet.addMergedRegion(new CellRangeAddress(rowNumber, rowNumber, 10, 16));
					outPutRow.createCell(9).setCellValue(StringConvertedLegendData);

				}
				rowNumber++;
			}
		}

//		rowNumber++;
	}

	private void createHeadersForEmptyRecords() {
		outPutRow = outPutSheet.createRow(0);
		outPutRow.createCell(0).setCellValue("Member ID");
		outPutRow.createCell(1).setCellValue("Member DOB");
		outPutRow.createCell(2).setCellValue("Member DOS");
	}

	private void createHeaders() {
		outPutRow = outPutSheet.createRow(0);
		outPutRow.createCell(0).setCellValue("Service ID");
		outPutRow.createCell(1).setCellValue("Claim ID");
		outPutRow.createCell(2).setCellValue("CPT Code");
		outPutRow.createCell(3).setCellValue("Claim Received Date");
		outPutRow.createCell(4).setCellValue("Status");
//		outPutRow.createCell(5).setCellValue("Paid Amount");
		outPutRow.createCell(5).setCellValue("Paid Date");
//		outPutRow.createCell(7).setCellValue("Check/EFT/VCard #");

		outPutRow.createCell(6).setCellValue("Total Charge");
		outPutRow.createCell(7).setCellValue("Remark COde");
		outPutRow.createCell(8).setCellValue("Paid Amount");
		outPutSheet.addMergedRegion(new CellRangeAddress(0, 0, 10, 16));
		outPutRow.createCell(9).setCellValue("Remark Code Description");

	}

	public void emptywrite() {

	}

	public void filOut() throws IOException {
		String fileName = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
		FileOutputStream fileOut = new FileOutputStream(
				System.getProperty("user.dir") + "//HealthFirst_OutPut_" + fileName + ".xlsx");
		outPutworkBook.write(fileOut);
		fileOut.close();
	}

	public static void dataForEmptyRecord(
			Map<String, Map<Integer, Map<String, String>>> dataFromExcelWorkBookForEmptyRecord,
			String sheetNameForEmptyRecord, int rowNumForEmptyRecord) {
		outPutRow = outPutSheet.createRow(rowNumber);
		outPutRow.createCell(0).setCellValue(dataFromExcelWorkBookForEmptyRecord.get(sheetNameForEmptyRecord)
				.get(rowNumForEmptyRecord).get("Member ID"));
		outPutRow.createCell(1).setCellValue(dataFromExcelWorkBookForEmptyRecord.get(sheetNameForEmptyRecord)
				.get(rowNumForEmptyRecord).get("Member Date of Birth"));
		outPutRow.createCell(2).setCellValue(dataFromExcelWorkBookForEmptyRecord.get(sheetNameForEmptyRecord)
				.get(rowNumForEmptyRecord).get("Service Start Date"));
		rowNumber++;
	}

	public static void tagUpdaterData() {

	}
}
