package com.buby.blocknet.core.master;

import static com.buby.blocknet.util.CommonUtils.log;

import javax.servlet.http.HttpServletResponse;

import com.buby.blocknet.BlockNet;
import com.buby.blocknet.TemplateConfigurationProfile;
import com.buby.blocknet.core.RestApi;
import com.buby.blocknet.core.master.model.Slave;
import com.buby.blocknet.model.ServerInstance;
import com.buby.blocknet.util.model.HeaderModel;

public class MasterRestApi extends RestApi{

	@Override
	public void body() {
		BlockNetMaster blockNet = (BlockNetMaster)BlockNet.blockNetCore;
		
		app.post("/ping", 
				ctx -> {
					ctx.res.setStatus(HttpServletResponse.SC_OK);
				});
		
		app.post("/from_slave/register",
			ctx -> {
				String ip = ctx.header("ip");
				String port = ctx.header("port");
				blockNet.registerSlave(new Slave(ip, Integer.parseInt(port)));
			});
		
		app.post("/from_slave/akwn_ready", 
			ctx -> {
				String ip = ctx.header("ip");
				String port = ctx.header("port");
				blockNet.instanceReady(ip, port);
			});
		
		app.post("/from_master/provision", 
				ctx -> {
					String template = ctx.header("template");
					ServerInstance newInstance = new ServerInstance(template);
					log("provisioning \"" + template +"\" server");
					ctx.res.setHeader("port", newInstance.getPort() + "");
				});
				
		app.post("/from_master/can_host", 
				ctx -> {
					String template = ctx.header("template");
					if(!blockNet.getServerTemplates().contains(template)) {
						ctx.res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						return;
					}
					if(blockNet.getCurrentWeight() + TemplateConfigurationProfile.getConfig(BlockNet.BLOCK_NET_CORE_DIR + "/" + template + "/template-config.json", "template-config.json", TemplateConfigurationProfile.class).getWeight() > BlockNet.configProfile.getMaxWeight()) {
						ctx.res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						return;
					}
					ctx.res.setStatus(HttpServletResponse.SC_OK);
				});
		app.post("/servlet_to_slave/ready",
			ctx -> {
				String port = ctx.header("port");
				blockNet.getMaster().post("/from_slave/akwn_ready", new HeaderModel("ip", BlockNet.configProfile.getAdvertisementIp()), new HeaderModel("port", port));
			});
	}

}
