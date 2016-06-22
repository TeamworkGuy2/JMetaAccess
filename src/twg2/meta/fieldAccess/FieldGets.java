package twg2.meta.fieldAccess;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import twg2.primitiveIoTypes.JPrimitiveType;

/** Helpers for creating {@link SimpleField SimpleFields} and gathering all fields from class hierarchies
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


	/** Retrieve a map of all field names and their {@link Field} references for a class' inheritance tree
	 * @param clazz the class to get fields for
	 * @return all the fields of a class (as if {@link Class#getDeclaredFields()} was invoked recursively on the class and all super classes and interfaces)
	 */
	public static Map<String, Field> getFields(Class<?> clazz) {
		return getFields(clazz, null);
	}


	/** Get all class fields, including parent classes and interfaces
	 * @param clazz get all fields of this class
	 * @param tmpChecked a temporary set of all visited parent classes, these classes are not visited when encountered in the class' hierarchy
	 * @return a new or existing cached map of all fields in the specified class
	 */
	public static Map<String, Field> getFields(Class<?> clazz, Set<Class<?>> tmpChecked) {
		if(cacheClassMetaData && classFieldsCache.containsKey(clazz)) {
			return classFieldsCache.get(clazz);
		}
		Map<String, Field> tmpFields = new HashMap<>();
		_getAllFields(clazz, tmpChecked, tmpFields);
		if(cacheClassMetaData) {
			classFieldsCache.put(clazz, tmpFields);
		}
		return tmpFields;
	}


	/**
	 * @see #getField(Class, String, Set)
	 */
	public static Field getField(Class<?> clazz, String fieldName) {
		return getField(clazz, fieldName, null);
	}


	/** Get a class field by name, search parent classes and interfaces
	 * @param clazz get a field from this class
	 * @param fieldName the name of the field to search for
	 * @param tmpChecked a temporary set of all visited parent classes, these classes are not visited when encountered in the class' hierarchy
	 * @return a new or existing cached map of all fields in the specified class
	 */
	public static Field getField(Class<?> clazz, String fieldName, Set<Class<?>> tmpChecked) {
		if(cacheClassMetaData && classFieldsCache.containsKey(clazz)) {
			return classFieldsCache.get(clazz).get(fieldName);
		}
		Map<String, Field> tmpFields = new HashMap<>();
		_getAllFields(clazz, tmpChecked, tmpFields);
		if(cacheClassMetaData) {
			classFieldsCache.put(clazz, tmpFields);
		}
		return tmpFields.get(fieldName);
	}


	// package-private
	public static void _getAllFields(Class<?> clazz, Set<Class<?>> visitedTypes, Map<String, Field> dst) {
		Field[] fields = clazz.getDeclaredFields();
		boolean visitedThisClass = false;
		int fieldCount = fields.length;

		// traverse parent class fields
		if(!clazz.isInterface()) {
			Class<?> parentClass = clazz.getSuperclass();
			if(parentClass != null) {
				// only track visited classes that have one or more fields
				if(!visitedThisClass && fieldCount > 0) {
					if(visitedTypes == null) {
						visitedTypes = new HashSet<>();
					}
					visitedTypes.add(clazz);
					visitedThisClass = true;
				}
				_getAllFields(parentClass, visitedTypes, dst);
			}
		}

		// traverse implemented interface fields
		for(Class<?> parentType : clazz.getInterfaces()) {
			if(visitedTypes == null || !visitedTypes.contains(parentType)) {
				// only track visited classes that have one or more fields
				if(!visitedThisClass && fieldCount > 0) {
					if(visitedTypes == null) {
						visitedTypes = new HashSet<>();
					}
					visitedTypes.add(clazz);
					visitedThisClass = true;
				}
				_getAllFields(parentType, visitedTypes, dst);
			}
		}

		// add fields later, so over-riding fields appear in the destination map
		for(Field field : fields) {
			dst.put(field.getName(), field);
		}
	}


	/** Given an iterable group of fields, split them into known and unknown groups based on parameters
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
			JPrimitiveType primitive = JPrimitiveType.tryFromWrapperType(type);
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
