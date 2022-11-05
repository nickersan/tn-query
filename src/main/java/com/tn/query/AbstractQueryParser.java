package com.tn.query;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import com.tn.query.node.And;
import com.tn.query.node.Equal;
import com.tn.query.node.GreaterThan;
import com.tn.query.node.GreaterThanOrEqual;
import com.tn.query.node.In;
import com.tn.query.node.LessThan;
import com.tn.query.node.LessThanOrEqual;
import com.tn.query.node.Like;
import com.tn.query.node.Node;
import com.tn.query.node.NotEqual;
import com.tn.query.node.NotLike;
import com.tn.query.node.Or;
import com.tn.query.node.Parenthesis;

public abstract class AbstractQueryParser<T> implements QueryParser<T>
{
  protected static final String WILDCARD = "*";

  private static final String EMPTY = "";
  private static final String CLOSE_LIST = "]";
  private static final String OPEN_LIST = "[";
  private static final String REGX_COMMA = ",";

  private final Map<Object, Function<String, Object>> mappers;

  protected AbstractQueryParser(Collection<Mapper> mappers)
  {
    this.mappers = mappers.stream().collect(toMap(Mapper::name, mapper -> mapper::map));
  }

  @Override
  public final T parse(String s) throws QueryParseException
  {
    return predicate(Query.parse(s));
  }

  public T predicate(Node node)
  {
    if (node instanceof Parenthesis)
    {
      return parenthesis(predicate(((Parenthesis)node).getNode()));
    }
    else
    {
      return predicateFactory(node).apply(
        node.getLeft() instanceof Node ? predicate((Node)node.getLeft()) : node.getLeft(),
        node.getRight() instanceof Node ? predicate((Node)node.getRight()) : node.getRight()
      );
    }
  }

  @SuppressWarnings("unchecked")
  protected BiFunction<Object, Object, T> predicateFactory(Node node)
  {
    if (node instanceof Equal) return (left, right) -> equal((String)left, map(left, right));
    else if (node instanceof NotEqual) return (left, right) -> notEqual((String)left, map(left, right));
    else if (node instanceof GreaterThan) return (left, right) -> greaterThan((String)left, map(left, right));
    else if (node instanceof GreaterThanOrEqual) return (left, right) -> greaterThanOrEqual((String)left, map(left, right));
    else if (node instanceof LessThan) return (left, right) -> lessThan((String)left, map(left, right));
    else if (node instanceof LessThanOrEqual) return (left, right) -> lessThanOrEqual((String)left, map(left, right));
    else if (node instanceof Like) return (left, right) -> like((String)left, map(left, right));
    else if (node instanceof NotLike) return (left, right) -> notLike((String)left, map(left, right));
    else if (node instanceof In) return (left, right) -> in((String)left, mapList(left, right));
    else if (node instanceof And) return (left, right) -> and((T)left, (T)right);
    else if (node instanceof Or) return (left, right) -> or((T)left, (T)right);
    else throw new QueryParseException("Node type not handled: " + node.getClass().getCanonicalName());
  }

  protected abstract T equal(String left, Object right);

  protected abstract T notEqual(String left, Object right);

  protected abstract T greaterThan(String left, Object right);

  protected abstract T greaterThanOrEqual(String left, Object right);

  protected abstract T lessThan(String left, Object right);

  protected abstract T lessThanOrEqual(String left, Object right);

  protected abstract T like(String left, Object right);

  protected abstract T notLike(String left, Object right);

  protected abstract T in(String left, List<?> right);

  protected abstract T and(T left, T right);

  protected abstract T or(T left, T right);

  protected abstract T parenthesis(T node);

  private Object map(Object left, Object right)
  {
    return mapper(left).apply((String)right);
  }

  private List<?> mapList(Object left, Object right)
  {
    return Stream.of(((String)right).replace(OPEN_LIST, EMPTY).replace(CLOSE_LIST, EMPTY).split(REGX_COMMA))
      .filter(Objects::nonNull)
      .map(item -> map(left, item.trim()))
      .collect(toList());
  }

  protected Function<String, Object> mapper(Object left)
  {
    Function<String, Object> mapper = this.mappers.get(left);
    return mapper != null ? mapper : s -> s;
  }
}
