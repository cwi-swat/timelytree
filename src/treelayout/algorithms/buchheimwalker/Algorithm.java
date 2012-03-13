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
  
  Originally from the prefuse library.
 */


import java.util.Arrays;


/**
 * <p>TreeLayout that computes a tidy layout of a node-link tree
 * diagram. This algorithm lays out a rooted tree such that each
 * depth level of the tree is on a shared line. The orientation of the
 * tree can be set such that the tree goes left-to-right (default),
 * right-to-left, top-to-bottom, or bottom-to-top.</p>
 * 
 * <p>The algorithm used is that of Christoph Buchheim, Michael J�nger,
 * and Sebastian Leipert from their research paper
 * <a href="http://citeseer.ist.psu.edu/buchheim02improving.html">
 * Improving Walker's Algorithm to Run in Linear Time</a>, Graph Drawing 2002.
 * This algorithm corrects performance issues in Walker's algorithm, which
 * generalizes Reingold and Tilford's method for tidy drawings of trees to
 * support trees with an arbitrary number of children at any given node.</p>
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class Algorithm {
    
    private double[] m_depths = new double[10];
    private int      m_maxDepth = 0;
    
    /**
     * Create a new NodeLinkTreeLayout. A left-to-right orientation is assumed.
     * @param group the data group to layout. Must resolve to a Graph instance.
     */
    public Algorithm() {
    }
    
   
    
    private double spacing(NodeItem l, NodeItem r, boolean siblings) {
        return  0.5 *
            ( l.width + r.width);
    }
    
    private void updateDepths(int depth, NodeItem item) {
        double d = (  item.height );
        if ( m_depths.length <= depth )
            m_depths = ArrayLib.resize(m_depths, 3*depth/2);
        m_depths[depth] = Math.max(m_depths[depth], d);
        m_maxDepth = Math.max(m_maxDepth, depth);
    }
    
    private void determineDepths() {
        for ( int i=1; i<m_maxDepth; ++i )
            m_depths[i] += m_depths[i-1] ;
    }
    
    // ------------------------------------------------------------------------
    
    /**
     * @see prefuse.action.Action#run(double)
     */
    public void run(NodeItem root) {
        
        Arrays.fill(m_depths, 0);
        m_maxDepth = 0;
        
        // do first pass - compute breadth information, collect depth info
        firstWalk(root, 0, 1);
        
        // sum up the depth info
        determineDepths();
        
        // do second pass - assign layout positions
        secondWalk(root, null, -root.prelim, 0);
    }


	private void firstWalk(NodeItem n, int num, int depth) {
        n.number = num;
        updateDepths(depth, n);
        
        if ( n.children.length == 0 ) // is leaf
        { 
            NodeItem l = (NodeItem)n.prevSibling;
            if ( l == null ) {
                n.prelim = 0;
            } else {
                n.prelim = l.prelim + spacing(l,n,true);
            }
        }
        else
        {
            NodeItem leftMost = (NodeItem)n.getFirstChild();
            NodeItem rightMost = (NodeItem)n.getLastChild();
            NodeItem defaultAncestor = leftMost;
            NodeItem c = leftMost;
            for ( int i=0; c != null; ++i, c = (NodeItem)c.nextSibling )
            {
                firstWalk(c, i, depth+1);
                defaultAncestor = apportion(c, defaultAncestor);
            }
            
            executeShifts(n);
            
            double midpoint = 0.5 *
                (leftMost.prelim + rightMost.prelim);
            
            NodeItem left = (NodeItem)n.prevSibling;
            if ( left != null ) {
                n.prelim = left.prelim + spacing(left, n, true);
                n.mod = n.prelim - midpoint;
            } else {
                n.prelim = midpoint;
            }
        }
    }
    
    private NodeItem apportion(NodeItem v, NodeItem a) {        
        NodeItem w = (NodeItem)v.prevSibling;
        if ( w != null ) {
            NodeItem vip, vim, vop, vom;
            double   sip, sim, sop, som;
            
            vip = vop = v;
            vim = w;
            vom = (NodeItem)vip.parent.getFirstChild();
            
            sip = vip.mod;
            sop = vop.mod;
            sim = vim.mod;
            som = vom.mod;
            
            NodeItem nr = nextRight(vim);
            NodeItem nl = nextLeft(vip);
            while ( nr != null && nl != null ) {
                vim = nr;
                vip = nl;
                vom = nextLeft(vom);
                vop = nextRight(vop);
                vop.ancestor = v;
                double shift = (vim.prelim + sim) - 
                    (vip.prelim + sip) + spacing(vim,vip,false);
                if ( shift > 0 ) {
                    moveSubtree(ancestor(vim,v,a), v, shift);
                    sip += shift;
                    sop += shift;
                }
                sim += vim.mod;
                sip += vip.mod;
                som += vom.mod;
                sop += vop.mod;
                
                nr = nextRight(vim);
                nl = nextLeft(vip);
            }
            if ( nr != null && nextRight(vop) == null ) {
                vop.thread = nr;
                vop.mod += sim - sop;
            }
            if ( nl != null && nextLeft(vom) == null ) {
                vom.thread = nl;
                vom.mod += sip - som;
                a = v;
            }
        }
        return a;
    }
    
    private NodeItem nextLeft(NodeItem n) {
        NodeItem c = null;
         c = (NodeItem)n.getFirstChild();
        return ( c != null ? c : n.thread );
    }
    
    private NodeItem nextRight(NodeItem n) {
        NodeItem c = null;
        c = (NodeItem)n.getLastChild();
        return ( c != null ? c : n.thread );
    }
    
    private void moveSubtree(NodeItem wm, NodeItem wp, double shift) {
        double subtrees = wp.number - wm.number;
        wp.change -= shift/subtrees;
        wp.shift += shift;
        wm.change += shift/subtrees;
        wp.prelim += shift;
        wp.mod += shift;
    }
    
    private void executeShifts(NodeItem n) {
        double shift = 0, change = 0;
        for ( NodeItem c = (NodeItem)n.getLastChild();
              c != null; c = (NodeItem)c.prevSibling )
        {
            c.prelim += shift;
            c.mod += shift;
            change += c.change;
            shift += c.shift + change;
        }
    }
    
    private NodeItem ancestor(NodeItem vim, NodeItem v, NodeItem a) {
        NodeItem p = (NodeItem)v.parent;
        if ( vim.ancestor.parent == p ) {
            return vim.ancestor;
        } else {
            return a;
        }
    }
    
    private void secondWalk(NodeItem n, NodeItem p, double m, int depth) {
        n.x =n.prelim + m;
        n.y = m_depths[depth];
        
       depth += 1;
        for ( NodeItem c = (NodeItem)n.getFirstChild();
              c != null; c = (NodeItem)c.nextSibling )
        {
            secondWalk(c, n, m + n.mod, depth);
        }
        
        n.clear();
    }
    

    
} // end of class NodeLinkTreeLayout
