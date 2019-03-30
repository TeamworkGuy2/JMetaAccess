package twg2.meta.propertyAccessor;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author TeamworkGuy2
 * @since 2015-6-27
 * @param <T> the type of object the field comes from
 * @param <E> the field value type
 */
public class PropertyFromMethods<T, E> {
	final Consumer<E> setter;
	final Supplier<E> getter;
	final PropertyDefinition<E> propertyDefinition;
	final T srcObject;


	public PropertyFromMethods(Consumer<E> setter, Supplier<E> getter, PropertyDefinition<E> propertyDefinition, T srcObject) {
		this.setter = setter;
		this.getter = getter;
		this.propertyDefinition = propertyDefinition;
		this.srcObject = srcObject;
	}


	public Consumer<E> getSetter() {
		return setter;
	}


	public Supplier<E> getGetter() {
		return getter;
	}


	public PropertyDefinition<E> getPropertyDefinition() {
		return propertyDefinition;
	}


	public T getSrcObject() {
		return srcObject;
	}


	public E getValue() {
		return getter.get();
	}


	public void setValue(E val) {
		setter.accept(val);
	}


	public static <T, E> PropertyFromMethods<T, E> of(final PropertyDefinition<E> fieldGetSet, final T srcObject) {
		return newInstance(fieldGetSet, srcObject);
	}


	public static <T, E> PropertyFromMethods<T, E> newInstance(final PropertyDefinition<E> fieldGetSet, final T srcObject) {

		Consumer<E> setter = (E val) -> {
			fieldGetSet.setVal(val, srcObject);
		};

		Supplier<E> getter = () -> {
			return fieldGetSet.getVal(srcObject);
		};

		return new PropertyFromMethods<>(setter, getter, fieldGetSet, srcObject);
	}

}
