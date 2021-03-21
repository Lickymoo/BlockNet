package com.buby.blocknet.core.master;

import static com.buby.blocknet.util.CommonUtils.log;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import com.buby.blocknet.BlockNet;
import com.buby.blocknet.core.BlockNetCore;
import com.buby.blocknet.core.Servlet;
import com.buby.blocknet.core.master.model.Slave;
import com.buby.blocknet.util.CommonUtils.FileUtil;
import com.buby.blocknet.util.CommonUtils.MathUtil;
import com.buby.blocknet.util.model.HeaderModel;
import com.buby.blocknet.util.model.Pair;

import lombok.Getter;

public class BlockNetMaster extends BlockNetCore{
	
	@Getter private Set<Slave> activeSlaves = new HashSet<>();
	
	public BlockNetMaster() {
		this.commandProcessor = new MasterCommandProcessor();
		FileUtil.deleteFile(BLOCK_NET_CORE_DIR + "/_temp");
		long deployTime = System.currentTimeMillis();
		log("Starting BlockNet");
		log("Servlet Mode: Master");
		log("   ");

		log("Advertising IP: " + BlockNet.configProfile.getAdvertisementIp());
		log("Advertising API Port: " + BlockNet.configProfile.getApiPort());
		log("   ");
		
		log("Generating _temp folder");
		FileUtil.getOrMkdirs(BLOCK_NET_CORE_DIR + "/_temp/", true);
		log("   ");
		
		log("Registered " + registerServerTemplates() + " template(s)");
		log("   ");
		
		log("Initialising REST API");
		this.restApi = new MasterRestApi();
		log("   ");
		
		log("Done! (" + ((System.currentTimeMillis() - deployTime)) + "ms)");
		log("   ");
	}
	
	/* Provision a server on either master or slave
	 * Returns servlet & port of server
	 */
	public Pair<Servlet, Integer> provisionServer(String template) {
		/*Provision server on slave, or if out of bounds on master*/
		try {
			Slave[] slaveArray = activeSlaves.toArray(new Slave[0]);
			
			//No slaves, so have to resort to master
			if(slaveArray.length == 0) throw new Exception();
			Slave slave = slaveArray[MathUtil.random(0, activeSlaves.size())];
			
			if(slave.post("/ping") != HttpServletResponse.SC_OK) {
				//Slave is no longer pingable
				log("Could not connect to slave " + slave.getIp() + ":" +slave.getPort());
				log("Terminating connection, this slave will no longer be accessible");
				activeSlaves.remove(slave);
				return provisionServer(template);
			}
			if(slave.post("/from_master/has_template", new HeaderModel("template", template)) != HttpServletResponse.SC_OK) {
				return provisionServer(template);
			}
			
			int port = Integer.parseInt(slave.postComplex("/from_master/provision", new HeaderModel("template", template)).getFirstHeader("port").getValue() );
			return Pair.of(slave, port);
		}catch(Exception e) {
			return provisionServer(template);
		}
	}
	
	public void registerSlave(Slave slave) {
		activeSlaves.add(slave);
		log("Slave " + slave.getIp() + ":" + slave.getPort() + " registered.");
	}
	
	public void instanceReady(String ip, String port) {
		log("Server instance ready: " + ip + ":" + port);
	}
	
}



















