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

public class TagUpdaterWIthServiceID extends WrapperClass {
	private By useThis;
	Map<String, Map<Integer, Map<String, String>>> dataFromExcelWorkBook = new HashMap<>();
	ExcelReadWrite excelReadWrite = new ExcelReadWrite();
	TagFinderWriteObject tagWriteObject;
	String servidId = "";
	ArrayList<String> removedTags = new ArrayList<>();
	Set<String> alreadyAvailableTags = new HashSet<>();
	Set<String> matchedTags = new HashSet<>();;
	Set<String> updatedMatchedTags = new HashSet<>();
	Set<String> tagsAfterUpdation = new HashSet<>();
	Set<String> initialTagsSet = new HashSet<>();
	Map<String, String> exportData = new HashMap<>();
	Map<Integer, TagFinderWriteObject> dataToExcelWorkBook = new HashMap<>();

	public TagUpdaterWIthServiceID() {
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
		click("Centers Lab by service Check Box", useThis);
		for (int i = 1; i <= dataFromExcelWorkBook.get("Sheet1").size(); i++) {

//			writeHere("Centers Lab Accession Number Box",
//					dataFromExcelWorkBook.get("Sheet1").get(i).get("A/N").toString(), useThis);

			writeHere("Centers Lab Service ID Box",
					dataFromExcelWorkBook.get("Sheet1").get(i).get("Service ID").toString(), useThis);

			servidId = dataFromExcelWorkBook.get("Sheet1").get(i).get("Service ID").toString();
			List<WebElement> searchButtons = findElements("Centers Lab Find Button");
			emosowLoaderWait();

			for (WebElement eachButton : searchButtons) {
				emosowLoaderWait();
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
//		String notes = "";

//		String notes = "Paid and alkajsdklasd Non-Covered";
		String initialTags = "";
//		List<WebElement> initialTagsAsElement=null;

//		initialTagsAsElement = driver.findElements(By.xpath("//div[span[normalize-space(text())='Tags:']]"));
//			initialTags = gettext("Centers Lab Tags");
//		for (WebElement tagEle : initialTagsAsElement) {
//			initialTags = tagEle.getText();
//		}
//
//		if (initialTags.isEmpty() || initialTags.isBlank()) {
//			initialTags = "NA";
//		} else {
//			String tag = "Tags:";
//			initialTags = initialTags.substring(initialTags.indexOf("Tags:") + tag.length()).trim();
//		}

//		int matchCount = 0;

		matchedTags.add(dataFromExcelWorkBook.get("Sheet2").get(i).get("Tags").trim());

//		}
		click("Centers Lab Check Box", useThis);
//		updateTags(matchedTags, notes, initialTags);
		addTags(matchedTags);
		System.out.println("Tag Completed for->" + servidId);
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
}
