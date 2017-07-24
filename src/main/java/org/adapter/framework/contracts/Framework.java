package org.adapter.framework.contracts;

import org.adapter.framework.event.contract.Context;

public interface Framework {

	public Context init(InitFramework initFramework);

	public void destroy();

}
