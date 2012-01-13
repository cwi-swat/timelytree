package treelayout.algorithms.atzenicedist;
public class Algorithm {
	
	static final double GapPerDepth = 20;
	
	static final class OutlineElement{
		double x;
		double yEnd;
		double shiftBelow;
		OutlineElement next;
		public OutlineElement(double x, double yEnd, OutlineElement next) {
			this.x = x;
			this.yEnd = yEnd;
			this.shiftBelow = 0;
			this.next = next;
		}
	}
	
	static final class Outline{
		OutlineElement first;
		OutlineElement last;
		Outline(){
			first = last = null;
		}
		Outline(OutlineElement first, OutlineElement last){
			this.first = first;
			this.last = last;
		}
		
		void addAbove(double x, double yEnd){
			first = new OutlineElement(x, yEnd, first);
			if(last == null){
				last = first;
			}
		}
		
		void shift(double shift){
			if(first == null) {
				return;
			}
			first.x += shift;
			first.shiftBelow+=shift;
			last.shiftBelow-=shift;
		}
		
		void addBelow(Outline outline){
				last.next = outline.first;
				last = outline.last;
		}
	}
	
	static final class Silhoutte{
		Outline left;
		Outline right;
		Silhoutte(){
			left = new Outline();
			right = new Outline();
		}
		
		void addAbove(Node node){
			left.addAbove(node.x, node.y + node.height);
			right.addAbove(node.x + node.width, node.y + node.height);
		}
		
		double merge(Silhoutte toMerge, double extraDist){
			OutlineElement lr = right.first;
			OutlineElement rl = toMerge.left.first;
			if(lr == null){
				left = toMerge.left;
				right = toMerge.right;
				return 0;
			}
			double lrshift = 0;
			double rlshift = 0;
			double minDistance;
			minDistance = Double.NEGATIVE_INFINITY;
			while(lr != null && rl != null){
				minDistance = Math.max(minDistance,(lr.x + lrshift) - (rl.x + rlshift) );
				double endDiff = lr.yEnd - rl.yEnd;
				if(endDiff <= 0){
					lrshift += lr.shiftBelow;
					lr = lr.next;
				} 
				if(endDiff >= 0){
					rlshift += rl.shiftBelow;
					rl = rl.next;
				}
			}
			minDistance+=extraDist;
			toMerge.right.shift(minDistance);
			if(lr == null && rl == null){
				right = toMerge.right;
			} else if(lr == null){ // this shorter than toMerge
				right = toMerge.right;
				Outline rlBelowLL = new Outline(rl,toMerge.left.last);
				rlBelowLL.shift(minDistance);
				rlBelowLL.first.shiftBelow+=rlshift;
				rlBelowLL.first.x+=rlshift;
				left.addBelow(rlBelowLL);
			} else { // rl == null, this longer than toMerge
				Outline lrBelowRR = new Outline(lr,right.last);
				lr.shiftBelow+=lrshift;
				lr.x += lrshift;
				right = toMerge.right;
				right.addBelow(lrBelowRR);
			}
			return minDistance;
		}
	}
	
	private static Silhoutte doRelativeLayout(Node tree){
		Silhoutte result = new Silhoutte();
		for(Node child : tree.children){
			child.y = tree.y + tree.height;
			Silhoutte childSil = doRelativeLayout(child);
			double minDistance = result.merge(childSil, (tree.depthTillLowest-2) * GapPerDepth);
			child.x += minDistance;
			child.distanceFromLeftMostSibling = minDistance;
		}
		tree.x = middleBetweenChildren(tree);
		result.addAbove(tree);
		return result;
	}
	
	private static double middleBetweenChildren(Node root) {
		if(root.children.length == 0){
			return 0;
		}
		int    l = root.children.length-1;
		return ((root.children[0].x  + 
		  root.children[0].width / 2.0 +
		 root.children[l].x +
		  root.children[l].width / 2.0 ) 
		/ 2.0) - root.width /2.0;
	}

	private static void setAbsoluteDistances(Node root, double shift){
		root.x += shift;
		shift += root.distanceFromLeftMostSibling;
		for(Node child : root.children){
			setAbsoluteDistances(child, shift);
		}
	}
	
	public static void layout(Node tree){
		doRelativeLayout(tree);
		setAbsoluteDistances(tree, 0);
	}
	
}
