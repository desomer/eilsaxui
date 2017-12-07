/**
 * 
 */
package com.elisaxui.app.elisys.srv;

import java.util.Objects;
import java.util.function.Function;

public interface Expression
{
  <A> A match (
    Function<Constant, A> constant,
    Function<Add, A> add,
    Function<Multiply, A> multiply
  );
}

final class Constant implements Expression
{
  public final int n;

  Constant(int i)
  {
    this.n = i;
  }

  public <A> A match(
    Function<Constant, A> constant,
    Function<Add, A> add,
    Function<Multiply, A> multiply)
  {
    return constant.apply(this);
  }
}

final class Add implements Expression
{
  public final Expression e0;
  public final Expression e1;

  Add(final Expression ie0, final Expression ie1)
  {
    this.e0 = Objects.requireNonNull(ie0);
    this.e1 = Objects.requireNonNull(ie1);
  }

  public <A> A match(
    Function<Constant, A> constant,
    Function<Add, A> add,
    Function<Multiply, A> multiply)
  {
    return add.apply(this);
  }
}

final class Multiply implements Expression
{
  public final Expression e0;
  public final Expression e1;

  Multiply(final Expression ie0, final Expression ie1)
  {
    this.e0 = Objects.requireNonNull(ie0);
    this.e1 = Objects.requireNonNull(ie1);
  }

  public <A> A match(
    Function<Constant, A> constant,
    Function<Add, A> add,
    Function<Multiply, A> multiply)
  {
    return multiply.apply(this);
  }
}