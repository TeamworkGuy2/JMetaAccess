package test;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PropertyPermission;
import java.util.Scanner;

import javax.swing.border.Border;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicBorders.MarginBorder;

import org.junit.Test;

import simpleReflect.ClassInstanceOf;
import checks.CheckTask;

/**
 * @author TeamworkGuy2
 * @since 2015-6-27
 */
public class ClassInstanceOfTest {

	@Test
	public void testDepthSeparation() {
		// key=parent, value=child
		Map.Entry<Class<?>, Class<?>>[] inputs = new Map.Entry[] {
				entry(Number.class, Integer.class),
				entry(Comparable.class, Integer.class),
				entry(Serializable.class, Integer.class),
				entry(Serializable.class, PropertyPermission.class), // serializable appears twice in the inheritance tree at different depths
		};

		Integer[] expected = {
				1,
				1,
				2,
				2,
		};

		CheckTask.assertTests(inputs, expected, (entry) -> {
			return ClassInstanceOf.depthSeparation(entry.getKey(), entry.getValue());
		});
	}


	@Test
	public void testClassInstanceOfClosest() {
		Map.Entry<Class<?>, List<Class<?>>>[] inputs = new Map.Entry[] {
				entry(Integer.class, Arrays.asList(Integer.class, Number.class, Comparable.class, Serializable.class)),
				entry(Scanner.class, Arrays.asList(Iterator.class, AutoCloseable.class)),
				entry(MarginBorder.class, Arrays.asList(UIResource.class, Border.class)),
		};

		Class<?>[] expected = {
				Integer.class,
				Iterator.class,
				UIResource.class
		};

		CheckTask.assertTests(inputs, expected, (entry) -> {
			return ClassInstanceOf.closest(entry.getValue(), entry.getKey());
		});
	}


	public static <K, V> Map.Entry<K, V> entry(K key, V value) {
		return new AbstractMap.SimpleImmutableEntry(key, value);
	}

}
