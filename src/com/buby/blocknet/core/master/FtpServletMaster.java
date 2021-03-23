package com.buby.blocknet.core.master;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.FtpReply;
import org.apache.ftpserver.ftplet.FtpRequest;
import org.apache.ftpserver.ftplet.FtpSession;
import org.apache.ftpserver.ftplet.Ftplet;
import org.apache.ftpserver.ftplet.FtpletContext;
import org.apache.ftpserver.ftplet.FtpletResult;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PasswordEncryptor;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;

import com.buby.blocknet.BlockNet;
import com.buby.blocknet.core.BlockNetCore;
import com.buby.blocknet.core.FtpServlet;
import com.buby.blocknet.util.CommonUtils.FileUtil;

public class FtpServletMaster extends FtpServlet {

	public FtpServletMaster(BlockNetCore bNetCore) {
		super(bNetCore);

		FtpServerFactory serverFactory = new FtpServerFactory();
		ListenerFactory factory = new ListenerFactory();
		
		factory.setPort(BlockNet.configProfile.getFtpPort());
		
		serverFactory.addListener("default", factory.createListener());
		
		PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
		
		userManagerFactory.setFile(FileUtil.getResourceAsFile("ftp.properties"));
		userManagerFactory.setPasswordEncryptor(new PasswordEncryptor(){

		        @Override
		        public String encrypt(String password) {
		            return password;
		        }

		        @Override
		        public boolean matches(String passwordToCheck, String storedPassword) {
		            return passwordToCheck.equals(storedPassword);
		        }
		    });
		
		    BaseUser user = new BaseUser();
		    
		    user.setName("BNETCORE");
		    user.setPassword("BNETCORE");
		    
		    user.setHomeDirectory(FileUtil.getOrMkdirs(BlockNet.BLOCK_NET_CORE_DIR).getAbsolutePath());
		    List<Authority> authorities = new ArrayList<Authority>();
		    authorities.add(new WritePermission());
		    user.setAuthorities(authorities);
		    UserManager um = userManagerFactory.createUserManager();
		    try{
		        um.save(user);
		    }
		    catch (FtpException ex){
		    	ex.printStackTrace();
		    }
		    serverFactory.setUserManager(um);
		    Map<String, Ftplet> m = new HashMap<String, Ftplet>();
		    m.put("miaFtplet", new Ftplet(){

		        @Override
		        public void init(FtpletContext ftpletContext) throws FtpException {
		        }

		        @Override
		        public void destroy() {
		        }

		        @Override
		        public FtpletResult beforeCommand(FtpSession session, FtpRequest request) throws FtpException, IOException
		        {
		            return FtpletResult.DEFAULT;
		        }

		        @Override
		        public FtpletResult afterCommand(FtpSession session, FtpRequest request, FtpReply reply) throws FtpException, IOException
		        {
		            return FtpletResult.DEFAULT;
		        }

		        @Override
		        public FtpletResult onConnect(FtpSession session) throws FtpException, IOException
		        {
		            return FtpletResult.DEFAULT;
		        }

		        @Override
		        public FtpletResult onDisconnect(FtpSession session) throws FtpException, IOException
		        {
		            return FtpletResult.DEFAULT;
		        }
		    });
		    serverFactory.setFtplets(m);
		    FtpServer server = serverFactory.createServer();
		    try{
		        server.start();
		    }catch (FtpException ex){
		        ex.printStackTrace();
		    }
	}

}
