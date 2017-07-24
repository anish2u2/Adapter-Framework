package org.adapter.framework.abstracts;

import java.util.HashSet;
import java.util.Set;

import org.adapter.framework.contracts.BeanFactory;

public abstract class BeansFactory implements BeanFactory {

	private static Set<BeanFactory> beanFactories = new HashSet<BeanFactory>(2);

	public static Set<BeanFactory> getBeanFactories() {
		return beanFactories;
	}

	public static void addBeanFactory(BeanFactory beanFactory) {
		beanFactories.add(beanFactory);
	}

	public static void setBeanFactories(Set<BeanFactory> beanFactories) {
		BeansFactory.beanFactories = beanFactories;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}

}
