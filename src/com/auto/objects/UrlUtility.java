package com.auto.objects;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.auto.Supports.DataReader;
import com.auto.Supports.WrapperClass;

public class UrlUtility extends WrapperClass {

	By useThis;

	int count = 0;

	public UrlUtility(String dataSet) {

		DataReader.getData(dataSet);
		DataReader.getDataKeyandValue(dataSet);
	}

	public UrlUtility(String dataSet, int count) {

		DataReader.getData(dataSet);
		DataReader.getDataKeyandValue(dataSet);
	}

	public void healthFirstLogin(UrlUtility urlUtility) throws InterruptedException {

		urlUtility.setUserName("Health First Username Box", "Health First Username");
		urlUtility.setUserName("Health First Password Box", "Health First Password");
		urlUtility.clickLoginButton("Health First Login Button");
	}

	public void centersLabLogin(UrlUtility urlUtility) throws InterruptedException {

		urlUtility.setUserName("Centers Lab Username Box", "Centers Lab Username");
		urlUtility.setUserName("Centers Lab Password Box", "Centers Lab Password");
		urlUtility.clickLoginButton("Centers Lab  Login Button");

	}

	public void centersLabLogin2(UrlUtility urlUtility) throws InterruptedException {

		urlUtility.setUserName("Centers Lab Username Box", "Centers Lab Username2");
		urlUtility.setUserName("Centers Lab Password Box", "Centers Lab Password2");
		urlUtility.clickLoginButton("Centers Lab  Login Button");

	}

	public void ChargeEntryLogin(UrlUtility urlUtility) throws InterruptedException {

		urlUtility.setUserName("Charge Username Box", "Charge Entry Username");
		urlUtility.setUserName("Charge Password Box", "Charge Entry Password");
		urlUtility.clickLoginButton("Charge  Login Button");
	}

	public void kindleLogin(UrlUtility urlUtility) throws InterruptedException {

		urlUtility.setUserName("Amazon Username Box", "Amazon Username");
		urlUtility.setUserName("Amazon Password Box", "Amazon Password");
		urlUtility.clickLoginButton("Amazon Login Button");
	}

	public void openUrl(String dataKey) {
		System.out.println("OPENURL METHOD" + DataReader.getEachKeyandKeyValue(dataKey));
		System.out.println(System.getProperty("user.dir"));
		driver = new ChromeDriver();
		waiting = new WebDriverWait(driver, 30);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		driver.get(DataReader.getEachKeyandKeyValue(dataKey));
	}

	public void openUrl(String dataKey, WebDriver driver) {
		System.out.println("OPENURL METHOD" + DataReader.getEachKeyandKeyValue(dataKey));
		System.out.println(System.getProperty("user.dir"));

		driver = new ChromeDriver();
//		waiting = new WebDriverWait(driver, 30);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		driver.get(DataReader.getEachKeyandKeyValue(dataKey));
		this.driver = driver;

	}

	public static WebDriver getDriver() {
		return driver;

	}

	public void setUserName(String dataSet, String dataKey) {
		write(dataSet, dataKey, useThis);
	}

	public void setPassword(String dataSet, String dataKey) {
		write(dataSet, dataKey, useThis);
	}

	public void clickLoginButton(String dataset) {
		click(dataset, useThis);
	}

}