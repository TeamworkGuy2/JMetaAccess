package typeInfo;

/**
 * @author TeamworkGuy2
 * @since 20151-18
 */
public enum JavaPrimitives {
	BOOLEAN(Boolean.TYPE, Boolean.class, Boolean.FALSE, "boolean", "Boolean", "boolean", "Boolean", "boolean", "Boolean"),
	BYTE(Byte.TYPE, Byte.class, Byte.valueOf((byte)0), "byte", "Byte", "byte", "Byte", "byte", "Byte"),
	CHAR(Character.TYPE, Character.class, Character.valueOf((char)0), "char", "Character", "char", "Char", "character", "Character"),
	SHORT(Short.TYPE, Short.class, Short.valueOf((short)0), "short", "Short", "short", "Short", "short", "Short"),
	INT(Integer.TYPE, Integer.class, Integer.valueOf((int)0), "int", "Integer", "int", "Int", "integer", "Integer"),
	FLOAT(Float.TYPE, Float.class, Float.valueOf((float)0), "float", "Float", "float", "Float", "float", "Float"),
	LONG(Long.TYPE, Long.class, Long.valueOf((long)0), "long", "Long", "long", "Long", "long", "Long"),
	DOUBLE(Double.TYPE, Double.class, Double.valueOf((double)0), "double", "Double", "double", "Double", "double", "Double");

	final Class<?> classType;
	final Class<?> wrapperType;
	final Object defaultValue;
	final String javaPrimitiveName;
	final String javaObjectName;
	final String shortName;
	final String shortTitleName;
	final String fullName;
	final String fullTitleName;


	private JavaPrimitives(Class<?> type, Class<?> wrapperType, Object defaultValue, String javaPrimitiveName, String javaObjectName,
			String shortName, String shortTitleCaseName, String fullName, String fullTitleCaseName) {
		this.classType = type;
		this.wrapperType = wrapperType;
		this.defaultValue = defaultValue;
		this.javaPrimitiveName = javaPrimitiveName;
		this.javaObjectName = javaObjectName;

		this.shortName = shortName;
		this.shortTitleName = shortTitleCaseName;
		this.fullName = fullName;
		this.fullTitleName = fullTitleCaseName;
	}


	public static final JavaPrimitives getByType(Class<?> type) {
		JavaPrimitives prim = tryGetByType(type);
		if(prim == null) {
			throw new IllegalArgumentException("unknown primitive type: " + type);
		}
		return prim;
	}


	public static final JavaPrimitives tryGetByType(Class<?> type) {
		if(type == Boolean.TYPE || type == Boolean.class) {
			return BOOLEAN;
		}
		else if(type == Byte.TYPE || type == Byte.class) {
			return BYTE;
		}
		else if(type == Character.TYPE || type == Character.class) {
			return CHAR;
		}
		else if(type == Short.TYPE || type == Short.class) {
			return SHORT;
		}
		else if(type == Integer.TYPE || type == Integer.class) {
			return INT;
		}
		else if(type == Float.TYPE || type == Float.class) {
			return FLOAT;
		}
		else if(type == Long.TYPE || type == Long.class) {
			return LONG;
		}
		else if(type == Double.TYPE || type == Double.class) {
			return DOUBLE;
		}
		else {
			return null;
		}
	}


	public static final JavaPrimitives getWrapperType(Class<?> type) {
		JavaPrimitives prim = tryGetWrapperType(type);
		if(prim == null) {
			throw new IllegalArgumentException("unknown primitive wrapper type: " + type);
		}
		return prim;
	}


	public static final JavaPrimitives tryGetWrapperType(Class<?> type) {
		if(type == Boolean.TYPE) {
			return BOOLEAN;
		}
		else if(type == Byte.TYPE) {
			return BYTE;
		}
		else if(type == Character.TYPE) {
			return CHAR;
		}
		else if(type == Short.TYPE) {
			return SHORT;
		}
		else if(type == Integer.TYPE) {
			return INT;
		}
		else if(type == Float.TYPE) {
			return FLOAT;
		}
		else if(type == Long.TYPE) {
			return LONG;
		}
		else if(type == Double.TYPE) {
			return DOUBLE;
		}
		else {
			return null;
		}
	}


	public static final JavaPrimitives getPrimitiveType(Class<?> type) {
		JavaPrimitives prim = tryGetPrimitiveType(type);
		if(prim == null) {
			throw new IllegalArgumentException("unknown primitive type: " + type);
		}
		return prim;
	}


	public static final JavaPrimitives tryGetPrimitiveType(Class<?> type) {
		if(type == Boolean.class) {
			return BOOLEAN;
		}
		else if(type == Byte.class) {
			return BYTE;
		}
		else if(type == Character.class) {
			return CHAR;
		}
		else if(type == Short.class) {
			return SHORT;
		}
		else if(type == Integer.class) {
			return INT;
		}
		else if(type == Float.class) {
			return FLOAT;
		}
		else if(type == Long.class) {
			return LONG;
		}
		else if(type == Double.class) {
			return DOUBLE;
		}
		else {
			return null;
		}
	}


	public String getShortName() {
		return shortName;
	}


	public String getShortTitleCaseName() {
		return shortTitleName;
	}


	public String getFullName() {
		return fullName;
	}


	public String getFullTitleCaseName() {
		return fullTitleName;
	}


	public String getJavaPrimitiveName() {
		return javaPrimitiveName;
	}


	public String getJavaObjectName() {
		return javaObjectName;
	}


	public Class<?> getType() {
		return classType;
	}


	public Class<?> getWrapperType() {
		return wrapperType;
	}


	public Object getDefaultValue() {
		return defaultValue;
	}

}
