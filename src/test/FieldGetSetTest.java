package test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import org.junit.Assert;
import org.junit.Test;

import propertyAccessor.CompoundProperty;
import propertyAccessor.FieldGet;
import propertyAccessor.PropertyDefinition;
import propertyAccessor.PropertyNamerConvention;
import checks.CheckCollections;
import collectionUtils.ListUtil;

/**
 * @author TeamworkGuy2
 * @since 2015-6-27
 */
public class FieldGetSetTest {

	@AllArgsConstructor
	@EqualsAndHashCode
	public static class Base {
		private @Getter @Setter int count;
		private @Getter @Setter boolean t;
	}


	@EqualsAndHashCode(callSuper = true)
	public static class Impl extends Base {
		private @Getter @Setter boolean awesome;
		private @Getter @Setter String id;


		public Impl(int count, boolean t, boolean awesome, String id) {
			super(count, t);
			this.awesome = awesome;
			this.id = id;
		}

	}


	@EqualsAndHashCode(callSuper = true)
	public static class Branch extends Impl {
		private @Getter @Setter int branchId;
		private @Getter @Setter String branchName;
		private @Getter @Setter String branchDescription;
		private @Getter @Setter StringBuilder tmpStrB;

		public Branch(int branchId, String branchName, String branchDescription, int count, boolean t, boolean awesome, String id) {
			super(count, t, awesome, id);
			this.branchId = branchId;
			this.branchName = branchName;
			this.branchDescription = branchDescription;
			this.tmpStrB = new StringBuilder();
		}

	}




	@Test
	public void testFieldGetterSetter() {
		Impl[] inputs = {
				new Impl(23, false, false, "a"),
				new Impl(42, true, true, "z")
		};

		for(Impl obj : inputs) {
			Impl copy = new Impl(0, false, false, null);
			Map<String, PropertyDefinition<Object>> fields = PropertyDefinition.createFromObject(obj.getClass(), PropertyNamerConvention.JAVA_BEAN_LIKE);

			for(String fieldName : fields.keySet()) {
				PropertyDefinition<Object> field = fields.get(fieldName);

				Object value = field.getVal(obj);
				field.setVal(value, copy);
			}
			Assert.assertEquals(obj, copy);
		}
	}


	@Test
	public void testGetFieldsRecursive() {
		Branch branch = new Branch(19, "first branch!", "witty branch description -HERE-", 12345, false, true, "IDs, IDs, IDS for all!");
		List<CompoundProperty<Object>> fields = FieldGet.getAllFieldsRecursive(Branch.class, Arrays.asList(StringBuilder.class, String.class));

		List<String> expectBranchFields = Arrays.asList("branchId", "branchName", "branchDescription", "tmpStrB", "awesome", "id", "t", "count");
		CheckCollections.assertLooseEquals(expectBranchFields, ListUtil.map(fields, (f) -> f.getFieldName()));


		Branch copy = new Branch(0, "", "", 0, false, false, "");
		fields.forEach((f) -> {
			f.setVal(f.getVal(branch), copy);
		});

		Assert.assertEquals(branch, copy);
	}

}
