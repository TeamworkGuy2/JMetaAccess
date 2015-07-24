package propertyAccessor;

import java.lang.reflect.Field;
import java.util.function.BiFunction;
import java.util.function.Function;

/** Inference utilities for mapping field names to getter/setter method for various naming conventions
 * @author TeamworkGuy2
 * @since 2015-6-27
 */
public enum PropertyNamerConvention implements PropertyNamer {
	JAVA_BEAN_LIKE(PropertyNamerConvention::inferJavaBeanGetterName, PropertyNamerConvention::inferJavaBeanGetterName, PropertyNamerConvention::inferJavaBeanSetterName, PropertyNamerConvention::inferJavaBeanSetterName),
	BUILDER_LIKE(PropertyNamerConvention::inferBuilderGetterName, PropertyNamerConvention::inferBuilderGetterName, PropertyNamerConvention::inferBuilderSetterName, PropertyNamerConvention::inferBuilderSetterName);

	final Function<Field, String> getterNameFromFieldFunc;
	final BiFunction<String, Class<?>, String> getterNameFromNameTypeFunc;
	final Function<Field, String> setterNameFromFieldFunc;
	final BiFunction<String, Class<?>, String> setterNameFromNameTypeFunc;


	private PropertyNamerConvention(Function<Field, String> getterNameFromFieldFunc, BiFunction<String, Class<?>, String> getterNameFromNameTypeFunc,
			Function<Field, String> setterNameFromFieldFunc, BiFunction<String, Class<?>, String> setterNameFromNameTypeFunc) {
		this.getterNameFromFieldFunc = getterNameFromFieldFunc;
		this.getterNameFromNameTypeFunc = getterNameFromNameTypeFunc;
		this.setterNameFromFieldFunc = setterNameFromFieldFunc;
		this.setterNameFromNameTypeFunc = setterNameFromNameTypeFunc;
	}


	@Override
	public String createGetterName(Field field) {
		return this.getterNameFromFieldFunc.apply(field);
	}


	@Override
	public String createGetterName(String fieldName, Class<?> clazz) {
		return this.getterNameFromNameTypeFunc.apply(fieldName, clazz);
	}


	@Override
	public String createSetterName(Field field) {
		return this.setterNameFromFieldFunc.apply(field);
	}


	@Override
	public String createSetterName(String fieldName, Class<?> clazz) {
		return this.setterNameFromNameTypeFunc.apply(fieldName, clazz);
	}



	// Java Bean like naming, (i.e. field 'boolean fulfilled' has getter 'isFulfilled()' and setter 'setFulfilled(...)')
	public static String inferJavaBeanGetterName(Field field) {
		return inferJavaBeanGetterName(field.getName(), field.getType());
	}


	public static String inferJavaBeanGetterName(String fieldName, Class<?> fieldType) {
		String name = firstCharToTitleCase(fieldName);

		if(fieldType == Boolean.TYPE) {
			if(isBooleanyName(fieldName)) {
				return fieldName;
			}
			else {
				return "is" + name;
			}
		}
		return "get" + name;
	}


	public static String inferJavaBeanSetterName(Field field) {
		return inferJavaBeanSetterName(field.getName(), field.getType());
	}


	public static String inferJavaBeanSetterName(String fieldName, Class<?> fieldType) {
		String name = firstCharToTitleCase(fieldName);
		if(fieldType == Boolean.TYPE) {
			if(isBooleanyName(fieldName)) {
				return "set" + fieldName.substring(2);
			}
		}
		return "set" + name;
	}


	// Builder pattern like naming, (i.e. field 'boolean fulfilled' has getter 'fulfilled()' and setter 'fulfilled(...)')
	public static String inferBuilderGetterName(Field field) {
		return inferBuilderGetterName(field.getName(), field.getType());
	}


	public static String inferBuilderGetterName(String fieldName, Class<?> fieldType) {
		return fieldName;
	}


	public static String inferBuilderSetterName(Field field) {
		return inferBuilderSetterName(field.getName(), field.getType());
	}


	public static String inferBuilderSetterName(String fieldName, Class<?> fieldType) {
		return fieldName;
	}


	// utility methods
	private static String firstCharToTitleCase(String name) {
		String res = name;
		if(Character.isLowerCase(name.charAt(0))) {
			res = Character.toUpperCase(name.charAt(0)) + name.substring(1);
		}
		return res;
	}


	private static boolean isBooleanyName(String name) {
		return (name.startsWith("is") && name.length() > 2 && Character.isUpperCase(name.charAt(2)));
	}

}
