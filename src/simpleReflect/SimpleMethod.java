package simpleReflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author TeamworkGuy2
 * @since 2015-03-29
 */
@javax.annotation.Generated("StringTemplate")
public class SimpleMethod {
	private Method method;


	public SimpleMethod(Method method) {
		this.method = method;
	}


	public Method getMethod() {
		return this.method;
	}


	public Class<?> getDeclaringClass() {
		return this.method.getDeclaringClass();
	}


	public String getName() {
		return this.method.getName();
	}


	@Override
	public boolean equals(Object obj) {
		return this.method.equals(obj);
	}


	@Override
	public int hashCode() {
		return this.method.hashCode();
	}


	@Override
	public String toString() {
		return this.method.toString();
	}


	public Object invoke(Object obj, Object... args) {
		try {
			return this.method.invoke(obj, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}


	public <T> T invoke(Class<T> expectedReturnType, Object obj, Object... args) {
		try {
			Object res = this.method.invoke(obj, args);
			if(!expectedReturnType.isInstance(obj)) {
				throw new RuntimeException(this.getName() + " expected return value is not a " + expectedReturnType.getSimpleName());
			}
			@SuppressWarnings("unchecked")
			T resT = (T)res;
			return resT;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}


	public boolean invokeBoolean(Object obj, Object... args) {
		try {
			Object res = this.method.invoke(obj, args);
			if(!(res instanceof Boolean)) {
				throw new RuntimeException(this.getName() + " expected return value is not a boolean");
			}
			return (boolean)res;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}


	public byte invokeByte(Object obj, Object... args) {
		try {
			Object res = this.method.invoke(obj, args);
			if(!(res instanceof Byte)) {
				throw new RuntimeException(this.getName() + " expected return value is not a byte");
			}
			return (byte)res;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}


	public char invokeChar(Object obj, Object... args) {
		try {
			Object res = this.method.invoke(obj, args);
			if(!(res instanceof Character)) {
				throw new RuntimeException(this.getName() + " expected return value is not a char");
			}
			return (char)res;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}


	public short invokeShort(Object obj, Object... args) {
		try {
			Object res = this.method.invoke(obj, args);
			if(!(res instanceof Short)) {
				throw new RuntimeException(this.getName() + " expected return value is not a short");
			}
			return (short)res;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}


	public int invokeInt(Object obj, Object... args) {
		try {
			Object res = this.method.invoke(obj, args);
			if(!(res instanceof Integer)) {
				throw new RuntimeException(this.getName() + " expected return value is not a int");
			}
			return (int)res;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}


	public float invokeFloat(Object obj, Object... args) {
		try {
			Object res = this.method.invoke(obj, args);
			if(!(res instanceof Float)) {
				throw new RuntimeException(this.getName() + " expected return value is not a float");
			}
			return (float)res;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}


	public long invokeLong(Object obj, Object... args) {
		try {
			Object res = this.method.invoke(obj, args);
			if(!(res instanceof Long)) {
				throw new RuntimeException(this.getName() + " expected return value is not a long");
			}
			return (long)res;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}


	public double invokeDouble(Object obj, Object... args) {
		try {
			Object res = this.method.invoke(obj, args);
			if(!(res instanceof Double)) {
				throw new RuntimeException(this.getName() + " expected return value is not a double");
			}
			return (double)res;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}


	public boolean[] invokeBooleanArray(Object obj, Object... args) {
		try {
			Object res = this.method.invoke(obj, args);
			if(!(res instanceof boolean[])) {
				throw new RuntimeException(this.getName() + " expected return value is not a boolean[]");
			}
			return (boolean[])res;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}


	public byte[] invokeByteArray(Object obj, Object... args) {
		try {
			Object res = this.method.invoke(obj, args);
			if(!(res instanceof byte[])) {
				throw new RuntimeException(this.getName() + " expected return value is not a byte[]");
			}
			return (byte[])res;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}


	public char[] invokeCharArray(Object obj, Object... args) {
		try {
			Object res = this.method.invoke(obj, args);
			if(!(res instanceof char[])) {
				throw new RuntimeException(this.getName() + " expected return value is not a char[]");
			}
			return (char[])res;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}


	public short[] invokeShortArray(Object obj, Object... args) {
		try {
			Object res = this.method.invoke(obj, args);
			if(!(res instanceof short[])) {
				throw new RuntimeException(this.getName() + " expected return value is not a short[]");
			}
			return (short[])res;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}


	public int[] invokeIntArray(Object obj, Object... args) {
		try {
			Object res = this.method.invoke(obj, args);
			if(!(res instanceof int[])) {
				throw new RuntimeException(this.getName() + " expected return value is not a int[]");
			}
			return (int[])res;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}


	public float[] invokeFloatArray(Object obj, Object... args) {
		try {
			Object res = this.method.invoke(obj, args);
			if(!(res instanceof float[])) {
				throw new RuntimeException(this.getName() + " expected return value is not a float[]");
			}
			return (float[])res;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}


	public long[] invokeLongArray(Object obj, Object... args) {
		try {
			Object res = this.method.invoke(obj, args);
			if(!(res instanceof long[])) {
				throw new RuntimeException(this.getName() + " expected return value is not a long[]");
			}
			return (long[])res;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}


	public double[] invokeDoubleArray(Object obj, Object... args) {
		try {
			Object res = this.method.invoke(obj, args);
			if(!(res instanceof double[])) {
				throw new RuntimeException(this.getName() + " expected return value is not a double[]");
			}
			return (double[])res;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}


}
