package com.elisaxui.core.helper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionHelper {

	public static void setFinalStatic(Field field, Object newValue) throws Exception {
		if (!field.isAccessible())
			field.setAccessible(true);

		if ((field.getModifiers() & Modifier.FINAL) == Modifier.FINAL) {
			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
		}

		field.set(null, newValue);
	}
	
	public static Object getFinalStatic(Field field, Object obj) throws Exception {
		if (!field.isAccessible())
			field.setAccessible(true);

		if ((field.getModifiers() & Modifier.FINAL) == Modifier.FINAL) {
			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
		}

		return field.get(obj);
	}

	// // wrapping setAccessible
	// AccessController.doPrivileged(new PrivilegedAction<Object>() {
	// @Override
	// public Object run() {
	// modifiersField.setAccessible(true);
	// return null;
	// }
	// });
}
