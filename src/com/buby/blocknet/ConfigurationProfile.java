package com.buby.blocknet;

import java.io.File;

import com.buby.blocknet.core.BlockNetCore;
import com.buby.blocknet.util.CommonUtils.FileUtil;
import com.buby.blocknet.util.model.MinMaxPair;
import com.google.gson.Gson;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ConfigurationProfile {
	@Getter private MinMaxPair portRange = MinMaxPair.of(25566, 30000);
	@Getter private int masterApiPort = 8888;
	@Getter private int apiPort = 8888;
	@Getter private String advertisementIp = "localhost";
	@Getter private String masterIp = "localhost";
	@Getter private boolean master = true;
	
	public static ConfigurationProfile getConfigruationProfile() {
		Gson gson = new Gson();
		if(!FileUtil.fileExists(BlockNetCore.BLOCK_NET_CORE_DIR + "/blocknet-config.json")) {
			ConfigurationProfile configProfile = generateDefaults();
			String jsonString = gson.toJson(configProfile);
			FileUtil.saveJSONtoFile(jsonString, FileUtil.getOrMkdirs(BlockNetCore.BLOCK_NET_CORE_DIR + "/blocknet-config.json"));
			return configProfile;
		}else {
			File configFile = FileUtil.getOrMkdirs(BlockNetCore.BLOCK_NET_CORE_DIR + "/blocknet-config.json");
			String profileJson = FileUtil.readFileAsString(configFile);
			ConfigurationProfile configProfile = gson.fromJson(profileJson, ConfigurationProfile.class);
			return configProfile;
			
		}
	}
	private static ConfigurationProfile generateDefaults() {
		ConfigurationProfile newProfile = new ConfigurationProfile();
		return newProfile;
	}
}
