package twg2.meta.propertyAccessor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import twg2.collections.dataStructures.BiTypeList;

/**
 * @author TeamworkGuy2
 * @since 2015-7-18
 * @param <T> the data type of the field
 */
public class CompoundProperty<T> implements PropertyDefinition<T> {
	BiTypeList<Method, Field> hierarchicalAccessors;
	private boolean setAccessible;


	public CompoundProperty() {
		this.hierarchicalAccessors = new BiTypeList<>(Method.class, Field.class);
	}


	public CompoundProperty(BiTypeList<Method, Field> parentToFieldAccessors) {
		this.hierarchicalAccessors = parentToFieldAccessors;
	}


	@SafeVarargs
	public CompoundProperty(Method... fieldAccessors) {
		this.hierarchicalAccessors = new BiTypeList<>(Method.class, Field.class);
		this.hierarchicalAccessors.addAll1(fieldAccessors);
	}


	@SafeVarargs
	public CompoundProperty(Field... fieldAccessors) {
		this.hierarchicalAccessors = new BiTypeList<>(Method.class, Field.class);
		this.hierarchicalAccessors.addAll2(fieldAccessors);
	}


	@Override
	public String getFieldName() {
		int lastIdx = hierarchicalAccessors.size() - 1;
		return hierarchicalAccessors.getAs2(lastIdx).getName();
	}


	@Override
	public <E> void setVal(T val, E srcObject) {
		Object lastObj = traverseAccessors(srcObject);
		Field accessor = (Field)hierarchicalAccessors.get(hierarchicalAccessors.size() - 1);

		try {
			accessor.set(lastObj, val);
		} catch (Exception e) {
			throw new RuntimeException("setting deep field '" + accessor.getName() + "' (" + accessor.getType() + ") with value " + (val != null ? val.getClass() : "") + " '" + val + "'", e);
		}
	}


	@Override
	public <E> T getVal(E srcObject) {
		Object lastObj = traverseAccessors(srcObject);
		Field accessor = (Field)hierarchicalAccessors.get(hierarchicalAccessors.size() - 1);

		T res = null;
		try {
			@SuppressWarnings("unchecked")
			T val = (T) accessor.get(lastObj);
			res = val;
		} catch (Exception e) {
			throw new RuntimeException("getting deep field '" + accessor.getName() + "' (" + accessor.getType() + ")", e);
		}
		return res;
	}


	@Override
	public Class<T> getType() {
		@SuppressWarnings("unchecked")
		Class<T> type = (Class<T>)hierarchicalAccessors.getAs2(hierarchicalAccessors.size() - 1).getType();
		return type;
	}


	@Override
	public String toString() {
		return "compoundFields: " + hierarchicalAccessors;
	}


	private Object traverseAccessors(Object srcObject) {
		if(!setAccessible) {
			this.setAccessible = true;
			for(int i = 0, size = hierarchicalAccessors.size(); i < size; i++) {
				Object accessor = hierarchicalAccessors.get(i);
				if(accessor instanceof Field) {
					((Field)accessor).setAccessible(true);
				}
			}
		}
		Object lastObj = traverseAccessors(srcObject, hierarchicalAccessors);
		return lastObj;
	}


	/** traverse a series of methods and fields where each previous method/field returns
	 * the object to use to access the next method/field
	 */
	private static Object traverseAccessors(Object srcObject, BiTypeList<Method, Field> accessors) {
		int size = accessors.size();
		Object parentObj = srcObject;
		Field accessor = (Field)accessors.get(size - 1);

		for(int i = 0; i < size - 1; i++) {
			if(accessors.isType1(i)) {
				Method method = accessors.getAs1(i);
				try {
					parentObj = method.invoke(parentObj);
				} catch (Exception e) {
					throw new RuntimeException("accessing deep field (for setting) '" + accessor.getName() + "' (" + accessor.getType() + ") via getter '" + method.getName() + "()'", e);
				}
			}
			else {
				Field field = accessors.getAs2(i);
				try {
					parentObj = field.get(parentObj);
				} catch (Exception e) {
					throw new RuntimeException("accessing deep field (for setting) '" + accessor.getName() + "' (" + accessor.getType() + ") via field " + field.getName(), e);
				}
			}
		}

		return parentObj;
	}

}
