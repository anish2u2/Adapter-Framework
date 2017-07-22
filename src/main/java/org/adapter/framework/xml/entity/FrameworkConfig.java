package org.adapter.framework.xml.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "framework")
@XmlAccessorType(XmlAccessType.FIELD)
public class FrameworkConfig {

	@XmlElement(name = "config")
	private Config config;

	public Config getConfig() {
		return this.config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

}
