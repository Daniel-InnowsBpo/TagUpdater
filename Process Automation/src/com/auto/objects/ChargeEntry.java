package com.auto.objects;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.server.handler.SwitchToFrame;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.auto.Supports.DataReader;
import com.auto.Supports.Excel;
import com.auto.Supports.WrapperClass;
import com.beust.jcommander.JCommander.Builder;

public class ChargeEntry extends WrapperClass {

	boolean flag = false;
	private By useThis;
	List<WebElement> dropDownList = new ArrayList<>();
	List<WebElement> multiPartButtons = new ArrayList<>();

	Map<String, Map<Integer, Map<String, String>>> allDataFromAllSheetInChargeEntryWorkbook = new HashMap<>();
	Map<String, String> dataAndKeytoBeEntered = new HashMap<>();

	public ChargeEntry() {
		DataReader.getData("Charge Entry");
		DataReader.getDataKeyandValue("Charge Entry");
	}

	public void ChargeEntry() throws InterruptedException, ParseException {

		allDataFromAllSheetInChargeEntryWorkbook = ExcelReadWrite.extractChargeSheetData();

		waiting.until(ExpectedConditions.elementToBeClickable(driver.findElement(
				By.xpath("//div[@class='x-toolbar x-small-editor app-mainmenu-toolbar x-toolbar-layout-ct']"))));

		Thread.sleep(2000);
		if (isDisplayed("Charge Closable Tab")) {
			flag = true;
		}

		if (flag) {
			click("Charge Close all Tabs", useThis);
			flag = false;
		}

		for (String orderID : allDataFromAllSheetInChargeEntryWorkbook.keySet()) {

			click("Charge Entry Processing Button", useThis);
			click("Charge Entry Quick Search", useThis);

			writeHere("Charge Entry Order ID input box", orderID, useThis);
			KeyStroke("Charge Entry Order ID input box", Keys.ENTER, useThis);

			waitFor("Charge Order Window");
			click("Charge Add Button", useThis);
			waitFor("Charge Add Study Button");
			waitUntillItGoes("Charge Loading");

			for (Integer rowNumber : allDataFromAllSheetInChargeEntryWorkbook.get(orderID).keySet()) {
				enterValues(orderID, rowNumber);

			}
			click("Charge Save", useThis);
			waitFor("Charge Order Window");
		}

	}

	public void enterValues(String orderID, Integer rowNumber) throws ParseException, InterruptedException {
//		waitFor("Charge Order Window");
		try {

			for (int i = 0; i < 9; i++) {
				if (isDisplayed("Charge Loading")) {
					Thread.sleep(1000);
				} else {

					DOBEntry(orderID, rowNumber);
					nameEntry(orderID, rowNumber);
					click("Charge First Name", useThis);

					try {
						if (!findElements("Charge Snapshot").isEmpty()) {
//						if (isDisplayed("Charge Snapshot")) {
//							Actions action = new Actions(driver);
							WebElement firstNameBox = driver.findElement(By.xpath(
									"//legend[span[text()='Demographics']]/following::label[text()='First name:']/following::input[1]"));

							
							Thread.sleep(2000);
							firstNameBox.sendKeys(Keys.chord(Keys.CONTROL, Keys.RETURN));

						}
					} catch (NoSuchElementException e) {
						e.printStackTrace();
					}

					genderSelection(orderID, rowNumber);
					doctorEntry(orderID, rowNumber);

//					if (!driver.findElements(By.xpath(
//							"//fieldset[legend[span[text()='Studies']]]//div[@class='app-multifield-body']//div[@class='app-multifield-remove']"))
//							.isEmpty()) {
//
//						multiPartButtons = driver.findElements(By.xpath(
//								"//fieldset[legend[span[text()='Studies']]]//div[@class='app-multifield-body']//div[@class='app-multifield-remove']"));
//
//						for (WebElement multiPartButton : multiPartButtons) {
//							multiPartButton.click();
//						}
//					}
					studyEntry(orderID, rowNumber);
					dxEntry(orderID, rowNumber);
					click("Charge Save and Next", useThis);
					
					if (isDisplayed("Charge Error Text")) {

						click("Charge Ok Button", useThis);
					}
//					waitUntillItGoes("Charge Loading");
					
				}
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	public void studyEntry(String orderID, Integer rowNumber) throws InterruptedException {

		int count = 1;
		click("Charge Add Study Button", useThis);

		String studyValue = (((allDataFromAllSheetInChargeEntryWorkbook.get(orderID)).get(rowNumber)).get("CPT"))
				.toString();

		String valueWithoutDecimal = studyValue.split("\\.")[0];

		click("Charge Study Field", useThis);

		writeHere("Charge Study Field", (valueWithoutDecimal), useThis);

		Thread.sleep(1500);
//		waitFor("Charge Study Field DropDown");

//		dropDownList = findElementsPresent("Charge Study Field DropDown");

		for (WebElement dropDownSelect : dropDownList) {
			if (count > 1) {
				if ((dropDownSelect.getText()).contains(valueWithoutDecimal.toString())) {
					KeyStroke("Charge Study Field", Keys.ENTER, useThis);
					break;
				} else {
					KeyStroke("Charge Study Field", Keys.ARROW_DOWN, useThis);
				}

			}
			count++;
		}
		KeyStroke("Charge Study Field", Keys.TAB, useThis);
		count = 1;
	}

	public void dxEntry(String orderID, Integer rowNumber) throws InterruptedException {
		writeHere("Charge Diagnosis Field",
				allDataFromAllSheetInChargeEntryWorkbook.get(orderID).get(rowNumber).get("Dx").toString(), useThis);
		Thread.sleep(1500);
		for (int i = 0; i < 5; i++) {
			if (gettext("Charge Diagnosis Field").equals(
					allDataFromAllSheetInChargeEntryWorkbook.get(orderID).get(rowNumber).get("Dx").toString().trim())) {
				KeyStroke("Charge Diagnosis Field", Keys.TAB, useThis);
			} else {
				Thread.sleep(500);
			}
		}
		KeyStroke("Charge Diagnosis Field", Keys.TAB, useThis);
	}

	public void DOBEntry(String orderID, Integer rowNumber) throws ParseException, InterruptedException {
		DateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
		DateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);

		Date date = inputFormat
				.parse(allDataFromAllSheetInChargeEntryWorkbook.get(orderID).get(rowNumber).get("Date of Birth"));
		writeHereDate("Charge DOB Field", outputFormat.format(date), useThis);
		Thread.sleep(1500);
		if (gettext("Charge DOB Field").equals(outputFormat.format(date))) {
			KeyStroke("Charge DOB Field", Keys.TAB, useThis);
		}
	}

	public void nameEntry(String orderID, Integer rowNumber) {

		if (allDataFromAllSheetInChargeEntryWorkbook.get(orderID).get(rowNumber).containsKey("Last Name")) {
			writeHere("Charge Last Name",
					allDataFromAllSheetInChargeEntryWorkbook.get(orderID).get(rowNumber).get("Last Name").trim(),
					useThis);
		}
		
		writeHere("Charge First Name",
				allDataFromAllSheetInChargeEntryWorkbook.get(orderID).get(rowNumber).get("First Name").trim(), useThis);

		if (allDataFromAllSheetInChargeEntryWorkbook.get(orderID).get(rowNumber).containsKey("Middle Name")) {

			if (!allDataFromAllSheetInChargeEntryWorkbook.get(orderID).get(rowNumber).get("Middle Name").isEmpty()) {
				writeHere("Charge Middle Name",
						allDataFromAllSheetInChargeEntryWorkbook.get(orderID).get(rowNumber).get("Middle Name").trim(),
						useThis);

			}
		}

		

	}

	public void doctorEntry(String orderID, Integer rowNumber) {
		writeHere("Charge Referring Doctor",
				allDataFromAllSheetInChargeEntryWorkbook.get(orderID).get(rowNumber).get("Referring Doctor").trim(),
				useThis);
	}

	public void genderSelection(String orderID, Integer rowNumber) {
		if (allDataFromAllSheetInChargeEntryWorkbook.get(orderID).get(rowNumber).get("Gender").trim()
				.equalsIgnoreCase("Female")) {
			click("Charge Gender Female", useThis);
		} else {
			click("Charge Gender Male", useThis);
		}
	}
}
