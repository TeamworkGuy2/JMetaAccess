package twg2.meta.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import lombok.val;

import org.junit.Assert;
import org.junit.Test;

import twg2.meta.propertyAccessor.CompoundProperty;
import twg2.meta.propertyAccessor.PropertyDefinition;
import twg2.meta.propertyAccessor.PropertyGets;
import twg2.meta.propertyAccessor.PropertyNamingConvention;
import twg2.meta.test.FieldGetData.ColonyBug;
import twg2.meta.test.FieldGetData.Termite;
import checks.CheckCollections;

/**
 * @author TeamworkGuy2
 * @since 2015-6-27
 */
public class FieldGetSetTest {


	@Test
	public void testFieldGetterSetter() {
		ColonyBug[] inputs = {
				new ColonyBug(23, false, false, "a"),
				new ColonyBug(42, true, true, "z")
		};

		for(ColonyBug obj : inputs) {
			ColonyBug copy = new ColonyBug(0, false, false, null);
			Map<String, PropertyDefinition<Object>> fields = PropertyGets.createFromObject(obj.getClass(), PropertyNamingConvention.JAVA_BEAN_LIKE);

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
		Termite termite = FieldGetData.Dummy.newTermite1();

		List<CompoundProperty<Object>> fields = PropertyGets.getAllPropertiesRecursive(Termite.class, Arrays.asList(StringBuilder.class, String.class));

		List<String> termiteFields = Arrays.asList("colonyNum", "termiteName", "colonyNotesBuf", "manager", "id", "t", "count");

		List<String> fieldNames = new ArrayList<>();
		for(val field : fields) {
			fieldNames.add(field.getFieldName());
		}
		CheckCollections.assertLooseEquals(termiteFields, fieldNames);

		Termite copy = new Termite(0, "", 0, false, false, "");

		fields.forEach((f) -> {
			f.setVal(f.getVal(termite), copy);
		});

		Assert.assertEquals(termite, copy);
	}

}
