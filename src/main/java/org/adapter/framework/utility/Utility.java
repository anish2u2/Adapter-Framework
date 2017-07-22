package org.adapter.framework.utility;

import java.io.InputStream;

public class Utility {

	public static InputStream findResourcesInClassPath(String fileName) {
		try {
			return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
		} catch (Exception ex) {
			try {
				return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	

}
