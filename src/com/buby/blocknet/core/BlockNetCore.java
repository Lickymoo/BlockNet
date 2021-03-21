package com.buby.blocknet.core;

import static com.buby.blocknet.util.CommonUtils.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

import com.buby.blocknet.BlockNet;
import com.buby.blocknet.command.CommandProcessor;
import com.buby.blocknet.model.ServerInstance;
import com.buby.blocknet.util.CommonUtils.FileUtil;
import com.google.common.collect.ImmutableSet;

import lombok.Getter;

public abstract class BlockNetCore {

	public static final String BLOCK_NET_CORE_DIR = "BlockNetCore";
	
	@Getter protected Set<String> serverTemplates;
	
	@Getter protected Set<ServerInstance> serverInstances = new HashSet<>();
	@Getter protected HashMap<UUID, Integer> portMappings = new HashMap<>();
	@Getter protected CommandProcessor commandProcessor;
	@Getter protected RestApi restApi;
	@Getter protected boolean headless = false;
	
	protected int registerServerTemplates() {
		final File coreDir = FileUtil.getOrMkdirs(BLOCK_NET_CORE_DIR);	
		final ImmutableSet<String> excludeFileSearch = ImmutableSet.<String>builder().add("_temp").add("blocknet-config.json").add("logs").build();
		
		Set<String> templatePaths = new HashSet<>();
		
		for(String path : coreDir.list()) {
			//Ensure server template meets requirements
			if(excludeFileSearch.contains(path) || path.contains(".")) continue;
			boolean hasJar = false;
			
			if(!FileUtil.fileExists(BLOCK_NET_CORE_DIR + "/" + path)) continue;
			
			for(String nestedPath : FileUtil.getOrMkdirs(BLOCK_NET_CORE_DIR + "/" + path).list()) {
				if(nestedPath.contains(".jar"))
					hasJar = true;
			}
			if(!hasJar) {
				log("\"" + path + "\" template does not contain a server jar, skipping.");
				continue;
			}
			templatePaths.add(path);
		}
		serverTemplates = templatePaths;
		return templatePaths.size();
	}	
	
	public void registerNewServerInstance(ServerInstance instance) {
		portMappings.put(instance.getInstanceID(), instance.getPort());
		serverInstances.add(instance);
	}
	
	public int getAvailablePort() {
		int port = BlockNet.configProfile.getPortRange().random();
		if(portMappings.containsValue(port))
			return getAvailablePort();
		return port;
	}
	
	public void commandPrompt() {

		//Scanner scanner = new Scanner(System.in);

        try {
        	//Will run forever
        	if(headless) {
        		Scanner scanner = new Scanner(System.in);
        		while(!scanner.hasNext());
        		scanner.close();
        	}
            while (true && !headless) { 
            	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            	String line = reader.readLine();
                switch(commandProcessor.processInput(line)) {
				case EXIT:
					return;
				case INVALID:
					log("Invalid command");
					break;
				case OK:
					break;
                }
            }
        } catch(Exception e) {
        	log("Scanner running in headless mode");
        	headless = true;
        	commandPrompt();
        	return;
        } finally {
        	disable();
        }
	}
	
	public void disable() {
		log("Killing all processes (" + serverInstances.size() + ")");
		
		for(ServerInstance inst : serverInstances)
			inst.disable();
		
		if(restApi != null)
			restApi.disable();
		
		log("Goodbye!");
	}
}










































