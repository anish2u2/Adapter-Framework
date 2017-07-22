package org.adapter.framework.abstracts;

import org.adapter.framework.contracts.Framework;
import org.adapter.framework.contracts.InitFramework;

public class AbstractFramework implements Framework {

	private InitFramework framework;

	public void init(InitFramework initFramework) {
		
		framework = initFramework;
		initFramework.init();
	}

	public void destroy() {
		framework.destroy();
	}

}
