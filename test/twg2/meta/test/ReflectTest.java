package twg2.meta.test;

import java.util.Arrays;

import org.junit.Test;

import twg2.meta.fieldAccess.SimpleField;

/**
 * @author TeamworkGuy2
 * @since 2015-6-6
 */
public class ReflectTest {

	@Test
	public void testSimpleMethodReflection() {
		
	}


	@Test
	public void testSimpleFieldReflection() throws ClassNotFoundException {
		SimpleField strContent = SimpleField.createSimpleDeclaredField(String.class, "value");
		System.out.println("get: " + Arrays.toString((byte[])strContent.get("str1"))); // changed from char[] -> byte[] in Java 9

		SimpleField strBuilderContent = SimpleField.createSimpleDeclaredField(Class.forName("java.lang.AbstractStringBuilder"), "value");
		System.out.println("get: " + Arrays.toString((byte[])strBuilderContent.get(new StringBuilder("strB")))); // changed from char[] -> byte[] in Java 9
	}

}
