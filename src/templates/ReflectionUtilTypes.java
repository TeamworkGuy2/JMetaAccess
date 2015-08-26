package templates;

import java.util.ArrayList;
import java.util.List;

import codeTemplate.ClassTemplate;
import codeTemplate.primitiveTemplate.PrimitiveTypeClassTemplate;

/**
 * @author TeamworkGuy2
 * @since 2015-6-6
 */
public class ReflectionUtilTypes extends ClassTemplate {
	public List<PrimitiveTypeClassTemplate> primitiveTypes;
	public List<PrimitiveTypeClassTemplate> arrayTypes;


	public ReflectionUtilTypes(List<PrimitiveTypeClassTemplate> primitiveTypes, List<PrimitiveTypeClassTemplate> arrayTypes) {
		super();
		this.primitiveTypes = new ArrayList<>(primitiveTypes);
		this.arrayTypes = new ArrayList<>(arrayTypes);
	}

}
