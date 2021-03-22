package com.buby.blocknet;

import static com.buby.blocknet.util.CommonUtils.log;

import com.buby.blocknet.core.BlockNetCore;
import com.buby.blocknet.core.master.BlockNetMaster;
import com.buby.blocknet.core.slave.BlockNetSlave;

public class BlockNet {

	public static final String BLOCK_NET_CORE_DIR = ".";
	public static final String VERSION = "0.0.1";
	
	public static BlockNetCore blockNetCore;
	public static BlockConfigurationProfile configProfile;
	public static ThreadGroup threadGroup;
	public static boolean enabled = true;
	
	public static void main(String args[]) {
		configProfile = BlockConfigurationProfile.getConfig("blocknet-config.json", "blocknet-config.json", BlockConfigurationProfile.class);
		blockNetCore = configProfile.isMaster() ? new BlockNetMaster() : new BlockNetSlave();
		if(!configProfile.getVersion().equals(VERSION))
			log("Your config is out of date");
		threadGroup = new ThreadGroup("BlockNet");   
		if(!enabled) return;
		blockNetCore.getRestApi().body();
		blockNetCore.commandPrompt();
	}
	
}
