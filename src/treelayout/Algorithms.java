package treelayout;

import treelayout.algorithms.atze.VanDerPloeg;
import treelayout.algorithms.bloeschreingold.BloeschReingold;
import treelayout.algorithms.buchheimwalker.BuchheimWalker;
import treelayout.algorithms.miyaderamasud.MiyaderaMasud;
import treelayout.algorithms.atzefixeddistfirstchild.VanDerPloegFixedDistFromFirstChild;
import treelayout.algorithms.atzenicedist.VanDerPloegNiceDist;
import treelayout.interfaces.TreeLayoutAlgorithm;
import treelayout.interfaces.TreeNode;

public enum Algorithms {

	VanDerPloeg(new VanDerPloeg()),
	BuchHeimWalker(new BuchheimWalker()),
	BloeschReingold(new BloeschReingold()),
	MiyaderaMasud(new MiyaderaMasud()),
	VanDerPloegFixedDistFromFirstChild(new VanDerPloegFixedDistFromFirstChild()),
	VanDerPloegNiceDist(new VanDerPloegNiceDist());
	

	public static final Algorithms[] equallySized = {Algorithms.VanDerPloeg, Algorithms.BuchHeimWalker};
	public static final Algorithms[] arbitrarilySized = {Algorithms.VanDerPloeg, Algorithms.BloeschReingold, Algorithms.MiyaderaMasud, Algorithms.VanDerPloegFixedDistFromFirstChild};	
	public static final Algorithms[] arbitrarilySizedMiddle = {Algorithms.VanDerPloeg, Algorithms.BloeschReingold};	
	public static final Algorithms[] arbitrarilySizedFixedDist= {Algorithms.VanDerPloegFixedDistFromFirstChild, Algorithms.MiyaderaMasud};	
	TreeLayoutAlgorithm alg;
	
	Algorithms(TreeLayoutAlgorithm alg){
		this.alg = alg;
	}
	
	public TreeLayoutAlgorithm getAlgorithm(){
		return alg;
	}
	
	public void run(TreeNode root){
		alg.run(root);
	}
}
