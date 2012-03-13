package treelayout.algorithms.miyaderamasud;

import treelayout.interfaces.TreeLayoutAlgorithm;
import treelayout.interfaces.TreeNode;


public class MiyaderaMasud implements TreeLayoutAlgorithm{
	
	public static final double ROOT_FROM_FIRST_CHILD = 6;
	
	private void convertBackNode(Node converted, TreeNode root) {
		root.x = converted.x;
		root.y = converted.y;
		for(int i = 0 ; i < root.children.length;i++){
			convertBackNode(converted.children[i], root.children[i]);
		}
		
	}

	private Node convertNode(TreeNode root) {
		Node result = new Node();
		result.width = root.width;
		result.height = root.height;
		result.children = new Node[root.children.length];
		for(int i = 0 ; i < root.children.length ; i++){
			result.children[i] = convertNode(root.children[i]);
		}
		return result;
	}

	@Override
	public Object convert(TreeNode root) {
		return convertNode(root);
	}

	@Override
	public void convertBack(Object converted, TreeNode root) {
		convertBackNode((Node)converted, root);
		
	}

	@Override
	public void runOnConverted(Object root) {
		new Algorithm().doLayout((Node)root,null,0);
	}


}
