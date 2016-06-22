package twg2.meta.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author TeamworkGuy2
 * @since 2015-9-7
 */
public class FieldGetData {

	// ==== Base, Tree, Leaf! twg2.meta.test data hierarchy ====
	public static interface BaseI {
		public int getBaseI_Field1();
		public void setBaseI_Field1(int val);
	}


	public static interface Tree1I {
		public int getTree1I_Field1();
		public void setTree1I_Field1(int val);
	}


	public static interface Tree2I {
		public int getTree2I_Field1();
		public void setTree2I_Field1(int val);
	}


	public static interface Leaf1I extends Tree1I {
		public void blowInTheWind_Leaf1();
	}


	public static interface Leaf2I extends Tree2I {
		public void blowInTheWind_Leaf2();
	}


	public static class Base implements BaseI {
		private @Getter @Setter int baseI_Field1;
	}


	public static class Tree1 implements Tree1I {
		private @Getter @Setter int tree1I_Field1;
	}


	public static class Tree2 implements Tree2I {
		private @Getter @Setter int tree2I_Field1;
	}


	public static class Leaf1 extends Tree1 implements Leaf1I {
		private @Getter @Setter double leaf1_Field1;
		public @Override void blowInTheWind_Leaf1() { System.out.println("blow in the wind 1"); }
	}


	public static class Leaf2 extends Tree2 implements Leaf2I {
		private @Getter @Setter Object leaf2_Field1;
		private @Getter @Setter java.lang.annotation.ElementType leaf2_Field2;
		public @Override void blowInTheWind_Leaf2() { System.out.println("blow in the wind 2"); }
	}


	public static class BaseLeaf extends Base implements BaseI {
		public @Getter @Setter String baseLeaf_Field1;
	}




	// ==== Bugs! twg2.meta.test classes ====
	@AllArgsConstructor
	@NoArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class Bug {
		private @Getter @Setter int count;
		private @Getter @Setter boolean t;
	}


	@NoArgsConstructor
	@EqualsAndHashCode(callSuper = true)
	@ToString
	public static class ColonyBug extends Bug {
		private @Getter @Setter boolean manager;
		private @Getter @Setter String id;

		public ColonyBug(int count, boolean t, boolean manager, String id) {
			super(count, t);
			this.manager = manager;
			this.id = id;
		}

	}


	@NoArgsConstructor
	@EqualsAndHashCode(callSuper = true)
	@ToString
	public static class Termite extends ColonyBug {
		private @Getter @Setter int colonyNum;
		private @Getter @Setter String termiteName;
		private @Getter @Setter StringBuilder colonyNotesBuf;

		public Termite(int colonyNum, String termiteName, int count, boolean t, boolean awesome, String id) {
			super(count, t, awesome, id);
			this.colonyNum = colonyNum;
			this.termiteName = termiteName;
			this.colonyNotesBuf = new StringBuilder();
		}

	}


	@NoArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class TermiteColony {
		private @Getter @Setter Termite boss;
		private @Getter @Setter List<Termite> termites;
		private @Getter @Setter long memPool;
		private @Getter @Setter char[] colonyName;

		public TermiteColony(Termite boss, List<Termite> termites, long memPool, String colonyName) {
			this.boss = boss;
			this.termites = termites;
			this.memPool = memPool;
			this.colonyName = colonyName.toCharArray();
		}

	}




	public static class Dummy {

		public static Termite newTermite1() {
			Termite bug = new Termite(42, "termite-42", 3, false, true, "id2332");
			bug.setColonyNotesBuf(new StringBuilder("fastest termite ever!"));
			return bug;
		}


		public static Termite newTermite2() {
			Termite bug = new Termite(19, "first termite!", 12345, false, true, "IDs, IDs, IDS for all!");
			bug.setColonyNotesBuf(new StringBuilder("witty termite description -HERE-"));
			return bug;
		}


		public static Termite newTermiteBoss() {
			Termite bug = new Termite(423, "termite-1", 83, false, true, "id423");
			bug.setColonyNotesBuf(new StringBuilder("bossiest termite ever!"));
			return bug;
		}


		public static TermiteColony newTermiteColony1() {
			Termite boss = newTermiteBoss();
			Termite bug1 = newTermite1();
			Termite bug2 = newTermite2();
			TermiteColony colony = new TermiteColony(boss, new ArrayList<>(Arrays.asList(bug1, bug2)), 1234567890, "names, tames");
			return colony;
		}
	}



	public static class NotAccessible {
		private List<String> myPasswords;

		public NotAccessible() {
			this.myPasswords = new ArrayList<>();
			this.myPasswords.add("what does the fox say?");
		}
	}

}
