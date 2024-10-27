package com.tn.query;

import java.util.List;

public interface PredicateFactory<T>
{
  T equal(String left, Object right);

  T notEqual(String left, Object right);

  T greaterThan(String left, Object right);

  T greaterThanOrEqual(String left, Object right);

  T lessThan(String left, Object right);

  T lessThanOrEqual(String left, Object right);

  T like(String left, Object right);

  T notLike(String left, Object right);

  T in(String left, List<?> right);

  T and(T left, T right);

  T or(T left, T right);

  T parenthesis(T node);
}
