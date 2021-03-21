package com.buby.blocknet.util.model;

import lombok.Getter;

public class HeaderModel {
	@Getter private String name;
	@Getter private String value;
	
	public HeaderModel(String name, String value) {
		this.name = name;
		this.value = value;
	}
}
