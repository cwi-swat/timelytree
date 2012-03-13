package treelayout.algorithms.supernet;

import treelayout.interfaces.TreeLayoutAlgorithm;
import treelayout.interfaces.TreeNode;
import static treelayout.algorithms.supernet.Algorithm.*;

public class SuperNet implements TreeLayoutAlgorithm{


	@Override
	public void run(TreeNode root){
		Node converted = convert(root);
		Algorithm.setYs(converted);
		Algorithm.layout(converted);
		Algorithm.makeAbsolute(converted, 0);
		convertBack(converted,root);
	}

	private void convertBack(Node converted, TreeNode root) {
		root.x = converted.getXLeft();
		root.y = converted.getYTop();
		for(int i = 0 ; i < root.children.length;i++){
			convertBack(converted.getChildren()[i], root.children[i]);
		}
		
	}

	private Node convert(TreeNode root) {
		Node[] children = new Node[root.children.length];
		for(int i = 0 ; i < root.children.length ; i++){
			children[i] = convert(root.children[i]);
		}
		if(children.length == 0){
			return new Leaf(root.width,root.height);
		} else {
			return new InternalNode(root.width,root.height,children);
		}
	}


}

