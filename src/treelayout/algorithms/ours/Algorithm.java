package treelayout.algorithms.ours;

import static java.lang.Math.max;

public class Algorithm {

	/*
	 *  Our algorithm exactly as desribed in the paper
	 *  
	 *  Author: A.J. van der Ploeg
	 */
	
	static void doLayout(Node t, IGetPos getPos){
		int m = nrKids(t)-1;
		for(int i = 0 ; i <= m ; i++) doLayout(t.c[i], getPos);
		for(int i = 1 ; i <= m ; i++){
			Tuple<OutlineEnv,OutlineEnv> srcl = getOutlinesStart(t.c,i);
			OutlineEnv sr = srcl.l;
			OutlineEnv cl = srcl.r;
			Triple<Double, OutlineEnv, OutlineEnv> dsrcl = getOffset(sr,cl);
			double d = dsrcl.a;
			sr = dsrcl.b; cl = dsrcl.c;
			t.c[i].s = d;
			cl = addShift(cl,d);
			merge(sr,cl);
		}
		t.x = getPos.getPos(t);
	}
	
	static Tuple<OutlineEnv, OutlineEnv> getOutlinesStart(Node[] c,
			int i) {
		return new Tuple<OutlineEnv, OutlineEnv>(new OutlineEnv(c[i-1], c[i-1].s), 
				new OutlineEnv(c[i], c[i].s));
	}

	static Triple<Double, OutlineEnv, OutlineEnv> getOffset(
			OutlineEnv sr, OutlineEnv cl) {
		double s = Double.NEGATIVE_INFINITY;
		OutlineEnv psr = null, pcl = null;
		while(!hasEnded(sr) && !hasEnded(cl)){
			s = max(s,getRight(sr) - getLeft(cl));
			psr = sr; pcl = cl;
			Tuple<OutlineEnv,OutlineEnv> srcl = nextPair(sr,cl);
			sr = srcl.l;
			cl = srcl.r;
		}
		return new Triple<Double, OutlineEnv, OutlineEnv>(s,psr,pcl);
	}

	static OutlineEnv addShift(OutlineEnv cl, double d) {
		return new OutlineEnv(cl.cur, cl.shift + d);
	}
	static double getLeft(OutlineEnv cl) {
		return cl.cur.x + cl.shift;
	}
	static double getRight(OutlineEnv sr) {
		return sr.cur.x + sr.cur.w + sr.shift;
	}
	static boolean hasEnded(OutlineEnv sr) {
		return sr.cur == null;
	}

	static Tuple<OutlineEnv, OutlineEnv> nextPair(OutlineEnv sr,
			OutlineEnv cl) {
		double sb = bottom(sr);
		double nb = bottom(cl);
		if(sb == nb){
			return new Tuple<OutlineEnv, OutlineEnv>(nextRight(sr), nextLeft(cl));
		} else if(sb > nb){
			return new Tuple<OutlineEnv, OutlineEnv>(nextRight(sr), cl);
		} else {
			return new Tuple<OutlineEnv, OutlineEnv>(sr, nextLeft(cl));
		}
	}

	private static OutlineEnv nextLeft(OutlineEnv cl) {
		if(isLeaf(cl.cur)){
			return new OutlineEnv(cl.cur.nxtl,cl.shift + cl.cur.nxtls);
		} else {
			return new OutlineEnv(cl.cur.c[0],cl.shift + cl.cur.c[0].s);
		}
	}
	
	private static OutlineEnv nextRight(OutlineEnv sr) {
		if(isLeaf(sr.cur)){
			return new OutlineEnv(sr.cur.nxtr,sr.shift + sr.cur.nxtrs);
		} else {
			int l = sr.cur.c.length-1;
			return new OutlineEnv(sr.cur.c[l],sr.shift + sr.cur.c[l].s);
		}
	}

	static boolean isLeaf(Node cur) { return cur.c.length == 0; }

	static double bottom(OutlineEnv sr) { return sr.cur.y; }

	static void merge(OutlineEnv sr, OutlineEnv cl) {
		if(bottom(sr) == bottom(cl)){
			if(!hasEnded(nextRight(sr))) sr = nextRight(sr);
			else if(!hasEnded(nextLeft(cl))) cl = nextLeft(cl);
		}
		if(bottom(sr) < bottom(cl)) setNextRight(cl,sr);
		else setNextLeft(sr,cl);
	}

	static void setNextLeft(OutlineEnv cl, OutlineEnv sr) {
		Node l = cl.cur;
		Node r = sr.cur;
		double s = cl.shift;
		double sprime = sr.shift;
		l.nxtl = r;
		l.nxtls = sprime - s;
	}

	static void setNextRight(OutlineEnv sr, OutlineEnv cl) {
		Node r = sr.cur;
		Node l = cl.cur;
		double s = sr.shift;
		double sprime = cl.shift;
		r.nxtr = l;
		r.nxtrs = sprime - s;
	}

	static int nrKids(Node n) {
		return n.c.length;
	}
	
	// end algorithm from paper implementation

	static void doAllLayout(Node t, IGetPos getPos){
		doLayout(t, getPos);
		makeAbsolute(t,0);
	}
	
	static void makeAbsolute(Node root, double shift){
		shift += root.s;
		root.x += shift;
		for(Node child : root.c){
			makeAbsolute(child, shift);
		}
	}
}
