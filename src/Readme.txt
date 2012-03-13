
*******************************************************************************
                   This code accompanies the paper:
                "On the timely drawing of tidy trees" 
                       by A.J. van der Ploeg
*******************************************************************************

The code in this repository in the public domain, except the code
in the treelayout.algorithms.buchheimwalker package, which is taken from
the prefuse (http://prefuse.org) library.

The implementation of the algorithm can be found in the 
treelayout.algorithms.ours package. Other algorithms named in the paper
can be found in treelayout.algorithms.

In treelayout.measure package contains code to reproduce the measurements
presented in the paper.

The package treelayout.swt contains an visual displayer of tree layouts. It 
required SWT to run. 
Press 'a' to generate a new tree.
Press 'q' to switch between tree types:
	*equally sized, root middle between children
	*arbitrarily sized, root middle between children
	*arbitrarily sized, root fixed distance from first child
Press 'a' to switch to another algorithm.


 
The code in the treelayout.algorithms.buchheimwalker package has the following
copyright notice:

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
 