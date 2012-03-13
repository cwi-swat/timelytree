package treelayout.algorithms.bloeschreingold;

import treelayout.interfaces.TreeLayoutAlgorithm;
import treelayout.interfaces.TreeNode;

public class BloeschReingold implements TreeLayoutAlgorithm{

	private void convertBackNode(Node converted, TreeNode root) {
		root.x = converted.x;
		root.y = converted.y;
		for(int i = 0 ; i < root.children.length;i++){
			convertBackNode(converted.children[i], root.children[i]);
		}
		
	}

	private Node convertNode(TreeNode root) {
		Node[] children = new Node[root.children.length];
		for(int i = 0 ; i < root.children.length ; i++){
			children[i] = convertNode(root.children[i]);
		}
		return new Node(root.width,root.height,children);
	}

	@Override
	public Object convert(TreeNode root) {
		return convertNode(root);
	}

	@Override
	public void convertBack(Object converted, TreeNode root) {
		Node convertedN = (Node) converted;
		convertedN.makeCoordinateAbsolute(0, 0);
		convertBackNode(convertedN,root);
		
	}

	@Override
	public void runOnConverted(Object converted) {
		((Node)converted).shapeTree(0,0,new TreeNodeRaster());
	}

}
