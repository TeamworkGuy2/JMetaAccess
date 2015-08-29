package propertyAccessor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import twg2.collections.tuple.Entries;
import twg2.collections.util.MapUtil;

/**
 * @author TeamworkGuy2
 * @since 20157-18
 * @param <T> the type of value contained in this field reference
 */
public interface PropertyDefinition<T> {

	public <E> void setVal(T val, E srcObject);

	public <E> T getVal(E srcObject);

	public String getFieldName();

	public Class<T> getType();


	// static utilities that use PropertyImpl
	public static Map<String, PropertyDefinition<Object>> createFromObject(Class<?> clazz, PropertyNamer namingConvention) {
		Map<String, Field> fieldMap = FieldGet.getAllFields(clazz);
		return createFromFieldsIncluding(clazz, namingConvention, fieldMap, fieldMap.keySet());
	}


	public static Map<String, PropertyDefinition<Object>> createFromObjectRecursive(Class<?> clazz, PropertyNamer namingConvention, Collection<Class<?>> stopAtFields) {
		List<CompoundProperty<Object>> fields = FieldGet.getAllPropertiesRecursive(clazz, stopAtFields);
		Map<String, PropertyDefinition<Object>> fieldMap = MapUtil.map(fields, (f) -> Entries.of(f.getFieldName(), f));
		//return createFromFieldsIncluding(clazz, namingConvention, fieldMap, fieldMap.keySet());
		return fieldMap;
	}


	public static Map<String, PropertyDefinition<Object>> createFromObjectIncluding(Class<?> clazz, PropertyNamer namingConvention, Collection<String> includingFieldNames) {
		Map<String, Field> fieldMap = FieldGet.getAllFields(clazz);
		return createFromFieldsIncluding(clazz, namingConvention, fieldMap, includingFieldNames);
	}


	public static Map<String, PropertyDefinition<Object>> createFromObjectExcluding(Class<?> clazz, PropertyNamer namingConvention, Collection<String> excludingFieldNames) {
		Map<String, Field> fieldMap = FieldGet.getAllFields(clazz);
		return createFromFieldsExcluding(clazz, namingConvention, fieldMap, excludingFieldNames);
	}


	public static <T> PropertyDefinition<T> createFromName(Class<?> instClass, Class<T> fieldType, PropertyNamer namingConvention, Map<String, Field> fieldMap, String fieldName) {
		try {
			Field field = fieldMap.get(fieldName);
			if(field == null) {
				throw new NoSuchFieldException("'" + fieldName + "' on type: " + instClass);
			}

			String getterName = namingConvention.createGetterName(field);
			String setterName = namingConvention.createSetterName(field);

			Method getterRef = instClass.getMethod(getterName);
			Method setterRef = instClass.getMethod(setterName, fieldType);

			PropertyDefinition<T> t = new PropertyImpl<T>(setterRef, getterRef, fieldName, field);

			return t;
		} catch (SecurityException | IllegalArgumentException | NoSuchMethodException | NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}


	public static Map<String, PropertyDefinition<Object>> createFromFields(Class<?> instClass, PropertyNamer namingConvention, Map<String, Field> fieldMap) {
		Map<String, PropertyDefinition<Object>> resFields = new HashMap<>();

		for(Map.Entry<String, Field> objField : fieldMap.entrySet()) {
			@SuppressWarnings("unchecked")
			Class<Object> fieldType = (Class<Object>)objField.getValue().getType();
			PropertyDefinition<Object> getSetField = createFromName(instClass, fieldType, namingConvention, fieldMap, objField.getKey());
			resFields.put(objField.getKey(), getSetField);
		}

		return resFields;
	}


	public static Map<String, PropertyDefinition<Object>> createFromFieldsExcluding(Class<?> instClass, PropertyNamer namingConvention, Map<String, Field> fieldMap, Collection<String> excludeFieldNames) {
		Map<String, PropertyDefinition<Object>> resFields = new HashMap<>();

		for(Map.Entry<String, Field> objField : fieldMap.entrySet()) {
			if(excludeFieldNames.contains(objField.getKey())) {
				//continue;
			}
			else {
				@SuppressWarnings("unchecked")
				Class<Object> fieldType = (Class<Object>)objField.getValue().getType();
				PropertyDefinition<Object> getSetField = createFromName(instClass, fieldType, namingConvention, fieldMap, objField.getKey());
				resFields.put(objField.getKey(), getSetField);
			}
		}

		return resFields;
	}


	public static Map<String, PropertyDefinition<Object>> createFromFieldsIncluding(Class<?> instClass, PropertyNamer namingConvention, Map<String, Field> fieldMap, Collection<String> includeFieldNames) {
		Map<String, PropertyDefinition<Object>> resFields = new HashMap<>();

		for(String fieldName : includeFieldNames) {
			Field field = fieldMap.get(fieldName);
			if(field == null) {
				throw new NoSuchFieldError("'" + fieldName + "' on type: " + instClass);
			}
			@SuppressWarnings("unchecked")
			Class<Object> fieldType = (Class<Object>)field.getType();
			PropertyDefinition<Object> getSetField = createFromName(instClass, fieldType, namingConvention, fieldMap, fieldName);
			resFields.put(fieldName, getSetField);
		}

		return resFields;
	}

}
