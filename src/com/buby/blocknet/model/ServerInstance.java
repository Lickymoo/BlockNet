package com.buby.blocknet.model;

import static com.buby.blocknet.util.CommonUtils.log;

import java.io.File;
import java.util.UUID;

import com.buby.blocknet.BlockNet;
import com.buby.blocknet.TemplateConfigurationProfile;
import com.buby.blocknet.util.CommonUtils.FileUtil;

import lombok.Getter;
import lombok.Setter;

public class ServerInstance {
	@Getter @Setter private UUID instanceID;
	@Getter private int port;
	@Getter private TemplateConfigurationProfile configProfile;
	@Getter private Process process;
	
	public ServerInstance(String template) {
		if(!BlockNet.blockNetCore.getServerTemplates().contains(template)) {
			log("Could not find template \"" + template + "\". Aborting..");
			return;
		}
		
		this.instanceID = UUID.randomUUID();
		this.port = BlockNet.blockNetCore.getAvailablePort();
		this.configProfile = TemplateConfigurationProfile.getConfig(BlockNet.BLOCK_NET_CORE_DIR + "/" + template + "/template-config.json", "template-config.json", TemplateConfigurationProfile.class);
		
		log("Copying server files...");
		File serverFile = FileUtil.copyFolder(
				FileUtil.getOrMkdirs(BlockNet.BLOCK_NET_CORE_DIR + "/" + template), 
				FileUtil.getOrMkdirs(BlockNet.BLOCK_NET_CORE_DIR + "/_temp/" + instanceID));
		FileUtil.unlockFiles(serverFile);
		
		log("Created new server instance");
		log("    Instance ID: " + instanceID);
		log("    Port: " + port);
		log("    Current Weight: " + (BlockNet.blockNetCore.getCurrentWeight()+this.getConfigProfile().getWeight()) + "/" + BlockNet.configProfile.getMaxWeight());
		
		FileUtil.saveJSONtoFile("{\"apiport\":"+BlockNet.configProfile.getApiPort()+","+"\"id\":\""+instanceID+"\"}", FileUtil.getOrMkdirs(serverFile.getAbsolutePath() + "/_BlockNetTemp.bn"));
		
		//Inject BlockNetServlet
		FileUtil.getOrMkdirs(serverFile.getAbsolutePath() + "/plugins");
		FileUtil.copyFolder(FileUtil.getResourceAsFile("BlockNetServlet.jar"), FileUtil.getOrMkdirs(serverFile.getAbsolutePath() + "/plugins/BlockNetServlet.jar"));
		
		File jar = null;
		
		for(String nestedPath : FileUtil.getOrMkdirs(BlockNet.BLOCK_NET_CORE_DIR + "/_temp/" + instanceID).list()) {
			if(nestedPath.contains(".jar"))
				jar = FileUtil.getOrMkdirs(BlockNet.BLOCK_NET_CORE_DIR + "/_temp/" + instanceID + "/" + nestedPath);
		}
		
		class ServerRunnable implements Runnable {

			File jar = null;
			public ServerRunnable(File jar) {
				this.jar = jar;
			}

			@Override
			public void run() {
				try {   
					String cmd = "java -Dmyprocessname="+instanceID+" -Dcom.mojang.eula.agree=true -DIReallyKnowWhatIAmDoingISwear=true -Xmx"+configProfile.getXmx()+" -Xms"+configProfile.getXms()+" -jar " + jar.getAbsolutePath() + " --nogui --port=" + port;
					ProcessBuilder builder = new ProcessBuilder(cmd.split(" "));
					builder.directory(serverFile);
		        	process = builder.start();
				}catch(Exception e) {
					e.printStackTrace();
				}
			}		
		}
		
		try {
			Thread thread = new Thread(BlockNet.threadGroup, new ServerRunnable(jar), instanceID.toString());
			thread.run();
			
		}catch(Exception e) {
			if(e.getMessage() == null) return;	
			log("Error starting server. Log file created: /logs/" + instanceID + ".txt");
			FileUtil.saveJSONtoFile(e.getMessage(), FileUtil.getOrMkdirs(BlockNet.BLOCK_NET_CORE_DIR + "/logs/" + instanceID + ".txt"));
		}
		BlockNet.blockNetCore.registerNewServerInstance(this);
	}
	
	public void disable() {
		if(process != null)
			process.destroy();
	}
}




















