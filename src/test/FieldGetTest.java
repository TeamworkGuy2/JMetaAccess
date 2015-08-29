package test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

import org.junit.Test;

import propertyAccessor.FieldGet;
import simpleReflect.SimpleField;
import simpleReflect.SimpleFields;
import simpleTree.SimpleTree;
import checks.CheckTask;

/**
 * @author TeamworkGuy2
 * @since 2015-6-27
 */
public class FieldGetTest {

	public static interface BaseI {
		public int getBaseI_Field1();
		public void setBaseI_Field1(int val);
	}


	public static interface Tree1I {
		public int getTree1I_Field1();
		public void setTree1I_Field1(int val);
	}


	public static interface Tree2I {
		public int getTree2I_Field1();
		public void setTree2I_Field1(int val);
	}


	public static interface Leaf1I extends Tree1I {
		public void blowInTheWind_Leaf1();
	}


	public static interface Leaf2I extends Tree2I {
		public void blowInTheWind_Leaf2();
	}


	public static class Base implements BaseI {
		private @Getter @Setter int baseI_Field1;
	}


	public static class Tree1 implements Tree1I {
		private @Getter @Setter int tree1I_Field1;
	}


	public static class Tree2 implements Tree2I {
		private @Getter @Setter int tree2I_Field1;
	}


	public static class Leaf1 extends Tree1 implements Leaf1I {
		private @Getter @Setter double leaf1_Field1;
		public @Override void blowInTheWind_Leaf1() { System.out.println("blow in the wind 1"); }
	}


	public static class Leaf2 extends Tree2 implements Leaf2I {
		private @Getter @Setter Object leaf2_Field1;
		private @Getter @Setter java.lang.annotation.ElementType leaf2_Field2;
		public @Override void blowInTheWind_Leaf2() { System.out.println("blow in the wind 2"); }
	}


	public static class BaseLeaf extends Base implements BaseI {
		public @Getter @Setter String baseLeaf_Field1;
	}


	@Test
	public void testFieldGet() {
		Object[] inputs = {
				new Leaf2(),
				new Tree1(),
				new BaseLeaf()
		};

		@SuppressWarnings("unchecked")
		Set<String>[] expectedFields = (Set<String>[])Arrays.asList(
				new HashSet<>(Arrays.asList("leaf2_Field1", "leaf2_Field2", "tree2I_Field1")),
				new HashSet<>(Arrays.asList("tree1I_Field1")),
				new HashSet<>(Arrays.asList("baseLeaf_Field1", "baseI_Field1"))
		).toArray(new Set[0]);

		CheckTask.assertTests(inputs, expectedFields, (obj) -> {
			Set<String> fields = new HashSet<>(FieldGet.getAllFields(obj.getClass()).keySet());
			return fields;
		});
	}


	@Test
	public void testFieldGetRecursive() {
		List<Class<?>> branchStopFields = Arrays.asList(StringBuilder.class, String.class);

		SimpleTree<SimpleField> fields = FieldGet.getAllFieldsRecursive(FieldGetSetTest.Branch.class, branchStopFields);

		Map<String, SimpleField> fieldMap = SimpleFields.createFromObjectRecursive(FieldGetSetTest.Branch.class, branchStopFields);

		System.out.println(fields);
		System.out.println(fieldMap);
	}

}
