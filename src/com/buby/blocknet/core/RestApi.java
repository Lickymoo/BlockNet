package com.buby.blocknet.core;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.slf4j.LoggerFactory;

import com.buby.blocknet.BlockNet;
import com.buby.blocknet.util.model.HeaderModel;

import io.javalin.Javalin;

public abstract class RestApi {
	protected Javalin app = null;
	
	public RestApi() {
		Javalin.log = LoggerFactory.getLogger(BlockNet.class);

		app = Javalin.create().start(BlockNet.configProfile.getApiPort());
	}
	
	public abstract void body();
	
	public void disable() {
		
		app.stop();
	}
	
	public int post(Servlet servlet, String page, HeaderModel... headers) {
		return servlet.post(page, headers);
	}
	
	public CloseableHttpResponse postComplex(Servlet servlet, String page, HeaderModel... headers) {
		return servlet.postComplex(page, headers);	
	}
}
