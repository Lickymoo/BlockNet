package com.buby.blocknet.core.master;

import javax.servlet.http.HttpServletResponse;

import com.buby.blocknet.BlockNet;
import com.buby.blocknet.core.RestApi;
import com.buby.blocknet.core.master.model.Slave;

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
	}

}
