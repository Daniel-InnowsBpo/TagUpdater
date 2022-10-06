package com.auto.Supports;

public class DataSheetReader {

	private String dataSheeMethodName;
	private String dataSheetDataKey;
	private String dataSheetDataValue;
	private String eachMerthoddataSheetDataKey;
	private String eachMethoddataSheetDataValue;

	public DataSheetReader(String methodName, String dataKey, String dataValue){
		this.dataSheeMethodName=methodName;
		this.dataSheetDataKey = dataKey;
		this.dataSheetDataValue=dataValue;	
	}

	public DataSheetReader(String dataKey, String dataValue){
		this.eachMerthoddataSheetDataKey= dataKey;
		this.eachMethoddataSheetDataValue=dataValue;	
	}
	public void setDatakey(String dataSheetdatakey){
		this.dataSheetDataKey=dataSheetdatakey;
	}

	public void setDatakeyValue(String dataSheetDataValue){
		this.dataSheetDataValue=dataSheetDataValue;
	}

	public void setDataSheetMethodName(String dataSheeMethodName){
		this.dataSheeMethodName=dataSheeMethodName;
	}

	public void seteachMerthoddataSheetDataKey(String dataKey){
		this.eachMerthoddataSheetDataKey=dataKey;
	}

	public void seteachMethodDataSheetDataKeyValue(String dataKeyValue){
		this.eachMethoddataSheetDataValue=dataKeyValue;
	}

	public String getDatakey(){
		return this.dataSheetDataKey;
	}

	public String getDatakeyValue(){
		return this.dataSheetDataValue;
	}

	public String getDataSheetMethodName(){
		return this.dataSheeMethodName;
	}

	public String geteachMerthoddataSheetDataKey(){
		return this.eachMerthoddataSheetDataKey;
	}

	public String getseteachMethodDataSheetDataKeyValue(){
		return this.eachMethoddataSheetDataValue;
	}
}
