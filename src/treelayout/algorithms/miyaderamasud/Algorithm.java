package treelayout.algorithms.miyaderamasud;

public class Algorithm {
	
	
	static final double ROOT_FROM_FIRST_CHILD = 6;
	
	class LowerEnvelope{
		double x;
		double yEnd;
		LowerEnvelope next;
		
		LowerEnvelope prepend(double x, double yEnd){
			return new LowerEnvelope(x, yEnd, this);
		}
		
		LowerEnvelope(double x, double yEnd, LowerEnvelope next){
			this.x = x;
			this.yEnd = yEnd;
			this.next = next;
		}
		
		
	}

	LowerEnvelope doLayout(Node root, LowerEnvelope env, double parentX){
		root.x = parentX-ROOT_FROM_FIRST_CHILD;
		LowerEnvelope prev = env;
		while(env != null &&  root.y + root.height >= env.yEnd){
			root.x = Math.max(root.x, env.x);
			prev = env;
			env = env.next;
		}
		if(env!=null && prev != null && prev.yEnd!= root.y + root.height ){
			root.x = Math.max(root.x, env.x);
		}
		if(!root.isLeaf()){
			for(int i = 0 ; i < root.children.length ; i++){
				root.children[i].y = root.y + root.height;
			}
			env = doLayout(root.children[0],env, root.x);
			root.x = root.children[0].x + ROOT_FROM_FIRST_CHILD;
			for(int i = 1 ; i < root.children.length ; i++){
				env = doLayout(root.children[i],env,0);
			}
		} 
		return new LowerEnvelope(root.x + root.width, root.y + root.height, env);
		
	}
}
