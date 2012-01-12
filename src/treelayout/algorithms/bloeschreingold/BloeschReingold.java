package treelayout.algorithms.bloeschreingold;

import treelayout.interfaces.TreeLayoutAlgorithm;
import treelayout.interfaces.TreeNode;

public class BloeschReingold implements TreeLayoutAlgorithm{

	@Override
	public void run(TreeNode root){
		Node converted = convert(root);
		converted.shapeTree(0,0,new TreeNodeRaster());
		converted.makeCoordinateAbsolute(0, 0);
		convertBack(converted,root);
	}

	private void convertBack(Node converted, TreeNode root) {
		root.x = converted.x;
		root.y = converted.y;
		for(int i = 0 ; i < root.children.length;i++){
			convertBack(converted.children[i], root.children[i]);
		}
		
	}

	private Node convert(TreeNode root) {
		Node[] children = new Node[root.children.length];
		for(int i = 0 ; i < root.children.length ; i++){
			children[i] = convert(root.children[i]);
		}
		return new Node(root.width,root.height,children);
	}

}
