package twg2.meta.propertyAccessor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import twg2.collections.builder.ListUtil;
import twg2.collections.builder.MapUtil;
import twg2.collections.dataStructures.BaseList;
import twg2.meta.fieldAccess.FieldGets;
import twg2.treeLike.TreeTraversalOrder;
import twg2.treeLike.TreeTraverse;
import twg2.treeLike.parameters.TreePathTraverseParameters;
import twg2.tuple.Entries;

/**
 * @author TeamworkGuy2
 * @since 2015-7-18
 */
public class PropertyFactory {

	// static utilities that use PropertyImpl
	public static Map<String, PropertyDefinition<Object>> fromObject(Class<?> clazz, PropertyNamer namingConvention) {
		Map<String, Field> fieldMap = FieldGets.getFields(clazz);
		return fromFieldsIncluding(clazz, namingConvention, fieldMap, fieldMap.keySet());
	}


	public static Map<String, PropertyDefinition<Object>> fromObjectRecursive(Class<?> clazz, PropertyNamer namingConvention, Collection<Class<?>> stopAtFields) {
		List<CompoundProperty<Object>> fields = getAllPropertiesRecursive(clazz, stopAtFields);
		Map<String, PropertyDefinition<Object>> fieldMap = MapUtil.map(fields, (f) -> Entries.of(f.getFieldName(), f));
		return fieldMap;
	}


	public static Map<String, PropertyDefinition<Object>> fromObjectIncluding(Class<?> clazz, PropertyNamer namingConvention, Collection<String> includingFieldNames) {
		Map<String, Field> fieldMap = FieldGets.getFields(clazz);
		return fromFieldsIncluding(clazz, namingConvention, fieldMap, includingFieldNames);
	}


	public static Map<String, PropertyDefinition<Object>> fromObjectExcluding(Class<?> clazz, PropertyNamer namingConvention, Collection<String> excludingFieldNames) {
		Map<String, Field> fieldMap = FieldGets.getFields(clazz);
		return fromFieldsExcluding(clazz, namingConvention, fieldMap, excludingFieldNames);
	}


	public static <T> PropertyDefinition<T> fromName(Class<?> instClass, Class<T> fieldType, PropertyNamer namingConvention, Map<String, Field> fieldMap, String fieldName) {
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


	public static Map<String, PropertyDefinition<Object>> fromFields(Class<?> instClass, PropertyNamer namingConvention, Map<String, Field> fieldMap) {
		Map<String, PropertyDefinition<Object>> resFields = new HashMap<>();

		for(Map.Entry<String, Field> objField : fieldMap.entrySet()) {
			@SuppressWarnings("unchecked")
			Class<Object> fieldType = (Class<Object>)objField.getValue().getType();
			PropertyDefinition<Object> getSetField = fromName(instClass, fieldType, namingConvention, fieldMap, objField.getKey());
			resFields.put(objField.getKey(), getSetField);
		}

		return resFields;
	}


	public static Map<String, PropertyDefinition<Object>> fromFieldsExcluding(Class<?> instClass, PropertyNamer namingConvention, Map<String, Field> fieldMap, Collection<String> excludeFieldNames) {
		Map<String, PropertyDefinition<Object>> resFields = new HashMap<>();

		for(Map.Entry<String, Field> objField : fieldMap.entrySet()) {
			if(excludeFieldNames.contains(objField.getKey())) {
				//continue;
			}
			else {
				@SuppressWarnings("unchecked")
				Class<Object> fieldType = (Class<Object>)objField.getValue().getType();
				PropertyDefinition<Object> getSetField = fromName(instClass, fieldType, namingConvention, fieldMap, objField.getKey());
				resFields.put(objField.getKey(), getSetField);
			}
		}

		return resFields;
	}


	public static Map<String, PropertyDefinition<Object>> fromFieldsIncluding(Class<?> instClass, PropertyNamer namingConvention, Map<String, Field> fieldMap, Collection<String> includeFieldNames) {
		Map<String, PropertyDefinition<Object>> resFields = new HashMap<>();

		for(String fieldName : includeFieldNames) {
			Field field = fieldMap.get(fieldName);
			if(field == null) {
				throw new NoSuchFieldError("'" + fieldName + "' on type: " + instClass);
			}
			@SuppressWarnings("unchecked")
			Class<Object> fieldType = (Class<Object>)field.getType();
			PropertyDefinition<Object> getSetField = fromName(instClass, fieldType, namingConvention, fieldMap, fieldName);
			resFields.put(fieldName, getSetField);
		}

		return resFields;
	}


	/** Retrieve list of {@link PropertyDefinition} for the {@link Field Fields} from a class' inheritance tree
	 * @param clazz the class to get fields for
	 * @return all the fields of a class (as if {@link Class#getDeclaredFields()} was invoked recursively on the class and all super classes and interfaces
	 */
	public static List<CompoundProperty<Object>> getAllPropertiesRecursive(Class<?> clazz, Collection<Class<?>> stopAtFieldTypes) {
		Set<Class<?>> tmpChecked = new HashSet<>();
		Map<String, Field> baseFields = new HashMap<String, Field>();
		// these are reused for all field filters
		List<Field> tmpKnownFilteredFields = new ArrayList<>();
		List<Field> tmpUnknownFilteredFields = new ArrayList<>();

		List<CompoundProperty<Object>> allFields = new ArrayList<>();
		boolean filterOutStaticFields = true;

		FieldGets._getAllFields(clazz, tmpChecked, baseFields);
		FieldGets.filterKnownFields(baseFields.values(), filterOutStaticFields, true, true, stopAtFieldTypes, tmpKnownFilteredFields, tmpUnknownFilteredFields);
		allFields.addAll(ListUtil.map(tmpKnownFilteredFields, CompoundProperty<Object>::new));
		Field[] initialBaseFields = tmpUnknownFilteredFields.toArray(new Field[tmpUnknownFilteredFields.size()]);

		tmpKnownFilteredFields.clear();
		tmpUnknownFilteredFields.clear();

		for(Field baseField : initialBaseFields) {
			TreeTraverse.traverseTreeWithPath(TreePathTraverseParameters.of(baseField, true, TreeTraversalOrder.POST_ORDER, (t) -> {
				if(FieldGets.filterKnownField(t.getType(), true, true, stopAtFieldTypes)) {
					return false;
				}

				tmpChecked.clear();
				Map<String, Field> tmpFields = FieldGets.getFields(t.getType(), tmpChecked);
				FieldGets.filterKnownFields(tmpFields.values(), filterOutStaticFields, true, true, stopAtFieldTypes, tmpKnownFilteredFields, tmpUnknownFilteredFields);
				int unknownFieldCount = tmpUnknownFilteredFields.size();

				tmpKnownFilteredFields.clear();
				tmpUnknownFilteredFields.clear();

				return unknownFieldCount > 0;
			}, (t) -> {
				tmpChecked.clear();
				Map<String, Field> tmpFields = FieldGets.getFields(t.getType(), tmpChecked);
				FieldGets.filterKnownFields(tmpFields.values(), filterOutStaticFields, true, true, stopAtFieldTypes, tmpKnownFilteredFields, tmpUnknownFilteredFields);

				BaseList<Field> resList = new BaseList<>(tmpUnknownFilteredFields);
				resList.addAll(tmpKnownFilteredFields);

				tmpKnownFilteredFields.clear();
				tmpUnknownFilteredFields.clear();

				return resList;
			}).setConsumerTreePath((branch, depth, parentBranches) -> {
				// create the compound field getter/setter from the leaf field and parent field list
				CompoundProperty<Object> compoundFieldGetSet = new CompoundProperty<>();
				compoundFieldGetSet.hierarchicalAccessors.addAll2(parentBranches);
				compoundFieldGetSet.hierarchicalAccessors.add2(branch);

				allFields.add(compoundFieldGetSet);
			}));
		}

		return allFields;
	}

}
