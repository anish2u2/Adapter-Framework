package org.adapter.framework.xml.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "app")
@XmlAccessorType(XmlAccessType.FIELD)
public class AppConfig {

	@XmlElement(name = "init-app")
	private AppInitClass initClass;

	public AppInitClass getInitClass() {
		return initClass;
	}

	public void setInitClass(AppInitClass initClass) {
		this.initClass = initClass;
	}

}
