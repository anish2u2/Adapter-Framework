package org.adapter.framework.event.contract;

public interface EventHandler {

	public void addAppEvents(AppEvent events);

	public void executeEvents();

}
