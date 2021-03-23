package com.buby.blocknet.core.master.model;

import java.util.UUID;

import com.buby.blocknet.core.Servlet;

import lombok.Getter;

public class ProvisionedServerModel {
	@Getter private Servlet servlet;
	@Getter int port;
	@Getter UUID uuid;
	
	public ProvisionedServerModel(Servlet servlet, int port, UUID uuid) {
		this.servlet = servlet;
		this.port = port;
		this .uuid = uuid;
	}
}
