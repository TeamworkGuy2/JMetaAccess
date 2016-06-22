package twg2.meta.fieldAccess;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/** A field accessor for a deeply nested field.
 * A hierarchical composition of {@link Field Fields} to access a field one or more levels inside a compound object
 * @author TeamworkGuy2
 * @since 2015-8-28
 * @param <T> the data type of the field
 */
public class CompoundField<T> implements SimpleField {
	private List<Field> hierarchicalFields;
	private Field cachedField;
	private boolean setAccessible;


	public CompoundField() {
		this.hierarchicalFields = new ArrayList<>();
	}


	public CompoundField(List<Field> parentToFieldAccessors) {
		this.hierarchicalFields = parentToFieldAccessors;
		this.cachedField = parentToFieldAccessors.get(parentToFieldAccessors.size() - 1);
	}


	@SafeVarargs
	public CompoundField(Field... fieldAccessors) {
		List<Field> fields = new ArrayList<>();
		for(Field fieldAccessor : fieldAccessors) {
			fields.add(fieldAccessor);
		}
		this.hierarchicalFields = fields;
		this.cachedField = fields.get(fields.size() - 1);
	}


	public <E> void setVal(T val, E srcObject) {
		Object parentObj = traverseAccessors(srcObject);
		Field accessor = cachedField;

		try {
			accessor.set(parentObj, val);
		} catch (Exception e) {
			throw new RuntimeException("setting deep field '" + accessor.getName() + "' (" + accessor.getType() + ") with value " + (val != null ? val.getClass() : "") + " '" + val + "'", e);
		}
	}


	public <E> T getVal(E srcObject) {
		Object parentObj = traverseAccessors(srcObject);
		Field accessor = cachedField;

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
		Class<T> type = (Class<T>)cachedField.getType();
		return type;
	}


	@Override
	public String toString() {
		return "compoundFields: " + hierarchicalFields;
	}


	private final Object traverseAccessors(Object srcObject) {
		if(!this.setAccessible) {
			this.setAccessible = true;
			List<Field> fields = hierarchicalFields;
			for(int i = 0, size = fields.size(); i < size; i++) {
				fields.get(i).setAccessible(true);
			}
		}
		return traverseAccessors(srcObject, hierarchicalFields);
	}


	/** traverse a series of methods and fields where each previous field returns
	 * the object to use to access the next field
	 */
	private static final Object traverseAccessors(Object srcObject, List<Field> accessors) {
		int size = accessors.size();
		Object parentObj = srcObject;
		Field accessor = accessors.get(size - 1);

		for(int i = 0; i < size - 1; i++) {
			Field field = accessors.get(i);
			try {
				parentObj = field.get(parentObj);
			} catch (Exception e) {
				throw new RuntimeException("accessing deep field '" + accessor.getName() + "' (" + accessor.getType() + ") failed at field '" + field.getName() + "'", e);
			}
		}

		return parentObj;
	}


	@Override
	public Field getField() {
		return cachedField;
	}


	@Override
	public Class<?> getDeclaringClass() {
		return cachedField.getDeclaringClass();
	}


	@Override
	public String getName() {
		return cachedField.getName();
	}


	@Override
	public Object get(Object obj) {
		Object lastObj = traverseAccessors(obj);
		Field lastField = cachedField;
		try {
			return lastField.get(lastObj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public boolean getBoolean(Object obj) {
		Object lastObj = traverseAccessors(obj);
		Field lastField = cachedField;
		try {
			return lastField.getBoolean(lastObj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public byte getByte(Object obj) {
		Object lastObj = traverseAccessors(obj);
		Field lastField = cachedField;
		try {
			return lastField.getByte(lastObj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public char getChar(Object obj) {
		Object lastObj = traverseAccessors(obj);
		Field lastField = cachedField;
		try {
			return lastField.getChar(lastObj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public short getShort(Object obj) {
		Object lastObj = traverseAccessors(obj);
		Field lastField = cachedField;
		try {
			return lastField.getShort(lastObj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}

	}


	@Override
	public int getInt(Object obj) {
		Object lastObj = traverseAccessors(obj);
		Field lastField = cachedField;
		try {
			return lastField.getInt(lastObj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public float getFloat(Object obj) {
		Object lastObj = traverseAccessors(obj);
		Field lastField = cachedField;
		try {
			return lastField.getFloat(lastObj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public long getLong(Object obj) {
		Object lastObj = traverseAccessors(obj);
		Field lastField = cachedField;
		try {
			return lastField.getLong(lastObj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public double getDouble(Object obj) {
		Object lastObj = traverseAccessors(obj);
		Field lastField = cachedField;
		try {
			return lastField.getDouble(lastObj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public void set(Object obj, Object z) {
		Object lastObj = traverseAccessors(obj);
		Field lastField = cachedField;
		try {
			lastField.set(lastObj, z);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public void setBoolean(Object obj, boolean z) {
		Object lastObj = traverseAccessors(obj);
		Field lastField = cachedField;
		try {
			lastField.setBoolean(lastObj, z);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public void setByte(Object obj, byte z) {
		Object lastObj = traverseAccessors(obj);
		Field lastField = cachedField;
		try {
			lastField.setByte(lastObj, z);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public void setCharacter(Object obj, char z) {
		Object lastObj = traverseAccessors(obj);
		Field lastField = cachedField;
		try {
			lastField.setChar(lastObj, z);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public void setShort(Object obj, short z) {
		Object lastObj = traverseAccessors(obj);
		Field lastField = cachedField;
		try {
			lastField.setShort(lastObj, z);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public void setInteger(Object obj, int z) {
		Object lastObj = traverseAccessors(obj);
		Field lastField = cachedField;
		try {
			lastField.setInt(lastObj, z);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public void setFloat(Object obj, float z) {
		Object lastObj = traverseAccessors(obj);
		Field lastField = cachedField;
		try {
			lastField.setFloat(lastObj, z);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public void setLong(Object obj, long z) {
		Object lastObj = traverseAccessors(obj);
		Field lastField = cachedField;
		try {
			lastField.setLong(lastObj, z);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public void setDouble(Object obj, double z) {
		Object lastObj = traverseAccessors(obj);
		Field lastField = cachedField;
		try {
			lastField.setDouble(lastObj, z);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
