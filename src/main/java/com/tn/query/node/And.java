package com.tn.query.node;

import static java.lang.String.format;

public class And extends LogicalNode<And>
{
  private static final String TEMPLATE_TO_STRING = "%s && %s";

  public And(Object left)
  {
    super(left);
  }

  public And(Object left, Object right)
  {
    super(left, right);
  }

  @Override
  public And withRight(Object right)
  {
    return new And(getLeft(), right);
  }

  @Override
  public String toString()
  {
    return format(TEMPLATE_TO_STRING, getLeft(), getRight());
  }
}