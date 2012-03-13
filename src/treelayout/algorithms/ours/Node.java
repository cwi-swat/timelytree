package treelayout.algorithms.ours;

public class Node {

	public double w,h;
	public Node[] c;
	public double x,y;
	public double s;
	
	// for leafs
	Node nxtl;
	double nxtls;
	Node nxtr;
	double nxtrs;
	
	
	public Node(double width, double height, Node ... children){
		this.w = width;
		this.h = height;
		this.c = children;
		nxtls = nxtrs = x = y = 0;
	}
}