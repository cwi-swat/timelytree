package treelayout.swt;
import java.util.ArrayList;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import treelayout.Algorithms;
import treelayout.BoundingBox;
import treelayout.GenerateTrees;
import treelayout.interfaces.TreeNode;




public class TreeElement extends Composite implements SelectionListener, PaintListener, ControlListener , Listener, KeyListener{
	TreeNode tree ;
	double xOffset, yOffset;
	double width, height;
	double zoom = 1.0;
	double vgap = 15;
	GenerateTrees treeGen;
	Algorithms algorithmChoice;
	Random rand;
	
	public static int SEED = 42;
	
	public TreeElement(Composite parent) {
		super(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		algorithmChoice = Algorithms.Ours;
		treeGen = new GenerateTrees(20,2000, 2,4, 30, 200, 30, 200, 10);
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
//			tree = new TreeNode(0.898053,0.969990 , new TreeNode(0.968450,1.388073 , new TreeNode(0.899350,1.311214 , new TreeNode(1.309017,0.811011 ), new TreeNode(1.025135,0.782323 ), new TreeNode(0.788178,0.763877 )), new TreeNode(1.338348,0.511385 , new TreeNode(1.248801,0.255418 ), new TreeNode(0.727234,0.548851 ), new TreeNode(0.909806,0.511432 ))), new TreeNode(0.532270,0.474506 , new TreeNode(0.725585,1.014471 )), new TreeNode(1.112632,0.251274 , new TreeNode(0.496188,2.9225, new TreeNode(2.0,0.4),new TreeNode(1.0,0.8) ),new TreeNode(0.6,1)));
//			tree = new TreeNode(187.828942,311.710997 , new TreeNode(141.901729,212.010992 , new TreeNode(228.234730,196.928556 ), new TreeNode(47.009715,310.122316 ), new TreeNode(77.505928,201.409231 )), new TreeNode(306.716650,262.018437 , new TreeNode(125.735333,114.985398 , new TreeNode(205.432359,222.264511 , new TreeNode(239.148993,194.980397 , new TreeNode(173.781810,199.913732 ), new TreeNode(256.427942,299.443952 ), new TreeNode(288.945889,198.889672 )), new TreeNode(293.322927,268.542733 , new TreeNode(176.426141,281.251008 ), new TreeNode(286.747910,253.515736 ))), new TreeNode(173.145612,93.873724 , new TreeNode(107.031465,108.066558 , new TreeNode(219.689018,235.259037 , new TreeNode(237.127111,338.564635 ))), new TreeNode(88.145160,243.449795 , new TreeNode(249.534650,189.766751 )), new TreeNode(66.716614,118.708992 , new TreeNode(168.352935,334.241448 , new TreeNode(213.308232,320.346280 ), new TreeNode(96.354818,178.254284 )))))));
//			tree.mul(1.0/4.5, (1.0/4.5)*0.94);
//			tree.addSize(-10, -40);
//			tree.addGap(0.2, 0.2);
//			tree = new TreeNode(100, 100, new TreeNode(100,100,new TreeNode(200,100)), new TreeNode(100,200),  new TreeNode(100,100,new TreeNode(200,100)));
//			tree = NastyExamples.nestedLeftFree(4);
//			tree = NastyExamples.simpleFree2();
			long dur = now - start;
//			System.out.printf("Generation took %d ms size(%d) \n",dur,tree.size());
			doLayout();
			tree.print();
//			tree.tikzPrint();
//			tree.tikzDashed();
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


	
	void paintTree(TreeNode root, GC gc,Rectangle r){
//		if( !(root.x + root.width - xOffset < 0 || (root.x - xOffset) *zoom > r.width 
//				|| root.y + root.height + yOffset - yOffset < 0 || (root.y + offsetY - yOffset )*zoom > r.width)){
//			gc.setAlpha(100);
		Color c = new Color(gc.getDevice(), new RGB((int)((rand.nextDouble() * 150) ), (int)((rand.nextDouble() * 150)), (int)((rand.nextDouble() * 150) )));
		gc.setBackground(c);
			gc.fillRectangle(roundInt(zoom * (root.x + root.hgap/2 - xOffset)), roundInt(zoom * (root.y + root.vgap/2 -yOffset)), roundInt(zoom * (root.width - root.hgap)), roundInt(zoom * (root.height - root.vgap)));
			gc.setAlpha(255);
			gc.drawRectangle(roundInt(zoom * (root.x + root.hgap/2 - xOffset)), roundInt(zoom * (root.y + root.vgap/2 -yOffset)), roundInt(zoom * (root.width - root.hgap)), roundInt(zoom * (root.height -  root.vgap)));
//		}
			c.dispose();
		if(root.children.length > 0){
			double endYRoot =  root.y + root.height -root.vgap/2 ;
			double rootMiddle = root.x + root.width/2.0;
			
			double middleY = endYRoot + root.vgap/2;
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
				gc.drawLine(roundInt(zoom *(middleKid-xOffset)), roundInt(zoom *(middleY-yOffset)), roundInt(zoom *(middleKid-xOffset)), roundInt(zoom *( kid.y + kid.vgap/2.0 -yOffset)));
			}
		}
	}
	

	@Override
	public void paintControl(PaintEvent e) {
		e.gc.setAdvanced(true);
		Rectangle r = getClientArea();
		e.gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		e.gc.fillRectangle(r);
		e.gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
		rand = new Random(SEED);
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
		} else if(e.keyCode == 'p'){
			tree.print();
			System.out.printf("\n");
		}
		setScrollBars();
		redraw();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}


}
