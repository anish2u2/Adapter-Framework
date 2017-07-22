package org.adapter.framework.event.contract;

public interface AppEvent {

	public void addEventListener(EventListener listener);

	public void triggerEvent();

}
