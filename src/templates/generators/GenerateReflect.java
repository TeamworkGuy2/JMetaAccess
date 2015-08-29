package templates.generators;

import java.util.Arrays;
import java.util.List;

import templates.ReflectionUtilTypes;
import twg2.collections.util.ListUtil;
import codeTemplate.primitiveTemplate.PrimitiveTemplates;
import codeTemplate.primitiveTemplate.PrimitiveTypeClassTemplate;
import codeTemplate.render.TemplateRenders;


/**
 * @author TeamworkGuy2
 * @since 2015-6-6
 */
public class GenerateReflect {
	private static List<Class<?>> primitiveTypes = Arrays.asList(new Class<?>[] { Boolean.TYPE, Byte.TYPE, Character.TYPE, Short.TYPE, Integer.TYPE, Float.TYPE, Long.TYPE, Double.TYPE });


	public static final void generateSimpleField() {
		List<PrimitiveTypeClassTemplate> primitiveTypes = ListUtil.map(GenerateReflect.primitiveTypes, (type) -> PrimitiveTemplates.ofType(type, "subtype"));
		ReflectionUtilTypes tmpl = new ReflectionUtilTypes(primitiveTypes, primitiveTypes);
		tmpl.className = "SimpleField";
		tmpl.packageName = "simpleReflect";
		TemplateRenders.renderClassTemplates("src/templates/SimpleField.stg", "SimpleField", tmpl);
	}


	public static final void generateSimpleFieldImpl() {
		List<PrimitiveTypeClassTemplate> primitiveTypes = ListUtil.map(GenerateReflect.primitiveTypes, (type) -> PrimitiveTemplates.ofType(type, "subtype"));
		ReflectionUtilTypes tmpl = new ReflectionUtilTypes(primitiveTypes, primitiveTypes);
		tmpl.className = "SimpleFieldImpl";
		tmpl.packageName = "simpleReflect";
		TemplateRenders.renderClassTemplates("src/templates/SimpleFieldImpl.stg", "SimpleFieldImpl", tmpl);
	}


	public static final void generateSimpleMethod() {
		List<PrimitiveTypeClassTemplate> primitiveTypes = ListUtil.map(GenerateReflect.primitiveTypes, (type) -> PrimitiveTemplates.ofType(type, "subtype"));
		ReflectionUtilTypes tmpl = new ReflectionUtilTypes(primitiveTypes, primitiveTypes);
		tmpl.className = "SimpleMethod";
		tmpl.packageName = "simpleReflect";
		TemplateRenders.renderClassTemplates("src/templates/SimpleMethod.stg", "SimpleMethod", tmpl);
	}


	public static final void generateAll() {
		generateSimpleField();
		generateSimpleFieldImpl();
		generateSimpleMethod();
	}


	public static void main(String[] args) {
		generateAll();
	}

}
