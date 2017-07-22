package org.adapter.framework.conf;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.adapter.framework.files.FilesUtility;
import org.adapter.framework.xml.entity.FrameworkConfig;
import org.adapter.framework.xml.utility.XMLUtility;

public class Config {

	private final String SUFFIX_CONFIG_NAME = "-module.xml";

	private static Config config;

	private List<FrameworkConfig> frameworkConfig;

	private Config() {

	}

	public static Config getInstance() {
		if (config == null)
			config = new Config();
		return config;
	}

	public void loadConfig() {
		System.out.println("Config file root directory:" + getRootLocation().toString());
		FilesUtility.setDIR(getRootLocation().toString());
		List<String> configFiles = FilesUtility.readFileNamesOfTypeFromJar(getRootLocation().toString(), null,
				SUFFIX_CONFIG_NAME);
		frameworkConfig = new LinkedList<FrameworkConfig>();
		for (String confFile : configFiles) {
			System.out.println("Start Reading config-file :" + confFile);
			frameworkConfig.add((FrameworkConfig) XMLUtility.unmarshal(confFile, FrameworkConfig.class));
		}
		System.out.println("All the resources success fully loaded.");
	}

	private URL getRootLocation() {
		return getClass().getProtectionDomain().getCodeSource().getLocation();
	}

	public List<FrameworkConfig> getFrameworkConfig() {
		return frameworkConfig;
	}

	public void setFrameworkConfig(List<FrameworkConfig> frameworkConfig) {
		this.frameworkConfig = frameworkConfig;
	}

}
