package org.adapter.framework.event.impl;

import org.adapter.framework.event.contract.AppEvent;
import org.adapter.framework.event.contract.EventListener;

public class AppEvents implements AppEvent {

	private EventListener listener;

	public void addEventListener(EventListener listener) {
		this.listener = listener;
	}

	public void triggerEvent() {

	}

}
