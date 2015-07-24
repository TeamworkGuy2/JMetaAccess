package simpleReflect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import collectionUtils.Entries;

/**
 * @author TeamworkGuy2
 * @since 2015-6-27
 */
public class ClassInstanceOf {

	public static Class<?> closest(Collection<Class<?>> classes, Class<?> clazz) {
		// map of closest classes and their depths from the class
		List<Map.Entry<Class<?>, Integer>> assignableTo = new ArrayList<>();

		for(Class<?> type : classes) {
			if(type.isAssignableFrom(clazz)) {
				assignableTo.add(Entries.of(type, depthSeparation(type, clazz)));
			}
		}

		if(assignableTo.size() == 0) {
			return null;
		}

		Map.Entry<Class<?>, Integer> closest = assignableTo.get(0);
		for(int i = 1, size = assignableTo.size(); i < size; i++) {
			Map.Entry<Class<?>, Integer> parent = assignableTo.get(i);
			if(closest.getKey().isAssignableFrom(parent.getKey()) && closest.getValue() >= parent.getValue()) {
				closest = parent;
			}
		}

		return closest.getKey();
	}


	/** the number of sub/super classes/interfaces that separate two types, for example {@link Number} and {@link Integer} have a depth separation of {@code 1}
	 * @param parent
	 * @param child
	 * @return the number of classes/interfaces separating two types
	 */
	public static int depthSeparation(Class<?> parent, Class<?> child) {
		List<Integer> depths = new ArrayList<>();
		depthSeparation0(parent, child, 0, depths);
		int least = Integer.MAX_VALUE;
		for(int i = depths.size() - 1; i > -1; i--) {
			int depthI = depths.get(i);
			if(least > depthI) {
				least = depthI;
			}
		}
		return least;
	}


	public static void depthSeparation0(Class<?> parent, Class<?> child, int currentTravelDepth, List<Integer> resDepths) {
		if(child.equals(parent)) {
			resDepths.add(currentTravelDepth);
			return;
		}

		// traverse parent classes
		if(!child.isInterface()) {
			Class<?> parentClass = child.getSuperclass();
			if(parentClass != null) {
				depthSeparation0(parent, parentClass, currentTravelDepth + 1, resDepths);
			}
		}

		// traverse implemented interfaces
		for(Class<?> parentInterface : child.getInterfaces()) {
			if(parent.isAssignableFrom(parentInterface)) {
				depthSeparation0(parent, parentInterface, currentTravelDepth + 1, resDepths);
			}
		}
	}

}
