package org.adapter.framework.event.impl;

import java.util.HashSet;
import java.util.Set;

import org.adapter.framework.event.contract.AppEvent;
import org.adapter.framework.event.contract.EventHandler;

public class AppEventHandler implements EventHandler {

	private Set<AppEvent> listOfEvents = new HashSet<AppEvent>(3);

	private static EventHandler eventHandler;

	private AppEventHandler() {

	}

	public static EventHandler getInstance() {
		if (eventHandler == null)
			eventHandler = new AppEventHandler();
		return eventHandler;
	}

	public void addAppEvents(AppEvent events) {
		listOfEvents.add(events);
	}

	public void executeEvents() {
		for (AppEvent event : listOfEvents) {
			event.triggerEvent();
		}
	}

}
