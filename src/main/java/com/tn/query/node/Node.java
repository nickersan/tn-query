package com.tn.query.node;

public interface Node
{
  Object getLeft();

  Object getRight();

  boolean isValid();

  default boolean isNotValid()
  {
    return !this.isValid();
  }
}
