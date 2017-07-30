package org.adapter.framework.constants;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Constants {

	public static final Set<Class<?>> PRIMITVE_TYPE = new HashSet<Class<?>>();

	static {
		PRIMITVE_TYPE.add(String.class);
		PRIMITVE_TYPE.add(Integer.class);
		PRIMITVE_TYPE.add(Double.class);
		PRIMITVE_TYPE.add(Void.class);
		PRIMITVE_TYPE.add(Float.class);
		PRIMITVE_TYPE.add(Boolean.class);
		PRIMITVE_TYPE.add(Character.class);
		PRIMITVE_TYPE.add(Long.class);
		PRIMITVE_TYPE.add(Byte.class);
		PRIMITVE_TYPE.add(int.class);
		PRIMITVE_TYPE.add(double.class);
		PRIMITVE_TYPE.add(void.class);
		PRIMITVE_TYPE.add(float.class);
		PRIMITVE_TYPE.add(boolean.class);
		PRIMITVE_TYPE.add(char.class);
		PRIMITVE_TYPE.add(long.class);
		PRIMITVE_TYPE.add(byte.class);
		PRIMITVE_TYPE.add(AtomicInteger.class);
	}

	public static boolean isPrimitiveType(Class<?> clazz) {
		for (Class<?> primitiveType : PRIMITVE_TYPE) {
			if (primitiveType.isAssignableFrom(clazz)) {
				return true;
			}
		}
		return false;
	}
}
