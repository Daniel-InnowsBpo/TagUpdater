package com.auto.objects;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
	String filePath = System.getProperty("user.dir") + "//ClaimStatusCheckData.xlsx";
	WrapperClass wrap = new WrapperClass();

	public XSSFWorkbook outPutworkBook = new XSSFWorkbook();

	XSSFRow outPutRow;
	XSSFSheet outPutSheet;
	int totalNumberOfSheets;
	int rowNumber;
	int cellNumber = 0;

	Map<String, Map<Integer, Map<String, String>>> dataFromExcelWorkBook = new HashMap<>();

	Map<Integer, Map<String, String>> childData = new HashMap<>();

	public Map<String, Map<Integer, Map<String, String>>> extractData() throws IOException {

		fis = new FileInputStream(filePath);
		workBook = new XSSFWorkbook(fis);
		totalNumberOfSheets = workBook.getNumberOfSheets();

		for (int i = 0; i < totalNumberOfSheets; i++) {
			sheet = workBook.getSheetAt(i);
			int lastRowInSheet = sheet.getLastRowNum();
			for (int j = 1; j <= lastRowInSheet; j++) {
				Row row = sheet.getRow(j);
				Map<String, String> excelData = new HashMap<>();

				for (int k = 0; k < sheet.getRow(0).getLastCellNum(); k++) {
					if (row.getCell(k) != null) {
						excelData.put(sheet.getRow(0).getCell(k).toString(), row.getCell(k).toString());
					} else {
						excelData.put(sheet.getRow(0).getCell(k).toString(), null);
					}
				}
				childData.put(j, excelData);
			}
		}
		dataFromExcelWorkBook.put(sheet.getSheetName(), childData);
		fis.close();
		return dataFromExcelWorkBook;
	}

	private void createOuputExcelSheet(String typeOfSheet) {
//		String fileName = new SimpleDateFormat("Health First_" + "yyyyMMddHHmm'.xlsx'").format(new Date());
//		String fileName = System.getProperty("user.dir") + "//Health First_" + fileNameCount;

		if (outPutworkBook.getSheet("typeOfSheet") == null) {
			try {
				outPutSheet = outPutworkBook.createSheet(typeOfSheet);
				createHeaders();
			} catch (IllegalArgumentException e) {
				outPutworkBook.getSheet("typeOfSheet");
			}

			rowNumber = outPutSheet.getLastRowNum() + 1;

		} else {
			outPutworkBook.getSheet("typeOfSheet");
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
			Map<String, List<String>> legendData) throws IOException {
		int fileNameCount = 1;
		boolean legend = true;

		for (Entry<Integer, List<Map<String, String>>> EachRowclaimDetails : claimDetailsRowWise.entrySet()) {

			outPutRow = outPutSheet.createRow(rowNumber);

			outPutRow.createCell(fileNameCount);
			outPutRow.createCell(0).setCellValue(memberDetails.get("Member Details").get("Member Details").trim());
			outPutRow.createCell(1).setCellValue(overViewData.get("OverView").get("Claim Number").trim());
			outPutRow.createCell(2).setCellValue(EachRowclaimDetails.getValue().get(1).get("Pro-CPT Code"));
			outPutRow.createCell(3).setCellValue(overViewData.get("OverView").get("Claim Received").trim());
			outPutRow.createCell(4).setCellValue(overViewData.get("OverView").get("Status").trim());
			outPutRow.createCell(5).setCellValue(overViewData.get("OverView").get("Paid Amount").trim());
			outPutRow.createCell(6).setCellValue(overViewData.get("OverView").get("Paid Date").trim());
//			outPutRow.createCell(7).setCellValue(wrap.gettext("Health First Claim Number").toString().trim());

			outPutRow.createCell(7).setCellValue(EachRowclaimDetails.getValue().get(5).get("Total Charge"));
			outPutRow.createCell(8).setCellValue(EachRowclaimDetails.getValue().get(10).get("Remark Code"));
			outPutRow.createCell(9).setCellValue(EachRowclaimDetails.getValue().get(15).get("Paid Amount"));

			if (legend) {
				String eachLegend = String.join("," + "/n", legendData.get("Legends"));
				outPutSheet.addMergedRegion(new CellRangeAddress(rowNumber, rowNumber, 10, 16));
				outPutRow.createCell(10).setCellValue(eachLegend);
				legend = false;

			}
			rowNumber++;
		}
		rowNumber++;
		fileNameCount++;
	}

	private void createHeaders() {
		outPutRow = outPutSheet.createRow(0);
		outPutRow.createCell(0).setCellValue("Service ID");
		outPutRow.createCell(1).setCellValue("Claim ID");
		outPutRow.createCell(2).setCellValue("CPT Code");
		outPutRow.createCell(3).setCellValue("Claim Received Date");
		outPutRow.createCell(4).setCellValue("Status");
		outPutRow.createCell(5).setCellValue("Paid Amount");
		outPutRow.createCell(6).setCellValue("Paid Date");
//		outPutRow.createCell(7).setCellValue("Check/EFT/VCard #");

		outPutRow.createCell(7).setCellValue("Total Charge");
		outPutRow.createCell(8).setCellValue("Remark COde");
		outPutRow.createCell(9).setCellValue("Paid Amount");
		outPutSheet.addMergedRegion(new CellRangeAddress(0, 0, 10, 16));
		outPutRow.createCell(10).setCellValue("Remark Code Description");

	}

	public void filOut() throws IOException {
		FileOutputStream fileOut = new FileOutputStream(System.getProperty("user.dir") + "//OutPut File.xlsx");
		outPutworkBook.write(fileOut);
		fileOut.close();
	}
}
