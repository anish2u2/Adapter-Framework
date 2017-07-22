package org.adapter.framework.contracts;

import org.adapter.framework.event.contract.EventListener;

public interface ListenerRegister {

	public void registerListener(EventListener listener);

	public EventListener[] getListeners();

}
