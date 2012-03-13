package treelayout.interfaces;


public interface TreeLayoutAlgorithm {
	
	Object convert(TreeNode root);
	
	void convertBack(Object converted,TreeNode root);
	
	void runOnConverted(Object root);
}
