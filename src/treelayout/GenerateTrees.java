package treelayout;

import java.util.Random;

import treelayout.interfaces.TreeNode;




public class GenerateTrees {
	
	
	public int getRandomInRange(int start, int end){
		double r = rand.nextDouble();
		return start + (int)Math.rint(r * (end - start));
	}
	
	public double getRandomInRange(double start, double end){
		double r = rand.nextDouble();
		return start + r * (end - start);
	}
	
	Random rand;
	int nrElements;
	int minKids, maxKids, maxDepth;
	double minWidth, maxWidth;
	double minHeight, maxHeight;
	public GenerateTrees(int nr, int maxDepth, int minKids, int maxKids ,
			double minWidth,  double maxWidth, double minHeight, 
			double maxHeight, long seed){
		rand = new Random(seed);
		this.nrElements = nr;
		this.maxDepth = maxDepth;
		this.minKids = minKids;
		this.maxKids = maxKids;
		this.minWidth = minWidth;
		this.maxWidth = maxWidth;
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
	}


	private TreeNode generate(int nrElements) {
		int initElems = nrElements;
		double width = getRandomInRange(minWidth,maxWidth);
		double height = getRandomInRange(minHeight,maxHeight);
		TreeNode[] children;
		nrElements-=1;
		if(nrElements <= 0){
			return new TreeNode(width,height);
		} else {
			children = new TreeNode[getRandomInRange(1, Math.min(maxKids,nrElements))];
		}
		double[] childParts = new double[children.length];
		double total = 0;
		for(int i = 0 ; i < children.length ; i++){
			childParts[i] = Math.random();
			total+= childParts[i];
		}
		int totChild = 0;
		for(int i = 0 ; i < children.length-1 ; i++){
			int n = (int)(((double)(nrElements - children.length)) * (childParts[i]/total));
			
			totChild += n+1;
			children[i] = generate(n+1);
			
		}
		
		children[children.length-1] = generate(nrElements - totChild);
		TreeNode res = new TreeNode(width,height,children);
		int s = res.size();
		if(s != initElems){
			System.out.printf("WEIRD SHIT! %d %d\n", initElems, s);
		}
		return res;
	}
									  
	public TreeNode generate(){
		return generate(nrElements);
	}

}