package com.auto.Supports;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WrapperClass {

	SimpleFormatter formatter;
	// public static WrapperClass wOb=new WrapperClass();
	public static WebDriver driver;
	public static WebDriverWait waiting;
	public static Properties prop = new Properties();
	public static String filePath;
	public static String actions;

	public static Logger mylog = Logger.getLogger("Cashkaro");
	public static DesiredCapabilities capabilities;
	boolean flag = true;

	// public static WebDriverWait waiting;

	public WrapperClass() {

		/*
		 * try {
		 * 
		 * prop.load(new FileInputStream(System.getProperty("user.dir") +
		 * "\\Destinations.properties"));
		 * 
		 * } catch (FileNotFoundException e) {
		 * 
		 * System.out.println("Properties File is not available");
		 * 
		 * } catch (IOException e) {
		 * 
		 * e.printStackTrace();
		 * 
		 * }
		 * 
		 * ChromeOptions options = new ChromeOptions();
		 * options.addArguments("--incognito"); capabilities =
		 * DesiredCapabilities.chrome();
		 * capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		 */
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\driver\\chromedriver.exe");

		System.out.println("intialised");
	}

	public void dragndrop(String srcAttribute, String srcAttributeValue, String destAttribute,
			String destAttributeValue) {

		try {
			WebElement src = LocateHereDefault(srcAttribute, srcAttributeValue);
			WebElement dest = LocateHereDefault(destAttribute, destAttributeValue);
			Actions cursor = new Actions(driver);
			cursor.dragAndDrop(src, dest);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public List<WebElement> findElements(String dataSet) {
		String attribute_attributeValue = DataReader.getAttributeandAttributevalue(dataSet);
		String attribute = attributeSplitter(attribute_attributeValue);
		String attributeValue = attributeValueSplitter(attribute_attributeValue);

//		waiting.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(attributeValue))));
		List<WebElement> webElements = driver.findElements(By.xpath(attributeValue));
		return webElements;

	}

	public List<WebElement> findElementsPresent(String dataSet) {
		String attribute_attributeValue = DataReader.getAttributeandAttributevalue(dataSet);
		String attribute = attributeSplitter(attribute_attributeValue);
		String attributeValue = attributeValueSplitter(attribute_attributeValue);

		waiting.until(ExpectedConditions.visibilityOfAllElements(driver.findElement(By.xpath(attributeValue))));
		List<WebElement> webElements = driver.findElements(By.xpath(attributeValue));
		return webElements;

	}

	public void doubleClick(String dataSet, By elementName) {
		try {
			Actions act = new Actions(driver);
			String attribute_attributeValue = DataReader.getAttributeandAttributevalue(dataSet);
			elementName = AttributeFinder.attributefinder(elementName, attributeSplitter(attribute_attributeValue),
					attributeValueSplitter(attribute_attributeValue));
			WebElement wb = driver.findElement(elementName);
			act.doubleClick(wb).perform();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String gettext(String dataSet) {
		String text = null;
		try {
			String attribute_attributeValue = DataReader.getAttributeandAttributevalue(dataSet);
			String attribute = attributeSplitter(attribute_attributeValue);
			String attributeValue = attributeValueSplitter(attribute_attributeValue);
			WebElement element = LocateHereDefault(attribute, attributeValue);
			text = element.getText();

		} catch (Exception e) {

			e.printStackTrace();

			// TODO Auto-generated catch block

		}
		return text;
	}

	public void buttonStroke_enterKey(String dataSet) {
		try {
			String attribute_attributeValue = DataReader.getAttributeandAttributevalue(dataSet);
			String attribute = attributeSplitter(attribute_attributeValue);
			String attributeValue = attributeValueSplitter(attribute_attributeValue);
			WebElement element = LocateHereDefault(attribute, attributeValue);
			Thread.sleep(1000);
			element.sendKeys(Keys.RETURN);

		} catch (Exception e) {

			e.printStackTrace();

			// TODO Auto-generated catch block

		}
	}

	public String getTextBoxValue(String dataSet) {
		String attribute_attributeValue = DataReader.getAttributeandAttributevalue(dataSet);
		String attribute = attributeSplitter(attribute_attributeValue);
		String attributeValue = attributeValueSplitter(attribute_attributeValue);
		WebElement element = LocateHereDefault(attribute, attributeValue);
		String text = element.getAttribute("value");
		return text;
	}

	public boolean isDisplayed(String dataSet) {
		String attribute_attributeValue = DataReader.getAttributeandAttributevalue(dataSet);
		String attribute = attributeSplitter(attribute_attributeValue);
		String attributeValue = attributeValueSplitter(attribute_attributeValue);
//		WebElement element=LocateHereDefault(attribute, attributeValue);
//		waiting.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(attributeValue))));
		List<WebElement> errorAlert = driver.findElements(By.xpath(attributeValue));

		if (errorAlert.isEmpty()) {
			flag = false;
		} else {
			flag = true;
		}
		return flag;

	}

	public void scrollToElement(String dataSet) {
		String attribute_attributeValue = DataReader.getAttributeandAttributevalue(dataSet);
		String attribute = attributeSplitter(attribute_attributeValue);
		String attributeValue = attributeValueSplitter(attribute_attributeValue);
		WebElement element = LocateHereDefault(attribute, attributeValue);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
	}

	public void scrollToElement(WebElement element) {

		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
	}

	public void acceptAlert() {

		try {
			WebDriverWait waiting = new WebDriverWait(driver, 5);
			waiting.until(ExpectedConditions.alertIsPresent());
			driver.switchTo().alert().accept();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void pressEnter(String dataSet, By elementName) {

		String attribute_attributeValue = DataReader.getAttributeandAttributevalue(dataSet);
		elementName = AttributeFinder.attributefinder(elementName, attributeSplitter(attribute_attributeValue),
				attributeValueSplitter(attribute_attributeValue));
		driver.findElement(elementName).sendKeys(Keys.ENTER);

	}

	public void click(String dataSet, By elementName) {

		try {
			String attribute_attributeValue = DataReader.getAttributeandAttributevalue(dataSet);
			elementName = AttributeFinder.attributefinder(elementName, attributeSplitter(attribute_attributeValue),
					attributeValueSplitter(attribute_attributeValue));
			driver.findElement(elementName).click();
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public void get(String dataSet) {

	}

	public void clear(String dataSet, By elementName) {
		try {
			String attribute_attributeValue = DataReader.getAttributeandAttributevalue(dataSet);
			elementName = AttributeFinder.attributefinder(elementName, attributeSplitter(attribute_attributeValue),
					attributeValueSplitter(attribute_attributeValue));
			driver.findElement(elementName).clear();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void mouseHover(String dataSet, By elementName) {
		WebElement element;
		Actions mouseAction = new Actions(driver);
		try {
			String attribute_attributeValue = DataReader.getAttributeandAttributevalue(dataSet);
			elementName = AttributeFinder.attributefinder(elementName, attributeSplitter(attribute_attributeValue),
					attributeValueSplitter(attribute_attributeValue));

			element = driver.findElement(elementName);
			mouseAction.moveToElement(element).build().perform();
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public String attributeSplitter(String attribute_attributeValue) {

		String[] splittedAttributeandAttributeValue = attribute_attributeValue.split("#");
		String attribute = splittedAttributeandAttributeValue[0];
		return attribute;
	}

	public String attributeValueSplitter(String attribute_attributeValue) {

		String[] splittedAttributeandAttributeValue = attribute_attributeValue.split("#");
		String attributeValue = splittedAttributeandAttributeValue[1];
		return attributeValue;
	}

	public void write(String dataSet, String dataKey, By textBoxName) {

		try {
			String attribute_attributeValue = DataReader.getAttributeandAttributevalue(dataSet);
			textBoxName = AttributeFinder.attributefinder(textBoxName, attributeSplitter(attribute_attributeValue),
					attributeValueSplitter(attribute_attributeValue));
			driver.findElement(textBoxName).click();
//			System.out.println(
//					"Data From Excell&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&" + DataReader.getEachKeyandKeyValue(dataKey));
			driver.findElement(textBoxName).sendKeys(DataReader.getEachKeyandKeyValue(dataKey));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeHere(String dataSet, String dataKey, By textBoxName) {

		try {
			String attribute_attributeValue = DataReader.getAttributeandAttributevalue(dataSet);
			textBoxName = AttributeFinder.attributefinder(textBoxName, attributeSplitter(attribute_attributeValue),
					attributeValueSplitter(attribute_attributeValue));
			driver.findElement(textBoxName).clear();
			driver.findElement(textBoxName).sendKeys(dataKey);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeHere(String dataSet, Integer dataKey, By textBoxName) {

		try {
			String attribute_attributeValue = DataReader.getAttributeandAttributevalue(dataSet);
			textBoxName = AttributeFinder.attributefinder(textBoxName, attributeSplitter(attribute_attributeValue),
					attributeValueSplitter(attribute_attributeValue));
			driver.findElement(textBoxName).sendKeys(dataKey.toString());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeHereDate(String dataSet, String string, By textBoxName) {

		try {
			String attribute_attributeValue = DataReader.getAttributeandAttributevalue(dataSet);
			textBoxName = AttributeFinder.attributefinder(textBoxName, attributeSplitter(attribute_attributeValue),
					attributeValueSplitter(attribute_attributeValue));
			driver.findElement(textBoxName).clear();
			driver.findElement(textBoxName).sendKeys(string.toString());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void waitFor(String dataSet) {
		String attribute_attributeValue = DataReader.getAttributeandAttributevalue(dataSet);
		String attributeValue = attributeValueSplitter(attribute_attributeValue);
		waiting.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(attributeValue))));

	}

	public void waitUntillItGoes(String dataSet) {
		String attribute_attributeValue = DataReader.getAttributeandAttributevalue(dataSet);
		String attributeValue = attributeValueSplitter(attribute_attributeValue);
		waiting.until(ExpectedConditions.invisibilityOf(driver.findElement(By.xpath(attributeValue))));

	}

	public void KeyStroke(String dataSet, Keys dataKey, By textBoxName) {

		try {
			String attribute_attributeValue = DataReader.getAttributeandAttributevalue(dataSet);
			textBoxName = AttributeFinder.attributefinder(textBoxName, attributeSplitter(attribute_attributeValue),
					attributeValueSplitter(attribute_attributeValue));

			driver.findElement(textBoxName).sendKeys(dataKey);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void javaScriptWrite(String dataSet, String dataKey, By textBoxName) {
		WebElement fedexid = driver.findElement(
				By.xpath("//th[label[normalize-space(text())='Federal Tax ID']]/following-sibling::td/div/input"));
		((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('Value','987678654')", fedexid);
	}

	public static void waitForAjax(String dataKey) {
		String businessType = DataReader.getEachKeyandKeyValue(dataKey);
		String locator = "//option[@selected='selected' and text()='" + businessType + "']";
		waitForNow("xpath", locator);
		// By elementToFind=By.id(locator);
	}

	public void select(String dataSet, String dataKey, String selectby, By element) {

		try {
			String attribute_attributeValue = DataReader.getAttributeandAttributevalue(dataSet);
			element = AttributeFinder.attributefinder(element, attributeSplitter(attribute_attributeValue),
					attributeValueSplitter(attribute_attributeValue));
			WebElement selectable = driver.findElement(element);
			Select dropDown = new Select(selectable);
			if (selectby.equalsIgnoreCase("By value")) {
				dropDown.selectByValue(DataReader.getEachKeyandKeyValue(dataKey));
			}

			else if (selectby.equalsIgnoreCase("By Visibletext")) {
				dropDown.selectByVisibleText(DataReader.getEachKeyandKeyValue(dataKey));
			}

			else if (selectby.equalsIgnoreCase("By Index")) {
				dropDown.selectByIndex(Integer.parseInt(DataReader.getEachKeyandKeyValue(dataKey)));
			}

			else {
//				System.out.println("Please select valid Selectors");
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void selectThis(String dataSet, String dataKey, String selectBy, By element) {

		try {
			String attribute_attributeValue = DataReader.getAttributeandAttributevalue(dataSet);
			element = AttributeFinder.attributefinder(element, attributeSplitter(attribute_attributeValue),
					attributeValueSplitter(attribute_attributeValue));
			WebElement selectable = driver.findElement(element);
			Select dropDown = new Select(selectable);
			if (selectBy.equalsIgnoreCase("By value")) {
				dropDown.selectByValue(dataKey);
			}

			else if (selectBy.equalsIgnoreCase("By Visibletext")) {
				dropDown.selectByVisibleText(dataKey);
			}

			else if (selectBy.equalsIgnoreCase("By Index")) {
				dropDown.selectByIndex(Integer.parseInt(dataKey));
			}

			else {
				System.out.println("Please select valid Selectors");
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void scroll(String scrollvalue) {

		JavascriptExecutor jsx = (JavascriptExecutor) driver;
		jsx.executeScript("window.scrollBy(0," + scrollvalue + ")", "");

	}

	public void frameSwitch(String resultframe) {

		WebDriverWait waiting = new WebDriverWait(driver, 180);
		waiting.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(resultframe));
	}

	public void verifyElement(String dataSet, By elementName) {

		try {
			String attribute_attributeValue = DataReader.getAttributeandAttributevalue(dataSet);
			elementName = AttributeFinder.attributefinder(elementName, attributeSplitter(attribute_attributeValue),
					attributeValueSplitter(attribute_attributeValue));
			if (driver.findElement(elementName) != null) {
				System.out.println("Element is Present");
			} else {
				System.out.println("Element is Absent");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void datePicker(String month, String day, String year, WebElement calender) {
		int count1 = 0;
		int count = 0;
		List<WebElement> monthTabandDateTab = calender.findElements(By.tagName("div"));
		for (WebElement monthsAndYear : monthTabandDateTab) {
			if (count1 == 0) {
				List<WebElement> calenderMonthsandYears = calender.findElements(By.tagName("select"));
				for (WebElement eachMonthandYear : calenderMonthsandYears) {
					if (count == 0) {
						List<WebElement> allMonths = eachMonthandYear.findElements(By.tagName("option"));
						Select monthtoBeSelected = new Select(eachMonthandYear);
						for (WebElement eachMonth : allMonths) {
							if (eachMonth.getText().equalsIgnoreCase(month)) {
								monthtoBeSelected.selectByVisibleText(month);
							}
						}
						count++;
					} else {
						List<WebElement> allYears = eachMonthandYear.findElements(By.tagName("Option"));
						Select YeartoBeSelected = new Select(eachMonthandYear);
						for (WebElement eachYear : allYears) {
							if (eachYear.getText().equalsIgnoreCase(year)) {
								YeartoBeSelected.selectByVisibleText(year);
							}
						}
					}

				}
				count1++;
			} else {
				List<WebElement> dates = monthsAndYear.findElements(By.tagName("tr"));
				for (WebElement dateRows : dates) {
					List<WebElement> dateColumns = dateRows.findElements(By.tagName("td"));
					for (WebElement eachDate : dateColumns) {
						if (eachDate.getText().equalsIgnoreCase(day)) {
							eachDate.click();
						}
					}
				}
			}
		}
	}

	public void frameSwitch(int resultframe) {

		WebDriverWait waiting = new WebDriverWait(driver, 5);
		waiting.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(resultframe));
	}

	public boolean waitforPageLoad() {

		if ((((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete")))
			;
		return true;
	}

	public void waitForText(String dataSet, String sourceDataSet, By elementName) {

		waiting = new WebDriverWait(driver, 5);
		String attribute_attributeValue = DataReader.getAttributeandAttributevalue(dataSet);
		elementName = AttributeFinder.attributefinder(elementName, attributeSplitter(attribute_attributeValue),
				attributeValueSplitter(attribute_attributeValue));
		waiting.until(ExpectedConditions.textToBePresentInElementValue(elementName, getTextBoxValue(sourceDataSet)));
	}

	public void windowSwitch(String method, String value) {

		Set<String> windowHandles = driver.getWindowHandles();
		if (method.equalsIgnoreCase("By Title")) {
			for (String singleHandle : windowHandles) {
				driver.switchTo().window(singleHandle);
				String currentTitle = driver.getTitle();
				if (currentTitle.equals(value)) {
					break;
				}
			}
		}
		if (method.equalsIgnoreCase("By URL")) {
			for (String singleHandle : windowHandles) {
				driver.switchTo().window(singleHandle);
				String currentURL = driver.getCurrentUrl();
				if (currentURL.equals(value)) {
					break;
				}
			}
		}
	}

	public void lookupSwitch(String currentWindowHandle) {
		Set<String> windowHandles = driver.getWindowHandles();
		for (String singleHandle : windowHandles) {
			if (!(singleHandle.equals(currentWindowHandle))) {
				driver.switchTo().window(singleHandle);
				break;
			}
		}

	}

	// public void lookUpWindow
	public void screenshot(String filename, String filetype) {

		try {
			File pic = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			File screenShot = new File(System.getProperty("user.dir") + filename + "." + filetype);
			FileUtils.copyFile(pic, screenShot);
			filePath = screenShot.toString();
			System.out.println(filePath);
			// filePath=filePath.replaceAll(" ", "%20")
			filePath = filename + "." + filetype;
			filePath = filePath.replace("/", "\\");
			filePath = "<a target='_blank' href=\"" + filePath + "\"><img src=\"" + filePath + "\" "
					+ "style='width:150px'></a>";
			System.out.println("final path---" + filePath);
		} catch (WebDriverException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Error while saving screenshot" + e.toString());
		}
	}

	public static void waitForNow(String type, String value) {

		// waiting= new WebDriverWait(driver,30);
		try {
			if (type.equalsIgnoreCase("Xpath")) {

				waiting.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(value)));
				waiting.until(ExpectedConditions.elementToBeClickable(By.xpath(value)));

			}
			if (type.equalsIgnoreCase("id")) {

				waiting.until(ExpectedConditions.visibilityOfElementLocated(By.id(value)));

			}
			if (type.equalsIgnoreCase("classname")) {

				waiting.until(ExpectedConditions.visibilityOfElementLocated(By.className(value)));

			}
			if (type.equalsIgnoreCase("partial link text")) {

				waiting.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(value)));

			}
			if (type.equalsIgnoreCase("link text")) {

				waiting.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(value)));

			}
			if (type.equalsIgnoreCase("name")) {

				waiting.until(ExpectedConditions.visibilityOfElementLocated(By.name(value)));

			}
			if (type.equalsIgnoreCase("cssselector")) {

				waiting.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(value)));

			}
			if (type.equalsIgnoreCase("tag name")) {

				waiting.until(ExpectedConditions.visibilityOfElementLocated(By.tagName(value)));

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void waitForNow(String type, String value, String Text) {

		WebDriverWait waiting = new WebDriverWait(driver, 5);

		try {
			if (type.equalsIgnoreCase("Xpath")) {

				waiting.until(ExpectedConditions.invisibilityOfElementWithText(By.xpath(value), Text));

			}
			if (type.equalsIgnoreCase("id")) {

				waiting.until(ExpectedConditions.invisibilityOfElementWithText(By.id(value), Text));

			}
			if (type.equalsIgnoreCase("classname")) {

				waiting.until(ExpectedConditions.invisibilityOfElementWithText(By.className(value), Text));

			}
			if (type.equalsIgnoreCase("partial link text")) {

				waiting.until(ExpectedConditions.invisibilityOfElementWithText(By.partialLinkText(value), Text));

			}
			if (type.equalsIgnoreCase("link text")) {

				waiting.until(ExpectedConditions.invisibilityOfElementWithText(By.linkText(value), Text));

			}
			if (type.equalsIgnoreCase("name")) {

				waiting.until(ExpectedConditions.invisibilityOfElementWithText(By.name(value), Text));

			}
			if (type.equalsIgnoreCase("cssselector")) {

				waiting.until(ExpectedConditions.invisibilityOfElementWithText(By.cssSelector(value), Text));

			}
			if (type.equalsIgnoreCase("tag name")) {

				waiting.until(ExpectedConditions.invisibilityOfElementWithText(By.tagName(value), Text));

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public WebElement LocateHereDefault(String type, String Value) {

		WebElement Action = null;
		try {
			if (type.equalsIgnoreCase("id")) {

				waitForNow(type, Value);
				Action = driver.findElement(By.id(Value));

			} else if (type.equalsIgnoreCase("name")) {

				waitForNow(type, Value);
				Action = driver.findElement(By.name(Value));

			} else if (type.equalsIgnoreCase("classname")) {

				waitForNow(type, Value);
				Action = driver.findElement(By.className(Value));

			} else if (type.equalsIgnoreCase("linktext")) {

				waitForNow(type, Value);
				Action = driver.findElement(By.linkText(Value));

			} else if (type.equalsIgnoreCase("partiallinktext")) {

				waitForNow(type, Value);
				Action = driver.findElement(By.partialLinkText(Value));

			} else if (type.equalsIgnoreCase("Xpath")) {

				waitForNow(type, Value);
				Action = driver.findElement(By.xpath(Value));

			} else if (type.equalsIgnoreCase("CssSelector")) {

				waitForNow(type, Value);
				Action = driver.findElement(By.cssSelector(Value));

			} else if (type.equalsIgnoreCase("Tagname")) {

				waitForNow(type, Value);
				Action = driver.findElement(By.tagName(Value));

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Action;
	}

	public void loggerInitiation() {
		try {
			FileHandler file = new FileHandler(prop.getProperty("Logfile_Location"));
			mylog.setLevel(Level.ALL);
			formatter = new SimpleFormatter();
			file.setFormatter(formatter);
			mylog.addHandler(file);
		} catch (Exception e) {
			System.out.println("Error when writing report log:\n" + e.toString());
		}

	}

}