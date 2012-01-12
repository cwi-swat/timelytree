package treelayout.measure;

import java.util.Arrays;

import treelayout.Algorithms;
import treelayout.GenerateTrees;
import treelayout.algorithms.atze.Algorithm;
import treelayout.algorithms.atze.VanDerPloeg;
import treelayout.algorithms.buchheimwalker.BuchheimWalker;
import treelayout.interfaces.TreeLayoutAlgorithm;
import treelayout.interfaces.TreeNode;

public class Measure {

	public static int MAX_SIZE = 10000000;
	public static int INCREMENT = 5000;
	public static int NR_TESTS = 20;
	public static int NR_WARMUP = 20;
	public static long SEED = 42;
	
	static long timeLayout(TreeNode tree, TreeLayoutAlgorithm alg){
		long start = System.currentTimeMillis();
		alg.run(tree);
		long now = System.currentTimeMillis();
		System.gc();
		return now - start;
		
	}
	
	static long getMedian(long[] elems){
		Arrays.sort(elems);
		if(elems.length %2 == 1){
			return elems[elems.length/2 ];
		} else {
			int mid = elems.length/2;
			return (elems[mid-1] + elems[mid])/2;
		}
	}

	static long medianOfTests(GenerateTrees gen, TreeLayoutAlgorithm alg, int nrTests){
		long[] results = new long[nrTests];
		for(int i = 0; i < NR_WARMUP ; i++){
			TreeNode tree = gen.generate();
			timeLayout(tree, alg);
		}
		for(int i = 0 ; i < nrTests ; i++){
			TreeNode tree = gen.generate();
			results[i] = timeLayout(tree, alg);
		}
		return getMedian(results);
	}
	
	static void measureEquallySized(Algorithms[] algs){
		for(int i = 0; i < MAX_SIZE; i+= INCREMENT){
			for(Algorithms alg : algs){
				GenerateTrees gen = new GenerateTrees(i, 500000, 0,6, 10, 10, 10, 10, SEED);
				System.out.printf("%40s %15d %15d\n",alg.toString(), i, medianOfTests(gen, alg.getAlgorithm(), NR_TESTS));
			}
		}
	}
	
	static void measureArbitrarilySized(Algorithms[] algs){
		for(int i = 0; i < MAX_SIZE; i+= INCREMENT){
			for(Algorithms alg : algs){
				GenerateTrees gen = new GenerateTrees(i, 500000, 0,6, 1, 5000, 1,5000, SEED);
				System.out.printf("%40s %15d %15d\n",alg.toString(), i, medianOfTests(gen, alg.getAlgorithm(), NR_TESTS));
			}
		}
	}
	
	public static void main(String[] argv){
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		measureArbitrarilySized(Algorithms.arbitrarilySizedFixedDist);
		
	}
	
}
