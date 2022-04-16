package com.tn.query.node;

import static java.lang.String.format;

import java.util.Objects;

public class Parenthesis implements Node
{
  private static final String TEMPLATE_TO_STRING = "(%s)";

  private final Node node;

  public Parenthesis(Node node)
  {
    this.node = node;
  }

  public Node getNode()
  {
    return this.node;
  }

  @Override
  public Object getLeft()
  {
    return this.node.getLeft();
  }

  @Override
  public Object getRight()
  {
    return this.node.getRight();
  }

  @Override
  public boolean isValid()
  {
    return this.node.isValid();
  }

  @Override
  public boolean equals(Object other)
  {
    return this == other || (
      other != null &&
      getClass().equals(other.getClass()) &&
      Objects.equals(this.node, ((Parenthesis)other).node)
    );
  }

  @Override
  public int hashCode()
  {
    return this.node.hashCode();
  }

  @Override
  public String toString()
  {
    return format(TEMPLATE_TO_STRING, node);
  }
}
