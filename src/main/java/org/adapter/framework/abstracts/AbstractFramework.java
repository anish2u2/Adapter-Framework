package org.adapter.framework.abstracts;

import org.adapter.framework.contracts.Framework;
import org.adapter.framework.contracts.InitFramework;
import org.adapter.framework.event.contract.Context;

public class AbstractFramework implements Framework {

	private InitFramework framework;

	public Context init(InitFramework initFramework) {

		framework = initFramework;
		return initFramework.init();
	}

	public void destroy() {
		framework.destroy();
	}

}
