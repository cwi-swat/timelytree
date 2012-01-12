package treelayout.algorithms.atze;

import java.util.ArrayList;


public class Node {
	// input
	public double width,height;
	public Node[] children;

	// output
	public double x,y;
	
	// temp variables
	double shift;
	double distanceFromLeftMostSibling;
	
	public Node(double width, double height, Node ... children){
		this.width = width;
		this.height = height;
		this.children = children;
		y = 0;
	}
	
	double yEnd(){ return y + height; }
	
	public String toString(){
		String me = String.format("<(%f+%f,%f)(%f,%f)>",x,shift,y,width,height);
		if(children.length == 0){
			return me;
		} else {
			String kids = "";
			for(Node child : children){
				kids += child.toString();
			}
			return "{" + me + " " + kids + "}";
		}
	}
	
	public int size(){
		int res = 1;
		for(Node node : children){
			res += node.size();
		}
		return res;
	}

	
}