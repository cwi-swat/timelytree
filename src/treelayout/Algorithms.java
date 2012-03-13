package treelayout;

import treelayout.algorithms.ours.*;
import treelayout.algorithms.bloeschreingold.BloeschReingold;
import treelayout.algorithms.buchheimwalker.BuchheimWalker;
import treelayout.algorithms.miyaderamasud.MiyaderaMasud;
import treelayout.interfaces.TreeLayoutAlgorithm;
import treelayout.interfaces.TreeNode;

public enum Algorithms implements TreeLayoutAlgorithm{

	Ours(new Ours(new CenterGetPos()), AlgorithmTypes.ARBIT_SIZED_MIDDLE),
	BuchHeimWalker(new BuchheimWalker(), AlgorithmTypes.EQUALLY_SIZED),
	BloeschReingold(new BloeschReingold(), AlgorithmTypes.ARBIT_SIZED_MIDDLE),
	OursEq(new Ours(new CenterGetPos()), AlgorithmTypes.EQUALLY_SIZED),
	BloeschReingoldEq(new BloeschReingold(), AlgorithmTypes.EQUALLY_SIZED),
	MiyaderaMasud(new MiyaderaMasud(), AlgorithmTypes.ARBIT_SIZED_FIXED_DIST),
	OursFixedDist(new Ours(new FixedDistFromFirstChildPos(treelayout.algorithms.miyaderamasud.MiyaderaMasud.ROOT_FROM_FIRST_CHILD)),
			AlgorithmTypes.ARBIT_SIZED_FIXED_DIST);
	
	public final TreeLayoutAlgorithm alg;
	public final AlgorithmTypes type;
	
	Algorithms(TreeLayoutAlgorithm alg, AlgorithmTypes type){
		this.alg = alg;
		this.type = type;
	}
	
	@Override
	public Object convert(TreeNode root) {
		return alg.convert(root);
	}

	@Override
	public void convertBack(Object converted, TreeNode root) {
		alg.convertBack(converted, root);
	}

	@Override
	public void runOnConverted(Object root) {
		alg.runOnConverted(root);
	}
	
	public Algorithms nextOfType(AlgorithmTypes t){
		int i = ordinal() + 1;
		while(values()[i % values().length].type != t){
			i++;
		}
		return values()[i % values().length];
	}
}
