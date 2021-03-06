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
