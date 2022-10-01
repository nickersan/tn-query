package com.tn.query;

import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.annotation.Nonnull;

import com.tn.query.node.ComparisonNode;
import com.tn.query.node.Equal;
import com.tn.query.node.GreaterThan;
import com.tn.query.node.GreaterThanOrEqual;
import com.tn.query.node.In;
import com.tn.query.node.LessThan;
import com.tn.query.node.LessThanOrEqual;
import com.tn.query.node.NotEqual;

public enum ComparisonOperator
{
  EQUAL(
    "=",
    ComparisonOperator::matchesEqual,
    Equal::new
  ),

  NOT_EQUAL(
    "!=",
    ComparisonOperator::matchesNotEqual,
    NotEqual::new
  ),

  GREATER_THAN(
    ">",
    ComparisonOperator::matchesGreaterThan,
    GreaterThan::new
  ),

  GREATER_THAN_OR_EQUAL(
    ">=",
    ComparisonOperator::matchesGreaterThanOrEqual,
    GreaterThanOrEqual::new
  ),

  LESS_THAN(
    "<",
    ComparisonOperator::matchesLessThan,
    LessThan::new
  ),

  LESS_THAN_OR_EQUAL(
    "<=",
    ComparisonOperator::matchesLessThanOrEqual,
    LessThanOrEqual::new
  ),

  IN(
    "∈",
    ComparisonOperator::matchesIn,
    In::new
  );

  private static final int EXPECTED_TOKENS = 2;
  private static final int INDEX_LEFT = 0;
  private static final int INDEX_RIGHT = 1;
  private static final String REGEX_EQUAL = "^.+\\s*=\\s*.+$";
  private static final String REGEX_NOT_EQUAL = "^.+\\s*!=\\s*.+$";
  private static final String REGEX_GREATER_THAN = "^.+\\s*>\\s*.+$";
  private static final String REGEX_GREATER_THAN_OR_EQUAL = "^.+\\s*>=\\s*.+$";
  private static final String REGEX_LESS_THAN = "^.+\\s*<\\s*.+$";
  private static final String REGEX_LESS_THAN_OR_EQUAL = "^.+\\s*<=\\s*.+$";
  private static final String REGEX_IN = "^.+\\s*∈\\s*.+$";

  private final BiFunction<Object, Object, ComparisonNode> nodeFactory;
  private final Predicate<String> queryPartMatcher;
  private final String symbol;

  ComparisonOperator(String symbol, Predicate<String> queryPartMatcher, BiFunction<Object, Object, ComparisonNode> nodeFactory)
  {
    this.symbol = symbol;
    this.queryPartMatcher = queryPartMatcher;
    this.nodeFactory = nodeFactory;
  }

  public static @Nonnull ComparisonNode parseNode(@Nonnull String queryPart)
  {
    return Stream.of(ComparisonOperator.values())
      .filter(comparisonOperator -> comparisonOperator.matches(queryPart))
      .findFirst()
      .map(comparisonOperator -> comparisonOperator.parse(queryPart))
      .orElseThrow(() -> new QueryParseException("Illegal query part: " + queryPart));
  }

  boolean matches(@Nonnull String queryPart)
  {
    return this.queryPartMatcher.test(queryPart);
  }

  ComparisonNode parse(@Nonnull String queryPart) throws QueryParseException
  {
    if (!matches(queryPart)) throw new IllegalArgumentException("Unmatched query: " + queryPart);

    String[] queryTokens = queryPart.split(this.symbol);

    if (queryTokens.length != EXPECTED_TOKENS) throw new QueryParseException("Invalid query part: " + queryPart);

    return this.nodeFactory.apply(queryTokens[INDEX_LEFT].trim(), queryTokens[INDEX_RIGHT].trim());
  }

  private static boolean matchesEqual(String queryPart)
  {
    return queryPart.matches(REGEX_EQUAL) &&
      !matchesNotEqual(queryPart) &&
      !matchesGreaterThanOrEqual(queryPart) &&
      !matchesLessThanOrEqual(queryPart);
  }

  private static boolean matchesNotEqual(String queryPart)
  {
    return queryPart.matches(REGEX_NOT_EQUAL);
  }

  private static boolean matchesGreaterThan(String queryPart)
  {
    return queryPart.matches(REGEX_GREATER_THAN) &&
      !matchesGreaterThanOrEqual(queryPart);
  }

  private static boolean matchesGreaterThanOrEqual(String queryPart)
  {
    return queryPart.matches(REGEX_GREATER_THAN_OR_EQUAL);
  }

  private static boolean matchesLessThan(String queryPart)
  {
    return queryPart.matches(REGEX_LESS_THAN) &&
      !matchesLessThanOrEqual(queryPart);
  }

  private static boolean matchesLessThanOrEqual(String queryPart)
  {
    return queryPart.matches(REGEX_LESS_THAN_OR_EQUAL);
  }

  private static boolean matchesIn(String queryPart)
  {
    return queryPart.matches(REGEX_IN);
  }
}
