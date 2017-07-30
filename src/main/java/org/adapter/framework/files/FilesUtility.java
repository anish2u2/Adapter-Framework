package org.adapter.framework.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.adapter.framework.abstracts.AbstractFrameworkAdapter;
import org.adapter.framework.impl.FrameworkAdapter.FramworkClassLoader;
import org.adapter.framework.reflection.ReflectionUtil;

/*
 * @author Anish Singh
 * 
 * This file is responsible for handling all function which are generic for the application. 
 */

public class FilesUtility {

	private static String dir;

	public static void setDIR(String directory) {
		dir = directory;
	}

	public static List<String> readFileNamesOfTypeFromJar(String dir, String prefix, String suffix) {
		if (dir.contains("file:"))
			dir = dir.substring("file:".length(), dir.length());
		/*
		 * if (dir.startsWith(File.separator)) { dir = dir.substring(1,
		 * dir.length()); }
		 */
		dir = dir.substring(1);
		System.out.println("Looking into DIR:" + dir);
		if (dir.endsWith(".jar")) {
			return readFilesFromJar(dir, null, prefix, suffix);
		}
		return readFilesFromZip(dir, null, prefix, suffix);
	}

	public static List<String> readFileNamesOfTypeFromJarInPackage(String packageName, String prefix, String suffix) {

		System.out.println("Dir:" + dir);
		if (dir.contains("file:")) {
			dir = dir.substring("file:".length(), dir.length());
			dir = dir.substring(1);
		}
		/*
		 * if (dir.startsWith(File.separator)) { dir = dir.substring(1,
		 * dir.length()); }
		 */

		System.out.println("Looking into DIR:" + dir);
		if (dir.endsWith(".jar")) {
			return readFilesFromJar(dir, packageName, prefix, suffix);
		}
		return readFilesFromZip(dir, packageName, prefix, suffix);
	}

	private static List<String> readFilesFromJar(String dir, String packageName, String prefix, String suffix) {
		List<String> listOfClass = new LinkedList<String>();
		try (JarInputStream jarInputStream = new JarInputStream(new FileInputStream(new File(dir)))) {

			JarEntry jarEntry = null;
			while (true) {
				jarEntry = jarInputStream.getNextJarEntry();
				if (jarEntry == null)
					break;
				if (prefix != null && suffix != null) {
					addToList(listOfClass, jarEntry.getName().replace("/", ".").replace(File.separator, "."),
							packageName);
				} else if (prefix != null && jarEntry.getName().startsWith(prefix)) {
					addToList(listOfClass, jarEntry.getName().replace("/", ".").replace(File.separator, "."),
							packageName);
				} else if (suffix != null && jarEntry.getName().endsWith(suffix)) {
					addToList(listOfClass, jarEntry.getName().replace("/", ".").replace(File.separator, "."),
							packageName);
				}
				// System.out.println("File name:" + jarEntry.getName());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return listOfClass;
	}

	private static List<String> readFilesFromZip(String dir, String packageName, String prefix, String suffix) {
		List<String> listOfClass = new LinkedList<String>();
		try (ZipInputStream zis = new ZipInputStream(new FileInputStream(dir))) {
			while (zis.available() == 0) {
				ZipEntry entry = zis.getNextEntry();
				if (!entry.isDirectory()) {
					if (prefix != null && suffix != null && entry.getName().endsWith(suffix)
							&& entry.getName().startsWith(prefix))
						addToList(listOfClass, entry.getName().replace(File.separator, "."), packageName);
					else if (prefix != null && entry.getName().startsWith(prefix))
						addToList(listOfClass, entry.getName().replace(File.separator, "."), packageName);
					else if (suffix != null && entry.getName().endsWith(suffix))
						addToList(listOfClass, entry.getName().replace(File.separator, "."), packageName);
				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return listOfClass;
	}

	public static void addToList(List<String> listOfClass, String file, String packageName) {
		if (packageName != null) {
			// System.out.println("Name of file:" + file);
			if (file.startsWith(packageName)) {
				listOfClass.add(file);
			}
		} else {
			listOfClass.add(file);
		}
	}

	public static String[] readFileNamesOfType(String dir, String prefix, String suffix) {
		File file = new File(dir);
		// System.out.println("Reading files ..");
		return file.list(new FileNameFilter(prefix, suffix));
	}

	public static String[] readClassFilesFromPackage(String packageName) {
		File file = new File(getSourcePathOfApp() + File.separator + packageName.replace(".", File.separator));
		return file.list(new FileNameFilter(null, ".class"));
	}

	public static Map<Class<?>, Set<Class<?>>> getAllClassesThatContainsAnnotatedclass(Class<?> targetAnnotation,
			String packageName) {
		File file = new File(getSourcePathOfApp() + File.separator + packageName.replace(".", File.separator));
		Map<Class<?>, Set<Class<?>>> listOfLoadedClass = new HashMap<Class<?>, Set<Class<?>>>();
		try {
			FramworkClassLoader loader = (FramworkClassLoader) ((AbstractFrameworkAdapter) org.adapter.framework.impl.FrameworkAdapter
					.getInstance()).getClassLoaderToLoadClasses();
			for (String classFileName : file.list(new FileNameFilter(null, ".class"))) {
				Set<Class<?>> listOfFields = ReflectionUtil
						.getListOfAnnotatedFieldsOfClass(Class.forName(classFileName), targetAnnotation);
				if (listOfFields != null) {
					listOfLoadedClass.put(Class.forName(classFileName), listOfFields);
					for (Class<?> clazz : listOfFields) {
						loader.loadClass(clazz.getName());
					}
				}

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return listOfLoadedClass;
	}

	public static String getSourcePathOfApp() {
		return FilesUtility.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	}

	/*
	 * This FileName Filter is to be used to filtering files based on perfix of
	 * file name and suffix of file name;
	 */
	public static class FileNameFilter implements FilenameFilter {

		private String prefix;
		private String suffix;

		public FileNameFilter(String prefix, String suffix) {
			this.prefix = prefix;
			this.suffix = suffix;
		}

		public boolean accept(File dir, String name) {
			System.out.println("Name of File:" + name);
			if (this.prefix != null && this.suffix != null)
				return name.startsWith(this.prefix) && name.endsWith(this.suffix);
			else if (this.prefix != null)
				return name.startsWith(this.prefix);
			else if (this.suffix != null)
				return name.endsWith(this.suffix);
			return false;
		}

	}

}
