package org.adapter.framework.abstracts;

import java.util.LinkedList;
import java.util.List;

import org.adapter.framework.conf.Config;
import org.adapter.framework.contracts.Framework;
import org.adapter.framework.contracts.FrameworkAdapter;
import org.adapter.framework.contracts.InitFramework;
import org.adapter.framework.impl.FrameworkAdapter.FramworkClassLoader;
import org.adapter.framework.reflection.ReflectionUtil;
import org.adapter.framework.xml.entity.FrameworkConfig;

public abstract class AbstractFrameworkAdapter implements FrameworkAdapter {

	private List<ClassLoader> loaders;
	private List<Framework> frameworks;

	public AbstractFrameworkAdapter() {
		loaders = new LinkedList<ClassLoader>();
	}

	public void adaptFramework() {
		Config config = Config.getInstance();
		config.loadConfig();
		List<InitFramework> initFrameworksList = new LinkedList<InitFramework>();
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
				initFrameworksList.add((InitFramework) ReflectionUtil.getObjectOfType(InitFramework.class,
						convertToArray(((FramworkClassLoader) loader).getListOfLoadedClass())));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		System.out.println("Classess are loaded successfully. Now starting initializing sub-frameworks.");
		for (InitFramework initFramework : initFrameworksList) {
			System.out.println("Initramework:" + initFramework.getClass().getName());
			Framework framework = new org.adapter.framework.impl.Framework();
			framework.init(initFramework);
			frameworks.add(framework);
		}
		System.out.println("System is initialized success fully.");
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
}
