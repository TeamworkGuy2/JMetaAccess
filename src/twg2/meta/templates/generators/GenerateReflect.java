package twg2.meta.templates.generators;

import java.util.Arrays;
import java.util.List;

import org.stringtemplate.v4.ST;

import twg2.collections.builder.ListUtil;
import twg2.meta.templates.ReflectionUtilTypes;
import twg2.template.codeTemplate.primitiveTemplate.PrimitiveTemplates;
import twg2.template.codeTemplate.primitiveTemplate.PrimitiveTypeClassTemplate;
import twg2.template.codeTemplate.render.STTemplates;
import twg2.template.codeTemplate.render.TemplateImports;
import twg2.template.codeTemplate.render.TemplateRenderBuilder;


/**
 * @author TeamworkGuy2
 * @since 2015-6-6
 */
public class GenerateReflect {
	private static String tmplDir = "src/twg2/meta/templates/";
	private static List<Class<?>> primitiveTypes = Arrays.asList(new Class<?>[] { Boolean.TYPE, Byte.TYPE, Character.TYPE, Short.TYPE, Integer.TYPE, Float.TYPE, Long.TYPE, Double.TYPE });
	private static TemplateImports importsMapper = TemplateImports.emptyInst();


	public static final void generateSimpleField() {
		List<PrimitiveTypeClassTemplate> primitiveTypes = ListUtil.map(GenerateReflect.primitiveTypes, (type) -> PrimitiveTemplates.ofType(type, "subtype"));
		ReflectionUtilTypes tmpl = new ReflectionUtilTypes(primitiveTypes, primitiveTypes);
		tmpl.className = "SimpleField";
		tmpl.packageName = "twg2.meta.fieldAccess";

		ST stTmpl = STTemplates.fromFile(tmplDir + "SimpleField.stg", "SimpleField", importsMapper);
		TemplateRenderBuilder.newInst()
				.addParam("var", tmpl)
				.writeDst(tmpl)
				.render(stTmpl);
	}


	public static final void generateSimpleFieldImpl() {
		List<PrimitiveTypeClassTemplate> primitiveTypes = ListUtil.map(GenerateReflect.primitiveTypes, (type) -> PrimitiveTemplates.ofType(type, "subtype"));
		ReflectionUtilTypes tmpl = new ReflectionUtilTypes(primitiveTypes, primitiveTypes);
		tmpl.className = "SimpleFieldImpl";
		tmpl.packageName = "twg2.meta.fieldAccess";

		ST stTmpl = STTemplates.fromFile(tmplDir + "SimpleFieldImpl.stg", "SimpleFieldImpl", importsMapper);
		TemplateRenderBuilder.newInst()
				.addParam("var", tmpl)
				.writeDst(tmpl)
				.render(stTmpl);
	}


	public static final void generateSimpleMethod() {
		List<PrimitiveTypeClassTemplate> primitiveTypes = ListUtil.map(GenerateReflect.primitiveTypes, (type) -> PrimitiveTemplates.ofType(type, "subtype"));
		ReflectionUtilTypes tmpl = new ReflectionUtilTypes(primitiveTypes, primitiveTypes);
		tmpl.className = "SimpleMethod";
		tmpl.packageName = "twg2.meta.simpleReflect";

		ST stTmpl = STTemplates.fromFile(tmplDir + "SimpleMethod.stg", "SimpleMethod", importsMapper);
		TemplateRenderBuilder.newInst()
				.addParam("var", tmpl)
				.writeDst(tmpl)
				.render(stTmpl);
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
