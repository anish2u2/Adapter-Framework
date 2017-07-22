package org.adapter.framework.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ReflectionUtil {

	public static Object getObjectOfType(Class<?> targetClassType, Class<?>[] listOfClasses)
			throws Exception, Exception {
		for (Class<?> targetClass : listOfClasses) {
			if (isImplementedInterfaceOfType(targetClass, targetClassType)
					|| isAnnotatedClassOfType(targetClass, targetClassType))
				return targetClass.newInstance();
		}
		return null;
	}

	public static boolean isImplementedInterfaceOfType(Class<?> targetClass, Class<?> targetType) {
		Class<?>[] implementatedInterfaces = targetClass.getInterfaces();
		for (Class<?> interfaces : implementatedInterfaces) {
			if (interfaces.isAssignableFrom(targetType)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isAnnotatedClassOfType(Class<?> targetClass, Class<?> targetType) {
		Annotation[] annotations = targetClass.getAnnotations();
		for (Annotation annotation : annotations) {
			if (annotation.annotationType().isAssignableFrom(targetType)) {
				return true;
			}
		}
		return false;
	}

	public static Set<Class<?>> getListOfAnnotatedFieldsOfClass(Class<?> targetClass, Class<?> annotationType) {
		Field[] fields = targetClass.getDeclaredFields();
		Set<Class<?>> listOfFields = new LinkedHashSet<Class<?>>();
		for (Field field : fields) {
			for (Annotation annotation : field.getDeclaredAnnotations()) {
				if (annotation.annotationType().isAssignableFrom(annotationType)) {
					listOfFields.add(field.getType());
				}
			}
		}
		if (!listOfFields.isEmpty()) {
			return listOfFields;
		}
		return null;
	}
}
