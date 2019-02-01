package com.jdxx.weixin.menu;

import org.nutz.weixin.bean.WxMenu;

public class Button extends WxMenu {
	
	private String name;
	
	public Button(){}
	
	public Button(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
