package test;

import java.util.Arrays;

import org.junit.Test;

import simpleReflect.SimpleField;

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
		System.out.println("get: " + Arrays.toString((char[])strContent.get("str1")));

		SimpleField strBuilderContent = SimpleField.createSimpleDeclaredField(Class.forName("java.lang.AbstractStringBuilder"), "value");
		System.out.println("get: " + Arrays.toString((char[])strBuilderContent.get(new StringBuilder("strB"))));
	}

}
