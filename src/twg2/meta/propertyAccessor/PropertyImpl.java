package twg2.meta.propertyAccessor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author TeamworkGuy2
 * @since 2015-6-27
 * @param <T> the data type of the field
 */
public class PropertyImpl<T> implements PropertyDefinition<T> {
	Method setterRef;
	Method getterRef;
	String fieldName;
	Field field;


	public PropertyImpl(Method setterRef, Method getterRef, String fieldName, Field field) {
		this.setterRef = setterRef;
		this.getterRef = getterRef;
		this.fieldName = fieldName;
		this.field = field;
	}


	public Method getSetterRef() {
		return setterRef;
	}


	public Method getGetterRef() {
		return getterRef;
	}


	@Override
	public String getFieldName() {
		return fieldName;
	}


	public Field getField() {
		return field;
	}


	public String getGetterName() {
		return getterRef.getName();
	}


	public String getSetterName() {
		return setterRef.getName();
	}


	@Override
	public <E> void setVal(T val, E srcObject) {
		try {
			setterRef.invoke(srcObject, val);
		} catch (Exception e) {
			throw new RuntimeException("setting field '" + fieldName + "' via setter '" + getSetterName() + "(" + field.getType() + ")' with argument " + (val != null ? val.getClass() : "") + " " + val, e);
		}
	}


	@Override
	public <E> T getVal(E srcObject) {
		T res = null;
		try {
			@SuppressWarnings("unchecked")
			T val = (T) getterRef.invoke(srcObject);
			res = val;
		} catch (Exception e) {
			throw new RuntimeException("getting field '" + fieldName + "' via getter '" + field.getType() + " " + getSetterName() + "()'", e);
		}
		return res;
	}


	@Override
	public Class<T> getType() {
		@SuppressWarnings("unchecked")
		Class<T> type = (Class<T>)field.getType();
		return type;
	}

}
