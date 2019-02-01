package com.jdxx.weixin.menu;

public class ClickButton extends Button {

	private String type;
	
	private String key;

	public ClickButton(){}
	
	public ClickButton(String type, String name, String key){
		super(name);
		this.type = type;
		this.key = key;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
}
