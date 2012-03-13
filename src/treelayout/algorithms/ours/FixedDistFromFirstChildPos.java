package treelayout.algorithms.ours;

public class FixedDistFromFirstChildPos implements IGetPos {

	private final double dist;
	
	public FixedDistFromFirstChildPos(double dist) {
		this.dist = dist;
	}

	@Override
	public double getPos(Node node) {
		if(node.c.length == 0){
			return 0;
		} else {
			return node.c[0].x + dist;
		}
	}

}
