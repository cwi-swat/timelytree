package treelayout.algorithms.supernet;


public class Algorithm {

	static abstract class Node{
		private double width, height;
		private double xNode,y;
		private double xTree;
		
		Node(double width, double height){
			this.width = width;
			this.height = height;
		}
		
		abstract boolean isLastOfLeftOutline();
		abstract boolean isLastOfRightOutline();
		void setYTop(double y) { this.y = y; }
		void setTreeX(double x) { this.xTree = x; } 
		void setNodeX(double x) { this.xNode = x; }
		double getXTree() { return xTree; }
		double getXLeft() { return xNode; }
		double getXRight() { return xNode + width; }
		double getYTop() { return y; }
		double getYBottom() { return y + height; }
		abstract boolean isLeaf();
		abstract Node[] getChildren();
		abstract double getNextLeftOutlineShift();
		abstract Node getNextLeftOutline();
		abstract double getNextRightOutlineShift();
		abstract Node getNextRightOutline();
		abstract void setNextLeftOutline(double shift, Node node);
		abstract void setNextRightOutline(double shift, Node node);
	}
	
	static class InternalNode extends Node{

		Node[] children;
		
		InternalNode(double width, double height, Node ... children){
			super(width,height);
			this.children = children;
		}
		
		@Override
		boolean isLeaf() { return false; }
		@Override
		Node[] getChildren() { return children; }
		@Override
		double getNextLeftOutlineShift() { return children[0].xTree; }
		@Override
		Node getNextLeftOutline() { return children[0]; }
		@Override
		double getNextRightOutlineShift() { return children[children.length-1].xTree; }
		@Override
		Node getNextRightOutline() { return children[children.length-1]; }
		@Override
		boolean isLastOfLeftOutline() { return false; }
		@Override
		boolean isLastOfRightOutline() { return false; }

		@Override
		void setNextLeftOutline(double shift, Node node) {
			throw new Error("Trying to set next outline on Inner node!");
		}

		@Override
		void setNextRightOutline(double shift, Node node) {
			throw new Error("Trying to set next outline on Inner node!");
		}
	}
	
	static class Leaf extends Node{

		private double leftOutlineShift;
		private Node leftOutlineNext;
		private double rightOutlineShift;
		private Node rightOutlineNext;
		
		Leaf(double width, double height){
			super(width,height);
			this.rightOutlineNext = this.leftOutlineNext = null;
			this.rightOutlineShift = this.leftOutlineShift = 0;
		}
		
		@Override
		boolean isLeaf() { return true; }
		@Override
		Node[] getChildren() { return new Node[] {}; }
		@Override
		double getNextLeftOutlineShift() { return leftOutlineShift; }
		@Override
		Node getNextLeftOutline() { return leftOutlineNext; }
		@Override
		double getNextRightOutlineShift() { return rightOutlineShift; }
		@Override
		Node getNextRightOutline() { return rightOutlineNext; }
		@Override
		boolean isLastOfLeftOutline() { return leftOutlineNext == null; }
		@Override
		boolean isLastOfRightOutline() { return rightOutlineNext == null; }
		
		void setNextLeftOutline(double shift, Node next){
			this.leftOutlineShift = shift;
			this.leftOutlineNext = next;
		}
		
		void setNextRightOutline(double shift, Node next){
			this.rightOutlineShift = shift;
			this.rightOutlineNext = next;
		}
	}
	
	static abstract class OutlineEnvironment{
		Node cur;
		double shift;
		Node prev;
		
		double getShift() { return shift; }
		
		void next(){
			prev = cur;
			shift += getNextShift();
			cur = getNext();
		}
		
		public double getYBottom() {
			return cur.getYBottom();
		}
		
		abstract double getX();
		abstract Node getNext() ;
		abstract double getNextShift() ;
		boolean isLast(){ return cur == null; } 
	}
	
	static class LeftOutlineEnvironment extends OutlineEnvironment{
		LeftOutlineEnvironment(Node start){
			cur = start;
			shift = cur.getXTree();
			prev = null;
		}
		@Override
		Node getNext() {
			return cur.getNextLeftOutline();
		}

		@Override
		double getNextShift() {
			return cur.getNextLeftOutlineShift();
		}
		@Override
		double getX() {
			return cur.getXLeft() + shift;
		}
		
		void addShift(double s){
			this.shift+=s;
		}
	}
	
	static class RightOutlineEnvironment extends OutlineEnvironment{
		RightOutlineEnvironment(Node start){
			cur = start;
			shift = cur.getXTree();
			prev = null;
		}
		@Override
		Node getNext() {
			return cur.getNextRightOutline();
		}

		@Override
		double getNextShift() {
			return cur.getNextRightOutlineShift();
		}
		@Override
		double getX() {
			return cur.getXRight() + shift;
		}
	}
	
	static class OffsetResult{
		RightOutlineEnvironment le;
		LeftOutlineEnvironment  re;
		double offset;
		OffsetResult(double offset,  RightOutlineEnvironment le, LeftOutlineEnvironment re){
			this.offset = offset;
			this.le = le;
			this.re = re;
		}
	}
	
	static void layout(Node root){
		if(root.getChildren().length == 0){
			return;
		}
		layout(root.getChildren()[0]);
		for(int i = 1; i < root.getChildren().length ; i++){
			layout(root.getChildren()[i]);
			OffsetResult off = getOffset(root.getChildren()[i-1],root.getChildren()[i]);
			root.getChildren()[i].setTreeX(off.offset);
			off.re.addShift(off.offset);
			merge(off.le,off.re);
		}
		root.setNodeX(getPos(root));
	}
	
	static private void merge(RightOutlineEnvironment le, LeftOutlineEnvironment re) {
		if(le.getYBottom() <= re.getYBottom()){ // left was shorter or equal
			le.cur.setNextLeftOutline(re.shift - le.shift, re.cur);
		} 
		if(re.getYBottom() <= le.getYBottom()){ // right was shorter or equal
			re.cur.setNextRightOutline(le.shift - re.shift, le.cur);
		}
	}

	static private OffsetResult getOffset(Node l, Node r) {
		double offset = Double.NEGATIVE_INFINITY;
		RightOutlineEnvironment le = new RightOutlineEnvironment(l);
		LeftOutlineEnvironment re = new LeftOutlineEnvironment(r);
		while(	!le.isLast() && !re.isLast()){
			offset = Math.max(offset, le.getX() - re.getX());
			nextPair(re,le);
		}
		return new OffsetResult(offset, le,re);
	}

	static private void nextPair(OutlineEnvironment re,
			OutlineEnvironment le) {
		if(re.getYBottom() == le.getYBottom()){
			le.next(); re.next();
		} else if(re.getYBottom() < le.getYBottom()){
			re.next();
		} else {
			le.next();
		}
	}

	static private double getPos(Node root) {
		if(root.getChildren().length == 0){
			return 0;
		}
		int    l = root.getChildren().length-1;
		return (root.getChildren()[0].getXLeft() + root.getChildren()[0].getXTree() + 
		 root.getChildren()[l].getXRight() + root.getChildren()[l].getXTree()) 
		/ 2.0 - root.width /2.0;
	}
	
	static void makeAbsolute(Node root, double shift){
		shift += root.getXTree();
		root.xNode += shift;
		for(Node child : root.getChildren()){
			makeAbsolute(child, shift);
		}
	}

	public static void setYs(Node root) {
		for(Node child : root.getChildren()){
			child.setYTop(root.getYBottom());
			setYs(child);
		}
		
	}
}
