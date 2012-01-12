package treelayout.algorithms.miyaderamasud;

import treelayout.interfaces.TreeLayoutAlgorithm;
import treelayout.interfaces.TreeNode;


public class MiyaderaMasud implements TreeLayoutAlgorithm{
	
	@Override
	public void run(TreeNode root){
		Node converted = convert(root);
		new Algorithm().doLayout(converted,null,0);
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
		Node result = new Node();
		result.width = root.width;
		result.height = root.height;
		result.children = new Node[root.children.length];
		for(int i = 0 ; i < root.children.length ; i++){
			result.children[i] = convert(root.children[i]);
		}
		return result;
	}


}
