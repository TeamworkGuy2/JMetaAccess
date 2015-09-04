package fieldAccess;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import twg2.collections.tuple.Tuples;

/** A hierarchical composition of {@link Field Fields} that access a field inside a compound object
 * @author TeamworkGuy2
 * @since 2015-8-28
 * @param <T> the data type of the field
 */
public class CompoundField<T> implements SimpleField {
	private List<Field> parentToFieldAccessors;
	private Field cachedField;
	private boolean setAccessible;


	public CompoundField() {
		this.parentToFieldAccessors = new ArrayList<>();
	}


	public CompoundField(List<Field> parentToFieldAccessors) {
		this.parentToFieldAccessors = parentToFieldAccessors;
		this.cachedField = parentToFieldAccessors.get(parentToFieldAccessors.size() - 1);
	}


	@SafeVarargs
	public CompoundField(Field... fieldAccessors) {
		List<Field> fields = new ArrayList<>();
		for(Field fieldAccessor : fieldAccessors) {
			fields.add(fieldAccessor);
		}
		this.parentToFieldAccessors = fields;
		this.cachedField = fields.get(fields.size() - 1);
	}


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
		Class<T> type = (Class<T>)cachedField.getType();
		return type;
	}


	@Override
	public String toString() {
		return "compoundFields: " + parentToFieldAccessors;
	}


	private Map.Entry<Object, Field> traverseAccessors(Object srcObject) {
		if(!setAccessible) {
			this.setAccessible = true;
			List<Field> fields = parentToFieldAccessors;
			for(int i = 0, size = fields.size(); i < size; i++) {
				fields.get(i).setAccessible(true);
			}
		}
		Map.Entry<Object, Field> lastField = traverseAccessors(srcObject, parentToFieldAccessors);
		return lastField;
	}


	/** traverse a series of methods and fields where each previous method/field returns
	 * the object to use to access the next method/field
	 */
	private static Map.Entry<Object, Field> traverseAccessors(Object srcObject, List<Field> accessors) {
		int size = accessors.size();
		Object parentObj = srcObject;
		Field accessor = (Field)accessors.get(size - 1);

		for(int i = 0; i < size - 1; i++) {
			Field field = accessors.get(i);
			try {
				parentObj = field.get(parentObj);
			} catch (Exception e) {
				throw new RuntimeException("accessing deep field (for setting) '" + accessor.getName() + "' (" + accessor.getType() + ") via field " + field.getName(), e);
			}
		}

		return Tuples.of(parentObj, accessor);
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
		Map.Entry<Object, Field> objField = traverseAccessors(obj);
		try {
			return objField.getValue().get(objField.getKey());
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public boolean getBoolean(Object obj) {
		Map.Entry<Object, Field> objField = traverseAccessors(obj);
		try {
			return objField.getValue().getBoolean(objField.getKey());
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public byte getByte(Object obj) {
		Map.Entry<Object, Field> objField = traverseAccessors(obj);
		try {
			return objField.getValue().getByte(objField.getKey());
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public char getChar(Object obj) {
		Map.Entry<Object, Field> objField = traverseAccessors(obj);
		try {
			return objField.getValue().getChar(objField.getKey());
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public short getShort(Object obj) {
		Map.Entry<Object, Field> objField = traverseAccessors(obj);
		try {
			return objField.getValue().getShort(objField.getKey());
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}

	}


	@Override
	public int getInt(Object obj) {
		Map.Entry<Object, Field> objField = traverseAccessors(obj);
		try {
			return objField.getValue().getInt(objField.getKey());
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public float getFloat(Object obj) {
		Map.Entry<Object, Field> objField = traverseAccessors(obj);
		try {
			return objField.getValue().getFloat(objField.getKey());
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public long getLong(Object obj) {
		Map.Entry<Object, Field> objField = traverseAccessors(obj);
		try {
			return objField.getValue().getLong(objField.getKey());
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public double getDouble(Object obj) {
		Map.Entry<Object, Field> objField = traverseAccessors(obj);
		try {
			return objField.getValue().getDouble(objField.getKey());
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public void set(Object obj, Object z) {
		Map.Entry<Object, Field> objField = traverseAccessors(obj);
		try {
			objField.getValue().set(objField.getKey(), z);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public void setBoolean(Object obj, boolean z) {
		Map.Entry<Object, Field> objField = traverseAccessors(obj);
		try {
			objField.getValue().setBoolean(objField.getKey(), z);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public void setByte(Object obj, byte z) {
		Map.Entry<Object, Field> objField = traverseAccessors(obj);
		try {
			objField.getValue().setByte(objField.getKey(), z);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public void setCharacter(Object obj, char z) {
		Map.Entry<Object, Field> objField = traverseAccessors(obj);
		try {
			objField.getValue().setChar(objField.getKey(), z);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public void setShort(Object obj, short z) {
		Map.Entry<Object, Field> objField = traverseAccessors(obj);
		try {
			objField.getValue().setShort(objField.getKey(), z);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public void setInteger(Object obj, int z) {
		Map.Entry<Object, Field> objField = traverseAccessors(obj);
		try {
			objField.getValue().setInt(objField.getKey(), z);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public void setFloat(Object obj, float z) {
		Map.Entry<Object, Field> objField = traverseAccessors(obj);
		try {
			objField.getValue().setFloat(objField.getKey(), z);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public void setLong(Object obj, long z) {
		Map.Entry<Object, Field> objField = traverseAccessors(obj);
		try {
			objField.getValue().setLong(objField.getKey(), z);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public void setDouble(Object obj, double z) {
		Map.Entry<Object, Field> objField = traverseAccessors(obj);
		try {
			objField.getValue().setDouble(objField.getKey(), z);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
