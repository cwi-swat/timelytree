package treelayout.swt;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import treelayout.Algorithms;
import treelayout.BoundingBox;
import treelayout.GenerateTrees;
import treelayout.algorithms.atze.VanDerPloeg;
import treelayout.algorithms.buchheimwalker.BuchheimWalker;
import treelayout.interfaces.TreeNode;




public class TreeElement extends Composite implements SelectionListener, PaintListener, ControlListener , Listener, KeyListener{
	TreeNode tree ;
	double xOffset, yOffset;
	double width, height;
	double zoom = 1.0;
	double vgap = 15;
	GenerateTrees treeGen;
	Algorithms algorithmChoice;
	
	public TreeElement(Composite parent) {
		super(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		algorithmChoice = Algorithms.VanDerPloeg;
		treeGen = new GenerateTrees(30,2000, 0,2, 20, 200, 20, 200, 10);
		addPaintListener(this);
		getHorizontalBar().addSelectionListener(this);
		getVerticalBar().addSelectionListener(this);
		reinit();
		addControlListener(this);	
		addKeyListener(this);
		addListener(SWT.MouseVerticalWheel, this);
	}
	
	
	public void reinit(){
		do{
			System.out.printf("Starting generating... ");
			long start = System.currentTimeMillis();
			long now = System.currentTimeMillis();
			
			tree = treeGen.generate();
//			tree = new TreeNode(100, 100, new TreeNode(100,100,new TreeNode(200,100)), new TreeNode(100,200));
//			tree = NastyExamples.nestedLeftFree(4);
//			tree = NastyExamples.simpleFree2();
			long dur = now - start;
			System.out.printf("Generation took %d ms size(%d) \n",dur,tree.size());
			doLayout();
//			overlap();
//		}while(!overlap());
		}while(false);
//		tree = new InputTreeNode(100,100,0,
//				new InputTreeNode(150,50,0,
//						new InputTreeNode(200,100,0,new InputTreeNode(220,10,0))),
//				new InputTreeNode(100,190,0
//						,new InputTreeNode(100,110,0)
//						,new InputTreeNode(100,110,0)));
//		doLayout();
		
	}

	
	public boolean overlap(){
		ArrayList<TreeNode> nodes = new ArrayList<TreeNode>();
		tree.allNodes(nodes);
		for(int i = 0 ; i < nodes.size(); i++){
			for(int j = 0 ; j < i ; j++){
				if(nodes.get(i).overlapsWith(nodes.get(j))){
					System.out.printf("Overlap %d %d!!\n",i,j);
					return true;
				}
			}
		}
		return false;

	}

	public void doLayout() {
		System.out.printf("Starting layout... \n");
		long start = System.currentTimeMillis();
		algorithmChoice.run(tree);
		long now = System.currentTimeMillis();
		long dur = now - start;
		System.out.printf("Layout in %d ms \n\n",dur);
		
		tree.normalizeX();
//		BoundingBox b = SuperSnel.layout(tree);
		BoundingBox b = tree.getBoundingBox();
//		Layout.doLayoutConstraints(tree, null, 0);
		width = b.width;
		height = b.height;


		
	}



	public void setScrollBars() {
		Rectangle r = getClientArea();
		double w = r.width / zoom;
		if(w < width){
			getHorizontalBar().setVisible(true);
			getHorizontalBar().setMinimum(0);
			getHorizontalBar().setMaximum((int)width);
			getHorizontalBar().setThumb((int)(r.width / zoom));
			getHorizontalBar().setIncrement(50);
			getHorizontalBar().setPageIncrement((int)(r.width / zoom));

		} else {
			getHorizontalBar().setVisible(false);
		}
		double h = r.height / zoom;
		if(h < height){
			getVerticalBar().setVisible(true);
			getVerticalBar().setMinimum(0);
			getVerticalBar().setMaximum((int)height);
			getVerticalBar().setThumb((int)(r.height/zoom));
			getVerticalBar().setIncrement(50);
			getVerticalBar().setPageIncrement((int)(r.height/zoom));
		} else {
			getVerticalBar().setVisible(false);
		}
		
	}
	

	
	static int roundInt(double b){
		return (int)(b+0.5);
	}
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		xOffset = getHorizontalBar().getSelection() ;
		yOffset = getVerticalBar().getSelection() ;
		redraw();
	}
	
	@Override
	public void widgetDefaultSelected(SelectionEvent e) {	
	}


	private static final double GAP = 6;
	
	void paintTree(TreeNode root, GC gc,Rectangle r){
//		if( !(root.x + root.width - xOffset < 0 || (root.x - xOffset) *zoom > r.width 
//				|| root.y + root.height + yOffset - yOffset < 0 || (root.y + offsetY - yOffset )*zoom > r.width)){
			gc.setAlpha(100);
			gc.fillRectangle(roundInt(zoom * (root.x + GAP - xOffset)), roundInt(zoom * (root.y + GAP -yOffset)), roundInt(zoom * (root.width - 2*GAP)), roundInt(zoom * (root.height - 2 * GAP)));
			gc.setAlpha(255);
			gc.drawRectangle(roundInt(zoom * (root.x + GAP - xOffset)), roundInt(zoom * (root.y + GAP -yOffset)), roundInt(zoom * (root.width - 2*GAP)), roundInt(zoom * (root.height - 2 * GAP)));
//		}
		if(root.children.length > 0){
			double endYRoot =  root.y + root.height -GAP ;
			double kidY = endYRoot + GAP ;
			double rootMiddle = root.x + root.width/2.0;
			
			double middleY = endYRoot + GAP;
			gc.drawLine(roundInt(zoom *(rootMiddle-xOffset)), roundInt(zoom *( endYRoot -yOffset)), roundInt(zoom * (rootMiddle-xOffset)),roundInt(zoom * (middleY -yOffset)) );
			TreeNode firstKid = root.children[0];
			double middleFirstKid =  firstKid.x + firstKid.width/2.0;
			TreeNode lastKid = root.children[root.children.length-1];
			double middleLastKid = lastKid.x + lastKid.width/2.0;
			gc.drawLine(roundInt(zoom *(middleFirstKid-xOffset)), roundInt(zoom * (middleY-yOffset)), roundInt(zoom * (rootMiddle-xOffset)),roundInt(zoom * (middleY -yOffset)));
			gc.drawLine(roundInt(zoom *(middleFirstKid-xOffset)), roundInt(zoom * (middleY-yOffset)), roundInt(zoom * (middleLastKid-xOffset)),roundInt(zoom * (middleY -yOffset)));
			for(TreeNode kid : root.children){
				double middleKid = kid.x + kid.width/2.0;
				paintTree(kid, gc, r);
				gc.drawLine(roundInt(zoom *(middleKid-xOffset)), roundInt(zoom *(middleY-yOffset)), roundInt(zoom *(middleKid-xOffset)), roundInt(zoom *( kid.y + GAP -yOffset)));
			}
		}
	}
	

	@Override
	public void paintControl(PaintEvent e) {
		e.gc.setAdvanced(true);
		Rectangle r = getClientArea();
		e.gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		e.gc.fillRectangle(r);
		e.gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_BLUE));
		e.gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
		paintTree(tree, e.gc, r);
	
	}

	@Override
	public void controlMoved(ControlEvent e) {
	}

	@Override
	public void controlResized(ControlEvent e) {
		setScrollBars();
	}



	@Override
	public void handleEvent(Event event) {
		Rectangle r = getClientArea();
		
		if((event.stateMask & SWT.CONTROL) != 0){
			event.doit= false;
			double locX = xOffset + event.x / zoom;
			double locY = yOffset + event.y / zoom;
			if(event.count > 0){
				zoom*=1.05;
			} else {
				zoom/=1.05;
			}
			xOffset = Math.min(Math.max(0, locX - event.x / zoom),width - r.width /zoom);
			yOffset =  Math.min(Math.max(0,locY - event.y / zoom),height - r.height /zoom);
			getHorizontalBar().setSelection((int)xOffset);
			getVerticalBar().setSelection((int)yOffset);
			setScrollBars();
			
			redraw();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.keyCode == 'z'){
			reinit();
		} else if(e.keyCode == 'a'){
			algorithmChoice = Algorithms.values()[(algorithmChoice.ordinal()+1) % Algorithms.values().length];
			System.out.printf("Current : %s\n", algorithmChoice);
			doLayout();
		}
		setScrollBars();
		redraw();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


}
