package com.buby.blocknet;

import java.io.File;

import com.buby.blocknet.util.CommonUtils.FileUtil;
import com.google.gson.Gson;
public abstract class ConfigurationProfile {

	
	public static <T extends ConfigurationProfile> T getConfig(String path, String resourcePath, Class<? extends T> clazz) {
		try {
			Gson gson = new Gson();
			
			File sr = FileUtil.getResourceAsFile(resourcePath);
			
			if(!FileUtil.fileExists(path)) {
				FileUtil.copyFolder(sr, FileUtil.getOrMkdirs(path));
				return getConfig(path, resourcePath, clazz);
			}
		
			File configFile = FileUtil.getOrMkdirs(path);
			String profileJson = FileUtil.readFileAsString(configFile);
			return gson.fromJson(profileJson, clazz);
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
}

















































