package treelayout.algorithms.ours;

public class OutlineEnv {
	public final Node cur;
	public final double shift;
	
	public OutlineEnv(Node curNode, double cumShift) {
		this.cur = curNode;
		this.shift = cumShift;
	}
}
