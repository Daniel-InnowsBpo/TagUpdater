package com.auto.Supports;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Reporter {

	private static List<Result> details;
	private static List<Action> actionsDetails;
	private static List<StepReader> steps;
	private static String actionss="";
	private static int count=0;
	private static  String resultPlaceholder = "<!-- INSERT_RESULTS -->";
	private static  String templatePath = "F:\\Selenium\\Reports\\Template\\Test Result.html";

	public Reporter() {
	}

	public static void initialize() {

		details = new ArrayList<Result>();
		actionsDetails=new ArrayList<Action>();
		steps=new  ArrayList<StepReader>();
	}

	public static void report(String actualValue,String expectedValue) {

		if(actualValue.equals(expectedValue)) {
			Result r = new Result("Pass","Actual value '" + actualValue + "' matches expected value", WrapperClass.filePath);
			details.add(r);
		} else {
			Result r = new Result("Fail","Actual value '" + actualValue + "' does not match expected value '" + expectedValue + "'", WrapperClass.filePath);
			details.add(r);
		}
	}

	public static void report(String errorText) {

		Result r = new Result("Fail",errorText,WrapperClass.actions);
		details.add(r);
	}

	public static void showResults() {
		for (int i = 0;i < details.size();i++) {
			System.out.println("Result " + Integer.toString(i) + ": " + details.get(i).getResult());
		}
	}

	public static void actions(String Actions){
		Action a =new Action("<li>"+Actions+"</li>");
		actionsDetails.add(a);
	}

	public static void writeResults(){
		try {
			String testSteps="";
			System.out.println("actiondetailssize"+actionsDetails.size());
			if (count==0){
				for(int i=0; i<actionsDetails.size();i++){
					System.out.println(i);
					testSteps+=(actionsDetails.get(i).getActions());
					System.out.println(testSteps);
					count++;
					
				}
				
			}
			else{
				
				for(int i=count;i<actionsDetails.size();i++){
					System.out.println(i);
					testSteps+=(actionsDetails.get(i).getActions());
					System.out.println(testSteps+"else");
					count++;
				}
				
			}
			StepReader s = new StepReader(testSteps);
			steps.add(s);
			actionss=new String();
			String reportIn = new String(Files.readAllBytes(Paths.get(templatePath)));
			System.out.println(details.size());
			for (int i = 0; i < details.size(); i++) {

				if ((details.get(i).getErrorText()) != null) {
					reportIn = reportIn.replaceFirst(resultPlaceholder,"<tr><td>" + Integer.toString(i + 1) + "</td><td>" + actionss + "</td><td>"+ details.get(i).getResult() + "</td><td>" + details.get(i).getErrorText()+ "</td><td>"+ WrapperClass.filePath + "</td></tr>" + resultPlaceholder);
					
				} else {
					reportIn = reportIn.replaceFirst(resultPlaceholder,"<tr><td>" + Integer.toString(i + 1) + "</td><td>" + steps.get(i).getStepReader() + "</td><td>"+ details.get(i).getResult() + "</td><td>" + details.get(i).getResultText()+ "</td><td>" + WrapperClass.filePath.toString() + "</td></tr>"+ resultPlaceholder);
					System.out.println("reporst gene"+WrapperClass.filePath);
				}

			} 

			String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
			String reportPath = "F:\\Selenium\\Reports\\Report_" + timeStamp + ".html";
			Files.write(Paths.get(reportPath),reportIn.getBytes(),StandardOpenOption.CREATE);

		} catch (Exception e) {
			System.out.println("Error when writing report file:\n" + e.toString());
		}
	}
}