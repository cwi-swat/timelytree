package treelayout.algorithms.buchheimwalker;

/*
 * 
  Copyright (c) 2004-2007 Regents of the University of California.
  All rights reserved.

  Redistribution and use in source and binary forms, with or without
  modification, are permitted provided that the following conditions
  are met:

  1. Redistributions of source code must retain the above copyright
  notice, this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
  notice, this list of conditions and the following disclaimer in the
  documentation and/or other materials provided with the distribution.

  3.  Neither the name of the University nor the names of its contributors
  may be used to endorse or promote products derived from this software
  without specific prior written permission.

  THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND
  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
  ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE
  FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
  DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
  OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
  HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
  LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
  OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
  SUCH DAMAGE.
  
  Originally from the prefuse library. http://prefuse.org
 */

import treelayout.interfaces.TreeLayoutAlgorithm;
import treelayout.interfaces.TreeNode;

public class BuchheimWalker implements TreeLayoutAlgorithm{

	private void convertBackNode(NodeItem converted, TreeNode root) {
		root.x = converted.x;
		root.y = converted.y;
		for(int i = 0 ; i < root.children.length;i++){
			convertBack(converted.children[i], root.children[i]);
		}
		
	}

	private NodeItem convertNode(TreeNode root) {
		NodeItem result = new NodeItem();
		result.width = root.width;
		result.height = root.height;
		result.children = new NodeItem[root.children.length];
		for(int i = 0 ; i < root.children.length ; i++){
			result.children[i] = convertNode(root.children[i]);
			result.children[i].parent = result;
		}
		for(int i = 0 ;i < result.children.length ; i++){
			if(i!=0){
				result.children[i].prevSibling = result.children[i-1];
			}
			if(i!=result.children.length -1){
				result.children[i].nextSibling = result.children[i+1];
			}
		}
		return result;
	}

	@Override
	public Object convert(TreeNode root) {
		return convertNode(root);
	}

	@Override
	public void convertBack(Object converted, TreeNode root) {
		convertBackNode((NodeItem) converted,root);
	}

	@Override
	public void runOnConverted(Object root) {
		new Algorithm().run((NodeItem)root);
	}

}
