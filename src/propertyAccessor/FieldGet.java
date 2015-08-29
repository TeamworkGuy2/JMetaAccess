package propertyAccessor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import simpleReflect.CompoundField;
import simpleReflect.SimpleField;
import simpleReflect.SimpleFieldImpl;
import simpleTree.SimpleTree;
import simpleTree.SimpleTreeImpl;
import simpleTree.TreeBuilder;
import simpleTree.TreeUtil;
import twg2.collections.tuple.Tuples;
import twg2.collections.util.ListUtil;
import typeInfo.JavaPrimitives;

/**
 * @author TeamworkGuy2
 * @since 2015-6-27
 */
public class FieldGet {
	// package-private
	static final Map<Class<?>, Map<String, Field>> classFieldsCache = new HashMap<>();
	static final boolean cacheClassMetaData;


	static {
		boolean doCache = false;
		try {
			doCache = "true".equals(System.getProperty(FieldGet.class.getPackage().getName() + ".cacheClassMetaData", "true"));
		} catch(SecurityException se) {
			// do nothing
		}
		cacheClassMetaData = doCache;
	}


	/** Retrieve a map of all field names and their {@link Field} references for a class' inheritance tree
	 * @param clazz the class to get fields for
	 * @return all the fields of a class (as if {@link Class#getDeclaredFields()} was invoked recursively on the class and all super classes and interfaces
	 */
	public static Map<String, Field> getAllFields(Class<?> clazz) {
		Map<String, Field> dst = new HashMap<String, Field>();
		getAllFields0(clazz, new HashSet<>(), dst);
		return dst;
	}


	/** Retrieve list of {@link PropertyDefinition} for the {@link Field Fields} from a class' inheritance tree
	 * @param clazz the class to get fields for
	 * @return all the fields of a class (as if {@link Class#getDeclaredFields()} was invoked recursively on the class and all super classes and interfaces
	 */
	public static List<CompoundProperty<Object>> getAllPropertiesRecursive(Class<?> clazz, Collection<Class<?>> stopAtFieldTypes) {
		List<CompoundProperty<Object>> allFields = new ArrayList<>();

		Predicate<Field> fieldNotStaticFunc = (f) -> !Modifier.isStatic(f.getModifiers());
		Function<Field, Class<?>> fieldTypeFunc = (f) -> f.getType();
		Set<Class<?>> tmpChecked = new HashSet<>();
		Map<String, Field> baseFields = new HashMap<String, Field>();
		getAllFields0(clazz, tmpChecked, baseFields);
		Map.Entry<List<Field>, List<Field>> filteredFields = filterKnownFields(baseFields.values(), fieldNotStaticFunc, fieldTypeFunc, true, true, stopAtFieldTypes);
		allFields.addAll(ListUtil.map(filteredFields.getKey(), (f) -> new CompoundProperty<Object>(f)));

		for(Field baseField : filteredFields.getValue()) {
			TreeUtil.traverseNodesByDepthInPlace(baseField, true, (t) -> {
				if(filterKnownField(t.getType(), true, true, stopAtFieldTypes)) {
					return false;
				}

				Map<String, Field> tmpFields = getClassFields(t.getType(), tmpChecked);
				Map.Entry<List<Field>, List<Field>> filteredFieldMap = filterKnownFields(tmpFields.values(), fieldNotStaticFunc, fieldTypeFunc, true, true, stopAtFieldTypes);

				return filteredFieldMap.getValue().size() > 0;
			}, (t) -> {
				Map<String, Field> tmpFields = getClassFields(t.getType(), tmpChecked);
				Map.Entry<List<Field>, List<Field>> filteredFieldMap = filterKnownFields(tmpFields.values(), fieldNotStaticFunc, fieldTypeFunc, true, true, stopAtFieldTypes);

				List<Field> resList = new ArrayList<>(filteredFieldMap.getValue());
				resList.addAll(filteredFieldMap.getKey());

				return resList;
			}, (branch, depth, parentBranches) -> {
				// create the compound field getter/setter from the leaf field and parent field list
				CompoundProperty<Object> compoundFieldGetSet = new CompoundProperty<>();
				compoundFieldGetSet.parentToFieldAccessors.addAll2(parentBranches);
				compoundFieldGetSet.parentToFieldAccessors.add2(branch);

				allFields.add(compoundFieldGetSet);
			});
		}

		return allFields;
	}


	/** Retrieve a list of compound {@link SimpleField} for the {@link Field Fields} from a class' inheritance tree
	 * @param clazz the class to get fields for
	 * @return all the fields of a class (as if {@link Class#getDeclaredFields()} was invoked recursively on the class and all super classes and interfaces
	 */
	public static List<CompoundField<Object>> getAllFieldsRecursiveCompound(Class<?> clazz, Collection<Class<?>> stopAtFieldTypes) {
		List<CompoundField<Object>> allFields = new ArrayList<>();

		Predicate<Field> fieldNotStaticFunc = (f) -> !Modifier.isStatic(f.getModifiers());
		Function<Field, Class<?>> fieldTypeFunc = (f) -> f.getType();
		Set<Class<?>> tmpChecked = new HashSet<>();
		Map<String, Field> baseFields = new HashMap<String, Field>();
		getAllFields0(clazz, tmpChecked, baseFields);
		Map.Entry<List<Field>, List<Field>> filteredFields = filterKnownFields(baseFields.values(), fieldNotStaticFunc, fieldTypeFunc, true, true, stopAtFieldTypes);
		allFields.addAll(ListUtil.map(filteredFields.getKey(), (f) -> new CompoundField<Object>(f)));

		for(Field baseField : filteredFields.getValue()) {
			TreeUtil.traverseNodesByDepthInPlace(baseField, true, (t) -> {
				if(filterKnownField(t.getType(), true, true, stopAtFieldTypes)) {
					return false;
				}

				Map<String, Field> tmpFields = getClassFields(t.getType(), tmpChecked);
				Map.Entry<List<Field>, List<Field>> filteredFieldMap = filterKnownFields(tmpFields.values(), fieldNotStaticFunc, fieldTypeFunc, true, true, stopAtFieldTypes);

				return filteredFieldMap.getValue().size() > 0;
			}, (t) -> {
				Map<String, Field> tmpFields = getClassFields(t.getType(), tmpChecked);
				Map.Entry<List<Field>, List<Field>> filteredFieldMap = filterKnownFields(tmpFields.values(), fieldNotStaticFunc, fieldTypeFunc, true, true, stopAtFieldTypes);

				List<Field> resList = new ArrayList<>(filteredFieldMap.getValue());
				resList.addAll(filteredFieldMap.getKey());

				return resList;
			}, (branch, depth, parentBranches) -> {
				// create the compound field getter/setter from the leaf field and parent field list
				List<Field> fields = new ArrayList<>(parentBranches);
				fields.add(branch);
				CompoundField<Object> compoundField = new CompoundField<>(fields);

				allFields.add(compoundField);
			});
		}

		return allFields;
	}


	/** Retrieve a list of compound {@link SimpleField} for the {@link Field Fields} from a class' inheritance tree
	 * @param clazz the class to get fields for
	 * @return all the fields of a class (as if {@link Class#getDeclaredFields()} was invoked recursively on the class and all super classes and interfaces
	 */
	public static SimpleTree<SimpleField> getAllFieldsRecursive(Class<?> clazz, Collection<Class<?>> stopAtFieldTypes) {
		Predicate<Field> fieldNotStaticFunc = (f) -> !Modifier.isStatic(f.getModifiers());
		Function<Field, Class<?>> fieldTypeFunc = (f) -> f.getType();
		Set<Class<?>> tmpChecked = new HashSet<>();
		Map<String, Field> baseFields = new HashMap<String, Field>();
		getAllFields0(clazz, tmpChecked, baseFields);
		Map.Entry<List<Field>, List<Field>> filteredFields = filterKnownFields(baseFields.values(), fieldNotStaticFunc, fieldTypeFunc, true, true, stopAtFieldTypes);

		SimpleTree<SimpleField> allFields = new SimpleTreeImpl<>(null);

		for(Field f : filteredFields.getKey()) {
			allFields.addChild(new CompoundField<Object>(f));
		}

		for(Field baseField : filteredFields.getValue()) {
			SimpleField baseSimpleField = new SimpleFieldImpl(baseField);

			TreeBuilder.buildTree(allFields, 0, null, baseSimpleField, (t) -> {
				if(filterKnownField(t.getType(), true, true, stopAtFieldTypes)) {
					return false;
				}

				Map<String, Field> tmpFields = getClassFields(t.getType(), tmpChecked);
				Map.Entry<List<Field>, List<Field>> filteredFieldMap = filterKnownFields(tmpFields.values(), fieldNotStaticFunc, fieldTypeFunc, true, true, stopAtFieldTypes);

				return filteredFieldMap.getValue().size() > 0;
			}, (t) -> {
				Map<String, Field> tmpFields = getClassFields(t.getType(), tmpChecked);
				Map.Entry<List<Field>, List<Field>> filteredFieldMap = filterKnownFields(tmpFields.values(), fieldNotStaticFunc, fieldTypeFunc, true, true, stopAtFieldTypes);

				List<SimpleField> resList = ListUtil.map(filteredFieldMap.getValue(), (field) -> new SimpleFieldImpl(field));
				resList.addAll(ListUtil.map(filteredFieldMap.getKey(), (field) -> new SimpleFieldImpl(field)));

				return resList;
			}, false);
		}

		return allFields;
	}


	static Map<String, Field> getClassFields(Class<?> clazz, Set<Class<?>> tmpChecked) {
		if(cacheClassMetaData && classFieldsCache.containsKey(clazz)) {
			return classFieldsCache.get(clazz);
		}
		Map<String, Field> tmpFields = new HashMap<>();
		tmpChecked.clear();
		getAllFields0(clazz, tmpChecked, tmpFields);
		if(cacheClassMetaData) {
			classFieldsCache.put(clazz, tmpFields);
		}
		return tmpFields;
	}


	static void getAllFields0(Class<?> clazz, Set<Class<?>> visitedTypes, Map<String, Field> dst) {
		visitedTypes.add(clazz);

		// traverse parent class fields
		if(!clazz.isInterface()) {
			Class<?> parentClass = clazz.getSuperclass();
			if(parentClass != null) {
				getAllFields0(parentClass, visitedTypes, dst);
			}
		}

		// traverse implemented interface fields
		for(Class<?> parentType : clazz.getInterfaces()) {
			if(!visitedTypes.contains(parentType)) {
				getAllFields0(parentType, visitedTypes, dst);
			}
		}

		// add fields later, so over-ridding fields appear in the dst map
		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields) {
			dst.put(field.getName(), field);
		}
	}


	/**
	 * @param types
	 * @param filterPrimitives
	 * @param filterPrimitiveWrappers
	 * @param knownTypes
	 * @return the key list is the known values, the values list is the unknown values
	 */
	static Map.Entry<List<Class<?>>, List<Class<?>>> filterKnownFields(Iterable<Class<?>> types, Predicate<Class<?>> filter, boolean filterPrimitives, boolean filterPrimitiveWrappers, Collection<Class<?>> knownTypes) {
		return filterKnownFields(types, filter, (t) -> t, filterPrimitives, filterPrimitiveWrappers, knownTypes);
	}


	/**
	 * @return the key list is the known values, the values list is the unknown values
	 */
	static <T> Map.Entry<List<T>, List<T>> filterKnownFields(Iterable<T> values, Predicate<T> filter, Function<T, Class<?>> transformer,
			boolean filterPrimitives, boolean filterPrimitiveWrappers, Collection<Class<?>> knownTypes) {
		List<T> known = new ArrayList<>();
		List<T> unknown = new ArrayList<>();

		for(T value : values) {
			if(!filter.test(value)) {
				continue;
			}

			if(filterKnownField(transformer.apply(value), filterPrimitives, filterPrimitiveWrappers, knownTypes)) {
				known.add(value);
			}
			else {
				unknown.add(value);
			}
		}

		return Tuples.of(known, unknown);
	}


	/** check whether a type is a primitive, a primitive wrapper, or one of a set of types
	 */
	static boolean filterKnownField(Class<?> type, boolean filterPrimitives, boolean filterPrimitiveWrappers, Collection<Class<?>> knownTypes) {
		if(filterPrimitives) {
			JavaPrimitives primitive = JavaPrimitives.tryGetPrimitiveType(type);
			if(primitive != null) {
				return true;
			}
		}
		if(filterPrimitiveWrappers) {
			JavaPrimitives primitive = JavaPrimitives.tryGetWrapperType(type);
			if(primitive != null) {
				return true;
			}
		}
		if(knownTypes != null && knownTypes.contains(type)) {
			return true;
		}

		return false;
	}

}
