package treelayout.algorithms.buchheimwalker;

import treelayout.interfaces.TreeLayoutAlgorithm;
import treelayout.interfaces.TreeNode;

public class BuchheimWalker implements TreeLayoutAlgorithm{
	
	@Override
	public void run(TreeNode root){
		NodeItem converted = convert(root);
		new Algorithm().run(converted);
		convertBack(converted,root);
	}

	private void convertBack(NodeItem converted, TreeNode root) {
		root.x = converted.x;
		root.y = converted.y;
		for(int i = 0 ; i < root.children.length;i++){
			convertBack(converted.children[i], root.children[i]);
		}
		
	}

	private NodeItem convert(TreeNode root) {
		NodeItem result = new NodeItem();
		result.width = root.width;
		result.height = root.height;
		result.children = new NodeItem[root.children.length];
		for(int i = 0 ; i < root.children.length ; i++){
			result.children[i] = convert(root.children[i]);
			result.children[i].parent = result;
		}
		for(int i = 0 ;i < result.children.length ; i++){
			if(i!=0){
				result.children[i].prevSibling = result.children[i-1];
			}
			if(i!=result.children.length -1){
				result.children[i].nextSibling = result.children[i+1];
			}
		}
		return result;
	}

}
