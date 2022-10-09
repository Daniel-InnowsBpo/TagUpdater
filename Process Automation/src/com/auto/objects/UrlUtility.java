package com.auto.objects;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.auto.Supports.DataReader;
import com.auto.Supports.WrapperClass;

public class UrlUtility extends WrapperClass {

	By useThis;

	public UrlUtility(String dataSet) {

		DataReader.getData(dataSet);
		DataReader.getDataKeyandValue(dataSet);
	}

	public void healthFirstLogin(UrlUtility urlUtility) throws InterruptedException {

		urlUtility.setUserName("Health First Username Box", "Health First Username");
		urlUtility.setUserName("Health First Password Box", "Health First Password");
		urlUtility.clickLoginButton("Health First Login Button");
	}

	public void caparioLogin(UrlUtility urlUtility) throws InterruptedException {

		urlUtility.setUserName("Capario Username Box", "Capario Username");
		urlUtility.setUserName("Capario Password Box", "Capario Password");
		urlUtility.clickLoginButton("Capario Login Button");
	}

	public void ChargeEntryLogin(UrlUtility urlUtility) throws InterruptedException {

		urlUtility.setUserName("Charge Username Box", "Charge Entry Username");
		urlUtility.setUserName("Charge Password Box", "Charge Entry Password");
		urlUtility.clickLoginButton("Charge  Login Button");
	}

	public void openUrl(String dataKey) {
		System.out.println("OPENURL METHOD" + DataReader.getEachKeyandKeyValue(dataKey));
		System.out.println(System.getProperty("user.dir"));
		driver = new ChromeDriver();
		waiting = new WebDriverWait(driver, 10);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.get(DataReader.getEachKeyandKeyValue(dataKey));
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