package test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import test.FieldGetData.BaseLeaf;
import test.FieldGetData.Leaf2;
import test.FieldGetData.TermiteSsn;
import test.FieldGetData.Tree1;
import twg2.treeLike.simpleTree.SimpleTree;
import twg2.treeLike.simpleTree.SimpleTreeUtil;
import checks.CheckTask;
import fieldAccess.FieldGets;
import fieldAccess.SimpleField;

/**
 * @author TeamworkGuy2
 * @since 2015-6-27
 */
public class FieldGetTest {

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
			Set<String> fields = new HashSet<>(FieldGets.getAllFields(obj.getClass()).keySet());
			return fields;
		});
	}


	@Test
	public void testFieldGetRecursive() {
		List<Class<?>> branchStopFields = Arrays.asList(StringBuilder.class, String.class);

		TermiteSsn branchSet = FieldGetData.Dummy.newTermiteSsn1();

		//Map<String, SimpleField> fieldMap = SimpleFields.createFromObjectRecursive(branchSet.getClass(), branchStopFields);

		SimpleTree<SimpleField> fields = FieldGets.getAllFieldsRecursive(branchSet.getClass(), branchStopFields, false, false);

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

}
