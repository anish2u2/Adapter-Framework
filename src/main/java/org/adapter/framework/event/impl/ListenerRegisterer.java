package org.adapter.framework.event.impl;

import java.util.HashSet;
import java.util.Set;

import org.adapter.framework.contracts.ListenerRegister;
import org.adapter.framework.event.contract.EventListener;

public class ListenerRegisterer implements ListenerRegister {

	private Set<EventListener> listeners = new HashSet<EventListener>(2);

	private static ListenerRegister register;

	private ListenerRegisterer() {

	}

	public static ListenerRegister getInstance() {
		if (register == null)
			register = new ListenerRegisterer();
		return register;
	}

	public void registerListener(EventListener listener) {
		listeners.add(listener);
	}

	public EventListener[] getListeners() {
		EventListener[] eventListeners = new EventListener[listeners.size()];
		int counter = 0;
		for (EventListener listen : listeners) {
			eventListeners[counter] = listen;
			counter++;
		}
		return eventListeners;
	}

	@Override
	protected void finalize() throws Throwable {
		listeners.clear();
		listeners = null;
		super.finalize();
	}

}
