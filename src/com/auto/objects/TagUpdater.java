package com.auto.objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.auto.Supports.DataReader;
import com.auto.Supports.WrapperClass;

public class TagUpdater extends WrapperClass {
	private By useThis;
	Map<String, Map<Integer, Map<String, String>>> dataFromExcelWorkBook = new HashMap<>();
	ExcelReadWrite excelReadWrite = new ExcelReadWrite();
	TagFinderWriteObject tagWriteObject;
	ArrayList<String> removedTags = new ArrayList<>();
	Set<String> alreadyAvailableTags = new HashSet<>();
	Set<String> matchedTags = new HashSet<>();;
	Set<String> updatedMatchedTags = new HashSet<>();
	Set<String> tagsAfterUpdation = new HashSet<>();
	Set<String> initialTagsSet = new HashSet<>();
	Map<String, String> exportData = new HashMap<>();
	Map<Integer, TagFinderWriteObject> dataToExcelWorkBook = new HashMap<>();

	public TagUpdater() {
		DataReader.getData("Centers Lab");
		DataReader.getDataKeyandValue("Centers Lab");
	}

	public void updateTag() throws InterruptedException, IOException {

		readExcelForInput();
		clearTabs();
		openClaimToUpdate(dataFromExcelWorkBook);
		writeExcelForTagFinder();

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

	private void processingLoaderWait() {
		List<WebElement> loader = driver.findElements(By.xpath("//div[text()='Processing...']"));
		while (!(loader.isEmpty())) {
			loader = driver.findElements(By.xpath("//div[text()='Processing...']"));
			if (loader.isEmpty()) {
				break;
			}
		}
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

	private void openClaimToUpdate(Map<String, Map<Integer, Map<String, String>>> dataFromExcelWorkBook)
			throws InterruptedException, IOException {

		click("Centers Lab Billing DropDown", useThis);
		click("Centers Lab Billing Option", useThis);

		emosowLoaderWait();
		click("Centers Lab Billing Chechbox", useThis);
		for (int i = 1; i <= dataFromExcelWorkBook.get("Sheet1").size(); i++) {

			writeHere("Centers Lab Accession Number Box",
					dataFromExcelWorkBook.get("Sheet1").get(i).get("A/N").toString(), useThis);
			List<WebElement> searchButtons = findElements("Centers Lab Find Button");
			emosowLoaderWait();
			for (WebElement eachButton : searchButtons) {

				eachButton.click();

			}
			emosowLoaderWait();
			if (findElements("Centers Lab First Record").size() > 1) {
				System.out.println("Record Displayed");
			}
			readNotes(i, dataFromExcelWorkBook);

		}

	}

	private void readNotes(int i, Map<String, Map<Integer, Map<String, String>>> dataFromExcelWorkBook)
			throws InterruptedException, IOException {
		String notes = gettext("Centers First Notes");
//		String notes = "Paid and alkajsdklasd Non-Covered";
		String initialTags = "";
		List<WebElement> initialTagsAsElement;

		initialTagsAsElement = driver.findElements(By.xpath("//div[span[normalize-space(text())='Tags:']]"));
//			initialTags = gettext("Centers Lab Tags");
		for (WebElement tagEle : initialTagsAsElement) {
			initialTags = tagEle.getText();
		}

		if (initialTags.isEmpty() || initialTags.isBlank()) {
			initialTags = "NA";
		} else {
			String tag = "Tags:";
			initialTags = initialTags.substring(initialTags.indexOf("Tags:") + tag.length()).trim();
		}

		int matchCount = 0;
		/*
		 * for (int j = 1; j <= dataFromExcelWorkBook.get("Sheet2").size(); j++) {
		 * 
		 * if (notes.isEmpty()) { /// add for empty notes matchedTags.add("askasas");
		 * break; } else {
		 * 
		 * for (int k = 1; k <= dataFromExcelWorkBook.get("Sheet2").size(); k++) { if
		 * (dataFromExcelWorkBook.get("Sheet2").get(k).get("Keyword").toLowerCase().trim
		 * ().contains(",")) { String[] keywords =
		 * dataFromExcelWorkBook.get("Sheet2").get(k).get("Keyword").toLowerCase()
		 * .trim().split(","); for (String eachKey : keywords) { if
		 * (notes.toLowerCase().contains(eachKey)) { matchCount++; } } if (matchCount ==
		 * keywords.length) {
		 * matchedTags.add(dataFromExcelWorkBook.get("Sheet2").get(k).get("Tags").trim()
		 * ); break; } } else { continue; }
		 * 
		 * }
		 * 
		 * if (matchedTags.isEmpty() && notes.toLowerCase()
		 * .contains(dataFromExcelWorkBook.get("Sheet2").get(j).get("Keyword").
		 * toLowerCase().trim())) {
		 * matchedTags.add(dataFromExcelWorkBook.get("Sheet2").get(j).get("Tags").trim()
		 * ); break; }
		 * 
		 * } // else { // dataToExcelWorkBook.put(j, new
		 * TagFinderWriteObject(initialTags, "", "", "")); ////
		 * System.out.println("No Tags in Excel Sheet Matched for Comment->" + notes);
		 * // } }
		 */

		// No Validation just goes on based on input from excel
//		for (int j = 1; j <= dataFromExcelWorkBook.get("Sheet2").size(); j++) {
		matchedTags.add(dataFromExcelWorkBook.get("Sheet2").get(i).get("Tags").trim());
//		}
		updateTags(matchedTags, notes, initialTags);
		addTags(updatedMatchedTags);
		dataToExcelWorkBook.put(i, new TagFinderWriteObject(initialTags, exportData.get("Tags Removed"),
				exportData.get("Tags Added"), exportData.get("Tags After Updation")));
		exportData.clear();

		removedTags = new ArrayList<>();
		alreadyAvailableTags = new HashSet<>();
		matchedTags = new HashSet<>();
		updatedMatchedTags = new HashSet<>();
		tagsAfterUpdation = new HashSet<>();
		initialTagsSet = new HashSet<>();

	}

	private void readExcelForInput() throws IOException {
		dataFromExcelWorkBook = excelReadWrite.extractData("TagUpdater");

	}

	private void readExcelForInput(String fileName) throws IOException {
		dataFromExcelWorkBook = excelReadWrite.extractData(fileName);

	}

	private void writeExcelForTagFinder() throws IOException {

		excelReadWrite.writeExcelForTagFinder("TagUpdater", dataToExcelWorkBook);
	}

	private void updateTags(Set<String> matchedTags, String notes, String initialTags) throws InterruptedException {
		click("Centers Lab Check Box", useThis);
		Thread.sleep(2000);
		updatedMatchedTags = matchedTags;
		for (String eachMatchedTag : matchedTags) {
			if (eachMatchedTag.isEmpty()) {
				continue;
			}
			removeTags(initialTags, eachMatchedTag.trim());

		}
		updatedMatchedTags.removeAll(alreadyAvailableTags);

	}

	private void removeTags(String initialTags, String tagFromExcel) throws InterruptedException {

		String[] allInitialTags = initialTags.split(",");
		for (String eachValue : allInitialTags) {
			initialTagsSet.add(eachValue.trim());
			if (eachValue.contains("_")) {

				if (eachValue.trim().equalsIgnoreCase(tagFromExcel)) {

					alreadyAvailableTags.add(eachValue.trim());
				} else {
					click("Centers Lab Service Edit  button", useThis);
					click("Centers Lab Tag Edit", useThis);
					emosowLoaderWait();
					click("Centers Lab Remove Tags", useThis);
					writeHere("Centers Lab Tags Edit", eachValue.trim(), useThis);
					Thread.sleep(2000);
					buttonStroke_enterKey("Centers Lab Tags Edit");
					click("Centers Lab Tag Apply", useThis);
					removedTags.add(eachValue.trim());
					emosowLoaderWait();
				}
			} else {

				if (eachValue.contains("HL")) {
					alreadyAvailableTags.add(eachValue);

				} else {
//					click("Centers Lab Service Edit  button", useThis);
//					click("Centers Lab Tag Edit", useThis);
//					emosowLoaderWait();
//					click("Centers Lab Remove Tags", useThis);
//					writeHere("Centers Lab Tags Edit", eachValue.trim(), useThis);
//					Thread.sleep(2000);
//					buttonStroke_enterKey("Centers Lab Tags Edit");
//					click("Centers Lab Tag Apply", useThis);
//					removedTags.add(eachValue.trim());
//					emosowLoaderWait();
				}
			}
		}
		if (removedTags.isEmpty()) {
			exportData.put("Tags Removed", "NA");
		} else {
			exportData.put("Tags Removed", removedTags.toString().replace("[", "").replace("]", ""));
		}

		System.out.println(removedTags.toString());

	}

	private void addTags(Set<String> matchedTags) throws InterruptedException {

		for (String eachMatchedTag : matchedTags) {
			if (eachMatchedTag.isEmpty()) {
				continue;
			}
			emosowLoaderWait();
			click("Centers Lab Service Edit  button", useThis);
			click("Centers Lab Tag Edit", useThis);
			emosowLoaderWait();
			writeHere("Centers Lab Tags Edit", eachMatchedTag.trim(), useThis);
			Thread.sleep(2000);
			buttonStroke_enterKey("Centers Lab Tags Edit");
			click("Centers Lab Tag Apply", useThis);
			emosowLoaderWait();
			exportData.put("After Tag Updation", eachMatchedTag.trim());
			exportData.put("Is Updated", "Yes");
		}
		tagsAfterUpdation.addAll(matchedTags);
		tagsAfterUpdation.addAll(alreadyAvailableTags);
		tagsAfterUpdation.addAll(initialTagsSet);
		tagsAfterUpdation.removeAll(removedTags);
		tagsAfterUpdation.remove("NA");

		if (matchedTags.isEmpty()) {
			exportData.put("Tags Added", "NA");
		} else {
			exportData.put("Tags Added", matchedTags.toString().replace("[", "").replace("]", ""));
		}

		exportData.put("Tags After Updation", tagsAfterUpdation.toString().replace("[", "").replace("]", ""));
	}

	private int openClaim() throws InterruptedException {
		int currentRowNumber = 0;
		click("Centers Lab Billing DropDown", useThis);
		click("Centers Lab Billing Option", useThis);

		emosowLoaderWait();
		click("Centers Lab Billing Chechbox", useThis);
		for (int i = 1; i <= dataFromExcelWorkBook.get("Sheet1").size(); i++) {

			writeHere("Centers Lab Accession Number Box",
					dataFromExcelWorkBook.get("Sheet1").get(i).get("A/N").toString(), useThis);
			click("Centers Lab Find Button", useThis);
			emosowLoaderWait();
			currentRowNumber = i;

			addDxForSearchResults(i);
			System.out.println("Completed->" + dataFromExcelWorkBook.get("Sheet1").get(i).get("A/N"));

		}
		return currentRowNumber;

	}

	private List<String> readTags() {

		String initialTag = "";
		List<WebElement> initialTagsAsElement;
		List<String> tagsAvailable = new ArrayList<>();

		initialTagsAsElement = driver.findElements(By.xpath("//div[span[normalize-space(text())='Tags:']]"));
		for (WebElement tagEle : initialTagsAsElement) {
			initialTag = tagEle.getText();
			if (initialTag.isEmpty() || initialTag.isBlank()) {
				initialTag = "NA";
			} else {
				String tag = "Tags:";
				initialTag = initialTag.substring(initialTag.indexOf("Tags:") + tag.length()).trim();
				tagsAvailable.add(initialTag);
			}
		}
		return tagsAvailable;

	}

	private boolean verifyTagAvailable(String tagName) {
		boolean isTagAvailable = false;
		List<String> tags = readTags();
		if (tags.contains(tagName)) {
			isTagAvailable = true;
		}
		return isTagAvailable;
	}

	private void removeTags(String tagToBeRemoved) throws InterruptedException {
		click("Centers Lab Service Edit  button", useThis);
		click("Centers Lab Tag Edit", useThis);
		emosowLoaderWait();
		click("Centers Lab Remove Tags", useThis);
		writeHere("Centers Lab Tags Edit", tagToBeRemoved.trim(), useThis);
		Thread.sleep(2000);
		buttonStroke_enterKey("Centers Lab Tags Edit");
		click("Centers Lab Tag Apply", useThis);
		emosowLoaderWait();
	}

	public void diagnosisAddAndRemoveTags() throws IOException, InterruptedException {
		int currentRowNumber;

		readExcelForInput("AddDx");
		clearTabs();
		currentRowNumber = openClaim();

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

	private void addDx(int i) throws InterruptedException {
		click("Centers Lab Dignosis Link", useThis);
		if (!isSameDxAvailable(i)) {
			emosowLoaderWait();
			writeHere("Centers Lab Diagnosis input",
					dataFromExcelWorkBook.get("Sheet1").get(i).get("HL7 diagnosis сode").toString().trim(), useThis);
			Thread.sleep(2500);
			buttonStroke_enterKey("Centers Lab Diagnosis input");
			click("Centers Lab Diagnosis Save Button", useThis);
			processingLoaderWait();
			emosowLoaderWait();
			Thread.sleep(500);
		} else {
			click("Centers Lab Diagnosis Save Button", useThis);
			processingLoaderWait();
			emosowLoaderWait();
			Thread.sleep(500);
		}

	}

	private boolean isSameDxAvailable(int i) throws InterruptedException {
		boolean isSameDxAvailable = false;
		List<WebElement> dxAvailable = findElements("Centers Lab Available Dx");
		Thread.sleep(1500);
		for (WebElement dx : dxAvailable) {
			String eachdx = dx.getText().toString().trim();
			if (eachdx.equalsIgnoreCase(
					dataFromExcelWorkBook.get("Sheet1").get(i).get("HL7 diagnosis сode").toString().trim())) {
				isSameDxAvailable = true;
				break;
			}
		}

		return isSameDxAvailable;

	}

	private void addDxForSearchResults(int i) throws InterruptedException {
		List<WebElement> searchResults = findElements("Centers Lab Claim Search Results");
		for (WebElement eachResult : searchResults) {
			clickChecker(eachResult);
			if (verifyTagAvailable("missing/invalid dx code")) {
				removeTags("missing/invalid dx code");
			}
			addDx(i);

			unselectRecord();
			emosowLoaderWait();
		}

	}
}
