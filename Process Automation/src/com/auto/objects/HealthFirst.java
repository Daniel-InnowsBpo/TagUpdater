package com.auto.objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.auto.Supports.DataReader;
import com.auto.Supports.WrapperClass;

public class HealthFirst extends WrapperClass {

	private By useThis;
	boolean flag = false;
	Map<String, Map<Integer, Map<String, String>>> dataFromExcelWorkBook = new HashMap<>();
	Map<String, List<String>> legendData;

	Map<String, Map<String, String>> overViewData = new HashMap<>();
	Map<String, String> overViewDataChildData = new HashMap<>();
	Map<String, Map<String, String>> memberDetails = new HashMap<>();
	Map<String, String> memberDetailsChildData = new HashMap<>();

	List<Map<String, Map<String, String>>> otherDetails;

	Map<Integer, List<Map<String, String>>> claimDetailsRowWise = new HashMap<>();

	Map<String, Map<Integer, List<Map<String, String>>>> claimDetails = new HashMap<>();

	List<String> remarks = new ArrayList<>();
	List<Map<String, String>> claimDetailsChild;
	ExcelReadWrite excelReadWrite = new ExcelReadWrite();

	String memberId = "";

	public HealthFirst() {
		DataReader.getData("Health First");
		DataReader.getDataKeyandValue("Health First");
	}

	public void statusCheck() throws InterruptedException, IOException {

		waitFor("Health First Home");

		waiting.until(ExpectedConditions.elementToBeClickable(
				driver.findElement(By.xpath("//b[normalize-space(text())='Consistency Is The Key To']"))));

		Thread.sleep(2000);

		click("Health First Proceed Button", useThis);
		Thread.sleep(2000);
		mouseHover("Health First Claims Search", useThis);

		click("Health First Claims Lookup", useThis);

		waitFor("Health First Claims search Page");

		dataFromExcelWorkBook = excelReadWrite.extractData();

		for (Entry<String, Map<Integer, Map<String, String>>> eachSheet : dataFromExcelWorkBook.entrySet()) {

			for (int i = 0; i < dataFromExcelWorkBook.size(); i++) {
				String sheetName = eachSheet.getKey().toString();
				switch (sheetName) {

				case "Health First":

					for (Map.Entry<Integer, Map<String, String>> excelData : dataFromExcelWorkBook.get(sheetName)
							.entrySet()) {
						int rowNum = excelData.getKey();

//						for (Map.Entry<String, String> data : dataFromExcelWorkBook.get(sheetName).get(rowNum)
//								.entrySet()) {
//						if (data.getKey().equalsIgnoreCase("Claim ID")) {

						// If claim comes by Claim ID

//						} else if (data.getKey().equalsIgnoreCase("Check/EFT/Vpay Payment Number")) {

						// If claim comes by Check/VFT/VPAY

//						} else {
						;
						writeHere("Health First Member ID box",
								dataFromExcelWorkBook.get(sheetName).get(rowNum).get("Member ID").toString().trim(),
								useThis);
						Thread.sleep(500);
						writeHere("Health First Member DOB box", dataFromExcelWorkBook.get(sheetName).get(rowNum)
								.get("Member Date of Birth").toString().trim(), useThis);
						Thread.sleep(500);
						writeHere("Health First Service Start Date", dataFromExcelWorkBook.get(sheetName).get(rowNum)
								.get("Service Start Date").toString().trim(), useThis);
						clear("Health First Service End Date", useThis);
						Thread.sleep(1000);
						scroll("500");
						click("Health First Submit Button", useThis);
//						datainitilization();
						dataExtract();

						if (dataFromExcelWorkBook.get(sheetName).size() > 1) {
							mouseHover("Health First Claims Search", useThis);

							click("Health First Claims Lookup", useThis);
							if (isDisplayed("Health First jumping window")) {
								flag = true;
							}

							if (flag) {
								click("Health First jumping window Close", useThis);
								flag = false;
							}

							waitFor("Health First Claims search Page");
						}

//						click("Health First New Search", useThis);
//						waitFor("Health First all Claims in search");

						// Any error code to display
					}

//						}
				}
				System.out.println("completed");

				break;

				// Add code here for other websites to check status

			}

//		}
			excelReadWrite.filOut();
		}

	}

	private void datainitilization() {
		// TODO Auto-generated method stub
		dataFromExcelWorkBook = new HashMap<>();
		overViewData = new HashMap<>();
		overViewDataChildData = new HashMap<>();
		memberDetails = new HashMap<>();
		memberDetailsChildData = new HashMap<>();
		claimDetailsRowWise = new HashMap<>();

		claimDetails = new HashMap<>();

		remarks = new ArrayList<>();

	}

	private void dataExtract() throws IOException, InterruptedException {

		int claimRow = 2;

		List<WebElement> numberOfclaims = new ArrayList<>();
		numberOfclaims = driver.findElements(By.xpath(
				"//div[@class='tabBody']//div[@class='pagegrid_container']/table/tbody/tr[contains(@class,'row')]/td[2]/a"));

		for (WebElement eachClaim : numberOfclaims) {
			if (claimRow == 2) {
				eachClaim.click();
			} else {
				scroll("500");
				click("Health First Submit Button", useThis);

				driver.findElement(By.xpath("//div[@class='tabBody']//div[@class='pagegrid_container']/table/tbody/tr["
						+ claimRow + "]/td[2]/a")).click();
			}

			waitFor("Health First Claim Page ID");
			otherDetails = new ArrayList<>();
			System.out.println("clicked");
			memberDetailsChildData.put("Member Details",
					dataExtractedFromWebElement("Member Details", "Patient Acct #"));
			memberDetails.put("Member Details", memberDetailsChildData);

			overViewDataChildData.put("Claim Number", dataExtractedFromWebElement("Overview", "Claim Number"));
			overViewDataChildData.put("Claim Received", dataExtractedFromWebElement("Overview", "Claim Received"));
			overViewDataChildData.put("Status", dataExtractedFromWebElement("Overview", "Status"));
			overViewDataChildData.put("Paid Amount", dataExtractedFromWebElement("Overview", "Paid Amount"));
			overViewDataChildData.put("Paid Date", dataExtractedFromWebElement("Overview", "Paid Date"));
//			overViewDataChildData.put("Check/EFT/VCard #",
//					dataExtractedFromWebElement("Overview", "Check/EFT/VCard #"));

			overViewData.put("OverView", overViewDataChildData);

			claimDetailsRowWise = claimDataExtracter();

			claimDetails.put(dataExtractedFromWebElement("Overview", "Claim Number"), claimDetailsRowWise);

			legendData = dataExtractForLegends();

			excelReadWrite.splitClaims(claimDetailsRowWise);
			excelReadWrite.OutputExcelCreation(memberDetails, overViewData, claimDetailsRowWise, legendData);
			if (numberOfclaims.size() > 1) {
				click("Health First Back to Results", useThis);
				waitFor("Health First New Search");
				claimRow++;
			}

		}

	}

	private Map<String, List<String>> dataExtractForLegends() {

		List<String> allLegends = new ArrayList<>();
		legendData = new HashMap<>();
		int sizeOfClaimDetailsRows = driver
				.findElements(By.xpath("//table[@class='dataTable claimDetailsTable']/tbody/tr")).size();

		List<WebElement> allLegendsAsWebElement = driver.findElements(
				By.xpath("//table[@class='dataTable claimDetailsTable']/tbody/tr[" + sizeOfClaimDetailsRows + "]/td"));

		for (WebElement eachLegend : allLegendsAsWebElement) {

			allLegends.add(eachLegend.getText().toString().trim());

		}

		legendData.put("Legends", allLegends);
		return legendData;
	}

	private String dataExtractedFromWebElement(String columnHeader, String valueHeader) {
		String value = null;

		String xpath = "//table[@class='dataTable']//tbody[tr[th[h3[text()='" + columnHeader
				+ "']]]]//td[normalize-space(text())='" + valueHeader + ":']/following-sibling::td[1]";
		if (valueHeader.equalsIgnoreCase("Patient Acct #")) {

			String[] serviceIdSplitter = driver.findElement(By.xpath(xpath)).getText().toString().trim().split("-");
			value = serviceIdSplitter[0];
		} else {
			value = driver.findElement(By.xpath(xpath)).getText().toString().trim();

		}
		return value;
	}

	private Map<Integer, List<Map<String, String>>> claimDataExtracter() {

		List<WebElement> claimDataHeaderCount = driver
				.findElements(By.xpath("//table[@class='dataTable claimDetailsTable']/tbody[1]/tr[2]/td"));

		List<WebElement> claimDataRowCount = driver.findElements(
				By.xpath("//table[@class='dataTable claimDetailsTable']/tbody[1]/tr[@class='contentheader']"));

		for (int i = 1; i <= claimDataRowCount.size(); i++) {
//			for (int j = 1; j < claimDataHeaderCount.size(); j++) {
			claimDetailsChild = new ArrayList<>();
			int k = 1;
			List<WebElement> singleRowOfClaimDetails = driver.findElements(
					By.xpath("//table[@class='dataTable claimDetailsTable']/tbody[1]/tr[" + i + "+" + 2 + "]/td"));

			List<WebElement> rowHeaders = driver
					.findElements(By.xpath("//table[@class='dataTable claimDetailsTable']/tbody[1]/tr[2]/td"));

			for (WebElement eachValue : singleRowOfClaimDetails) {

				Map<String, String> claimDetailsChildData = new HashMap<>();
				claimDetailsChildData.put(driver
						.findElement(
								By.xpath("//table[@class='dataTable claimDetailsTable']/tbody[1]/tr[2]/td[" + k + "]"))
						.getText().toString().trim(), eachValue.getText().toString().trim());
				claimDetailsChild.add(claimDetailsChildData);
				k++;
			}
			claimDetailsRowWise.put(i, claimDetailsChild);
//			}

		}
		return claimDetailsRowWise;
	}

}
