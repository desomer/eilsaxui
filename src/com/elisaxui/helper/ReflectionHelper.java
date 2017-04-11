package com.elisaxui.helper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionHelper {

	public static void setFinalStatic(Field field, Object newValue) throws Exception {
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
       

//          // wrapping setAccessible
//          AccessController.doPrivileged(new PrivilegedAction<Object>() {
//              @Override
//              public Object run() {
//                  modifiersField.setAccessible(true);
//                  return null;
//              }
//          });
       
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, newValue);
     }

	
}
