package org.adapter.framework.xml.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Listener {

	@XmlAttribute(name = "framework-init-listener")
	private String initListener;

	public String getInitListener() {
		return initListener;
	}

	public void setInitListener(String initListener) {
		this.initListener = initListener;
	}

}
