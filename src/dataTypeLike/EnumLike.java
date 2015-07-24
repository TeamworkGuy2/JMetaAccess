package dataTypeLike;

import java.util.List;

/** Interface for {@link Enum} like classes
 * @param <T> the type of elements in this enumeration
 * @author TeamworkGuy2
 * @since 2014-10-31
 */
public interface EnumLike<T> {

	/** Returns the list of values in this enumeration
	 * @return the values in this enumeration
	 */
	public List<T> values();


	/** Returns the enum constant corresponding to the provided name
	 * @param enumConstName the name of the enum constant to lookup
	 * @return the enum constant corresponding to the specified enum constant name,
	 * returns null if {@code enumConstName} does not match the name of any of this instance's values
	 */
	public T valueOf(String enumConstName);

}
