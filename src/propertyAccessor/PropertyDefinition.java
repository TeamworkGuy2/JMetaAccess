package propertyAccessor;

/**
 * @author TeamworkGuy2
 * @since 2015-7-18
 * @param <T> the type of value contained in this field reference
 */
public interface PropertyDefinition<T> {

	public <E> void setVal(T val, E srcObject);

	public <E> T getVal(E srcObject);

	public String getFieldName();

	public Class<T> getType();

}
