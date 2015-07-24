package simpleReflect;

import java.lang.reflect.Field;

/** A runtime error throwing version of {@link Field}
 * @author TeamworkGuy2
 * @since 2015-03-29
 */
@javax.annotation.Generated("StringTemplate")
public class SimpleField {
	private Field field;


	public SimpleField(Field field) {
		this.field = field;
		this.field.setAccessible(true);
	}


	public SimpleField(Field field, boolean setAccessible) {
		this.field = field;
		if(setAccessible) {
			this.field.setAccessible(true);
		}
	}


	public Field getField() {
		return this.field;
	}


	public Class<?> getDeclaringClass() {
		return this.field.getDeclaringClass();
	}


	public Class<?> getType() {
		return this.field.getType();
	}


	public String getName() {
		return this.field.getName();
	}


	@Override
	public boolean equals(Object obj) {
		return this.field.equals(obj);
	}


	@Override
	public int hashCode() {
		return this.field.hashCode();
	}


	@Override
	public String toString() {
		return this.field.toString();
	}


	public Object get(Object obj) {
		try {
			return this.field.get(obj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	public boolean getBoolean(Object obj) {
		try {
			return this.field.getBoolean(obj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	public byte getByte(Object obj) {
		try {
			return this.field.getByte(obj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	public char getChar(Object obj) {
		try {
			return this.field.getChar(obj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	public short getShort(Object obj) {
		try {
			return this.field.getShort(obj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	public int getInt(Object obj) {
		try {
			return this.field.getInt(obj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	public float getFloat(Object obj) {
		try {
			return this.field.getFloat(obj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	public long getLong(Object obj) {
		try {
			return this.field.getLong(obj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	public double getDouble(Object obj) {
		try {
			return this.field.getDouble(obj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	
	public void setBoolean(Object obj, boolean z) {
		try {
			this.field.setBoolean(obj, z);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	
	public void setByte(Object obj, byte z) {
		try {
			this.field.setByte(obj, z);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	
	public void setCharacter(Object obj, char z) {
		try {
			this.field.setChar(obj, z);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	
	public void setShort(Object obj, short z) {
		try {
			this.field.setShort(obj, z);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	
	public void setInteger(Object obj, int z) {
		try {
			this.field.setInt(obj, z);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	
	public void setFloat(Object obj, float z) {
		try {
			this.field.setFloat(obj, z);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	
	public void setLong(Object obj, long z) {
		try {
			this.field.setLong(obj, z);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	
	public void setDouble(Object obj, double z) {
		try {
			this.field.setDouble(obj, z);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	public static <T> SimpleField createSimpleField(Class<T> clazz, String name) {
		try {
			Field field = clazz.getField(name);
			return new SimpleField(field);
		} catch (NoSuchFieldException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}


	public static <T> SimpleField createSimpleDeclaredField(Class<T> clazz, String name) {
		try {
			Field field = clazz.getDeclaredField(name);
			return new SimpleField(field);
		} catch (NoSuchFieldException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

}
