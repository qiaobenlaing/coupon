package com.whhft.sysmanage.common.model;

public class DictJson {
	
	private String text;
	private String value;
	
	public DictJson() {
		super();
	}
	public DictJson(String text, String value) {
		super();
		this.text = text;
		this.value = value;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
