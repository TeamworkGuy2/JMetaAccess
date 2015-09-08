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

import twg2.collections.tuple.Tuples;
import twg2.collections.util.ListUtil;
import twg2.collections.util.MapUtil;
import twg2.primitiveIoTypes.JavaPrimitive;
import twg2.treeLike.TreeBuilder;
import twg2.treeLike.TreeTraversalOrder;
import twg2.treeLike.TreeTraverse;
import twg2.treeLike.parameters.TreePathTraverseParameters;
import twg2.treeLike.simpleTree.SimpleKeyTreeImpl;
import twg2.treeLike.simpleTree.SimpleTreeImpl;

/**
 * @author TeamworkGuy2
 * @since 2015-6-27
 */
public class FieldGets {
	// package-private
	static final Map<Class<?>, Map<String, Field>> classFieldsCache = new HashMap<>();
	static final boolean cacheClassMetaData;


	static {
		boolean doCache = false;
		try {
			doCache = "true".equals(System.getProperty(FieldGets.class.getPackage().getName() + ".cacheClassMetaData", "true"));
		} catch(SecurityException se) {
			// do nothing
		}
		cacheClassMetaData = doCache;
	}


	public static List<SimpleField> getSimpleFields(Class<?> clazz) {
		return getSimpleFields(clazz, true);
	}


	public static List<SimpleField> getSimpleFields(Class<?> clazz, boolean excludeStaticFields) {
		ArrayList<SimpleField> simpleFields = new ArrayList<>();
		Map<String, Field> fields = getAllFields(clazz);
		for(Map.Entry<String, Field> value : fields.entrySet()) {
			if(excludeStaticFields && Modifier.isStatic(value.getValue().getModifiers())) {
				continue;
			}
			simpleFields.add(new SimpleFieldImpl(value.getValue()));
		}
		return simpleFields;
	}


	public static Map<String, SimpleField> getSimpleFieldsByName(Class<?> clazz) {
		return getSimpleFieldsByName(clazz, true);
	}


	public static Map<String, SimpleField> getSimpleFieldsByName(Class<?> clazz, boolean excludeStaticFields) {
		Map<String, SimpleField> simpleFields = new HashMap<>();
		Map<String, Field> fields = getAllFields(clazz);
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
	public static SimpleKeyTreeImpl<String, SimpleField> getAllFieldMapRecursive(Class<?> clazz, Collection<Class<?>> stopAtFieldTypes, boolean filterPrimitives, boolean filterPrimitiveWrappers) {
		Set<Class<?>> tmpChecked = new HashSet<>();
		Map<String, Field> baseFields = new HashMap<String, Field>();
		// these are reused for all field filters
		List<Field> tmpKnownFilteredFields = new ArrayList<>();
		List<Field> tmpUnknownFilteredFields = new ArrayList<>();

		getAllFields0(clazz, tmpChecked, baseFields);
		boolean filterOutStaticFields = true;
		filterKnownFields(baseFields.values(), filterOutStaticFields, filterPrimitives, filterPrimitiveWrappers, stopAtFieldTypes, tmpKnownFilteredFields, tmpUnknownFilteredFields);
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
				if(filterKnownField(t.getValue().getType(), true, true, stopAtFieldTypes)) {
					return false;
				}

				tmpChecked.clear();
				Map<String, Field> tmpFields = getClassFields(t.getValue().getType(), tmpChecked);
				filterKnownFields(tmpFields.values(), filterOutStaticFields, filterPrimitives, filterPrimitiveWrappers, stopAtFieldTypes, tmpKnownFilteredFields, tmpUnknownFilteredFields);
				int unknownFieldCount = tmpUnknownFilteredFields.size();

				tmpKnownFilteredFields.clear();
				tmpUnknownFilteredFields.clear();

				return unknownFieldCount > 0;
			}, (t) -> {
				tmpChecked.clear();
				Map<String, Field> tmpFields = getClassFields(t.getValue().getType(), tmpChecked);
				filterKnownFields(tmpFields.values(), filterOutStaticFields, filterPrimitives, filterPrimitiveWrappers, stopAtFieldTypes, tmpKnownFilteredFields, tmpUnknownFilteredFields);

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
	public static SimpleTreeImpl<SimpleField> getAllFieldsRecursive(Class<?> clazz, Collection<Class<?>> stopAtFieldTypes, boolean filterPrimitives, boolean filterPrimitiveWrappers) {
		Set<Class<?>> tmpChecked = new HashSet<>();
		Map<String, Field> baseFields = new HashMap<String, Field>();
		// these are reused for all field filters
		List<Field> tmpKnownFilteredFields = new ArrayList<>();
		List<Field> tmpUnknownFilteredFields = new ArrayList<>();

		getAllFields0(clazz, tmpChecked, baseFields);
		boolean filterOutStaticFields = true;
		filterKnownFields(baseFields.values(), filterOutStaticFields, filterPrimitives, filterPrimitiveWrappers, stopAtFieldTypes, tmpKnownFilteredFields, tmpUnknownFilteredFields);
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
				if(filterKnownField(t.getType(), true, true, stopAtFieldTypes)) {
					return false;
				}

				tmpChecked.clear();
				Map<String, Field> tmpFields = getClassFields(t.getType(), tmpChecked);
				filterKnownFields(tmpFields.values(), filterOutStaticFields, filterPrimitives, filterPrimitiveWrappers, stopAtFieldTypes, tmpKnownFilteredFields, tmpUnknownFilteredFields);
				int unknownFieldCount = tmpUnknownFilteredFields.size();

				tmpKnownFilteredFields.clear();
				tmpUnknownFilteredFields.clear();

				return unknownFieldCount > 0;
			}, (t) -> {
				tmpChecked.clear();
				Map<String, Field> tmpFields = getClassFields(t.getType(), tmpChecked);
				filterKnownFields(tmpFields.values(), filterOutStaticFields, filterPrimitives, filterPrimitiveWrappers, stopAtFieldTypes, tmpKnownFilteredFields, tmpUnknownFilteredFields);

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
	public static List<CompoundField<Object>> getAllFieldsRecursiveCompound(Class<?> clazz, Collection<Class<?>> stopAtFieldTypes) {
		Set<Class<?>> tmpChecked = new HashSet<>();
		Map<String, Field> baseFields = new HashMap<String, Field>();
		// these are reused for all field filters
		List<Field> tmpKnownFilteredFields = new ArrayList<>();
		List<Field> tmpUnknownFilteredFields = new ArrayList<>();

		List<CompoundField<Object>> allFields = new ArrayList<>();
		boolean filterOutStaticFields = true;

		getAllFields0(clazz, tmpChecked, baseFields);
		filterKnownFields(baseFields.values(), filterOutStaticFields, true, true, stopAtFieldTypes, tmpKnownFilteredFields, tmpUnknownFilteredFields);
		allFields.addAll(ListUtil.map(tmpKnownFilteredFields, (f) -> new CompoundField<Object>(f)));
		Field[] initialBaseFields = tmpUnknownFilteredFields.toArray(new Field[tmpUnknownFilteredFields.size()]);

		tmpKnownFilteredFields.clear();
		tmpUnknownFilteredFields.clear();

		for(Field baseField : initialBaseFields) {
			TreeTraverse.traverseTreeWithPath(TreePathTraverseParameters.of(baseField, true, TreeTraversalOrder.POST_ORDER, (t) -> {
				if(filterKnownField(t.getType(), true, true, stopAtFieldTypes)) {
					return false;
				}

				tmpChecked.clear();
				Map<String, Field> tmpFields = getClassFields(t.getType(), tmpChecked);
				filterKnownFields(tmpFields.values(), filterOutStaticFields, true, true, stopAtFieldTypes, tmpKnownFilteredFields, tmpUnknownFilteredFields);
				int unknownFieldCount = tmpUnknownFilteredFields.size();

				tmpKnownFilteredFields.clear();
				tmpUnknownFilteredFields.clear();

				return unknownFieldCount > 0;
			}, (t) -> {
				tmpChecked.clear();
				Map<String, Field> tmpFields = getClassFields(t.getType(), tmpChecked);
				filterKnownFields(tmpFields.values(), filterOutStaticFields, true, true, stopAtFieldTypes, tmpKnownFilteredFields, tmpUnknownFilteredFields);

				List<Field> resList = new ArrayList<>(tmpUnknownFilteredFields);
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


	/** Retrieve a map of all field names and their {@link Field} references for a class' inheritance tree
	 * @param clazz the class to get fields for
	 * @return all the fields of a class (as if {@link Class#getDeclaredFields()} was invoked recursively on the class and all super classes and interfaces
	 */
	public static Map<String, Field> getAllFields(Class<?> clazz) {
		Map<String, Field> dst = getClassFields(clazz, new HashSet<>());
		return dst;
	}


	public static Map<String, Field> getClassFields(Class<?> clazz, Set<Class<?>> tmpChecked) {
		if(cacheClassMetaData && classFieldsCache.containsKey(clazz)) {
			return classFieldsCache.get(clazz);
		}
		Map<String, Field> tmpFields = new HashMap<>();
		getAllFields0(clazz, tmpChecked, tmpFields);
		if(cacheClassMetaData) {
			classFieldsCache.put(clazz, tmpFields);
		}
		return tmpFields;
	}


	// package-private
	public static void getAllFields0(Class<?> clazz, Set<Class<?>> visitedTypes, Map<String, Field> dst) {
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

		// add fields later, so over-riding fields appear in the destination map
		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields) {
			dst.put(field.getName(), field);
		}
	}


	/**
	 * @param dstKnown the destination list to store the filtered known values in
	 * @param dstUnknown the destination list to store the filtered unknown values in
	 */
	public static void filterKnownFields(Iterable<Field> values, boolean excludeStaticFields, boolean filterPrimitives, boolean filterPrimitiveWrappers,
			Collection<Class<?>> knownTypes, List<Field> dstKnown, List<Field> dstUnknown) {

		for(Field value : values) {
			if(excludeStaticFields && Modifier.isStatic(value.getModifiers())) {
				continue;
			}

			if(filterKnownField(value.getType(), filterPrimitives, filterPrimitiveWrappers, knownTypes)) {
				dstKnown.add(value);
			}
			else {
				dstUnknown.add(value);
			}
		}
	}


	/** check whether a type is a primitive, a primitive wrapper, or one of a set of types
	 */
	public static boolean filterKnownField(Class<?> type, boolean filterPrimitives, boolean filterPrimitiveWrappers, Collection<Class<?>> knownTypes) {
		if(type == null) {
			return false;
		}

		if(filterPrimitives) {
			if(type.isPrimitive()) {
				return true;
			}
		}
		if(filterPrimitiveWrappers) {
			JavaPrimitive primitive = JavaPrimitive.tryGetWrapperType(type);
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
