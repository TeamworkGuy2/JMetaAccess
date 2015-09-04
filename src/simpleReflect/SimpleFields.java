package simpleReflect;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import fieldAccess.FieldGets;
import fieldAccess.SimpleField;
import fieldAccess.SimpleFieldImpl;
import twg2.treeLike.TreeTraversalOrder;
import twg2.treeLike.simpleTree.SimpleTree;
import twg2.treeLike.simpleTree.SimpleTreeUtil;

/**
 * @author TeamworkGuy2
 * @since 2015-8-24
 */
public class SimpleFields {

	// static utilities that use PropertyImpl
	public static Map<String, SimpleField> createFromObject(Class<?> clazz) {
		Map<String, Field> fieldMap = FieldGets.getAllFields(clazz);
		return createFromFieldsIncluding(clazz, fieldMap, fieldMap.keySet());
	}


	public static Map<String, SimpleField> createFromObjectRecursive(Class<?> clazz, Collection<Class<?>> stopAtFields) {
		SimpleTree<SimpleField> fields = FieldGets.getAllFieldsRecursive(clazz, stopAtFields, false, false);

		Map<String, SimpleField> fieldMap = new HashMap<>();

		SimpleTreeUtil.traverseLeafNodes(fields, TreeTraversalOrder.POST_ORDER, (field, depth, parentBranch) -> {
			String name = field.getName();
			if(fieldMap.containsKey(name)) {
				throw new IllegalStateException("duplicate field '" + name + "' found will retrieving all fields recursively from '" + clazz + "'");
			}
			fieldMap.put(name, field);
		});
		//return createFromFieldsIncluding(clazz, namingConvention, fieldMap, fieldMap.keySet());
		return fieldMap;
	}


	public static Map<String, SimpleField> createFromObjectIncluding(Class<?> clazz, Collection<String> includingFieldNames) {
		Map<String, Field> fieldMap = FieldGets.getAllFields(clazz);
		return createFromFieldsIncluding(clazz, fieldMap, includingFieldNames);
	}


	public static Map<String, SimpleField> createFromObjectExcluding(Class<?> clazz, Collection<String> excludingFieldNames) {
		Map<String, Field> fieldMap = FieldGets.getAllFields(clazz);
		return createFromFieldsExcluding(clazz, fieldMap, excludingFieldNames);
	}


	public static <T> SimpleField createFromName(Class<?> instClass, Class<T> fieldType, Map<String, Field> fieldMap, String fieldName) {
		try {
			Field field = fieldMap.get(fieldName);
			if(field == null) {
				throw new NoSuchFieldException("'" + fieldName + "' on type: " + instClass);
			}

			SimpleFieldImpl t = new SimpleFieldImpl(field, true);

			return t;
		} catch (SecurityException | IllegalArgumentException | NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}


	public static Map<String, SimpleField> createFromFields(Class<?> instClass, Map<String, Field> fieldMap) {
		Map<String, SimpleField> resFields = new HashMap<>();

		for(Map.Entry<String, Field> objField : fieldMap.entrySet()) {
			@SuppressWarnings("unchecked")
			Class<Object> fieldType = (Class<Object>)objField.getValue().getType();
			SimpleField getSetField = createFromName(instClass, fieldType, fieldMap, objField.getKey());
			resFields.put(objField.getKey(), getSetField);
		}

		return resFields;
	}


	public static Map<String, SimpleField> createFromFieldsExcluding(Class<?> instClass, Map<String, Field> fieldMap, Collection<String> excludeFieldNames) {
		Map<String, SimpleField> resFields = new HashMap<>();

		for(Map.Entry<String, Field> objField : fieldMap.entrySet()) {
			if(excludeFieldNames.contains(objField.getKey())) {
				//continue;
			}
			else {
				@SuppressWarnings("unchecked")
				Class<Object> fieldType = (Class<Object>)objField.getValue().getType();
				SimpleField getSetField = createFromName(instClass, fieldType, fieldMap, objField.getKey());
				resFields.put(objField.getKey(), getSetField);
			}
		}

		return resFields;
	}


	public static Map<String, SimpleField> createFromFieldsIncluding(Class<?> instClass, Map<String, Field> fieldMap, Collection<String> includeFieldNames) {
		Map<String, SimpleField> resFields = new HashMap<>();

		for(String fieldName : includeFieldNames) {
			Field field = fieldMap.get(fieldName);
			if(field == null) {
				throw new NoSuchFieldError("'" + fieldName + "' on type: " + instClass);
			}
			@SuppressWarnings("unchecked")
			Class<Object> fieldType = (Class<Object>)field.getType();
			SimpleField getSetField = createFromName(instClass, fieldType, fieldMap, fieldName);
			resFields.put(fieldName, getSetField);
		}

		return resFields;
	}

}
