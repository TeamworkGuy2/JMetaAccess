package propertyAccessor;

import java.util.function.Consumer;
import java.util.function.Supplier;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author TeamworkGuy2
 * @since 2015-6-27
 * @param <T> the type of object the field comes from
 * @param <E> the field value type
 */
@AllArgsConstructor
public class PropertyInst<T, E> {
	final @Getter Consumer<E> setter;
	final @Getter Supplier<E> getter;
	final @Getter PropertyDefinition<E> propertyDefinition;
	final @Getter T srcObject;


	public E getValue() {
		return getter.get();
	}


	public void setValue(E val) {
		setter.accept(val);
	}


	public static <T, E> PropertyInst<T, E> of(final PropertyDefinition<E> fieldGetSet, final T srcObject) {
		return newInstance(fieldGetSet, srcObject);
	}


	public static <T, E> PropertyInst<T, E> newInstance(final PropertyDefinition<E> fieldGetSet, final T srcObject) {

		Consumer<E> setter = (E val) -> {
			fieldGetSet.setVal(val, srcObject);
		};

		Supplier<E> getter = () -> {
			return fieldGetSet.getVal(srcObject);
		};

		return new PropertyInst<>(setter, getter, fieldGetSet, srcObject);
	}

}
