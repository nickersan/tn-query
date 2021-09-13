package com.tn.query.node;

import static java.lang.String.format;

public class LessThanOrEqual extends ComparisonNode
{
  private static final String TEMPLATE_TO_STRING = "%s <= %s";

  public LessThanOrEqual(Object left, Object right)
  {
    super(left, right);
  }

  @Override
  public String toString()
  {
    return format(TEMPLATE_TO_STRING, getLeft(), getRight());
  }
}