package propertyAccessor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import twg2.collections.tuple.Tuples;
import twg2.collections.util.dataStructures.BiTypeList;

/**
 * @author TeamworkGuy2
 * @since 2015-7-18
 * @param <T> the data type of the field
 */
public class CompoundProperty<T> implements PropertyDefinition<T> {
	BiTypeList<Method, Field> parentToFieldAccessors;
	private boolean setAccessible;


	private Map.Entry<Object, Field> traverseAccessors(Object srcObject) {
		if(!setAccessible) {
			this.setAccessible = true;
			parentToFieldAccessors.forEach((m) -> {}, (f) -> {
				f.setAccessible(true);
			});
		}
		Map.Entry<Object, Field> lastField = traverseAccessors(srcObject, parentToFieldAccessors);
		return lastField;
	}

	public CompoundProperty() {
		this.parentToFieldAccessors = new BiTypeList<>(Method.class, Field.class);
	}


	public CompoundProperty(BiTypeList<Method, Field> parentToFieldAccessors) {
		this.parentToFieldAccessors = parentToFieldAccessors;
	}


	@SafeVarargs
	public CompoundProperty(Method... fieldAccessors) {
		this.parentToFieldAccessors = new BiTypeList<>(Method.class, Field.class);
		this.parentToFieldAccessors.addAll1(fieldAccessors);
	}


	@SafeVarargs
	public CompoundProperty(Field... fieldAccessors) {
		this.parentToFieldAccessors = new BiTypeList<>(Method.class, Field.class);
		this.parentToFieldAccessors.addAll2(fieldAccessors);
	}


	@Override
	public String getFieldName() {
		int lastIdx = parentToFieldAccessors.size() - 1;
		return parentToFieldAccessors.getAs2(lastIdx).getName();
	}


	@Override
	public <E> void setVal(T val, E srcObject) {
		Map.Entry<Object, Field> lastField = traverseAccessors(srcObject);

		Object parentObj = lastField.getKey();
		Field accessor = lastField.getValue();

		try {
			accessor.set(parentObj, val);
		} catch (Exception e) {
			throw new RuntimeException("setting deep field '" + accessor.getName() + "' (" + accessor.getType() + ") with value " + (val != null ? val.getClass() : "") + " '" + val + "'", e);
		}
	}


	@Override
	public <E> T getVal(E srcObject) {
		Map.Entry<Object, Field> lastField = traverseAccessors(srcObject);

		Object parentObj = lastField.getKey();
		Field accessor = lastField.getValue();

		T res = null;
		try {
			@SuppressWarnings("unchecked")
			T val = (T) accessor.get(parentObj);
			res = val;
		} catch (Exception e) {
			throw new RuntimeException("getting deep field '" + accessor.getName() + "' (" + accessor.getType() + ")", e);
		}
		return res;
	}


	@Override
	public Class<T> getType() {
		@SuppressWarnings("unchecked")
		Class<T> type = (Class<T>)parentToFieldAccessors.getAs2(parentToFieldAccessors.size() - 1).getType();
		return type;
	}


	@Override
	public String toString() {
		return "compoundFields: " + parentToFieldAccessors;
	}


	/** traverse a series of methods and fields where each previous method/field returns
	 * the object to use to access the next method/field
	 */
	private static Map.Entry<Object, Field> traverseAccessors(Object srcObject, BiTypeList<Method, Field> accessors) {
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

		return Tuples.of(parentObj, accessor);
	}

}
