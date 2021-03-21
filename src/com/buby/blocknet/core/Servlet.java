package com.buby.blocknet.core;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.buby.blocknet.util.model.HeaderModel;

import lombok.Getter;

public abstract class Servlet {
	@Getter private String ip;
	@Getter private int port;
	
	public Servlet(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}
	
	public int post(String page, HeaderModel... headers) {
		try {
			CloseableHttpClient client = HttpClients.createDefault();
			
			HttpPost post = new HttpPost("http://" + ip + ":" + port + "/" + page);
			for(HeaderModel header : headers) {
				post.addHeader(header.getName(), header.getValue());
			}
			CloseableHttpResponse response = client.execute(post);
			client.close();
			return response.getStatusLine().getStatusCode();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return 404;
	}
	
	public CloseableHttpResponse postComplex(String page, HeaderModel... headers) {
		try {
			CloseableHttpClient client = HttpClients.createDefault();
			
			HttpPost post = new HttpPost("http://" + ip + ":" + port + "/" + page);
			for(HeaderModel header : headers) {
				post.addHeader(header.getName(), header.getValue());
			}
			CloseableHttpResponse response = client.execute(post);
			client.close();
			return response;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

























