package twg2.meta.test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import twg2.meta.fieldAccess.FieldGets;
import twg2.meta.fieldAccess.SimpleField;
import twg2.meta.fieldAccess.SimpleFields;
import twg2.meta.test.FieldGetData.BaseLeaf;
import twg2.meta.test.FieldGetData.Leaf2;
import twg2.meta.test.FieldGetData.TermiteColony;
import twg2.meta.test.FieldGetData.Tree1;
import twg2.treeLike.TreeTraversalOrder;
import twg2.treeLike.TreeTraverse;
import twg2.treeLike.parameters.KeyTreeTraverseParameters;
import twg2.treeLike.simpleTree.SimpleKeyTree;
import twg2.treeLike.simpleTree.SimpleTree;
import twg2.treeLike.simpleTree.SimpleTreeUtil;
import checks.CheckTask;

/**
 * @author TeamworkGuy2
 * @since 2015-6-27
 */
public class FieldGetTest {

	@Test
	public void getFieldsTest() {
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
			Set<String> fields = new HashSet<>(FieldGets.getFields(obj.getClass()).keySet());
			return fields;
		});
	}


	@Test
	public void getFieldsRecursiveTest() {
		List<Class<?>> branchStopFields = Arrays.asList(StringBuilder.class, String.class);

		TermiteColony branchSet = FieldGetData.Dummy.newTermiteColony1();

		//Map<String, SimpleField> fieldMap = SimpleFields.createFromObjectRecursive(branchSet.getClass(), branchStopFields);

		SimpleTree<SimpleField> fields = SimpleFields.getFieldsRecursive(branchSet.getClass(), branchStopFields, false, false);

		SimpleTreeUtil.transformTree(fields, (Object)branchSet, (field, parentTransformed, parent) -> {
			//System.out.println("from '" + field.getData().getName() + "', parent=" + parentTransformed.getClass());
			Object val = field.getData().get(parentTransformed);
			//System.out.println("field '" + field.getData().getName() + "': " + val + ",\t\t parent=" + parentTransformed.getClass().getSimpleName());
			return val;
		}, (node, depth, parent) -> {
			//System.out.println("result: " + node);
		}, (depth) -> {
			//System.out.println("start depth: " + depth);
		}, (depth) -> {
			//System.out.println("end depth: " + depth);
		});

		//System.out.println(fields);
		//System.out.println(fieldMap);
	}


	@Test
	public void getFieldMapRecursiveTest() {
		List<Class<?>> branchStopFields = Arrays.asList(StringBuilder.class, String.class);

		TermiteColony branchSet = FieldGetData.Dummy.newTermiteColony1();

		//Map<String, SimpleField> fieldMap = SimpleFields.createFromObjectRecursive(branchSet.getClass(), branchStopFields);

		SimpleKeyTree<String, SimpleField> fields = SimpleFields.getFieldMapRecursive(branchSet.getClass(), branchStopFields, false, false);

		TreeTraverse.traverse(KeyTreeTraverseParameters.allNodes(fields, TreeTraversalOrder.PRE_ORDER, (t) -> t.hasChildren(), (t) -> t.getChildren().entrySet())
			.setConsumer((node, depth, parent) -> {
				System.out.println("result: " + node);
			}).setStartSubtreeFunc((depth) -> {
				System.out.println("start depth: " + depth);
			}).setEndSubtreeFunc((depth) -> {
				System.out.println("end depth: " + depth);
			}));

		//System.out.println(fields);
		//System.out.println(fieldMap);
	}


	@Test
	public void compoundFieldGetTest() {
		FieldGetData.NotAccessible objNa = new FieldGetData.NotAccessible();

		Field f = FieldGets.getField(FieldGetData.NotAccessible.class, "myPasswords");

		Assert.assertNotNull(f);
		Assert.assertFalse(f.isAccessible());

		SimpleField sf = SimpleFields.getSimpleField(FieldGetData.NotAccessible.class, "myPasswords", true);
		Object pwds = sf.get(objNa);

		Assert.assertTrue("expected List, found " + pwds.getClass(), List.class.isAssignableFrom(pwds.getClass()));
		Assert.assertTrue(sf.getField().isAccessible());
	}

}
