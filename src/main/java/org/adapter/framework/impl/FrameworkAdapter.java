package org.adapter.framework.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.adapter.framework.abstracts.AbstractFrameworkAdapter;
import org.adapter.framework.abstracts.BeansFactory;
import org.adapter.framework.annotations.InitApp;
import org.adapter.framework.constants.Constants;
import org.adapter.framework.contracts.BeanFactory;
import org.adapter.framework.files.FilesUtility;
import org.adapter.framework.reflection.ReflectionUtil;

public class FrameworkAdapter extends AbstractFrameworkAdapter {

	private static org.adapter.framework.contracts.FrameworkAdapter adapter;

	private FrameworkAdapter() {

	}

	public static org.adapter.framework.contracts.FrameworkAdapter getInstance() {
		if (adapter == null) {
			adapter = new FrameworkAdapter();
		}
		return adapter;
	}

	@Override
	protected ClassLoader getFrameworkClassLoader() {

		return new FramworkClassLoader();
	}

	public static class FramworkClassLoader extends ClassLoader {

		private List<Class<?>> listOfLoadedClass = new LinkedList<Class<?>>();

		public Package[] retrieveAllPackage() {
			return super.getPackages();
		}

		public void loadClassfromPackage(String packageName) throws Exception {
			System.out.println("Loading classes from package:" + packageName);
			for (String classFiles : FilesUtility.readFileNamesOfTypeFromJarInPackage(packageName, null, ".class")) {
				listOfLoadedClass.add(super.loadClass(classFiles.substring(0, classFiles.length() - 6)));
			}
		}

		public List<Class<?>> getListOfLoadedClass() {
			return this.listOfLoadedClass;
		}

	}

	public static void main(String args[]) {

		org.adapter.framework.contracts.FrameworkAdapter adapter = getInstance();
		adapter.adaptFramework();
		System.out.println("Framework initialization completed.. Now launching App.");
	}

	@Override
	protected void invokeAppInitMethod(Object appInitObject, InitApp initApp) {
		if (initApp == null)
			throw new RuntimeException("Unable to find App descriptions...");
		String appName = initApp.appName();
		System.out.println("Starting App:" + appName);
		String initMethod = initApp.initMethod();
		for (Method method : appInitObject.getClass().getMethods()) {
			if (method.getName().equals(initMethod)) {
				ReflectionUtil.invokeMethod(method, null, appInitObject);
				return;
			}
		}

	}

	@Override
	protected void injectAnnotatedFieldObjects(Object object, Class<?> clazz) {
		System.out.println("Clazz:" + clazz.getName());
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			System.out.println("Fields:" + field.getType().getName());
		}
		for (Field field : fields) {
			Class<?> fieldType = field.getType();
			if (!Constants.isPrimitiveType(fieldType)) {
				System.out.println("Creating class of tyoe:" + fieldType.getName());
				Object fieldInstance = null;
				for (BeanFactory beansFactory : BeansFactory.getBeanFactories()) {
					if (!field.isAccessible())
						field.setAccessible(true);
					fieldInstance = beansFactory.getBean(fieldType);
					System.out.println("bean found from cache:" + fieldInstance);
					if (fieldInstance == null) {
						fieldInstance = ReflectionUtil.createInstance(fieldType);
						System.out.println("Instance created:" + fieldInstance);
					}
					injectAnnotatedFieldObjects(fieldInstance,
							(fieldInstance.getClass().getSuperclass() != null
									&& !fieldInstance.getClass().getSuperclass().equals(Object.class))
											? fieldInstance.getClass().getSuperclass() : fieldInstance.getClass());
					try {
						System.out.println("Now trying to set Field value:" + fieldInstance);
						System.out.println("Field Name:" + field.getName());
						field.set(object, fieldInstance);
						System.out.println("Field Object is setted..");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
