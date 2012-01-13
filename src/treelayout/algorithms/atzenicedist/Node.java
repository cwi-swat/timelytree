package treelayout.algorithms.atzenicedist;

import java.util.ArrayList;


public class Node {
	// input
	public double width,height;
	public Node[] children;

	// output
	public double x,y;
	int depth;
	int depthTillLowest;
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
	
	public void setDepth(int depth){
		this.depth = depth;
		for(Node child : children){
			child.setDepth(depth+1);
		}
	}
	
	public void setDepthTillLowest(int depth2){
		this.depthTillLowest = depth2;
		for(Node child : children){
			child.setDepthTillLowest(depth2-1);
		}
	}
	
	public int maxDepth(){
		int res = 1;
		for(Node child : children){
			res = Math.max(res, child.maxDepth()+1);
		}
		return res;
	}

	
}