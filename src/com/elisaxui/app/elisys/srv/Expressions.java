/**
 * 
 */
package com.elisaxui.app.elisys.srv;

public class Expressions
{
  public static int evaluate (Expression e)
  {
    return e.match(
      (Constant constant) -> constant.n,
      (Add add)           -> evaluate (add.e0) + evaluate (add.e1),
      (Multiply multiply) -> evaluate (multiply.e0) * evaluate (multiply.e1));
  }
  
  static
  {
	  
	  Expressions.evaluate(new Multiply (new Constant(5), new Constant(2)) );
	  
  }
}