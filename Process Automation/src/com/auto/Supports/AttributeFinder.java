package com.auto.Supports;

import org.openqa.selenium.By;

public class AttributeFinder extends WrapperClass {
	

	public static By attributefinder(By elementToFind, String attribute, String attributeValue){

		if( attribute.equalsIgnoreCase("id")){
			waitForNow(attribute, attributeValue);
			elementToFind=By.id(attributeValue);
		}
		if(attribute.equalsIgnoreCase("Name")){
			waitForNow(attribute, attributeValue);
			elementToFind=By.name(attributeValue);
		}
		if( attribute.equalsIgnoreCase("xpath")){
			waitForNow(attribute, attributeValue);
			elementToFind=By.xpath(attributeValue);
		}
		if(attribute.equalsIgnoreCase("css Selector")){
			waitForNow(attribute, attributeValue);
			elementToFind=By.cssSelector(attributeValue);
		}
		if( attribute.equalsIgnoreCase("class name")){
			waitForNow(attribute, attributeValue);
			elementToFind=By.className(attributeValue);
		}
		if(attribute.equalsIgnoreCase("Link Text")){
			waitForNow(attribute, attributeValue);
			elementToFind=By.linkText(attributeValue);
		}
		if( attribute.equalsIgnoreCase("Partial Link Text")){
			waitForNow(attribute, attributeValue);
			elementToFind=By.partialLinkText(attributeValue);
		}
		if(attribute.equalsIgnoreCase("Tag Name")){
			waitForNow(attribute, attributeValue);
			elementToFind=By.tagName(attributeValue);
		}
		else {
			System.out.println("Please provide valid selectors");
		}
		return elementToFind;
	
	}
	
}
