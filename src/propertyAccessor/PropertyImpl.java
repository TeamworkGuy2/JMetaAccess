package propertyAccessor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author TeamworkGuy2
 * @since 2015-6-27
 * @param <T> the data type of the field
 */
@AllArgsConstructor
public class PropertyImpl<T> implements PropertyDefinition<T> {
	@Getter Method setterRef;
	@Getter Method getterRef;
	@Getter String fieldName;
	@Getter Field field;


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
