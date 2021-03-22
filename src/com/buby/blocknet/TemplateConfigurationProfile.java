package com.buby.blocknet;

import lombok.Getter;

public class TemplateConfigurationProfile extends ConfigurationProfile{
	@Getter private String Xmx = "1G";
	@Getter private String Xms = "1G";
	@Getter private int weight = 1;
}
