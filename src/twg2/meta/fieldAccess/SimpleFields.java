package twg2.meta.fieldAccess;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.AbstractMap;
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
import twg2.treeLike.TreeBuilder;
import twg2.treeLike.TreeTraversalOrder;
import twg2.treeLike.TreeTraverse;
import twg2.treeLike.parameters.TreePathTraverseParameters;
import twg2.treeLike.simpleTree.SimpleKeyTreeImpl;
import twg2.treeLike.simpleTree.SimpleTree;
import twg2.treeLike.simpleTree.SimpleTreeImpl;
import twg2.treeLike.simpleTree.SimpleTreeUtil;
import twg2.tuple.Tuples;

/**
 * @author TeamworkGuy2
 * @since 2015-8-24
 */
public class SimpleFields {

	// static utilities that use PropertyImpl
	public static Map<String, SimpleField> fromClass(Class<?> clazz) {
		Map<String, Field> fieldMap = FieldGets.getFields(clazz);
		return fromFieldsIncluding(clazz, fieldMap, fieldMap.keySet());
	}


	public static Map<String, SimpleField> fromClassRecursiveFlatten(Class<?> clazz, Collection<Class<?>> stopAtFields) {
		SimpleTree<SimpleField> fields = getFieldsRecursive(clazz, stopAtFields, false, false);

		Map<String, SimpleField> fieldMap = new HashMap<>();

		SimpleTreeUtil.traverseLeafNodes(fields, TreeTraversalOrder.POST_ORDER, (field, depth, parentBranch) -> {
			String name = field.getName();
			if(fieldMap.containsKey(name)) {
				throw new IllegalStateException("duplicate field '" + name + "' found will retrieving all fields recursively from '" + clazz + "'");
			}
			fieldMap.put(name, field);
		});
		return fieldMap;
	}


	public static Map<String, SimpleField> fromClassIncluding(Class<?> clazz, Iterable<String> includingFieldNames) {
		Map<String, Field> fieldMap = FieldGets.getFields(clazz);
		return fromFieldsIncluding(clazz, fieldMap, includingFieldNames);
	}


	public static Map<String, SimpleField> fromClassExcluding(Class<?> clazz, Collection<String> excludingFieldNames) {
		Map<String, Field> fieldMap = FieldGets.getFields(clazz);
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
			SimpleField getSetField = _fromName(instClass, objField.getKey(), objField.getValue());
			resFields.put(objField.getKey(), getSetField);
		}

		return resFields;
	}


	public static Map<String, SimpleField> fromFieldsIncluding(Class<?> instClass, Map<String, Field> fieldMap, Iterable<String> includeFieldNames) {
		Map<String, SimpleField> resFields = new HashMap<>();

		for(String fieldName : includeFieldNames) {
			Field field = fieldMap.get(fieldName);
			if(field == null) {
				throw new NoSuchFieldError("'" + fieldName + "' on type: " + instClass);
			}
			SimpleField getSetField = _fromName(instClass, fieldName, field);
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
				SimpleField getSetField = _fromName(instClass, objField.getKey(), objField.getValue());
				resFields.put(objField.getKey(), getSetField);
			}
		}

		return resFields;
	}


	/** {@link #getSimpleFields(Class, boolean)} with static fields filtered out
	 */
	public static List<SimpleField> getSimpleFields(Class<?> clazz) {
		return getSimpleFields(clazz, true);
	}


	/** {@link FieldGets#getFields(Class)} with the option to filter out static fields and create {@link SimpleField} wrappers for the resulting fields
	 */
	public static List<SimpleField> getSimpleFields(Class<?> clazz, boolean excludeStaticFields) {
		ArrayList<SimpleField> simpleFields = new ArrayList<>();
		Map<String, Field> fields = FieldGets.getFields(clazz);
		for(Map.Entry<String, Field> value : fields.entrySet()) {
			if(excludeStaticFields && Modifier.isStatic(value.getValue().getModifiers())) {
				continue;
			}
			simpleFields.add(new SimpleFieldImpl(value.getValue()));
		}
		return simpleFields;
	}


	/** {@link #getSimpleField(Class, String, boolean)} with static fields filtered out
	 */
	public static SimpleField getSimpleField(Class<?> clazz, String fieldName) {
		return getSimpleField(clazz, fieldName, true);
	}


	/** Use {@link FieldGets#getFields(Class)} and search for a field named {@code fieldName} with the option to filter out static fields.
	 * If found, create a {@link SimpleField} wrapper for the matching field and return it, else return null.
	 */
	public static SimpleField getSimpleField(Class<?> clazz, String fieldName, boolean excludeStaticFields) {
		Map<String, Field> fields = FieldGets.getFields(clazz);
		for(Map.Entry<String, Field> value : fields.entrySet()) {
			if(excludeStaticFields && Modifier.isStatic(value.getValue().getModifiers())) {
				continue;
			}
			if(value.getKey().equals(fieldName)) {
				return new SimpleFieldImpl(value.getValue());
			}
		}
		return null;
	}


	public static Map<String, SimpleField> getSimpleFieldsByName(Class<?> clazz) {
		return getSimpleFieldsByName(clazz, true);
	}


	public static Map<String, SimpleField> getSimpleFieldsByName(Class<?> clazz, boolean excludeStaticFields) {
		Map<String, SimpleField> simpleFields = new HashMap<>();
		Map<String, Field> fields = FieldGets.getFields(clazz);
		for(Map.Entry<String, Field> value : fields.entrySet()) {
			if(excludeStaticFields && Modifier.isStatic(value.getValue().getModifiers())) {
				continue;
			}
			simpleFields.put(value.getKey(), new SimpleFieldImpl(value.getValue()));
		}
		return simpleFields;
	}


	/** Retrieve a list of compound {@link SimpleField} for the {@link Field Fields} from a class' inheritance tree
	 * @param clazz the class to get fields for
	 * @return all the fields of a class (as if {@link Class#getDeclaredFields()} was invoked recursively on the class and all super classes and interfaces
	 */
	public static SimpleKeyTreeImpl<String, SimpleField> getFieldMapRecursive(Class<?> clazz, Collection<Class<?>> stopAtFieldTypes, boolean filterPrimitives, boolean filterPrimitiveWrappers) {
		Set<Class<?>> tmpChecked = new HashSet<>();
		Map<String, Field> baseFields = new HashMap<String, Field>();
		// these are reused for all field filters
		List<Field> tmpKnownFilteredFields = new ArrayList<>();
		List<Field> tmpUnknownFilteredFields = new ArrayList<>();

		FieldGets._getAllFields(clazz, tmpChecked, baseFields);
		boolean filterOutStaticFields = true;
		FieldGets.filterKnownFields(baseFields.values(), filterOutStaticFields, filterPrimitives, filterPrimitiveWrappers, stopAtFieldTypes, tmpKnownFilteredFields, tmpUnknownFilteredFields);
		Field[] initialKnownFields = tmpKnownFilteredFields.toArray(new Field[tmpKnownFilteredFields.size()]);
		Field[] initialBaseFields = tmpUnknownFilteredFields.toArray(new Field[tmpUnknownFilteredFields.size()]);

		tmpKnownFilteredFields.clear();
		tmpUnknownFilteredFields.clear();

		SimpleKeyTreeImpl<String, SimpleField> allFields = new SimpleKeyTreeImpl<>(null, null);

		for(Field f : initialKnownFields) {
			allFields.addChild(f.getName(), new SimpleFieldImpl(f));
		}

		for(Field baseField : initialBaseFields) {
			SimpleField baseSimpleField = new SimpleFieldImpl(baseField);

			TreeBuilder.buildTree(allFields, 0, null, new AbstractMap.SimpleImmutableEntry<>(baseSimpleField.getName(), baseSimpleField), (t) -> {
				// primitive types have no children, filter them out regardless of filter flags
				if(FieldGets.filterKnownField(t.getValue().getType(), true, true, stopAtFieldTypes)) {
					return false;
				}

				tmpChecked.clear();
				Map<String, Field> tmpFields = FieldGets.getFields(t.getValue().getType(), tmpChecked);
				FieldGets.filterKnownFields(tmpFields.values(), filterOutStaticFields, filterPrimitives, filterPrimitiveWrappers, stopAtFieldTypes, tmpKnownFilteredFields, tmpUnknownFilteredFields);
				int unknownFieldCount = tmpUnknownFilteredFields.size();

				tmpKnownFilteredFields.clear();
				tmpUnknownFilteredFields.clear();

				return unknownFieldCount > 0;
			}, (t) -> {
				tmpChecked.clear();
				Map<String, Field> tmpFields = FieldGets.getFields(t.getValue().getType(), tmpChecked);
				FieldGets.filterKnownFields(tmpFields.values(), filterOutStaticFields, filterPrimitives, filterPrimitiveWrappers, stopAtFieldTypes, tmpKnownFilteredFields, tmpUnknownFilteredFields);

				Map<String, SimpleField> resList = MapUtil.map(tmpUnknownFilteredFields, (f) -> Tuples.of(f.getName(), new SimpleFieldImpl(f)));
				MapUtil.map(tmpKnownFilteredFields, (f) -> Tuples.of(f.getName(), new SimpleFieldImpl(f)), resList);

				tmpKnownFilteredFields.clear();
				tmpUnknownFilteredFields.clear();

				return resList.entrySet();
			}, false);
		}

		return allFields;
	}


	/** Retrieve a list of compound {@link SimpleField} for the {@link Field Fields} from a class' inheritance tree
	 * @param clazz the class to get fields for
	 * @return all the fields of a class (as if {@link Class#getDeclaredFields()} was invoked recursively on the class and all super classes and interfaces
	 */
	public static SimpleTreeImpl<SimpleField> getFieldsRecursive(Class<?> clazz, Collection<Class<?>> stopAtFieldTypes, boolean filterPrimitives, boolean filterPrimitiveWrappers) {
		Set<Class<?>> tmpChecked = new HashSet<>();
		Map<String, Field> baseFields = new HashMap<String, Field>();
		// these are reused for all field filters
		List<Field> tmpKnownFilteredFields = new ArrayList<>();
		List<Field> tmpUnknownFilteredFields = new ArrayList<>();

		FieldGets._getAllFields(clazz, tmpChecked, baseFields);
		boolean filterOutStaticFields = true;
		FieldGets.filterKnownFields(baseFields.values(), filterOutStaticFields, filterPrimitives, filterPrimitiveWrappers, stopAtFieldTypes, tmpKnownFilteredFields, tmpUnknownFilteredFields);
		Field[] initialKnownFields = tmpKnownFilteredFields.toArray(new Field[tmpKnownFilteredFields.size()]);
		Field[] initialBaseFields = tmpUnknownFilteredFields.toArray(new Field[tmpUnknownFilteredFields.size()]);

		tmpKnownFilteredFields.clear();
		tmpUnknownFilteredFields.clear();

		SimpleTreeImpl<SimpleField> allFields = new SimpleTreeImpl<>(null);

		for(Field f : initialKnownFields) {
			allFields.addChild(new SimpleFieldImpl(f));
		}

		for(Field baseField : initialBaseFields) {
			SimpleField baseSimpleField = new SimpleFieldImpl(baseField);

			TreeBuilder.buildTree(allFields, 0, null, baseSimpleField, (t) -> {
				// primitive types have no children, filter them out regardless of filter flags
				if(FieldGets.filterKnownField(t.getType(), true, true, stopAtFieldTypes)) {
					return false;
				}

				tmpChecked.clear();
				Map<String, Field> tmpFields = FieldGets.getFields(t.getType(), tmpChecked);
				FieldGets.filterKnownFields(tmpFields.values(), filterOutStaticFields, filterPrimitives, filterPrimitiveWrappers, stopAtFieldTypes, tmpKnownFilteredFields, tmpUnknownFilteredFields);
				int unknownFieldCount = tmpUnknownFilteredFields.size();

				tmpKnownFilteredFields.clear();
				tmpUnknownFilteredFields.clear();

				return unknownFieldCount > 0;
			}, (t) -> {
				tmpChecked.clear();
				Map<String, Field> tmpFields = FieldGets.getFields(t.getType(), tmpChecked);
				FieldGets.filterKnownFields(tmpFields.values(), filterOutStaticFields, filterPrimitives, filterPrimitiveWrappers, stopAtFieldTypes, tmpKnownFilteredFields, tmpUnknownFilteredFields);

				List<SimpleField> resList = ListUtil.map(tmpUnknownFilteredFields, SimpleFieldImpl::new);
				ListUtil.map(tmpKnownFilteredFields, SimpleFieldImpl::new, resList);

				tmpKnownFilteredFields.clear();
				tmpUnknownFilteredFields.clear();

				return resList;
			}, false);
		}

		return allFields;
	}


	/** Retrieve a list of compound {@link SimpleField} for the {@link Field Fields} from a class' inheritance tree.
	 * 'Compound' meaning all the returned fields can accessed their values given a single root object.
	 * @param clazz the class to get fields for
	 * @return all the fields of a class (as if {@link Class#getDeclaredFields()} was invoked recursively on the class and all super classes and interfaces
	 */
	public static List<CompoundField<Object>> getCompoundFieldsRecursive(Class<?> clazz, Collection<Class<?>> stopAtFieldTypes) {
		Set<Class<?>> tmpChecked = new HashSet<>();
		Map<String, Field> baseFields = new HashMap<String, Field>();
		// these are reused for all field filters
		List<Field> tmpKnownFilteredFields = new ArrayList<>();
		List<Field> tmpUnknownFilteredFields = new ArrayList<>();

		List<CompoundField<Object>> allFields = new ArrayList<>();
		boolean filterOutStaticFields = true;

		FieldGets._getAllFields(clazz, tmpChecked, baseFields);
		FieldGets.filterKnownFields(baseFields.values(), filterOutStaticFields, true, true, stopAtFieldTypes, tmpKnownFilteredFields, tmpUnknownFilteredFields);
		allFields.addAll(ListUtil.map(tmpKnownFilteredFields, (f) -> new CompoundField<Object>(f)));
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
				List<Field> fields = new ArrayList<>(parentBranches);
				fields.add(branch);
				CompoundField<Object> compoundField = new CompoundField<>(fields);

				allFields.add(compoundField);
			}));
		}

		return allFields;
	}


	private static <T> SimpleField _fromName(Class<?> instClass, String fieldName, Field field) {
		try {
			if(field == null) {
				throw new NoSuchFieldException("'" + fieldName + "' on type: " + instClass);
			}

			SimpleFieldImpl t = new SimpleFieldImpl(field, true);

			return t;
		} catch (SecurityException | IllegalArgumentException | NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

}
