package com.auto.Supports;

public class Result {
	
	private String result;
	private String resultText;
	private String screen;
	private String errorText;
	
	public Result(String result,String resultText, String screen) {
		this.result = result;
		this.resultText = resultText;
		this.screen=screen;
		
	}
	
	public Result(String result, String errorText){
		this.result = result;
		this.errorText=errorText;
	
	}
	
	public void setErrorText(String errorText)  {
		 this.errorText = errorText;
	}
	
	public String getErrorText(){
		return this.errorText;
	}
	
	public void setResult(String result) {
		this.result = result;
	}
	
	public String getResult() {
		return this.result;
	}
	
	public void setResultText(String resultText) {
		this.resultText = resultText;
	}
	
	public String getResultText() {
		return this.resultText;
	}	
	
	public void Screen(String screen){
		this.screen=screen;
	}
	
	public String getScreenShot() {
		return this.screen;
	}	
}