package org.adapter.framework.abstracts;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.adapter.framework.annotations.InitApp;
import org.adapter.framework.conf.Config;
import org.adapter.framework.contracts.BeanFactory;
import org.adapter.framework.contracts.Framework;
import org.adapter.framework.contracts.FrameworkAdapter;
import org.adapter.framework.contracts.InitFramework;
import org.adapter.framework.event.contract.Context;
import org.adapter.framework.event.contract.EventListener;
import org.adapter.framework.files.FilesUtility;
import org.adapter.framework.impl.FrameworkAdapter.FramworkClassLoader;
import org.adapter.framework.reflection.ReflectionUtil;
import org.adapter.framework.xml.entity.AppConfig;
import org.adapter.framework.xml.entity.FrameworkConfig;
import org.adapter.framework.xml.utility.XMLUtility;

public abstract class AbstractFrameworkAdapter implements FrameworkAdapter {

	private List<ClassLoader> loaders;
	private List<Framework> frameworks = new LinkedList<Framework>();

	public AbstractFrameworkAdapter() {
		loaders = new LinkedList<ClassLoader>();
	}

	public void adaptFramework() {
		Config config = Config.getInstance();
		config.loadConfig();
		List<ModuleConfig> initFrameworksList = new LinkedList<ModuleConfig>();
		for (FrameworkConfig frameworkConfig : config.getFrameworkConfig()) {
			ClassLoader loader = getFrameworkClassLoader();
			loaders.add(loader);
			try {
				if (frameworkConfig != null)
					System.out.println("Found Init Class:" + frameworkConfig);
				if (frameworkConfig.getConfig() != null)
					System.out.println("Found Config Class:" + frameworkConfig.getConfig());
				if (frameworkConfig.getConfig().getComponentScanPackage() != null) {
					System.out.println(
							"Found Component scan Class:" + frameworkConfig.getConfig().getComponentScanPackage());
				}
				if (frameworkConfig.getConfig().getComponentScanPackage().getPackageName() != null) {
					System.out.println("Found package name:"
							+ frameworkConfig.getConfig().getComponentScanPackage().getPackageName());
				}
				((FramworkClassLoader) loader)
						.loadClassfromPackage(frameworkConfig.getConfig().getComponentScanPackage().getPackageName());
				ModuleConfig moduleConfig = new ModuleConfig();
				moduleConfig.setInitFramework((InitFramework) ReflectionUtil.getObjectOfType(InitFramework.class,
						convertToArray(((FramworkClassLoader) loader).getListOfLoadedClass())));
				moduleConfig
						.setEventListener((EventListener) ReflectionUtil.createInstance(((FramworkClassLoader) loader)
								.loadClass(frameworkConfig.getConfig().getListener().getInitListener())));
				initFrameworksList.add(moduleConfig);
				/*
				 * for (Class<?> clazz : ((FramworkClassLoader)
				 * loader).getListOfLoadedClass()) { System.out.println(
				 * "Loaded class:" + clazz.getName()); }
				 */
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		System.out.println("Classess are loaded successfully. Now starting initializing sub-frameworks.");
		for (ModuleConfig moduleConfig : initFrameworksList) {
			System.out.println("Initramework:" + moduleConfig.getInitFramework().getClass().getName());
			Framework framework = new org.adapter.framework.impl.Framework();
			Context context = framework.init(moduleConfig.getInitFramework());
			BeansFactory.addBeanFactory((BeanFactory) context.getBeanfactory());
			moduleConfig.getEventListener().onEvent(context);
			frameworks.add(framework);
		}
		System.out.println("Context is initialized successfully..");

		System.out.println("Reading App Config..");
		try {
			List<String> applist = FilesUtility.readFileNamesOfTypeFromJar(getRootLocation().toString(), null,
					"app.xml");
			AppConfig appConfig = null;
			for (String app : applist) {
				appConfig = (AppConfig) XMLUtility.unmarshal(app, AppConfig.class);
			}
			Object initAppBean = null;
			InitApp annotatedObject = null;
			for (BeanFactory beanFactory : BeansFactory.getBeanFactories()) {
				try {
					initAppBean = beanFactory.getBean(Class.forName(appConfig.getInitClass().getClassName()));
					System.out.println("Found Class:" + initAppBean.getClass());
					annotatedObject = Class.forName(appConfig.getInitClass().getClassName())
							.getAnnotation(InitApp.class);
					break;
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			injectAnnotatedFieldObjects(initAppBean, initAppBean.getClass().getSuperclass() != null
					? initAppBean.getClass().getSuperclass() : initAppBean.getClass());
			System.out.println("Starting App");
			invokeAppInitMethod(initAppBean, annotatedObject);

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("NO App config found ..");
		}

	}

	protected abstract ClassLoader getFrameworkClassLoader();

	public Class<?>[] convertToArray(List<Class<?>> listOfCLasses) {
		Class<?>[] arrayOfClasses = new Class<?>[listOfCLasses.size()];
		int counter = 0;
		for (Class<?> clazz : listOfCLasses) {
			arrayOfClasses[counter] = clazz;
			counter++;
		}
		return arrayOfClasses;
	}

	public ClassLoader getClassLoaderToLoadClasses() {
		ClassLoader loader = getFrameworkClassLoader();
		loaders.add(loader);
		return loader;
	}

	public static class ModuleConfig {
		private InitFramework initFramework;
		private EventListener eventListener;

		public InitFramework getInitFramework() {
			return initFramework;
		}

		public void setInitFramework(InitFramework initFramework) {
			this.initFramework = initFramework;
		}

		public EventListener getEventListener() {
			return eventListener;
		}

		public void setEventListener(EventListener eventListener) {
			this.eventListener = eventListener;
		}

	}

	protected abstract void invokeAppInitMethod(Object appInitObject, InitApp initApp);

	private URL getRootLocation() {
		return getClass().getProtectionDomain().getCodeSource().getLocation();
	}

	protected abstract void injectAnnotatedFieldObjects(Object object, Class<?> clazz);
}
