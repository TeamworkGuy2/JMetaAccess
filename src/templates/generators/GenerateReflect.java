package templates.generators;

import java.util.Arrays;
import java.util.List;

import templates.ReflectionUtilTypes;
import twg2.collections.util.ListUtil;
import codeTemplate.PrimitiveClassTemplateDeprecated;
import codeTemplate.PrimitiveTemplates;
import codeTemplate.TemplateRender;


/**
 * @author TeamworkGuy2
 * @since 2015-6-6
 */
public class GenerateReflect {
	private static List<Class<?>> primitiveTypes = Arrays.asList(new Class<?>[] { Boolean.TYPE, Byte.TYPE, Character.TYPE, Short.TYPE, Integer.TYPE, Float.TYPE, Long.TYPE, Double.TYPE });


	public static final void generateSimpleField() {
		List<PrimitiveClassTemplateDeprecated> primitiveTypes = ListUtil.map(GenerateReflect.primitiveTypes, (type) -> PrimitiveTemplates.newPrimitiveTemplate(type, "subtype"));
		ReflectionUtilTypes tmpl = new ReflectionUtilTypes(primitiveTypes, primitiveTypes);
		tmpl.className = "SimpleField";
		tmpl.packageName = "simpleReflect";
		TemplateRender.renderClassTemplates("src/templates/SimpleField.stg", "SimpleField", tmpl);
	}


	public static final void generateSimpleMethod() {
		List<PrimitiveClassTemplateDeprecated> primitiveTypes = ListUtil.map(GenerateReflect.primitiveTypes, (type) -> PrimitiveTemplates.newPrimitiveTemplate(type, "subtype"));
		ReflectionUtilTypes tmpl = new ReflectionUtilTypes(primitiveTypes, primitiveTypes);
		tmpl.className = "SimpleMethod";
		tmpl.packageName = "simpleReflect";
		TemplateRender.renderClassTemplates("src/templates/SimpleMethod.stg", "SimpleMethod", tmpl);
	}


	public static final void generateAll() {
		generateSimpleField();
		generateSimpleMethod();
	}


	public static void main(String[] args) {
		generateAll();
	}

}
