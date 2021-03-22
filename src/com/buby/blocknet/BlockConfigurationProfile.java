package com.buby.blocknet;

import com.buby.blocknet.util.model.MinMaxPair;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BlockConfigurationProfile  extends ConfigurationProfile{
	@Getter private String version;
	@Getter private MinMaxPair portRange = MinMaxPair.of(25566, 30000);
	@Getter private int masterApiPort = 8888;
	@Getter private int apiPort = 8888;
	@Getter private String advertisementIp = "localhost";
	@Getter private String masterIp = "localhost";
	@Getter private boolean master = true;
	@Getter private int maxWeight = 20;
	
}
