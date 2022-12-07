package com.auto.objects;

import java.util.List;

import org.openqa.selenium.WebElement;

import com.auto.Supports.DataReader;
import com.auto.Supports.WrapperClass;

public class ScreenCapture extends WrapperClass {
	public ScreenCapture() {
		DataReader.getData("Amazon");
		DataReader.getDataKeyandValue("Amazon");
	}

	public void screenCapturingBook() {
		loaderWait("//*[@class='loader-item']");
		List<WebElement> allBooks = findElements("Amazon List Of Books");
		for (WebElement eachBook : allBooks) {
			eachBook.click();
			loaderWait("//*[@class='loader-item']");
			gotoFirstPage();
			takeScreenShotofEachPage();
		}

	}

	private void gotoFirstPage() {
		List<WebElement> previousPageButton = findElements("Amazon Previous Page");
		while (!(previousPageButton.isEmpty())) {
			previousPageButton = findElements("Amazon Previous Page");
			if (previousPageButton.isEmpty()) {
				break;
			} else {
				scrollToElement(previousPageButton.get(0));
				previousPageButton.get(0).click();
				loaderWait("//*[@class='loader-item']");
			}

		}
	}

	private void takeScreenShotofEachPage() {
		int i = 1;
		List<WebElement> nextPageButton = findElements("Amazon Next Page");
		while (!(nextPageButton.isEmpty())) {

			nextPageButton = findElements("Amazon Next Page");
			if (nextPageButton.isEmpty()) {
				break;
			} else {
				scrollToElement(nextPageButton.get(0));
				screenshot("Page-" + i, "jpg", "Book");
				scrollToElement(nextPageButton.get(0));
				nextPageButton.get(0).click();
				loaderWait("//*[@class='loader-item']");
				i++;
			}

		}
	}
}
