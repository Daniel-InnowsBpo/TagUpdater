package com.auto.objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.auto.Supports.DataReader;
import com.auto.Supports.WrapperClass;

public class CentersLabEmsw extends WrapperClass {
	private By useThis;
	ExcelReadWrite excelReadWrite = new ExcelReadWrite();
	Map<String, Map<Integer, Map<String, String>>> dataFromExcelWorkBook = new HashMap<>();
	Set<String> groupedCPTs = new HashSet<>();;
	Set<String> groupedClaimIDs = new HashSet<>();
	Set<String> groupedRemarkCode = new HashSet<>();
	Set<String> groupedRemarkCodeDesc = new HashSet<>();

	Set<String> CPTToPostinNotes = new HashSet<>();;
	Set<String> claimIDToPostinNotes = new HashSet<>();
	Set<String> RemarkCode = new HashSet<>();
	Set<String> RemarkCodeWithCPT = new HashSet<>();
	Set<String> RemarkCodeDescToPostinNotes = new HashSet<>();
	Set<String> claimWithDeniedInfo = new HashSet<>();
	String remarkCodeRef = "";

	Set<String> internalCPTList = new HashSet<>();
	Workbook workBook;
	Sheet sheet;
	float paidAmount = 0;
	Row row;
	String serviceID = null;
	String groupedServiceID = null;
	WebElement checkBox;
	int groupingCounter = 0;
	String serviceIDForRef;
	int groupedDataRow;
	int counter = 0;
	GroupedData groupedData;
	DataToPostinNotes dataToPostinNotes;

	Map<Set<String>, Set<String>> remarkCodeWithDesc = new HashMap<>();

	Map<Set<String>, Map<Set<String>, Set<String>>> cptWithRemark = new HashMap<>();

	Map<Set<String>, Map<Set<String>, Map<Set<String>, Set<String>>>> claimWithCPts = new HashMap<>();

	Map<String, Map<Set<String>, Map<Set<String>, Map<Set<String>, Set<String>>>>> ServiceIdWthClaims = new HashMap<>();

	Map<Integer, DataToPostinNotes> overAllDataToPost = new HashMap<>();

	Map<Integer, GroupedData> overallGroupedData = new HashMap<>();

	ArrayList<String> collectedServiceID = new ArrayList<>();
	Map<String, Integer> occurencesOfServiceID;
	int recordCOunt = 1;

	public CentersLabEmsw() {
		DataReader.getData("Centers Lab");
		DataReader.getDataKeyandValue("Centers Lab");
	}

	public void notesUpdate() throws InterruptedException, IOException {
		clearTabs();
		readExcelForInput();
		readyDataToEnterIntoCentersLab();
		enterIntoCentersLab();
	}

	private void clearTabs() throws InterruptedException, IOException {

		List<WebElement> closableTabs = driver
				.findElements(By.xpath("//div[normalize-space(@class)='x-tab-panel tab-close-button x-border-panel']"
						+ "/div[1]/div/ul/li[contains(@class,'x-tab-strip-closable')]"));
		if (!(closableTabs.isEmpty())) {
			List<WebElement> loader = driver.findElements(By.xpath("//div[text()='Loading...']"));
			while (!(loader.isEmpty())) {
				loader = driver.findElements(By.xpath("//div[text()='Loading...']"));
				if (loader.isEmpty()) {
					break;
				}
			}
			click("Centers Lab Close Tab", useThis);
			Thread.sleep(3000);
		}

	}

	public void readyDataToEnterIntoCentersLab() {
		for (Map<Integer, Map<String, String>> eachSheetWithAllData : dataFromExcelWorkBook.values()) {
			for (Map<String, String> eachRow : eachSheetWithAllData.values()) {
				collectedServiceID.add(eachRow.get("Service ID"));
			}
		}
		countFrequencies(collectedServiceID);

//		for (Map<Integer, Map<String, String>> eachSheetWithAllData : dataFromExcelWorkBook.values()) {
//
//			for (Map<String, String> eachRowData : eachSheetWithAllData.values()) {
//
//				if (counter == 0) {
//					groupedDataRow = occurencesOfServiceID.get(eachRowData.get("Service ID"));
//				}
//
//				groupedServiceID = eachRowData.get("Service ID");
//				paidAmount = paidAmount + Float.parseFloat(eachRowData.get("Paid Amount").replace("$", ""));
//				groupedClaimIDs.add(eachRowData.get("Claim ID"));
//				groupedCPTs.add(eachRowData.get("CPT Code"));
//				groupedRemarkCode.add(eachRowData.get("Remark Code"));
//				groupedRemarkCodeDesc.add(eachRowData.get("Remark Code Description"));
//				counter++;
//				groupedDataRow--;
//
//				if (groupedDataRow == 0) {
//					groupedData = new GroupedData(groupedServiceID, paidAmount, String.join(", ", groupedCPTs),
//							String.join(", ", groupedClaimIDs), String.join(", ", groupedRemarkCode),
//							String.join(", ", groupedRemarkCodeDesc));
//					overallGroupedData.put(++groupingCounter, groupedData);
//
//					groupedDataRow = 0;
//					counter = 0;
//					groupedClaimIDs.clear();
//					groupedCPTs.clear();
//					groupedRemarkCode.clear();
//					groupedRemarkCodeDesc.clear();
//					groupedServiceID = "";
//					paidAmount = 0;
//				}
//
//			}
//
//		}

		for (Map<Integer, Map<String, String>> eachSheetWithAllData : dataFromExcelWorkBook.values()) {

			for (int i = 1; i <= eachSheetWithAllData.values().size(); i++) {

				if (counter == 0) {
					groupedDataRow = occurencesOfServiceID.get(eachSheetWithAllData.get(i).get("Service ID"));
				}

				groupedServiceID = eachSheetWithAllData.get(i).get("Service ID");

				paidAmount = paidAmount
						+ Float.parseFloat(eachSheetWithAllData.get(i).get("Paid Amount").replace("$", ""));

				if (Float.parseFloat(eachSheetWithAllData.get(i).get("Paid Amount").replace("$", "")) == 0) {

					claimIDToPostinNotes.add("Claim #" + eachSheetWithAllData.get(i).get("Claim ID")
							+ " is Fully Denied for CPT: " + eachSheetWithAllData.get(i).get("CPT Code"));

				} else {
					claimIDToPostinNotes.add("Claim #" + eachSheetWithAllData.get(i).get("Claim ID")
							+ " is Partially Denied for CPT: " + eachSheetWithAllData.get(i).get("CPT Code"));
				}

//				claimIDToPostinNotes.add(eachSheetWithAllData.get(i).get("Claim ID"));

				CPTToPostinNotes.add(eachSheetWithAllData.get(i).get("CPT Code"));

				RemarkCode.add(eachSheetWithAllData.get(i).get("Remark Code"));

				remarkCodeRef = eachSheetWithAllData.get(i).get("Remark Code");

				if (remarkCodeRef.length() > 0) {
					RemarkCodeWithCPT.add(eachSheetWithAllData.get(i).get("CPT Code") + " is Denied With Code-"
							+ eachSheetWithAllData.get(i).get("Remark Code") + ". ");

				}

				RemarkCodeDescToPostinNotes.add(eachSheetWithAllData.get(i).get("Remark Code Description"));

				counter++;
				groupedDataRow--;

				if (groupedDataRow == 0) {

//					remarkCodeWithDesc.put(RemarkCodeToPostinNotes, RemarkCodeDescToPostinNotes);
					cptWithRemark.put(CPTToPostinNotes, remarkCodeWithDesc);
					claimWithCPts.put(claimIDToPostinNotes, cptWithRemark);
					ServiceIdWthClaims.put(groupedServiceID, claimWithCPts);

					dataToPostinNotes = new DataToPostinNotes(groupedServiceID, paidAmount,
							String.join(", ", claimIDToPostinNotes), String.join(", ", CPTToPostinNotes),
							String.join(", ", RemarkCodeWithCPT), String.join(", ", RemarkCodeDescToPostinNotes),
							String.join(", ", RemarkCode));
					overAllDataToPost.put(++groupingCounter, dataToPostinNotes);

					groupedDataRow = 0;
					counter = 0;
					claimIDToPostinNotes.clear();
					RemarkCodeWithCPT.clear();
					RemarkCodeDescToPostinNotes.clear();
					remarkCodeWithDesc.clear();
					cptWithRemark.clear();
					claimWithCPts.clear();
					RemarkCodeWithCPT.clear();
					CPTToPostinNotes.clear();
					ServiceIdWthClaims.clear();
					RemarkCode.clear();
					paidAmount = 0;
				}

			}

		}

	}

	public void countFrequencies(ArrayList<String> collectedServiceID) {
		occurencesOfServiceID = new HashMap<String, Integer>();

		for (String i : collectedServiceID) {
			Integer j = occurencesOfServiceID.get(i);
			occurencesOfServiceID.put(i, (j == null) ? 1 : j + 1);
		}

	}

	public void readExcelForInput() throws IOException {
		dataFromExcelWorkBook = excelReadWrite.extractData("HealthFirstClaimUpdateData");
	}

	public void enterIntoCentersLab() throws InterruptedException {
//
		click("Centers Lab Billing Button", useThis);
		click("Centers Lab Billing Option", useThis);
		waitUntillItGoes("Centers Lab Loader");
		click("Centers Group By CheckBox", useThis);

		for (int i = 1; i <= overAllDataToPost.values().size(); i++) {

			writeHere("Centers Service ID Box", overAllDataToPost.get(i).groupedServiceID, useThis);
			click("Centers Search Button", useThis);
			Thread.sleep(1000);
			waitForNow("Xpath",
					"//div[@qtip='Service ID']/a[text()='" + overAllDataToPost.get(i).groupedServiceID + "']");
			Thread.sleep(1000);
//			waitForNow("Xpath", "//div[@class='x-panel-body']/div/div/div/following-sibling::div//tbody/tr[1]/td");
			List<WebElement> loader3 = driver.findElements(By.xpath("//div[text()='Loading...']"));
			while (!(loader3.isEmpty())) {
				loader3 = driver.findElements(By.xpath("//div[text()='Loading...']"));
				if (loader3.isEmpty()) {
					break;
				}
			}
			click("Centers First Claim Select", useThis);

			checkBox = driver.findElement(By.xpath(
					"//div[@class='x-panel-body']/div/div/div/following-sibling::div//tbody/tr[1]/td/div/div[1]/ancestor::table/parent::div[1]"));
			String checkboxClassName = checkBox.getAttribute("class");
			if (!checkboxClassName.contains("selected")) {

				click("Centers First Claim Select", useThis);
			}
			click("Centers Service Button", useThis);

			click("Centers Billing Edit", useThis);
			List<WebElement> loader = driver.findElements(By.xpath("//div[text()='Loading...']"));
			while (!(loader.isEmpty())) {
				loader = driver.findElements(By.xpath("//div[text()='Loading...']"));
				if (loader.isEmpty()) {
					break;
				}
			}
			try {
				waitFor("Centers Processing Button");
			} catch (NoSuchElementException e) {
				checkBox = driver.findElement(By.xpath(
						"//div[@class='x-panel-body']/div/div/div/following-sibling::div//tbody/tr[1]/td/div/div[1]/ancestor::table/parent::div[1]"));
				String checkboxClassNameinside = checkBox.getAttribute("class");
				if (!checkboxClassNameinside.contains("selected")) {

					click("Centers First Claim Select", useThis);
				}
				click("Centers Service Button", useThis);

				click("Centers Billing Edit", useThis);
				waitFor("Centers Processing Button");
			}

			click("Centers Edit Comment", useThis);
			String notesText = gettext("Centers Comment Counter").split(" ")[1];
			notesText = notesText.replace("(", "");
			notesText = notesText.replace(")", "");
			if (notesText.equals("0")) {
				click("Centers Add Button", useThis);
				waitFor("Centers Comment Area");

				if (overAllDataToPost.get(i).paidAmnt == 0) {

					writeHere("Centers Comment Area", overAllDataToPost.get(i).claimID + "." + "\n"
							+ "Remark Code Details:- " + overAllDataToPost.get(i).remarkCodeWithCPT + "." + "\n"
							+ "Remark Code description: " + "\n" + overAllDataToPost.get(i).remarkCodeDesc + ".",
							useThis);
					System.out.println(overAllDataToPost.get(i).claimID + "." + "\n" + "Remark Code Details:- "
							+ overAllDataToPost.get(i).remarkCodeWithCPT + "." + "\n" + "Remark Code description: "
							+ "\n" + overAllDataToPost.get(i).remarkCodeDesc + ".");
					System.out.println();

				} else {

					if (overAllDataToPost.get(i).remarkCode.length() < 1) {
						writeHere("Centers Comment Area",

								overAllDataToPost.get(i).claimID + "." + "\n" + "No Remark Codes. "
										+ "Got Paid overall $" + overAllDataToPost.get(i).paidAmnt + ".",
								useThis);
						System.out.println(overAllDataToPost.get(i).claimID + "." + "\n" + "No Remark Codes. "
								+ "Got Paid overall $" + overAllDataToPost.get(i).paidAmnt + ".");
						System.out.println();
					} else {
						writeHere("Centers Comment Area",

								overAllDataToPost.get(i).claimID + "." + "\n" + "Remark Code Details:- "
										+ overAllDataToPost.get(i).remarkCodeWithCPT + "." + "\n" + "Got Paid overall $"
										+ overAllDataToPost.get(i).paidAmnt + "." + "\n" + "Remark Code description: "
										+ "\n" + overAllDataToPost.get(i).remarkCodeDesc,
								useThis);
						System.out.println(overAllDataToPost.get(i).claimID + "." + "\n" + "Remark Code Details:- "
								+ overAllDataToPost.get(i).remarkCodeWithCPT + "." + "\n" + "Got Paid overall $"
								+ overAllDataToPost.get(i).paidAmnt + "." + "\n" + "Remark Code description: " + "\n"
								+ overAllDataToPost.get(i).remarkCodeDesc);
						System.out.println();
					}
				}
				Thread.sleep(1500);
				List<WebElement> loader2 = driver.findElements(By.xpath("//div[text()='Loading...']"));
				while (!(loader2.isEmpty())) {
					loader2 = driver.findElements(By.xpath("//div[text()='Loading...']"));
					if (loader2.isEmpty()) {
						break;
					}
				}
				click("Centers Save Button", useThis);
				Thread.sleep(3000);
				List<WebElement> loader1 = driver.findElements(By.xpath("//div[text()='Loading...']"));
				while (!(loader1.isEmpty())) {
					loader1 = driver.findElements(By.xpath("//div[text()='Loading...']"));
					if (loader1.isEmpty()) {
						break;
					}
				}
//				click("Centers Cancel Button", useThis);
				waitFor("Centers Processing Button");
			} else {
				System.out.println("For Service ID->" + overAllDataToPost.get(i).groupedServiceID
						+ " Comments already there so ignored");
			}

			List<WebElement> loader2 = driver.findElements(By.xpath("//div[text()='Loading...']"));
			while (!(loader2.isEmpty())) {
				loader2 = driver.findElements(By.xpath("//div[text()='Loading...']"));
				if (loader2.isEmpty()) {
					break;
				}
			}
			click("Centers Close Button", useThis);
			waitForNow("Xpath", "//div[@class='x-panel-body']/div/div/div/following-sibling::div//tbody/tr[1]/td");
		}

	}
}
