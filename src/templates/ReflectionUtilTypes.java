package templates;

import java.util.ArrayList;
import java.util.List;

import codeTemplate.ClassTemplate;
import codeTemplate.PrimitiveClassTemplateDeprecated;

/**
 * @author TeamworkGuy2
 * @since 2015-6-6
 */
public class ReflectionUtilTypes extends ClassTemplate {
	public List<PrimitiveClassTemplateDeprecated> primitiveTypes;
	public List<PrimitiveClassTemplateDeprecated> arrayTypes;


	public ReflectionUtilTypes(List<PrimitiveClassTemplateDeprecated> primitiveTypes, List<PrimitiveClassTemplateDeprecated> arrayTypes) {
		super();
		this.primitiveTypes = new ArrayList<>(primitiveTypes);
		this.arrayTypes = new ArrayList<>(arrayTypes);
	}

}
