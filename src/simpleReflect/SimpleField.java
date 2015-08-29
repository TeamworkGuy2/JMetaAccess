package simpleReflect;

import java.lang.reflect.Field;

/** A runtime error throwing version of {@link Field}
 * @author TeamworkGuy2
 * @since 2015-03-29
 */
@javax.annotation.Generated("StringTemplate")
public interface SimpleField {


	public Field getField();


	public Class<?> getDeclaringClass();


	public Class<?> getType();


	public String getName();


	public Object get(Object obj);


	public boolean getBoolean(Object obj);


	public byte getByte(Object obj);


	public char getChar(Object obj);


	public short getShort(Object obj);


	public int getInt(Object obj);


	public float getFloat(Object obj);


	public long getLong(Object obj);


	public double getDouble(Object obj);


	public void set(Object obj, Object z);


	public void setBoolean(Object obj, boolean z);


	public void setByte(Object obj, byte z);


	public void setCharacter(Object obj, char z);


	public void setShort(Object obj, short z);


	public void setInteger(Object obj, int z);


	public void setFloat(Object obj, float z);


	public void setLong(Object obj, long z);


	public void setDouble(Object obj, double z);


	public static <T> SimpleFieldImpl createSimpleField(Class<T> clazz, String name) {
		try {
			Field field = clazz.getField(name);
			return new SimpleFieldImpl(field);
		} catch (NoSuchFieldException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}


	public static <T> SimpleFieldImpl createSimpleDeclaredField(Class<T> clazz, String name) {
		try {
			Field field = clazz.getDeclaredField(name);
			return new SimpleFieldImpl(field);
		} catch (NoSuchFieldException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

}
