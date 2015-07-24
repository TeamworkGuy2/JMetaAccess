package propertyAccessor;

import java.lang.reflect.Field;

/**
 * @author TeamworkGuy2
 * @since 2015-6-27
 */
public interface PropertyNamer {

	public String createGetterName(Field field);

	public String createGetterName(String fieldName, Class<?> clazz);

	public String createSetterName(Field field);

	public String createSetterName(String fieldName, Class<?> clazz);

}