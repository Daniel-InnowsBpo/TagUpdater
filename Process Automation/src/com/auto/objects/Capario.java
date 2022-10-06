package com.auto.objects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.server.handler.SwitchToFrame;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.auto.Supports.DataReader;
import com.auto.Supports.Excel;
import com.auto.Supports.WrapperClass;

public class Capario extends WrapperClass {

	private By useThis;
	List<String> traceIDsfromExcel = new ArrayList<String>();
	Actions mouseAction = new Actions(driver);
	boolean flag = false;
	
	
	public Capario() {
		DataReader.getData("Caprio");
		DataReader.getDataKeyandValue("Caprio");
	}

	public void ClearCaprioRecords() {

		traceIDsfromExcel = ExcelReadWrite.extractCommunityUrgentCareTraceIDFromExcel();

		waiting.until(ExpectedConditions
				.elementToBeClickable(driver.findElement(By.xpath("//div[text()='Revenue Performance']"))));
		String maintWindow = driver.getWindowHandle();

		for (String singleID : traceIDsfromExcel) {

			if (flag) {
				driver.switchTo().defaultContent();
			}
			mouseAction.moveToElement(driver.findElement(By.xpath("//span[text()='Rejections']/parent::a"))).build()
					.perform();
			click("Capario Find All Claims", useThis);

			frameSwitch("appFrame");

			click("Capario TraceID Box", useThis);
			writeHere("Capario TraceID Box", singleID, useThis);

			click("Capario Search Button", useThis);

			String status = gettext("Capario Status");

			if (status.isEmpty()) {

				driver.findElement(By.xpath("//a[text()='" + singleID + "']")).click();
				driver.switchTo().defaultContent();
				windowSwitch("By Title", "Claim Details");

				selectThis("Capario Select Status", "Closed", "By Visibletext", useThis);

				click("Capario update Button", useThis);

				frameSwitch("cs_frame");
				String statusMessage = gettext("Capario Status Message");
				if ((statusMessage.trim()).equalsIgnoreCase("Client Status updated")) {
					driver.switchTo().defaultContent();
					driver.close();
					driver.switchTo().window(maintWindow);
					frameSwitch("appFrame");
					click("Capario Search Button", useThis);
					String finalStatus = gettext("Capario Status");
					if ((finalStatus.trim()).equalsIgnoreCase("Closed")) {
						System.out.println(singleID + "----" + "Closed");
						flag = true;
					}
				}

				click("Capario Clear Button", useThis);
			} else {
				System.out.println(singleID + "--->Record is already having some final Status hence not closing");
				flag = true;
			}

		}

	}
}
