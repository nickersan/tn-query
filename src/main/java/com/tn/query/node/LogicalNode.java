package com.tn.query.node;

public abstract class LogicalNode<T extends LogicalNode<T>> extends AbstractNode
{
  public LogicalNode(Object left)
  {
    this(left, null);
  }

  public LogicalNode(Object left, Object right)
  {
    super(left, right);
  }

  public abstract T withRight(Object right);
}
