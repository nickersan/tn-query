package com.tn.query.java;

import java.util.function.Function;

import com.tn.query.Named;

public class Getter<T> extends Named
{
  private final Function<T, ?> get;

  private Getter(String name, Function<T, ?> get)
  {
    super(name);
    this.get = get;
  }

  public Object get(T t)
  {
    return this.get.apply(t);
  }

  public static <T> Getter<T> booleanValue(String name, Function<T, Boolean> get)
  {
    return new Getter<>(name, get);
  }

  public static <T> Getter<T> byteValue(String name, Function<T, Byte> get)
  {
    return new Getter<>(name, get);
  }

  public static <T> Getter<T> charValue(String name, Function<T, Character> get)
  {
    return new Getter<>(name, get);
  }

  public static <T> Getter<T> comparableValue(String name, Function<T, Comparable<?>> get)
  {
    return new Getter<>(name, get);
  }

  public static <T> Getter<T> doubleValue(String name, Function<T, Double> get)
  {
    return new Getter<>(name, get);
  }

  public static <T> Getter<T> floatValue(String name, Function<T, Float> get)
  {
    return new Getter<>(name, get);
  }

  public static <T> Getter<T> intValue(String name, Function<T, Integer> get)
  {
    return new Getter<>(name, get);
  }

  public static <T> Getter<T> longValue(String name, Function<T, Long> get)
  {
    return new Getter<>(name, get);
  }

  public static <T> Getter<T> shortValue(String name, Function<T, Short> get)
  {
    return new Getter<>(name, get);
  }
}
