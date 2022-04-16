package com.tn.query.java;

import static java.util.stream.Collectors.toMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import javax.annotation.Nonnull;

import com.tn.query.AbstractQueryParser;
import com.tn.query.Mapper;
import com.tn.query.QueryParseException;

public class JavaQueryParser<T> extends AbstractQueryParser<Predicate<T>>
{
  private final Map<String, Function<T, ?>> getters;

  public JavaQueryParser(Collection<Getter<T>> getters, Collection<Mapper> mappers)
  {
    super(mappers);
    this.getters = getters.stream().collect(toMap(Getter::name, getter -> getter::get));
  }

  @Override
  protected Predicate<T> equal(String left, Object right)
  {
    Function<T, ?> getter = getter(left);
    return target -> Objects.equals(getter.apply(target), right);
  }

  @Override
  protected Predicate<T> notEqual(String left, Object right)
  {
    Function<T, ?> getter = getter(left);
    return target -> !Objects.equals(getter.apply(target), right);
  }

  @Override
  protected Predicate<T> greaterThan(String left, Object right)
  {
    Function<T, ?> getter = getter(left);
    return target -> compare(getter.apply(target), right) > 0;
  }

  @Override
  protected Predicate<T> greaterThanOrEqual(String left, Object right)
  {
    Function<T, ?> getter = getter(left);
    return target -> compare(getter.apply(target), right) >= 0;
  }

  @Override
  protected Predicate<T> lessThan(String left, Object right)
  {
    Function<T, ?> getter = getter(left);
    return target -> compare(getter.apply(target), right) < 0;
  }

  @Override
  protected Predicate<T> lessThanOrEqual(String left, Object right)
  {
    Function<T, ?> getter = getter(left);
    return target -> compare(getter.apply(target), right) <= 0;
  }

  @Override
  protected Predicate<T> in(String left, List<?> right)
  {
    Function<T, ?> getter = getter(left);
    return target -> right.contains(getter.apply(target));
  }

  @Override
  protected Predicate<T> and(Predicate<T> left, Predicate<T> right)
  {
    return value -> left.test(value) && right.test(value);
  }

  @Override
  protected Predicate<T> or(Predicate<T> left, Predicate<T> right)
  {
    return value -> left.test(value) || right.test(value);
  }

  @Override
  protected Predicate<T> parenthesis(Predicate<T> node)
  {
    //Parenthesis is handled implicitly when parsing queries.
    return node;
  }

  private <T1> int compare(T1 obj1, T1 obj2)
  {
    if (!(obj1 instanceof Comparable)) throw new QueryParseException("Cannot compare: " + obj1);
    if (!(obj2 instanceof Comparable)) throw new QueryParseException("Cannot compare: " + obj2);

    //noinspection unchecked
    return ((Comparable<T1>)obj1).compareTo(obj2);
  }

  private @Nonnull Function<T, ?> getter(String left)
  {
    Function<T, ?> getter = this.getters.get(left);
    if (getter == null) throw new QueryParseException("Getter missing for: " + left);

    return getter;
  }
}
