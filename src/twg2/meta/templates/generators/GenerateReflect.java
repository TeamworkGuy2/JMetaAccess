package twg2.meta.templates.generators;

import java.util.Arrays;
import java.util.List;

import twg2.collections.util.ListUtil;
import twg2.meta.templates.ReflectionUtilTypes;
import codeTemplate.primitiveTemplate.PrimitiveTemplates;
import codeTemplate.primitiveTemplate.PrimitiveTypeClassTemplate;
import codeTemplate.render.TemplateRenders;


/**
 * @author TeamworkGuy2
 * @since 2015-6-6
 */
public class GenerateReflect {
	private static String tmplDir = "src/twg2/meta/templates/";
	private static List<Class<?>> primitiveTypes = Arrays.asList(new Class<?>[] { Boolean.TYPE, Byte.TYPE, Character.TYPE, Short.TYPE, Integer.TYPE, Float.TYPE, Long.TYPE, Double.TYPE });


	public static final void generateSimpleField() {
		List<PrimitiveTypeClassTemplate> primitiveTypes = ListUtil.map(GenerateReflect.primitiveTypes, (type) -> PrimitiveTemplates.ofType(type, "subtype"));
		ReflectionUtilTypes tmpl = new ReflectionUtilTypes(primitiveTypes, primitiveTypes);
		tmpl.className = "SimpleField";
		tmpl.packageName = "twg2.meta.fieldAccess";
		TemplateRenders.renderClassTemplates(tmplDir + "SimpleField.stg", "SimpleField", tmpl);
	}


	public static final void generateSimpleFieldImpl() {
		List<PrimitiveTypeClassTemplate> primitiveTypes = ListUtil.map(GenerateReflect.primitiveTypes, (type) -> PrimitiveTemplates.ofType(type, "subtype"));
		ReflectionUtilTypes tmpl = new ReflectionUtilTypes(primitiveTypes, primitiveTypes);
		tmpl.className = "SimpleFieldImpl";
		tmpl.packageName = "twg2.meta.fieldAccess";
		TemplateRenders.renderClassTemplates(tmplDir + "SimpleFieldImpl.stg", "SimpleFieldImpl", tmpl);
	}


	public static final void generateSimpleMethod() {
		List<PrimitiveTypeClassTemplate> primitiveTypes = ListUtil.map(GenerateReflect.primitiveTypes, (type) -> PrimitiveTemplates.ofType(type, "subtype"));
		ReflectionUtilTypes tmpl = new ReflectionUtilTypes(primitiveTypes, primitiveTypes);
		tmpl.className = "SimpleMethod";
		tmpl.packageName = "twg2.meta.simpleReflect";
		TemplateRenders.renderClassTemplates(tmplDir + "SimpleMethod.stg", "SimpleMethod", tmpl);
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
