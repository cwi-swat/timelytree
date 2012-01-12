package treelayout.algorithms.bloeschreingold;


/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/


/**
 * A TreeNode is created for each "node" constructor that occurs in a Tree.
 * After creation, shapeTree is called to determine position and dimensions.
 * 
 * @author paulk
 *
 */
public class Node {
	
	double width, height, x,y;
	double rootWidth,rootHeight;
	Node[] children;     // Child nodes
	double[] childRoot;                // Root position of each child
	double rootPosition;               // Root position of this TreeNode (= middle of rootFigure)
	
	public Node(double width, double height, Node ... children) {
		this.rootWidth = width;
		this.rootHeight = height;
		this.children = children;
	}
	
	/*
	 * Distance between rootPosition and leftmost border of this node
	 */
	
	public double leftExtent(){
		return rootPosition;
	}
	
	/*
	 * Distance between rootPosition and rightmost border of this node
	 */
	
	public double rightExtent(){
		return width - rootPosition;
	}
	
	/**
	 * shapeTree places the current subtree (rooted in this TreeNode)  on the raster
	 * 
	 * @param rootMidX	x coordinate of center of the root figure
	 * @param rootTop	y coordinate of top of root figure
	 * @param raster	NodeRaster to be used
	 * @return the x position of the center of the root
	 */
	double shapeTree(double rootMidX, double rootTop, TreeNodeRaster raster) {
		y = rootTop;
		// Initial placement of figure of this TreeNode
		double position = raster.leftMostPosition(rootMidX, rootTop, rootWidth, rootHeight, 0);
		rootPosition = position;
		height = rootHeight;
		width = rootWidth;
		
		if(children.length == 0){
			rootPosition = width/2;
		} else {
			
			// Compute position of leftmost child
			
			double branchPosition = position;
			
			if(children.length > 1){
				double widthDirectChildren = (children[0].rootWidth + children[children.length-1].rootWidth)/2 ;
				for(int i = 1; i < children.length - 1; i++){
					widthDirectChildren += children[i].rootWidth;
				}
				branchPosition = position - widthDirectChildren/2; 		// Position of leftmost child
			}
			
			double childTop = rootTop + rootHeight ;         // Top of all children
			 
			childRoot = new double[children.length];
			
			// Place leftmost child
			double leftPosition = childRoot[0] = children[0].shapeTree(branchPosition, childTop, raster);
			double rightPosition = leftPosition;
			double heightChildren = children[0].height;
			double rightExtentChildren = leftPosition + children[0].rightExtent();
			
			for(int i = 1; i < children.length; i++){
				Node childi = children[i];
				branchPosition +=  (children[i-1].rootWidth + childi.rootWidth)/2;
				rightPosition = childi.shapeTree(branchPosition, childTop, raster);
				rightExtentChildren = Math.max(rightExtentChildren, rightPosition + childi.rightExtent());
				heightChildren = Math.max(heightChildren, childi.height);
				childRoot[i] = rightPosition;
			}
			position = (leftPosition + rightPosition)/2;
			height += heightChildren;
			width = Math.max(rootWidth, rightExtentChildren - (leftPosition - children[0].rootPosition));

			// Make child positions and rootPosition relative to this parent
			x = leftPosition - children[0].rootPosition;
			
			for(int i = 0; i < children.length; i++){
				childRoot[i] -= x;
			}
			rootPosition = position - x;
		}
	
		// After placing all children, we can finally add the current root figure to the raster.
		raster.add(position, rootTop, width, height);
		return position;
	}
	

	public
	void makeCoordinateAbsolute(double left, double top){
		
		x = left;
		y = top;
	
		
		int nChildren = children.length;
		double positionRoot = left + rootPosition;
		double leftRootFig = positionRoot - rootWidth/2;
		x = leftRootFig;
		
		
		double bottomRootFig = top + rootHeight;
		double childTop      = bottomRootFig ; 
		

		
		for(int i = 0; i < nChildren; i++){
			Node child = children[i];
			double positionChild = left + childRoot[i];
			child.makeCoordinateAbsolute(positionChild - child.leftExtent(), childTop);
		}
		
	}
	

}
