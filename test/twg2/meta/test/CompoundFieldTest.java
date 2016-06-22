package twg2.meta.test;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import twg2.meta.fieldAccess.CompoundField;
import twg2.meta.fieldAccess.SimpleFields;

public class CompoundFieldTest {

	@Test
	public void getDeepFieldTest() {
		List<CompoundField<Object>> fields = SimpleFields.getCompoundFieldsRecursive(FieldGetData.TermiteColony.class, Arrays.asList(String.class, StringBuilder.class));

		Assert.assertNotNull(fields.toString(), fields.stream().filter((f) -> f.getName().equals("boss")).findFirst().orElse(null));
	}

}
