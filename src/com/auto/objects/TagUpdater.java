package com.auto.objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
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

	public void emosowLoaderWait() {
		List<WebElement> loader = driver.findElements(By.xpath("//div[text()='Loading...']"));
		while (!(loader.isEmpty())) {
			loader = driver.findElements(By.xpath("//div[text()='Loading...']"));
			if (loader.isEmpty()) {
				break;
			}
		}
	}

	public void emsowLoggingInWait() {
		List<WebElement> loader = driver
				.findElements(By.xpath("//div[span='Logging on...' and @style='visibility: hidden;']"));
		while (!(loader.isEmpty())) {
			loader = driver.findElements(By.xpath("//div[span='Logging on...' and @style='visibility: hidden;']"));
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
		emosowLoaderWait();
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
		emosowLoaderWait();

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

	private void openBillingModule() {

		click("Centers Lab Billing DropDown", useThis);
		click("Centers Lab Billing Option", useThis);

		emosowLoaderWait();
	}

	private void openClaimByServiceId(String data) throws InterruptedException {

		writeHere("Centers Lab Service ID Box", data, useThis);
		Thread.sleep(700);
		click("Centers Lab Find Button", useThis);
		emosowLoaderWait();

	}

	private List<String> readTagsForSelectedRows() {

		List<WebElement> allResults = findElements("Centers Lab Selected Result");
		for (WebElement eachResult : allResults) {

		}
		return removedTags;

	}

	private List<String> readTags() {

		String initialTag = "";
		String[] allInitialTags = {};
		List<WebElement> initialTagsAsElement;
		List<String> tagsAvailable = new ArrayList<>();

		initialTagsAsElement = driver.findElements(By.xpath("//div[span[normalize-space(text())='Tags:']]"));
		for (WebElement tagEle : initialTagsAsElement) {
			initialTag = tagEle.getText();
			if (initialTag.isEmpty() || initialTag.isBlank()) {
				initialTag = "NA";
			} else {
				String tag = "Tags:";
				allInitialTags = initialTag.substring(initialTag.indexOf("Tags:") + tag.length()).trim().split(",");

			}
		}

		for (String tags : allInitialTags) {
			tagsAvailable.add(tags);
		}
		return tagsAvailable;

	}

	private List<String> readTags(WebElement selectedRecord) {

		String initialTag = "";
		String[] allInitialTags = {};
		List<WebElement> initialTagsAsElement;
		List<String> tagsAvailable = new ArrayList<>();

		List<WebElement> allDivisions = selectedRecord.findElements(By.tagName("td"));
		outer: for (WebElement eachDivision : allDivisions) {
			if (eachDivision.getAttribute("class").contains("notes")) {
				List<WebElement> allSpans = eachDivision.findElements(By.tagName("span"));
				for (WebElement eachSpan : allSpans) {
					if (eachSpan.getText().contains("Tags:")) {
						initialTag = eachDivision.getText().trim();
						break outer;

					}
				}
			}
		}

		if (initialTag.isEmpty() || initialTag.isBlank()) {
			initialTag = "NA";
		} else {
			String tag = "Tags:";
			allInitialTags = initialTag.substring(initialTag.indexOf("Tags:") + tag.length()).trim().split(",");

		}

		for (String tags : allInitialTags) {
			tagsAvailable.add(tags);
		}
		return tagsAvailable;

	}

	private boolean verifyTagAvailable(String tagName) {
		boolean isTagAvailable = false;
		List<String> tags = readTags();
		for (String eachtag : tags) {
			System.out.println(tags.size());
			System.out.println(eachtag);
			if (eachtag.trim().equalsIgnoreCase(tagName)) {
				isTagAvailable = true;
				break;
			}
		}
		return isTagAvailable;
	}

	private boolean verifyTagAvailable(String tagName, WebElement selectedRecord) {
		boolean isTagAvailable = false;
		List<String> tags = readTags(selectedRecord);
		for (String eachtag : tags) {
			System.out.println(tags.size());
			System.out.println(eachtag);
			if (eachtag.trim().equalsIgnoreCase(tagName)) {
				isTagAvailable = true;
				break;
			}
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

	private void addTag(String tagTobeAdded) throws InterruptedException {
		click("Centers Lab Service Edit  button", useThis);
		click("Centers Lab Tag Edit", useThis);
		emosowLoaderWait();
		writeHere("Centers Lab Tags Edit", tagTobeAdded.trim(), useThis);
		Thread.sleep(2000);
		buttonStroke_enterKey("Centers Lab Tags Edit");
		click("Centers Lab Tag Apply", useThis);
		processingLoaderWait();
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
		Thread.sleep(2500);
		List<WebElement> dxAvailable = findElements("Centers Lab Available Dx");

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

	public Map<String, Set<String>> readExcelForCollectiveTagUpdate() throws IOException {
		readExcelForInput("MissingDx");
		int dataWise = 1;
		Map<String, Set<String>> collectiveData = new HashMap<>();

		Set<String> collectiveStudies = new LinkedHashSet<>();
//		Map<Integer, Set<String>> orderedCollectiveData = new HashMap<>();
		for (int i = 1; i <= dataFromExcelWorkBook.get("Sheet1").size(); i++) {
			inner: for (int j = i; j < dataFromExcelWorkBook.get("Sheet1").size(); j++) {
				if (dataFromExcelWorkBook.get("Sheet1").get(j).get("Service ID")
						.equals(dataFromExcelWorkBook.get("Sheet1").get(j + 1).get("Service ID"))) {
					collectiveStudies.add(dataFromExcelWorkBook.get("Sheet1").get(j).get("Study short name"));
//					orderedCollectiveData.put(dataWise, collectiveStudies);
//					collectiveStudies = new LinkedHashSet<>();
//					dataWise++;
				} else {
					collectiveStudies.add(dataFromExcelWorkBook.get("Sheet1").get(j).get("Study short name"));
//					orderedCollectiveData.put(dataWise, collectiveStudies);
//					collectiveStudies = new LinkedHashSet<>();
//					dataWise++;
					i = j;

					collectiveData.put(dataFromExcelWorkBook.get("Sheet1").get(j).get("Service ID"), collectiveStudies);
//					orderedCollectiveData = new HashMap<>();
					collectiveStudies = new LinkedHashSet<>();
					dataWise = 1;
					break inner;
				}
			}

		}
		System.out.println(collectiveData);
		return collectiveData;

	}

	public void collectiveTagUpdate() throws IOException, InterruptedException {
		Map<String, Set<String>> collectiveData = new HashMap<>();
		collectiveData = readExcelForCollectiveTagUpdate();
		clearTabs();
		openBillingModule();
		for (String serviceId : collectiveData.keySet()) {
			openClaimByServiceId(serviceId);
//			click("Centers Lab CheckAll CheckBox", useThis);
			Thread.sleep(700);
			selectCollective(collectiveData.get(serviceId));
			List<WebElement> selectedRecords = findElements("Centers Lab Selected Result");
			for (WebElement eachRecord : selectedRecords) {
				if (!verifyTagAvailable("Dx removed by filter and added back from HL7", eachRecord)) {
					addTag("Dx removed by filter and added back from HL7");
					break;
				}
			}

		}
	}

	public void addCPtAndDx() throws InterruptedException, IOException {

		readExcelForInput("AddCPTDx");
		clearTabs();
		openBillingModule();
		click("Centers Lab Billing Chechbox", useThis);
		for (int i = 1; i < dataFromExcelWorkBook.get("Sheet1").size(); i++) {
			openClaimByServiceId(dataFromExcelWorkBook.get("Sheet1").get(i).get("Service ID"));
			click("Centers Lab CheckAll CheckBox", useThis);
			openServiceEdit();
			addCPTdx(i);
		}

	}

	private void openServiceEdit() {
		emosowLoaderWait();
		click("Centers Lab Service Edit  button", useThis);
		click("Centers Lab Service Edit Open ", useThis);
		emosowLoaderWait();
		emosowLoaderWait();

	}

	private void addCPTdx(int i) throws InterruptedException {
		List<WebElement> availablestudies = findElements("Centers lab Number of studies");
		int numberOfStudiesPresent = availablestudies.size();
		click("Centers Lab Add Study", useThis);
		newElementWait(
				"//span[text()='Studies']/ancestor::fieldset//div[@class='app-multifield-body']/div[@class='app-multifield-row']["
						+ numberOfStudiesPresent++ + "]");

		driver.findElement(By.xpath(
				"//span[text()='Studies']/ancestor::fieldset//div[@class='app-multifield-body']/div[@class='app-multifield-row']["
						+ numberOfStudiesPresent + "]/div[3]//input"))
				.sendKeys(dataFromExcelWorkBook.get("Sheet1").get(i).get("Study").trim());

//		writeHere("Centers Lab Study Box", dataFromExcelWorkBook.get("Sheet1").get(i).get("Study"), useThis);
		Thread.sleep(2000);
		buttonStroke_enterKeyWithXpath(
				"//span[text()='Studies']/ancestor::fieldset//div[@class='app-multifield-body']/div[@class='app-multifield-row']["
						+ numberOfStudiesPresent + "]/div[3]//input");

		driver.findElement(By.xpath(
				"// span[text()='Studies']/ancestor::fieldset//div[@class='app-multifield-body']/div[@class='app-multifield-row']["
						+ numberOfStudiesPresent + "]/div[7]/div/div//input"))
				.sendKeys(dataFromExcelWorkBook.get("Sheet1").get(i).get("Diagnosis").trim());
//		writeHere("Centers Lab Diagnosis Box", dataFromExcelWorkBook.get("Sheet1").get(i).get("Diagnosis"), useThis);
		Thread.sleep(800);
		buttonStroke_SpaceKeyWithXpath(
				"// span[text()='Studies']/ancestor::fieldset//div[@class='app-multifield-body']/div[@class='app-multifield-row']["
						+ numberOfStudiesPresent + "]/div[7]/div/div//input");
		Thread.sleep(2500);
		buttonStroke_enterKeyWithXpath(
				"// span[text()='Studies']/ancestor::fieldset//div[@class='app-multifield-body']/div[@class='app-multifield-row']["
						+ numberOfStudiesPresent + "]/div[7]/div/div//input");
		click("Centers Lab Service Save Button", useThis);
		emosowLoaderWait();
		emosowLoaderWait();
	}

	private void newElementWait(String xpathOfExpectedElement) {
		System.out.println(xpathOfExpectedElement);
		List<WebElement> loader = driver.findElements(By.xpath(xpathOfExpectedElement));
		while ((loader.isEmpty())) {
			loader = driver.findElements(By.xpath(xpathOfExpectedElement));
			if (!loader.isEmpty()) {
				break;
			}
		}
	}

	private void selectCollective(Set<String> data) {
		String study = "";
		for (String eachData : data) {
//			List<WebElement> searchResults = driver.findElements(By.xpath(
//					"//div[@class='x-panel-tbar']/following::div[contains(@class,'x-grid3-row') and not(contains(@class,'checker'))]//td[contains(@class,'study')]//div[@class='text-center']/span"));

			List<WebElement> searchResults = driver.findElements(By.xpath(
					"//div[@class='x-panel-tbar']/following::div[contains(@class,'x-grid3-row') and not(contains(@class,'checker'))]"));
			for (WebElement eachResult : searchResults) {
				List<WebElement> allDivsioninResult = eachResult.findElements(By.tagName("td"));
				for (WebElement eachDivision : allDivsioninResult) {
					if (eachDivision.getAttribute("class").contains("study")) {
						List<WebElement> allDivInsideStudy = eachDivision.findElements(By.tagName("div"));
						for (WebElement eachDiv : allDivInsideStudy) {
							List<WebElement> allSpan = eachDiv.findElements(By.tagName("span"));
							if (!allSpan.isEmpty()) {
								study = allSpan.get(0).getText().toString().trim();
							}

							if (study.equals(eachData)) {
								List<WebElement> insidediv = allDivsioninResult.get(0).findElements(By.tagName("div"));
								for (WebElement eachInsideDiv : insidediv) {
									WebElement clicable = eachInsideDiv.findElement(By.tagName("div"));
									if (!eachResult.getAttribute("class").contains("selected")) {
										clicable.click();
									}

									break;
								}
							} else {
								break;
							}
						}
					}
				}
			}
		}
	}

}
