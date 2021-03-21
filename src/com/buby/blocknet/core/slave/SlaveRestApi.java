package com.buby.blocknet.core.slave;

import static com.buby.blocknet.util.CommonUtils.log;

import javax.servlet.http.HttpServletResponse;

import com.buby.blocknet.BlockNet;
import com.buby.blocknet.core.RestApi;
import com.buby.blocknet.model.ServerInstance;
import com.buby.blocknet.util.model.HeaderModel;

public class SlaveRestApi extends RestApi{

	@Override
	public void body() {
		BlockNetSlave blockNet = (BlockNetSlave)BlockNet.blockNetCore;
		
		app.post("/ping", 
			ctx -> {
				ctx.res.setStatus(HttpServletResponse.SC_OK);
			});
		
		app.post("/from_master/provision", 
			ctx -> {
				String template = ctx.header("template");
				ServerInstance newInstance = new ServerInstance(template);
				log("provisioning \"" + template +"\" server");
				ctx.res.setHeader("port", newInstance.getPort() + "");
			});
			
		app.post("/from_master/has_template", 
			ctx -> {
				String template = ctx.header("template");
				ctx.res.setStatus(blockNet.getServerTemplates().contains(template) ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);
			});
		app.post("/servlet_to_slave/ready",
			ctx -> {
				String port = ctx.header("port");
				blockNet.getMaster().post("/from_slave/akwn_ready", new HeaderModel("ip", BlockNet.configProfile.getAdvertisementIp()), new HeaderModel("port", port));
			});
	}

}
