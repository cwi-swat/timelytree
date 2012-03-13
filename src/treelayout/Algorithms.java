package treelayout;

import treelayout.algorithms.ours.*;
import treelayout.algorithms.bloeschreingold.BloeschReingold;
import treelayout.algorithms.buchheimwalker.BuchheimWalker;
import treelayout.algorithms.miyaderamasud.MiyaderaMasud;
import treelayout.interfaces.TreeLayoutAlgorithm;
import treelayout.interfaces.TreeNode;

public enum Algorithms {

	Ours(new Ours(new CenterGetPos())),
	BuchHeimWalker(new BuchheimWalker()),
	BloeschReingold(new BloeschReingold()),
	MiyaderaMasud(new MiyaderaMasud());
	

	public static final Algorithms[] equallySized = {Algorithms.Ours, Algorithms.BuchHeimWalker};
	public static final Algorithms[] arbitrarilySized = {Algorithms.Ours, Algorithms.BloeschReingold, Algorithms.MiyaderaMasud, Algorithms.Ours};	
	public static final Algorithms[] arbitrarilySizedMiddle = {Algorithms.Ours, Algorithms.BloeschReingold};	
	public static final Algorithms[] arbitrarilySizedFixedDist= {Algorithms.Ours, Algorithms.MiyaderaMasud};	
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
