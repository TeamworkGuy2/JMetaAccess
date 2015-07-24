package test;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PropertyPermission;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.sound.sampled.Line;
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicBorders.MarginBorder;
import javax.swing.text.MaskFormatter;

import org.junit.Test;

import simpleReflect.ClassInstanceOf;
import checks.CheckTask;
import collectionUtils.Entries;

/**
 * @author TeamworkGuy2
 * @since 2015-6-27
 */
public class ClassInstanceOfTest {

	@Test
	public void testDepthSeparation() {
		// key=parent, value=child
		Map.Entry<Class<?>, Class<?>>[] inputs = new Map.Entry[] {
				Entries.of(Number.class, Integer.class),
				Entries.of(Comparable.class, Integer.class),
				Entries.of(Serializable.class, Integer.class),
				Entries.of(Serializable.class, PropertyPermission.class), // serializable appears twice in the inheritance tree at different depths
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
				Entries.of(Integer.class, Arrays.asList(Integer.class, Number.class, Comparable.class, Serializable.class)),
				Entries.of(Scanner.class, Arrays.asList(Iterator.class, AutoCloseable.class)),
				Entries.of(MarginBorder.class, Arrays.asList(UIResource.class, Border.class)),
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

}
