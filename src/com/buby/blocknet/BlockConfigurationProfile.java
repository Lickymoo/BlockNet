package com.buby.blocknet;

import com.buby.blocknet.util.model.MinMaxPair;

import lombok.Getter;

public class BlockConfigurationProfile  extends ConfigurationProfile{
	@Getter private String version;
	@Getter private MinMaxPair portRange;
	@Getter private int masterApiPort;
	@Getter private int apiPort;
	@Getter private int ftpPort;
	@Getter private String advertisementIp;
	@Getter private String masterIp;
	@Getter private boolean master;
	@Getter private int maxWeight;
	
}
