package org.adapter.framework.xml.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "config")
@XmlAccessorType(XmlAccessType.FIELD)
public class Config {

	@XmlElement(name = "component")
	private ComponentScanPackage componentScanPackage;

	@XmlElement(name = "listener")
	private Listener listener;

	public ComponentScanPackage getComponentScanPackage() {
		return componentScanPackage;
	}

	public Listener getListener() {
		return listener;
	}

	public void setComponentScanPackage(ComponentScanPackage componentScanPackage) {
		this.componentScanPackage = componentScanPackage;
	}

	public void setListener(Listener listener) {
		this.listener = listener;
	}

}
