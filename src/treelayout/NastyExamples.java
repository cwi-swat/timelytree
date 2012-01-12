package treelayout;

import treelayout.algorithms.atze.Node;

public class NastyExamples {

	public static Node sameSizeFree(){
		return new Node(100.0, 100.0, 
				new Node(100.0,200.0,new Node(600.0,100.0)),
				new Node(100.0,100.0,new Node(100,100),new Node(40.0,300.0))
		);
	}
	
	public static Node simpleFree(){
		return new Node(100.0, 100.0, 
				new Node(100.0,200.0,new Node(600.0,100.0)),
				new Node(100.0,100.0,new Node(40.0,30.0),new Node(40.0,40.0),new Node(40.0,50.0),new Node(40.0,300.0))
		);
	}
	
	public static Node simpleFree2(){
		return new Node(100.0, 100.0, 
				new Node(100.0,300.0),
				new Node(100.0,100.0,new Node(100,100),new Node(100.0,100.0,new Node(400,100),new Node(400,100)))
		);
	}


	public static Node simpleMiddleFree(){
		return new Node(100.0, 100.0, 
				new Node(50.0,300.0),
				new Node(25.0,100), //, new TreeNode(100,25), new TreeNode(100,25)),
				new Node(25.0,100.0),
				new Node(25.0,200.0,new Node(800,100))
		);
	}
	
	public static Node dualLeftFree(){
		return new Node(100.0, 100.0, 
				new Node(50.0,1200.0),
				new Node(25.0,100, new Node(100,200),new Node(100,100,new Node(50,50),new Node(25,400,new Node(300,100)))),
				new Node(25,900,new Node(900,100))
		);
	}
	
	static Node nestedLeftFreeRightSide(int nestLevel){
		if(nestLevel == 0){
			return new Node(50,50);
		}
		return new Node(50.0, 50.0, 
				new Node(50, 50 * nestLevel),
				nestedLeftFreeRightSide(nestLevel-1),
				new Node(50,100 * nestLevel,new Node( (50.0 *  5  + 50 * nestLevel) * Math.pow(2,nestLevel)  ,50))
		);
	}
	
	public static Node nestedLeftFree(int nestLevel){
		return nestedLeftFreeRightSide(nestLevel);
	}
}
