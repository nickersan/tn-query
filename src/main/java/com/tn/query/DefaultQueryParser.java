package com.tn.query;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

public class DefaultQueryParser<T> implements QueryParser<T>
{
  private static final String EMPTY = "";
  private static final String NULL = "null";
  private static final String CLOSE_LIST = "]";
  private static final String OPEN_LIST = "[";
  private static final String REGX_COMMA = ",";

  private final Map<Object, Function<String, Object>> mappers;
  private final PredicateFactory<T> predicateFactory;

  public DefaultQueryParser(PredicateFactory<T> predicateFactory)
  {
    this(predicateFactory, emptyList());
  }

  public DefaultQueryParser(PredicateFactory<T> predicateFactory, Collection<Mapper> mappers)
  {
    this.mappers = mappers.stream().collect(toMap(Mapper::name, mapper -> mapper::map));
    this.predicateFactory = predicateFactory;
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
      return this.predicateFactory.parenthesis(predicate(((Parenthesis)node).getNode()));
    }
    else
    {
      return predicateForNode(
        node,
        node.getLeft() instanceof Node ? predicate((Node)node.getLeft()) : node.getLeft(),
        node.getRight() instanceof Node ? predicate((Node)node.getRight()) : node.getRight()
      );
    }
  }

  @SuppressWarnings("unchecked")
  protected T predicateForNode(Node node, Object left, Object right)
  {
    if (node instanceof Equal) return this.predicateFactory.equal((String)left, map(left, right));
    else if (node instanceof NotEqual) return this.predicateFactory.notEqual((String)left, map(left, right));
    else if (node instanceof GreaterThan) return this.predicateFactory.greaterThan((String)left, map(left, right));
    else if (node instanceof GreaterThanOrEqual) return this.predicateFactory.greaterThanOrEqual((String)left, map(left, right));
    else if (node instanceof LessThan) return this.predicateFactory.lessThan((String)left, map(left, right));
    else if (node instanceof LessThanOrEqual) return this.predicateFactory.lessThanOrEqual((String)left, map(left, right));
    else if (node instanceof Like) return this.predicateFactory.like((String)left, map(left, right));
    else if (node instanceof NotLike) return this.predicateFactory.notLike((String)left, map(left, right));
    else if (node instanceof In) return this.predicateFactory.in((String)left, mapList(left, right));
    else if (node instanceof And) return this.predicateFactory.and((T)left, (T)right);
    else if (node instanceof Or) return this.predicateFactory.or((T)left, (T)right);

    throw new QueryParseException("Node type not handled: " + node.getClass().getCanonicalName());
  }

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

  private Function<String, Object> mapper(Object left)
  {
    Function<String, Object> mapper = this.mappers.get(left);
    return s ->
    {
      if (NULL.equals(s)) return null;
      else if (mapper != null) return mapper.apply(s);
      else return s;
    };
  }
}
