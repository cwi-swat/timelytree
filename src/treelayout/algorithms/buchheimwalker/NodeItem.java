package treelayout.algorithms.buchheimwalker;


public class NodeItem {

	public int number  = -2;
	public double prelim;
	public double mod;
	public NodeItem ancestor  = null;
	public NodeItem thread  = null;
	public double change;
	public double shift;
	
	double width, height;
	double x, y;
	NodeItem[] children;
	NodeItem parent;
	NodeItem nextSibling;
	NodeItem prevSibling;
	
	NodeItem(){
		init();
	}
	   
    public void init() {
        ancestor = this;
        number = -1;
    }


	public NodeItem getFirstChild() {
		if(children.length != 0){
			return children[0];
		}
		return null;
	}

	public NodeItem getLastChild() {
		if(children.length != 0){
			return children[children.length-1];
		}
		return null;
	}

	public void clear() {
         number = -2;
         prelim = mod = shift = change = 0;
         ancestor = thread = null;
     }

}
