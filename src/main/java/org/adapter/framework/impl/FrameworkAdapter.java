package org.adapter.framework.impl;

import java.util.LinkedList;
import java.util.List;

import org.adapter.framework.abstracts.AbstractFrameworkAdapter;
import org.adapter.framework.files.FilesUtility;

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
				listOfLoadedClass
						.add(super.loadClass(packageName + "." + classFiles.substring(0, classFiles.length() - 6)));
			}
		}

		public List<Class<?>> getListOfLoadedClass() {
			return this.listOfLoadedClass;
		}

	}

	public static void main(String args[]) {

		org.adapter.framework.contracts.FrameworkAdapter adapter = getInstance();
		adapter.adaptFramework();
	}

}
