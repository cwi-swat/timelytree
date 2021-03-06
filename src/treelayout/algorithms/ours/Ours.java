package treelayout.algorithms.ours;

import treelayout.interfaces.TreeLayoutAlgorithm;
import treelayout.interfaces.TreeNode;

public class Ours implements TreeLayoutAlgorithm{
	
	private final IGetPos getPos;
	
	
	public Ours(IGetPos getPos) {
		this.getPos = getPos;
	}
	
	private void convertBackNodes(Node converted, TreeNode root) {
		root.x = converted.x;
		root.y = converted.y ;
		for(int i = 0 ; i < root.children.length;i++){
			convertBackNodes(converted.c[i], root.children[i]);
		}
	}
	
	private Node convertNodes(TreeNode root) {
		Node[] children = new Node[root.children.length];
		for(int i = 0 ; i < root.children.length ; i++){
			children[i] = convertNodes(root.children[i]);
		}
		return new Node(root.width,root.height,children);
	}
	
	private double height(Node root){
		double heightChild = 0;
		for(Node c : root.c){
			heightChild = Math.max(heightChild,height(c));
		}
		return heightChild + root.h;
	}
	
	private void setYsBack(Node root, double y){
		root.y = y;
		y+=root.h;
		for(Node child : root.c){
			setYsBack(child, y);
		}
	}
	
	private void setYs(Node root, double y){
		y-=root.h;
		root.y = y;
		for(Node child : root.c){
			setYs(child, y);
		}
	}

	@Override
	public Object convert(TreeNode root) {
		Node rroot = convertNodes(root);
		setYs(rroot,height(rroot));
		return rroot;
	}

	@Override
	public void convertBack(Object convertedO, TreeNode root) {
		Node converted = (Node)convertedO;
		setYsBack(converted,0);
		convertBackNodes(converted,root);
	}

	@Override
	public void runOnConverted(Object root) {
		Algorithm.doAllLayout((Node)root, getPos);
		
	}
}