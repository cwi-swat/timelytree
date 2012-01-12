package treelayout.algorithms.miyaderamasud;

public class Node {

	double width, height;
	double x,y;
	Node[] children;
	boolean isLeaf(){
		return children.length == 0;
	}
	
}
