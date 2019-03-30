package twg2.meta.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
		private int baseI_Field1;

		@Override public int getBaseI_Field1() { return baseI_Field1; }

		@Override public void setBaseI_Field1(int baseI_Field1) { this.baseI_Field1 = baseI_Field1; }
	}


	public static class Tree1 implements Tree1I {
		private int tree1I_Field1;

		@Override public int getTree1I_Field1() { return tree1I_Field1; }

		@Override public void setTree1I_Field1(int tree1i_Field1) { tree1I_Field1 = tree1i_Field1; }
	}


	public static class Tree2 implements Tree2I {
		private int tree2I_Field1;

		@Override public int getTree2I_Field1() { return tree2I_Field1; }

		@Override public void setTree2I_Field1(int tree2i_Field1) { tree2I_Field1 = tree2i_Field1; }
	}


	public static class Leaf1 extends Tree1 implements Leaf1I {
		private double leaf1_Field1;

		public double getLeaf1_Field1() { return leaf1_Field1; }

		public void setLeaf1_Field1(double leaf1_Field1) { this.leaf1_Field1 = leaf1_Field1; }


		@Override
		public void blowInTheWind_Leaf1() {
			System.out.println("blow in the wind 1");
		}
	}


	public static class Leaf2 extends Tree2 implements Leaf2I {
		private Object leaf2_Field1;
		private java.lang.annotation.ElementType leaf2_Field2;

		public Object getLeaf2_Field1() { return leaf2_Field1; }

		public void setLeaf2_Field1(Object leaf2_Field1) { this.leaf2_Field1 = leaf2_Field1; }

		public java.lang.annotation.ElementType getLeaf2_Field2() { return leaf2_Field2; }

		public void setLeaf2_Field2(java.lang.annotation.ElementType leaf2_Field2) { this.leaf2_Field2 = leaf2_Field2; }


		@Override
		public void blowInTheWind_Leaf2() {
			System.out.println("blow in the wind 2");
		}
	}


	public static class BaseLeaf extends Base implements BaseI {
		public String baseLeaf_Field1;

		public String getBaseLeaf_Field1() { return baseLeaf_Field1; }

		public void setBaseLeaf_Field1(String baseLeaf_Field1) { this.baseLeaf_Field1 = baseLeaf_Field1; }
	}




	// ==== Bugs! twg2.meta.test classes ====
	public static class Bug {
		private int count;
		private boolean t;

		public Bug() {
		}

		public Bug(int count, boolean t) {
			this.count = count;
			this.t = t;
		}

		public int getCount() { return count; }

		public void setCount(int count) { this.count = count; }

		public boolean isT() { return t; }

		public void setT(boolean t) { this.t = t; }

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + count;
			result = prime * result + (t ? 1231 : 1237);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Bug other = (Bug) obj;
			if (count != other.count)
				return false;
			if (t != other.t)
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "Bug [count=" + count + ", t=" + t + "]";
		}
	}


	public static class ColonyBug extends Bug {
		private boolean manager;
		private String id;

		public ColonyBug() {
			super();
		}

		public ColonyBug(int count, boolean t, boolean manager, String id) {
			super(count, t);
			this.manager = manager;
			this.id = id;
		}

		public boolean isManager() { return manager; }

		public void setManager(boolean manager) { this.manager = manager; }

		public String getId() { return id; }

		public void setId(String id) { this.id = id; }

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			result = prime * result + (manager ? 1231 : 1237);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			ColonyBug other = (ColonyBug) obj;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			if (manager != other.manager)
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "ColonyBug [manager=" + manager + ", id=" + id + "]";
		}
	}


	public static class Termite extends ColonyBug {
		private int colonyNum;
		private String termiteName;
		private StringBuilder colonyNotesBuf;

		public Termite() {
			super();
		}

		public Termite(int colonyNum, String termiteName, int count, boolean t, boolean awesome, String id) {
			super(count, t, awesome, id);
			this.colonyNum = colonyNum;
			this.termiteName = termiteName;
			this.colonyNotesBuf = new StringBuilder();
		}

		public int getColonyNum() { return colonyNum; }

		public void setColonyNum(int colonyNum) { this.colonyNum = colonyNum; }

		public String getTermiteName() { return termiteName; }

		public void setTermiteName(String termiteName) { this.termiteName = termiteName; }

		public StringBuilder getColonyNotesBuf() { return colonyNotesBuf; }

		public void setColonyNotesBuf(StringBuilder colonyNotesBuf) { this.colonyNotesBuf = colonyNotesBuf; }

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((colonyNotesBuf == null) ? 0 : colonyNotesBuf.hashCode());
			result = prime * result + colonyNum;
			result = prime * result + ((termiteName == null) ? 0 : termiteName.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			Termite other = (Termite) obj;
			if (colonyNotesBuf == null) {
				if (other.colonyNotesBuf != null)
					return false;
			} else if (!colonyNotesBuf.equals(other.colonyNotesBuf))
				return false;
			if (colonyNum != other.colonyNum)
				return false;
			if (termiteName == null) {
				if (other.termiteName != null)
					return false;
			} else if (!termiteName.equals(other.termiteName))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "Termite [colonyNum=" + colonyNum + ", termiteName=" + termiteName + ", colonyNotesBuf=" + colonyNotesBuf + "]";
		}
	}


	public static class TermiteColony {
		private Termite boss;
		private List<Termite> termites;
		private long memPool;
		private char[] colonyName;

		public TermiteColony() {
		}

		public TermiteColony(Termite boss, List<Termite> termites, long memPool, String colonyName) {
			this.boss = boss;
			this.termites = termites;
			this.memPool = memPool;
			this.colonyName = colonyName.toCharArray();
		}

		public Termite getBoss() { return boss; }

		public void setBoss(Termite boss) { this.boss = boss; }

		public List<Termite> getTermites() { return termites; }

		public void setTermites(List<Termite> termites) { this.termites = termites; }

		public long getMemPool() { return memPool; }

		public void setMemPool(long memPool) { this.memPool = memPool; }

		public char[] getColonyName() { return colonyName; }

		public void setColonyName(char[] colonyName) { this.colonyName = colonyName; }

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((boss == null) ? 0 : boss.hashCode());
			result = prime * result + Arrays.hashCode(colonyName);
			result = prime * result + (int) (memPool ^ (memPool >>> 32));
			result = prime * result + ((termites == null) ? 0 : termites.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TermiteColony other = (TermiteColony) obj;
			if (boss == null) {
				if (other.boss != null)
					return false;
			} else if (!boss.equals(other.boss))
				return false;
			if (!Arrays.equals(colonyName, other.colonyName))
				return false;
			if (memPool != other.memPool)
				return false;
			if (termites == null) {
				if (other.termites != null)
					return false;
			} else if (!termites.equals(other.termites))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "TermiteColony [boss=" + boss + ", termites=" + termites + ", memPool=" + memPool + ", colonyName=" + Arrays.toString(colonyName) + "]";
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
