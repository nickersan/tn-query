package com.tn.query;

public abstract class Named
{
  private final String name;

  public Named(String name)
  {
    this.name = name;
  }

  public String name()
  {
    return this.name;
  }
}
