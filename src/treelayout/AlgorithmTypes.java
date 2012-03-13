package treelayout;

public enum AlgorithmTypes {

	EQUALLY_SIZED(Algorithms.OursEq, Algorithms.BloeschReingoldEq),
	ARBIT_SIZED_FIXED_DIST(Algorithms.OursFixedDist, Algorithms.MiyaderaMasud),
	ARBIT_SIZED_MIDDLE(Algorithms.Ours, Algorithms.BloeschReingold);

	
	public final Algorithms[] algorithms;
	
	AlgorithmTypes(Algorithms...algorithms){
		this.algorithms = algorithms;
	}
}
