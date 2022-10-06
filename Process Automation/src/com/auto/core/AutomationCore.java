package com.auto.core;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.auto.Supports.DataReader;
import com.auto.Supports.Excel;
import com.auto.Supports.WrapperClass;
import com.auto.objects.ExcelReadWrite;
import com.auto.objects.HealthFirst;
import com.auto.objects.UrlUtility;

public class AutomationCore extends WrapperClass {

	public static HealthFirst healthFirstObject;

	public static ExcelReadWrite excelReadWrite;

	@BeforeClass
	public void openWhatsapp() throws Throwable {
		Excel.ExcelReaderInitialization();
		Excel.excelObjectExtraction();
	}

	@BeforeMethod
	public void f() {
		DataReader.mvalueinitialization();

	}

	@Test
	public void healthFirst() throws IOException, InterruptedException {

		try {
			UrlUtility urlUtility = new UrlUtility("Health First");
			urlUtility.openUrl("Health First URL");
			waiting.until(ExpectedConditions.elementToBeClickable(
					driver.findElement(By.xpath("//h2[text()='Welcome to the New Provider Portal']"))));
			urlUtility.healthFirstLogin(urlUtility);
			healthFirstObject = new HealthFirst();
			healthFirstObject.statusCheck();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@AfterTest
	public void closeBrowers() throws IOException {

		driver.quit();
	}

}
