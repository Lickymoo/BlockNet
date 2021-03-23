package com.buby.blocknet.core.slave;

import java.io.IOException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import com.buby.blocknet.BlockNet;
import com.buby.blocknet.core.BlockNetCore;
import com.buby.blocknet.core.FtpServlet;
import com.buby.blocknet.util.CommonUtils.FileUtil;
import com.google.gson.Gson;

public class FtpServletSlave extends FtpServlet{

    String server;
    int port;
    String user = "BNETCORE";
    String pass = "BNETCORE";
    
	public FtpServletSlave(BlockNetCore bNetCore) {
		super(bNetCore);
		this.server = BlockNet.configProfile.getMasterIp();
		this.port = BlockNet.configProfile.getFtpPort();
	}
	
	public void downloadTemplates() {
		FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            Gson gson = new Gson();
        	String[] templates = gson.fromJson( bNetCore.getRestApi().postComplex(bNetCore.getMaster(), "/from_slave/req_templates_list").getFirstHeader("templates").getValue(), String[].class);
            for(String template : templates) {
                downloadDirectory(ftpClient, "/" + template, "", FileUtil.getOrMkdirs(BlockNet.BLOCK_NET_CORE_DIR).getAbsolutePath() + "/");
            }
        }catch(Exception ex) {
        	ex.printStackTrace();
        }finally {
        	try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
	}
}




















