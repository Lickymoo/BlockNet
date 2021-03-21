package com.buby.blocknet;

import java.io.File;

import com.buby.blocknet.core.BlockNetCore;
import com.buby.blocknet.util.CommonUtils.FileUtil;
import com.google.gson.Gson;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TemplateConfigurationProfile {
	@Getter private String Xmx = "1G";
	@Getter private String Xms = "1G";
	
	public static TemplateConfigurationProfile getConfigruationProfile(String template) {
		Gson gson = new Gson();
		if(!FileUtil.fileExists(BlockNetCore.BLOCK_NET_CORE_DIR + "/" + template + "/template-config.json")) {
			TemplateConfigurationProfile configProfile = generateDefaults();
			String jsonString = gson.toJson(configProfile);
			FileUtil.saveJSONtoFile(jsonString, FileUtil.getOrMkdirs(BlockNetCore.BLOCK_NET_CORE_DIR + "/" + template + "/template-config.json"));
			return configProfile;
		}else {
			File configFile = FileUtil.getOrMkdirs(BlockNetCore.BLOCK_NET_CORE_DIR + "/" + template + "/template-config.json");
			String profileJson = FileUtil.readFileAsString(configFile);
			TemplateConfigurationProfile configProfile = gson.fromJson(profileJson, TemplateConfigurationProfile.class);
			return configProfile;
			
		}
	}
	private static TemplateConfigurationProfile generateDefaults() {
		TemplateConfigurationProfile newProfile = new TemplateConfigurationProfile();
		return newProfile;
	}
}
