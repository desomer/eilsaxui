/**
 * 
 */
package com.elisaxui.app.elisys.srv;

import java.util.function.Function;

/**
 * @author gauth
 *
 */
public interface Term {
	  <A> A match (
			    Function<Boolean, A> bool,
			    Function<Method, A> meth
			  );


	  final class Boolean implements Term
	  {
	    public final boolean n;

	    Boolean(boolean i)
	    {
	      this.n = i;
	    }

		/* (non-Javadoc)
		 * @see com.elisaxui.app.elisys.srv.Term#match(java.util.function.Function, java.util.function.Function)
		 */
		@Override
		public <A> A match(Function<Boolean, A> bool, Function<Method, A> meth) {
			return bool.apply(this);
		}

	    
	  }
	  
	  final class Method implements Term
	  {
	    public final Function f;

	    Method(Function fd)
	    {
	      this.f = fd;
	    }

		/* (non-Javadoc)
		 * @see com.elisaxui.app.elisys.srv.Term#match(java.util.function.Function, java.util.function.Function)
		 */
		@Override
		public <A> A match(Function<Boolean, A> bool, Function<Method, A> meth) {
			// TODO Auto-generated method stub
			return meth.apply(this);
		}

	  }
	  
	
}
