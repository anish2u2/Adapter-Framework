package org.adapter.framework.contracts;

public interface BeanFactory {

	public Object getBean(Class<?> targetClassType);

	public Object getObject(String qualifier);

}
