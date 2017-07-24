package org.adapter.framework.spring.listener;

import org.adapter.framework.abstracts.BeansFactory;
import org.adapter.framework.contracts.BeanFactory;
import org.adapter.framework.event.contract.AppEvent;
import org.adapter.framework.event.contract.Context;
import org.adapter.framework.event.contract.EventListener;
import org.adapter.framework.event.impl.AppEventHandler;
import org.adapter.framework.event.impl.ListenerRegisterer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringAppInitListener implements ApplicationContextAware {

	private Context adapterFrameworkContext;

	private ApplicationContext context;

	public void setApplicationContext(final ApplicationContext context) throws BeansException {
		this.context = context;
		for (EventListener listener : ListenerRegisterer.getInstance().getListeners()) {
			AppEvent event = createAppEvent();
			event.addEventListener(listener);
			AppEventHandler.getInstance().addAppEvents(event);
		}
	}

	public ApplicationContext getContext() {
		return context;
	}

	private Context adapterContext() {
		if (adapterFrameworkContext == null)
			adapterFrameworkContext = new Context() {

				public BeanFactory getBeanfactory() {

					return new BeansFactory() {

						public Object getBean(Class<?> targetClassType) {
							if (!getBeanFactories().contains(this)) {
								getBeanFactories().add(this);
							}
							return context.getBean(targetClassType);
						}

						@Override
						public Object getObject(String qualifier) {
							return context.getBean(qualifier);
						}
					};
				}
			};
		return adapterFrameworkContext;
	}

	private AppEvent createAppEvent() {
		return new AppEvent() {
			private EventListener listener;

			public void triggerEvent() {
				this.listener.onEvent(adapterContext());
			}

			public void addEventListener(EventListener listener) {
				this.listener = listener;
			}
		};
	}

}
