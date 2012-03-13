package treelayout.algorithms.ours;

public class CenterGetPos implements IGetPos {

	@Override
	public double getPos(Node node) {
		if(node.c.length == 0){
			return 0;
		} else {
			int lastIndex = node.c.length - 1;
			Node first = node.c[0];
			Node last = node.c[lastIndex];
			double middle = (first.x + first.s + last.x + last.s + last.w) / 2.0;
			double halfRoot = node.w / 2.0;
			return middle - halfRoot;
		}
	}

}
