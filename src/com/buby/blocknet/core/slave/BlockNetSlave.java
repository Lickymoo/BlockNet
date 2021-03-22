package com.buby.blocknet.core.slave;

import static com.buby.blocknet.util.CommonUtils.log;

import javax.servlet.http.HttpServletResponse;

import com.buby.blocknet.BlockNet;
import com.buby.blocknet.core.BlockNetCore;
import com.buby.blocknet.core.slave.model.Master;
import com.buby.blocknet.util.CommonUtils.FileUtil;
import com.buby.blocknet.util.model.HeaderModel;

public class BlockNetSlave extends BlockNetCore{
	
	public static int maxAttempts = 10;
	
	public BlockNetSlave() {
		this.commandProcessor = new SlaveCommandProcessor();
		FileUtil.deleteFile(BlockNet.BLOCK_NET_CORE_DIR + "/_temp");
		long deployTime = System.currentTimeMillis();

		log("______ _            _    _   _      _   ");
		log("| ___ \\ |          | |  | \\ | |    | |  ");
		log("| |_/ / | ___   ___| | _|  \\| | ___| |_ ");
		log("| ___ \\ |/ _ \\ / __| |/ / . ` |/ _ \\ __|");
		log("| |_/ / | (_) | (__|   <| |\\  |  __/ |_ ");
		log("\\____/|_|\\___/ \\___|_|\\_\\_| \\_/\\___|\\__|");
		log("BlockNet v0.0.1 - By Lickymoo/Buby");
		log("https://github.com/Lickymoo");
		log("   ");
		
		log("Starting BlockNet");
		log("Servlet Mode: Slave");
		log("   ");
		
		if(BlockNet.configProfile.getApiPort() == BlockNet.configProfile.getMasterApiPort()) {
			BlockNet.enabled = false;
			log("Api port and Master api port cannot be the same in slave mode. Terminating");
			this.disable();
			return;
		}
		
		log("Master IP: " + BlockNet.configProfile.getMasterIp());
		log("Master API Port: " + BlockNet.configProfile.getMasterApiPort());
		log("Advertising IP: " + BlockNet.configProfile.getAdvertisementIp());
		log("Advertising API Port: " + BlockNet.configProfile.getApiPort());
		log("   ");
		
		log("Generating _temp folder");
		FileUtil.getOrMkdirs(BlockNet.BLOCK_NET_CORE_DIR + "/_temp/", true);
		log("   ");
		
		log("Registered " + registerServerTemplates() + " template(s)");
		log("   ");
		
		log("Initialising REST API");
		this.restApi = new SlaveRestApi();
		log("   ");
		
		log("Connecting to master");
		master = new Master(BlockNet.configProfile.getMasterIp(), BlockNet.configProfile.getMasterApiPort());
		boolean connected = false;
		for(int attempts = 0; attempts < maxAttempts; attempts++) {
			if(master.post("/ping") == HttpServletResponse.SC_OK) {
				connected = true;
				break;
			}
		}
		master.post("/from_slave/register", new HeaderModel("ip", BlockNet.configProfile.getAdvertisementIp()), new HeaderModel("port", BlockNet.configProfile.getApiPort() + "") );
		if(!connected) {
			BlockNet.enabled = false;
			log("Unable to connect to master. Terminating");
			this.disable();
			return;
		}
		log("   ");
		
		log("Done! (" + ((System.currentTimeMillis() - deployTime)) + "ms)");
		log("   ");
	}
}
























