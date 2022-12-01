package com.auto.objects;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.auto.Supports.DataReader;
import com.auto.Supports.WrapperClass;

public class WriteOffAndClose extends WrapperClass {
	private By useThis;
	Map<String, Map<Integer, Map<String, String>>> dataFromExcelWorkBook = new HashMap<>();
	Map<Integer, String> outPutForExcel = new HashMap<>();
	ExcelReadWrite excelReadWrite = new ExcelReadWrite();
	String accessionNumber = "";
	String proCode = "";
	boolean isDenied = false;

	public WriteOffAndClose() {
		DataReader.getData("Centers Lab");
		DataReader.getDataKeyandValue("Centers Lab");
	}

	private void readExcelForInput(String fileName) throws IOException {
		dataFromExcelWorkBook = excelReadWrite.extractData(fileName);

	}

	private void readExcelForInputForCLose() throws IOException {
		dataFromExcelWorkBook = excelReadWrite.extractData("Close");

	}

	private void emosowLoaderWait() {
		List<WebElement> loader = driver.findElements(By.xpath("//div[text()='Loading...']"));
		while (!(loader.isEmpty())) {
			loader = driver.findElements(By.xpath("//div[text()='Loading...']"));
			if (loader.isEmpty()) {
				break;
			}
		}
	}

	private boolean ischeckBoxChecked(WebElement checkBox) {
		boolean isCheckBoxSelected = false;

		if (checkBox.getAttribute("class").contains("checked")) {
			isCheckBoxSelected = true;
		}
		return isCheckBoxSelected;
	}

	private void clearTabs() throws InterruptedException, IOException {

		List<WebElement> closableTabs = driver
				.findElements(By.xpath("//div[normalize-space(@class)='x-tab-panel tab-close-button x-border-panel']"
						+ "/div[1]/div/ul/li[contains(@class,'x-tab-strip-closable')]"));
		if (!(closableTabs.isEmpty())) {
			emosowLoaderWait();
			click("Centers Lab Close Tab", useThis);

			List<WebElement> closabelTabs = driver.findElements(By
					.xpath("//ul[@class='x-tab-strip x-tab-strip-top']/li[contains(@class, 'x-tab-strip-closable')]"));
			if (closabelTabs.isEmpty()) {
				System.out.println("All Tabs Closed");
			}
			Thread.sleep(3000);
		}

	}

	private void openClaimVerifyAndWriteOff(Map<String, Map<Integer, Map<String, String>>> dataFromExcelWorkBook)
			throws InterruptedException, IOException {

		click("Centers Lab Billing DropDown", useThis);
		click("Centers Lab Billing Option", useThis);
		emosowLoaderWait();
		if (!ischeckBoxChecked(findElement("Centers Lab Billing Chechbox"))) {
			click("Centers Lab Billing Chechbox", useThis);
		}

		if (ischeckBoxChecked(findElement("Centers Lab by service Check Box"))) {
			click("Centers Lab by service Check Box", useThis);
		}

		for (int i = 1; i <= dataFromExcelWorkBook.get("Sheet1").size(); i++) {

			writeHere("Centers Lab Accession Number Box",
					dataFromExcelWorkBook.get("Sheet1").get(i).get("A/N").toString(), useThis);

//			writeHere("Centers Lab Service ID Box",
//					dataFromExcelWorkBook.get("Sheet1").get(i).get("Service ID").toString(), useThis);

//			accessionNumber = dataFromExcelWorkBook.get("Sheet1").get(i).get("Service ID").toString();
			emosowLoaderWait();

			click("Centers Lab Find Button", useThis);
			emosowLoaderWait();
			verifyStatusAndWriteoff();
			System.out.println("Completed Record->" + dataFromExcelWorkBook.get("Sheet1").get(i).get("A/N").toString());
		}

	}

	private void openClaimVerifyAndClose(Map<String, Map<Integer, Map<String, String>>> dataFromExcelWorkBook)
			throws InterruptedException, IOException {

		click("Centers Lab Billing DropDown", useThis);
		click("Centers Lab Billing Option", useThis);
		emosowLoaderWait();
		if (!ischeckBoxChecked(findElement("Centers Lab Billing Chechbox"))) {
			click("Centers Lab Billing Chechbox", useThis);
		}

		if (ischeckBoxChecked(findElement("Centers Lab by service Check Box"))) {
			click("Centers Lab by service Check Box", useThis);
		}

		for (int i = 1; i <= dataFromExcelWorkBook.get("Sheet1").size(); i++) {

			writeHere("Centers Lab Accession Number Box",
					dataFromExcelWorkBook.get("Sheet1").get(i).get("A/N").toString(), useThis);

//			writeHere("Centers Lab Service ID Box",
//					dataFromExcelWorkBook.get("Sheet1").get(i).get("Service ID").toString(), useThis);

//			accessionNumber = dataFromExcelWorkBook.get("Sheet1").get(i).get("Service ID").toString();
			emosowLoaderWait();

			click("Centers Lab Find Button", useThis);
			emosowLoaderWait();
			verifyStatusAndClose(dataFromExcelWorkBook, i);

		}

	}

	private boolean verifyDenied() {
		boolean isDenied = false;
		List<WebElement> billingDetails = findElements("Centers Lab Billing Enitities");
		outer: for (WebElement eachRowEntity : billingDetails) {
			List<WebElement> allTableDivisionOfEachEntities = eachRowEntity.findElements(By.tagName("td"));
			for (WebElement eachDivision : allTableDivisionOfEachEntities) {
				List<WebElement> allDivElement = eachDivision.findElements(By.tagName("div"));
				for (WebElement eachDivElement : allDivElement) {
					System.out.println(eachDivElement.getText());
					if (eachDivElement.getText().trim().toLowerCase().contains("denial from primary insurance")) {
						isDenied = true;
						break outer;
					}
				}
			}
		}
		return isDenied;

	}

	private void verifyStatusAndWriteoff() throws InterruptedException {

		List<WebElement> searchResults = findElements("Centers Lab Claim Search Results");
		for (WebElement eachResult : searchResults) {
			proCode = getProcCode(eachResult);
			if (proCode.toLowerCase().contains("p") || proCode.toLowerCase().contains("g")) {
				clickChecker(eachResult);
				writeOff(scrollToDenialView(eachResult), "medicaid, G0471, P9603 not covered");
				unselectRecord();
//				click("Centers Lab Service Edit  button", useThis);
//				click("Centers Lab Billing Edit Option", useThis);
//				emosowLoaderWait();
//
//				if (verifyDenied()) {
//					click("Centers Lab Billing Edit Close Button", useThis);
//					emosowLoaderWait();
//					writeOff(scrollToDenialView(eachResult));
//					emosowLoaderWait();
//				} else {
//					click("Centers Lab Billing Edit Close Button", useThis);
//					emosowLoaderWait();
//				}

			}
		}
	}

	private void verifyStatusAndClose(Map<String, Map<Integer, Map<String, String>>> dataFromExcelWorkBook, int i)
			throws InterruptedException, IOException {

		List<WebElement> searchResults = findElements("Centers Lab Claim Search Results");
		for (WebElement eachResult : searchResults) {

			clickChecker(eachResult);
			close(scrollToBillingTextView(eachResult, "closed"), dataFromExcelWorkBook, i);
			unselectRecord();
//				click("Centers Lab Service Edit  button", useThis);
//				click("Centers Lab Billing Edit Option", useThis);
//				emosowLoaderWait();
//
//				if (verifyDenied()) {
//					click("Centers Lab Billing Edit Close Button", useThis);
//					emosowLoaderWait();
//					writeOff(scrollToDenialView(eachResult));
//					emosowLoaderWait();
//				} else {
//					click("Centers Lab Billing Edit Close Button", useThis);
//					emosowLoaderWait();
//				}

		}
	}

	private void writeOff(WebElement denialText, String tag) throws InterruptedException {
		if (denialText.getText().toLowerCase().contains("denial")) {
			Actions actions = new Actions(driver);
			actions.moveToElement(denialText).perform();
			Thread.sleep(500);
			click("Centers Lab WriteOff Element", useThis);
			emosowLoaderWait();
			writeHere("Centers Lab Tag Input", tag, useThis);
			Thread.sleep(2000);
			buttonStroke_enterKey("Centers Lab Tag Input");
			click("Centers Lab Ok Button", useThis);
			emosowLoaderWait();
		}

	}

	private void close(WebElement denialText, Map<String, Map<Integer, Map<String, String>>> dataFromExcelWorkBook,
			int i) throws InterruptedException {

		if (denialText.getText().toLowerCase().contains("closed")) {
			System.out
					.println(dataFromExcelWorkBook.get("Sheet1").get(i).get("A/N").toString() + "-->is Already Closed");
			outPutForExcel.put(i, "Already Closed");
		} else if (denialText.getText().toLowerCase().contains("denial")) {
			outPutForExcel.put(i, "Record has " + denialText.getText());
			System.out
					.println(dataFromExcelWorkBook.get("Sheet1").get(i).get("A/N").toString() + "-->is having denial");
		} else if (denialText.getText().toLowerCase().contains("rebill")) {
			outPutForExcel.put(i, "Record has " + denialText.getText());
			System.out
					.println(dataFromExcelWorkBook.get("Sheet1").get(i).get("A/N").toString() + "-->is having Rebill");
		} else {
			Actions actions = new Actions(driver);
			actions.moveToElement(denialText).perform();
			Thread.sleep(500);
			click("Centers Lab Claim Close Button", useThis);
			Thread.sleep(100);
			emosowLoaderWait();
			outPutForExcel.put(i, "Record Closed");
			System.out.println(dataFromExcelWorkBook.get("Sheet1").get(i).get("A/N").toString() + "-->Record Closed");
		}

	}

	private WebElement scrollToDenialView(WebElement eachResult) throws InterruptedException {
		WebElement denialTextElement = getStatusElement(eachResult);
		if (denialTextElement.getText().toLowerCase().contains("denial")) {
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", denialTextElement);
			Thread.sleep(500);
		}
		return denialTextElement;
	}

	private WebElement scrollToBillingTextView(WebElement eachResult, String text) throws InterruptedException {
		WebElement denialTextElement = getStatusElement(eachResult);
		if (!denialTextElement.getText().toLowerCase().contains(text)
				&& !denialTextElement.getText().toLowerCase().contains("denial")
				&& !denialTextElement.getText().toLowerCase().contains("rebill")) {
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", denialTextElement);
			Thread.sleep(500);
		}
		return denialTextElement;
	}

	private void clickChecker(WebElement eachResult) {
		List<WebElement> allDivElementInsideResult = eachResult.findElements(By.tagName("div"));
		for (WebElement eachDivElement : allDivElementInsideResult) {
			if (eachDivElement.getAttribute("class").contains("row-checker")) {
				eachDivElement.click();
				break;
			}
		}
	}

	private void unselectRecord() {
		List<WebElement> searchResults = findElements("Centers Lab Selected Result");
		outer: for (WebElement eachResult : searchResults) {
			List<WebElement> allDivElementInsideResult = eachResult.findElements(By.tagName("div"));
			for (WebElement eachDivElement : allDivElementInsideResult) {
				if (eachDivElement.getAttribute("class").contains("row-checker")) {
					eachDivElement.click();
					break outer;
				}
			}
		}

	}

	private String getProcCode(WebElement eachResult) {
		String procCode = "";

		List<WebElement> allEntitiesInEachResult = eachResult.findElements(By.tagName("td"));
		outer: for (WebElement eachEntityInRow : allEntitiesInEachResult) {
			if (eachEntityInRow.getAttribute("class").contains("study")) {
				List<WebElement> procCodeTable = eachEntityInRow.findElements(By.tagName("tbody"));
				for (WebElement eachProcCodeTableEntity : procCodeTable) {
					List<WebElement> eachProcCodeTableEntityByRow = eachProcCodeTableEntity
							.findElements(By.tagName("tr"));
					for (WebElement proCodeEntity : eachProcCodeTableEntityByRow) {

						if (proCodeEntity.findElement(By.tagName("th")).getText().trim().toLowerCase()
								.contains("proc:")) {
							procCode = proCodeEntity.findElement(By.tagName("td")).getText().trim().split(" ")[0]
									.trim();
							break outer;
						}

					}
				}
			}
		}
		return procCode;
	}

	private WebElement getStatusElement(WebElement eachResult) {
		WebElement statusElement = null;
//		System.out.println(eachResult.findElement(By.xpath("//div[contains(@class,'billing')]/span")).getText());
		List<WebElement> allEntitiesInEachResult = eachResult.findElements(By.tagName("td"));
		outer: for (WebElement eachEntityInRow : allEntitiesInEachResult) {
			if (eachEntityInRow.getAttribute("class").contains("billing")) {
				List<WebElement> allDivInsideBillingColumn = eachEntityInRow.findElements(By.tagName("div"));
				for (WebElement eachDivInsideBillingCOlumn : allDivInsideBillingColumn) {
					List<WebElement> allElementsInsideDiv = eachDivInsideBillingCOlumn.findElements(By.tagName("div"));
					for (WebElement eachElement : allElementsInsideDiv) {
						if (eachElement.getAttribute("class").contains("billing")) {
							statusElement = eachElement;
							break outer;
						}
					}
				}
			}
		}
		return statusElement;
	}

	private void writeBackToExcel(Map<Integer, String> outPutContent, String fileName) throws IOException {
		excelReadWrite.writeExcelForManualClose(outPutContent, fileName);
	}

	public void ManualWriteOff() throws IOException, InterruptedException {

		readExcelForInput("WriteOff");
		clearTabs();
		openClaimVerifyAndWriteOff(dataFromExcelWorkBook);

	}

	private void updateNotes(String dataForNotes) throws InterruptedException {
		click("Centers Lab Service Edit  button", useThis);
		click("Centers Billing Edit", useThis);
		emosowLoaderWait();
		click("CentersLab Notes Popup", useThis);
		emosowLoaderWait();
		click("Centers Comment Add Button", useThis);
		emosowLoaderWait();
		writeHere("CentersLab Linkto", "Service", useThis);
		Thread.sleep(300);
		buttonStroke_enterKey("CentersLab Linkto");
		Thread.sleep(200);
		writeHere("Centers Lab Comment Area", dataForNotes, useThis);
		click("Centers Lab Save Button", useThis);
		emosowLoaderWait();

	}

	private void writeOffViaOperations(String tag) throws InterruptedException {
		click("Centers Lab Operations Option", useThis);
		click("Centers Lab WriteOff Option", useThis);
		emosowLoaderWait();
		writeHere("Centers Lab Tag Input", tag, useThis);
		Thread.sleep(300);
		buttonStroke_enterKey("Centers Lab Tag Input");
		Thread.sleep(200);
		click("Centers Lab Ok Button", useThis);
		emosowLoaderWait();
		click("Centers Lab Close Button", useThis);
		emosowLoaderWait();
	}

	private void searchRecord(Map<String, String> dataForSearch, String searchBasedOn) throws InterruptedException {

		if (searchBasedOn.toLowerCase().equalsIgnoreCase("accession")) {

			writeHere("Centers Lab Accession Number Box", dataForSearch.get("A/N").toString(), useThis);

			click("Centers Lab Find Button", useThis);
			emosowLoaderWait();
			List<WebElement> searchResults = findElements("Centers Lab Claim Search Results");
			for (WebElement eachResult : searchResults) {
				clickChecker(eachResult);
				return;
			}
		}

	}

	public void ManualCloseClaim() throws IOException, InterruptedException {

		readExcelForInputForCLose();
		clearTabs();
		openClaimVerifyAndClose(dataFromExcelWorkBook);
		writeBackToExcel(outPutForExcel, "Close");
	}

	public void updateNotesAndWriteOff() throws IOException, InterruptedException {
		readExcelForInput("UpdateNotesAndWriteOff");
		clearTabs();
		click("Centers Lab Billing DropDown", useThis);
		click("Centers Lab Billing Option", useThis);
		emosowLoaderWait();
		if (ischeckBoxChecked(findElement("Centers Lab by service Check Box"))) {
			click("Centers Lab by service Check Box", useThis);
		}
		for (int i = 1; i <= dataFromExcelWorkBook.get("Sheet1").size(); i++) {
			searchRecord(dataFromExcelWorkBook.get("Sheet1").get(i), "Accession");
			updateNotes(dataFromExcelWorkBook.get("Sheet1").get(i).get("Notes"));
			writeOffViaOperations(dataFromExcelWorkBook.get("Sheet1").get(i).get("Tag"));
			unselectRecord();
		}

	}
}
