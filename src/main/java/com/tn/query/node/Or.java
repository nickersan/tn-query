package com.tn.query.node;

import static java.lang.String.format;

public class Or extends LogicalNode<Or>
{
  private static final String TEMPLATE_TO_STRING = "%s || %s";

  public Or(Object left)
  {
    super(left);
  }

  public Or(Object left, Object right)
  {
    super(left, right);
  }

  @Override
  public Or withRight(Object right)
  {
    return new Or(getLeft(), right);
  }

  @Override
  public String toString()
  {
    return format(TEMPLATE_TO_STRING, getLeft(), getRight());
  }
}