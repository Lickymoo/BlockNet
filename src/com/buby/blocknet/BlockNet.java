package com.buby.blocknet;

import com.buby.blocknet.core.BlockNetCore;
import com.buby.blocknet.core.master.BlockNetMaster;
import com.buby.blocknet.core.slave.BlockNetSlave;

public class BlockNet {
	
	public static BlockNetCore blockNetCore;
	public static ConfigurationProfile configProfile;
	
	public static boolean enabled = true;
	
	public static void main(String args[]) {
		configProfile = ConfigurationProfile.getConfigruationProfile();
		blockNetCore = configProfile.isMaster() ? new BlockNetMaster() : new BlockNetSlave();
		
		if(!enabled) return;
		blockNetCore.getRestApi().body();
		blockNetCore.commandPrompt();
	}
	
}
