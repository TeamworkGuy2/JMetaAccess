package twg2.meta.simpleReflect;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import twg2.meta.fieldAccess.FieldGets;
import twg2.meta.fieldAccess.SimpleField;
import twg2.meta.fieldAccess.SimpleFieldImpl;
import twg2.treeLike.TreeTraversalOrder;
import twg2.treeLike.simpleTree.SimpleTree;
import twg2.treeLike.simpleTree.SimpleTreeUtil;

/**
 * @author TeamworkGuy2
 * @since 2015-8-24
 */
public class SimpleFields {

	// static utilities that use PropertyImpl
	public static Map<String, SimpleField> fromClass(Class<?> clazz) {
		Map<String, Field> fieldMap = FieldGets.getAllFields(clazz);
		return fromFieldsIncluding(clazz, fieldMap, fieldMap.keySet());
	}


	public static Map<String, SimpleField> fromClassRecursiveFlatten(Class<?> clazz, Collection<Class<?>> stopAtFields) {
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


	public static Map<String, SimpleField> fromClassIncluding(Class<?> clazz, Collection<String> includingFieldNames) {
		Map<String, Field> fieldMap = FieldGets.getAllFields(clazz);
		return fromFieldsIncluding(clazz, fieldMap, includingFieldNames);
	}


	public static Map<String, SimpleField> fromClassExcluding(Class<?> clazz, Collection<String> excludingFieldNames) {
		Map<String, Field> fieldMap = FieldGets.getAllFields(clazz);
		return fromFieldsExcluding(clazz, fieldMap, excludingFieldNames);
	}


	public static <T> SimpleField fromName(Class<?> instClass, Map<String, Field> fieldMap, String fieldName) {
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


	public static Map<String, SimpleField> fromFields(Class<?> instClass, Map<String, Field> fieldMap) {
		Map<String, SimpleField> resFields = new HashMap<>();

		for(Map.Entry<String, Field> objField : fieldMap.entrySet()) {
			SimpleField getSetField = fromName(instClass, fieldMap, objField.getKey());
			resFields.put(objField.getKey(), getSetField);
		}

		return resFields;
	}


	public static Map<String, SimpleField> fromFieldsIncluding(Class<?> instClass, Map<String, Field> fieldMap, Collection<String> includeFieldNames) {
		Map<String, SimpleField> resFields = new HashMap<>();

		for(String fieldName : includeFieldNames) {
			Field field = fieldMap.get(fieldName);
			if(field == null) {
				throw new NoSuchFieldError("'" + fieldName + "' on type: " + instClass);
			}
			SimpleField getSetField = fromName(instClass, fieldMap, fieldName);
			resFields.put(fieldName, getSetField);
		}

		return resFields;
	}


	public static Map<String, SimpleField> fromFieldsExcluding(Class<?> instClass, Map<String, Field> fieldMap, Collection<String> excludeFieldNames) {
		Map<String, SimpleField> resFields = new HashMap<>();

		for(Map.Entry<String, Field> objField : fieldMap.entrySet()) {
			if(excludeFieldNames.contains(objField.getKey())) {
				//continue;
			}
			else {
				SimpleField getSetField = fromName(instClass, fieldMap, objField.getKey());
				resFields.put(objField.getKey(), getSetField);
			}
		}

		return resFields;
	}

}
