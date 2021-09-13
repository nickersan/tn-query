package com.tn.query.node;

import static java.util.Objects.hash;

import java.util.Objects;

public abstract class AbstractNode implements Node
{
  private final Object left;
  private final Object right;

  public AbstractNode(Object left, Object right)
  {
    this.left = left;
    this.right = right;
  }

  @Override
  public Object getLeft()
  {
    return this.left;
  }

  @Override
  public Object getRight()
  {
    return this.right;
  }

  @Override
  public boolean isValid()
  {
    return this.left != null && this.right != null;
  }

  @Override
  public boolean equals(Object other)
  {
    return this == other || (
      other != null &&
        getClass().equals(other.getClass()) &&
        Objects.equals(this.left, ((AbstractNode)other).left) &&
        Objects.equals(this.right, ((AbstractNode)other).right)
    );
  }

  @Override
  public int hashCode()
  {
    return hash(this.left, this.right);
  }
}
