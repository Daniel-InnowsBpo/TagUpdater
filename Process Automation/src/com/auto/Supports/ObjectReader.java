package com.auto.Supports;

public class ObjectReader {

	private String methodName;
	private String dataset;
	private String attribute;
	private String attributeValue;
	private String mdataSet;
	private String mAttribute;
	private String mAttributeValue;
	
	
	public ObjectReader(String methodName, String dataset, String attribute, String attributeValue) {
		this.methodName = methodName;
		this.dataset =dataset;
		this.attribute = attribute;
		this.attributeValue=attributeValue;
		
	}
	
	public ObjectReader(String dataset, String attribute, String attributeValue){
		this.mdataSet =dataset;
		this.mAttribute = attribute;
		this.mAttributeValue=attributeValue;
	}
	
	
	
	public void setmethodName(String methodName)  {
		 this.methodName = methodName;
	}
	
	public String getmethodName(){
		return this.methodName;
	}
	
	public void setdataSet(String dataset)  {
		 this.dataset = dataset;
	}
	
	public String getdataSet(){
		return this.dataset;
	}
	
	public void setattribute(String attribute)  {
		 this.attribute = attribute;
	}
	
	public String getattribute(){
		return this.attribute;
	}
	
	public void setattributeName(String attributeValue)  {
		 this.attributeValue = attributeValue;
	}
	
	public String getattributeName(){
		return this.attributeValue;
	}
	
	public void setmdataSet(String dataset)  {
		 this.mdataSet = dataset;
	}
	
	public String getmdataSet(){
		return this.mdataSet;
	}
	
	public void setmattribute(String attribute)  {
		 this.mAttribute = attribute;
	}
	
	public String getmattribute(){
		return this.mAttribute;
	}
	
	public void setmattributeName(String attributeValue)  {
		 this.mAttributeValue = attributeValue;
	}
	
	public String getmattributeName(){
		return this.mAttributeValue;
	}
	
	
	@Override
	public String toString() {
		return "ObjectReader [methodName=" + methodName + ", dataset=" + dataset + ", attribute=" + attribute
				+ ", attributeValue=" + attributeValue + ", mdataSet=" + mdataSet + ", mAttribute=" + mAttribute
				+ ", mAttributeValue=" + mAttributeValue + "]";
	}
	
	
}
