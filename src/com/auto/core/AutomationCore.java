package com.auto.core;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.auto.Supports.DataReader;
import com.auto.Supports.Excel;
import com.auto.Supports.WrapperClass;
import com.auto.objects.CentersLabEmsw;
import com.auto.objects.ExcelReadWrite;
import com.auto.objects.HealthFirst;
import com.auto.objects.ScreenCapture;
import com.auto.objects.TagUpdater;
import com.auto.objects.TagUpdaterWIthServiceID;
import com.auto.objects.UrlUtility;
import com.auto.objects.WriteOffAndClose;

public class AutomationCore extends WrapperClass {

	public static HealthFirst healthFirstObject;

	public static CentersLabEmsw centersLab;

	public static TagUpdater tagUpdater;

	public static TagUpdaterWIthServiceID tagUpdaterWithServiceID;

	public static WriteOffAndClose writeOffAndClose;

	public static ScreenCapture screenCapture;

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

//	@Test
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

//	@Test
	public void CentersLab() throws IOException, InterruptedException {

		try {
			UrlUtility urlUtility = new UrlUtility("Centers Lab");
			urlUtility.openUrl("Centers Lab URL");
//			waiting.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(
//					"//div[text()='Welcome to Centers Lab NJ LLC d/b/a MedLabs Diagnostics. You need to logon to continue']"))));

			urlUtility.centersLabLogin(urlUtility);
			centersLab = new CentersLabEmsw();
			centersLab.notesUpdate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

//	@Test
	public void tagUpdater() throws IOException, InterruptedException {

		try {
			UrlUtility urlUtility = new UrlUtility("Centers Lab");
			urlUtility.openUrl("Centers Lab URL");
//			waiting.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(
//					"//div[text()='Welcome to Centers Lab NJ LLC d/b/a MedLabs Diagnostics. You need to logon to continue']"))));

			waiting.until(ExpectedConditions.elementToBeClickable(driver.findElement(
					By.xpath("//div[text()='Welcome to Shiny Triangle LCSW P.C. You need to logon to continue']"))));
			urlUtility.centersLabLogin(urlUtility);
			tagUpdater = new TagUpdater();
			tagUpdater.updateTag();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public void setDriver(WebDriver driver) {
		this.driver = UrlUtility.getDriver();
	}

//	@Test
	public void tagUpdaterWithServiceID() throws IOException, InterruptedException {

		try {
			UrlUtility urlUtility = new UrlUtility("Centers Lab");
			urlUtility.openUrl("Centers Lab URL");
//			waiting.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(
//					"//div[text()='Welcome to Centers Lab NJ LLC d/b/a MedLabs Diagnostics. You need to logon to continue']"))));

			waiting.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(
					"//div[text()='Welcome to Centers Lab NJ LLC d/b/a MedLabs Diagnostics. You need to logon to continue']"))));
			urlUtility.centersLabLogin(urlUtility);
			tagUpdaterWithServiceID = new TagUpdaterWIthServiceID();
			tagUpdaterWithServiceID.updateTag();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

//	@Test
	public void manualWriteOff() throws IOException, InterruptedException {

		try {
			UrlUtility urlUtility = new UrlUtility("Centers Lab");
			urlUtility.openUrl("Centers Lab URL");
//			waiting.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(
//					"//div[text()='Welcome to Centers Lab NJ LLC d/b/a MedLabs Diagnostics. You need to logon to continue']"))));

			waiting.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(
					"//div[text()='Welcome to Centers Lab NJ LLC d/b/a MedLabs Diagnostics. You need to logon to continue']"))));
			urlUtility.centersLabLogin(urlUtility);
			writeOffAndClose = new WriteOffAndClose();
			writeOffAndClose.ManualWriteOff();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	@Test
	public void manualWriteOffBasedOnStudy() throws IOException, InterruptedException {

		try {
			UrlUtility urlUtility = new UrlUtility("Centers Lab");
			urlUtility.openUrl("Centers Lab URL");
//			waiting.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(
//					"//div[text()='Welcome to Centers Lab NJ LLC d/b/a MedLabs Diagnostics. You need to logon to continue']"))));

			waiting.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(
					"//div[text()='Welcome to Centers Lab NJ LLC d/b/a MedLabs Diagnostics. You need to logon to continue']"))));
			urlUtility.centersLabLogin(urlUtility);
			writeOffAndClose = new WriteOffAndClose();
			writeOffAndClose.writeOffBasedOnStudy();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

//	@Test
	public void manualClose() throws IOException, InterruptedException {

		try {
			UrlUtility urlUtility = new UrlUtility("Centers Lab");
			urlUtility.openUrl("Centers Lab URL");
//			waiting.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(
//					"//div[text()='Welcome to Centers Lab NJ LLC d/b/a MedLabs Diagnostics. You need to logon to continue']"))));

			waiting.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(
					"//div[text()='Welcome to Centers Lab NJ LLC d/b/a MedLabs Diagnostics. You need to logon to continue']"))));
			urlUtility.centersLabLogin(urlUtility);
			writeOffAndClose = new WriteOffAndClose();
			writeOffAndClose.ManualCloseClaim();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

//	@Test
	public void manualWriteOffWithComment() {
		try {
			UrlUtility urlUtility = new UrlUtility("Centers Lab");
			urlUtility.openUrl("Centers Lab URL");
//			waiting.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(
//					"//div[text()='Welcome to Centers Lab NJ LLC d/b/a MedLabs Diagnostics. You need to logon to continue']"))));

			waiting.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(
					"//div[text()='Welcome to Centers Lab NJ LLC d/b/a MedLabs Diagnostics. You need to logon to continue']"))));
			urlUtility.centersLabLogin(urlUtility);
			writeOffAndClose = new WriteOffAndClose();
			writeOffAndClose.updateNotesAndWriteOff();

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

//	@Test
	public void screenCapturing() throws IOException, InterruptedException {

		try {
			UrlUtility urlUtility = new UrlUtility("Amazon");
			urlUtility.openUrl("Amazon URL");
			urlUtility.kindleLogin(urlUtility);
			screenCapture = new ScreenCapture();
			screenCapture.screenCapturingBook();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

//	@Test
	public void DxAddAndRemoveTag() throws IOException, InterruptedException {

		try {
			UrlUtility urlUtility = new UrlUtility("Centers Lab");
			urlUtility.openUrl("Centers Lab URL");
//			waiting.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(
//					"//div[text()='Welcome to Centers Lab NJ LLC d/b/a MedLabs Diagnostics. You need to logon to continue']"))));

			waiting.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(
					"//div[text()='Welcome to Centers Lab NJ LLC d/b/a MedLabs Diagnostics. You need to logon to continue']"))));
			urlUtility.centersLabLogin(urlUtility);
			tagUpdater = new TagUpdater();
			tagUpdater.diagnosisAddAndRemoveTags();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

//	@Test
	public void collectiveTagUpdate() throws IOException, InterruptedException {

		try {

			UrlUtility urlUtility = new UrlUtility("Centers Lab");
			urlUtility.openUrl("Centers Lab URL", driver);
			tagUpdater = new TagUpdater();
			tagUpdater.emosowLoaderWait();

//			waiting.until(ExpectedConditions.elementToBeClickable(WrapperClass.driver.findElement(By.xpath(
//					"//div[text()='Welcome to Centers Lab NJ LLC d/b/a MedLabs Diagnostics. You need to logon to continue']"))));
			urlUtility.centersLabLogin(urlUtility);
//			tagUpdater.emsowLoggingInWait();

			tagUpdater.collectiveTagUpdate();

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

//	@Test
	public void collectiveTagUpdate2() throws IOException, InterruptedException {

		try {
			UrlUtility urlUtility = new UrlUtility("Centers Lab");

			urlUtility.openUrl("Centers Lab URL", driver);
			tagUpdater = new TagUpdater();
			tagUpdater.emosowLoaderWait();
//			waiting.until(ExpectedConditions.elementToBeClickable(WrapperClass.driver.findElement(By.xpath(
//					"//div[text()='Welcome to Centers Lab NJ LLC d/b/a MedLabs Diagnostics. You need to logon to continue']"))));
			urlUtility.centersLabLogin2(urlUtility);
//			tagUpdater.emsowLoggingInWait();

			tagUpdater.collectiveTagUpdate();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

//	@Test
	public void addCptAndDx() {
		try {
			UrlUtility urlUtility = new UrlUtility("Centers Lab");
			tagUpdater = new TagUpdater();
			urlUtility.openUrl("Centers Lab URL");
			tagUpdater.emosowLoaderWait();
			waiting.until(ExpectedConditions.elementToBeClickable(WrapperClass.driver.findElement(By.xpath(
					"//div[text()='Welcome to Centers Lab NJ LLC d/b/a MedLabs Diagnostics. You need to logon to continue']"))));
			urlUtility.centersLabLogin(urlUtility);
//			tagUpdater.emsowLoggingInWait();

			tagUpdater.addCPtAndDx();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

//	@Test
	public void HoldClaim() {
		try {
			UrlUtility urlUtility = new UrlUtility("Centers Lab");
			tagUpdater = new TagUpdater();
			urlUtility.openUrl("Centers Lab URL");
			tagUpdater.emosowLoaderWait();
			waiting.until(ExpectedConditions.elementToBeClickable(WrapperClass.driver.findElement(By.xpath(
					"//div[text()='Welcome to Centers Lab NJ LLC d/b/a MedLabs Diagnostics. You need to logon to continue']"))));
			urlUtility.centersLabLogin(urlUtility);
//			tagUpdater.emsowLoggingInWait();

			tagUpdater.holdClaim();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	@AfterTest
	public void closeBrowers() throws IOException {

		driver.quit();
	}

}
