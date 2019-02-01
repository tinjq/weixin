package com.jdxx.weixin.menu;

public class Menu {

	private Button[] button;
	
	public Menu(){}
	
	public Menu(Button[] button){
		this.button = button;
	}

	public Button[] getButton() {
		return button;
	}

	public void setButton(Button[] button) {
		this.button = button;
	}

}
