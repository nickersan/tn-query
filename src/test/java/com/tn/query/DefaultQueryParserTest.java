package com.tn.query;

import static java.lang.String.format;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

class DefaultQueryParserTest
{
  private static final String LEFT = "A";
  private static final String RIGHT = "B";
  private static final String RIGHT_MAPPED = "B-MAPPED";
  private static final String PREDICATE = "predicate";

  @Test
  void shouldParseEqual()
  {
    shouldParse("=", predicateFactory -> predicateFactory::equal);
  }

  @Test
  void shouldParseEqualWithMapping()
  {
    shouldParseWithMapping("=", predicateFactory -> predicateFactory::equal);
  }

  @Test
  void shouldParseNotEqual()
  {
    shouldParse("!=", predicateFactory -> predicateFactory::notEqual);
  }

  @Test
  void shouldParseNotEqualWithMapping()
  {
    shouldParseWithMapping("!=", predicateFactory -> predicateFactory::notEqual);
  }

  @Test
  void shouldParseGreaterThan()
  {
    shouldParse(">", predicateFactory -> predicateFactory::greaterThan);
  }

  @Test
  void shouldParseGreaterThanWithMapping()
  {
    shouldParseWithMapping(">", predicateFactory -> predicateFactory::greaterThan);
  }

  @Test
  void shouldParseGreaterThanOrEqual()
  {
    shouldParse(">=", predicateFactory -> predicateFactory::greaterThanOrEqual);
  }

  @Test
  void shouldParseGreaterThanOrEqualWithMapping()
  {
    shouldParseWithMapping(">=", predicateFactory -> predicateFactory::greaterThanOrEqual);
  }

  @Test
  void shouldParseLessThan()
  {
    shouldParse("<", predicateFactory -> predicateFactory::lessThan);
  }

  @Test
  void shouldParseLessThanWithMapping()
  {
    shouldParseWithMapping("<", predicateFactory -> predicateFactory::lessThan);
  }

  @Test
  void shouldParseLessThanOrEqual()
  {
    shouldParse("<=", predicateFactory -> predicateFactory::lessThanOrEqual);
  }

  @Test
  void shouldParseLessThanOrEqualWithMapping()
  {
    shouldParseWithMapping("<=", predicateFactory -> predicateFactory::lessThanOrEqual);
  }

  @Test
  void shouldParseLike()
  {
    shouldParse("≈", predicateFactory -> predicateFactory::like);
  }

  @Test
  void shouldParseLikeWithMapping()
  {
    shouldParseWithMapping("≈", predicateFactory -> predicateFactory::like);
  }

  @Test
  void shouldParseNotLike()
  {
    shouldParse("!≈", predicateFactory -> predicateFactory::notLike);
  }

  @Test
  void shouldParseNotLikeWithMapping()
  {
    shouldParseWithMapping("!≈", predicateFactory -> predicateFactory::notLike);
  }

  @Test
  void shouldParseIn()
  {
    @SuppressWarnings("unchecked")
    PredicateFactory<String> predicateFactory = mock(PredicateFactory.class);
    when(predicateFactory.in(LEFT, List.of("A", "B", "C"))).thenReturn(PREDICATE);

    QueryParser<String> queryParser = new DefaultQueryParser<>(predicateFactory);
    assertEquals(PREDICATE, queryParser.parse(format("%s ∈ [A,B,C]", LEFT)));
  }

  @Test
  void shouldParseInWithMapping()
  {
    Mapper mapper = mock(Mapper.class);
    when(mapper.name()).thenReturn(LEFT);
    when(mapper.map("A")).thenReturn("X");
    when(mapper.map("B")).thenReturn("Y");
    when(mapper.map("C")).thenReturn("Z");

    @SuppressWarnings("unchecked")
    PredicateFactory<String> predicateFactory = mock(PredicateFactory.class);
    when(predicateFactory.in(LEFT, List.of("X", "Y", "Z"))).thenReturn(PREDICATE);

    QueryParser<String> queryParser = new DefaultQueryParser<>(predicateFactory, List.of(mapper));
    assertEquals(PREDICATE, queryParser.parse(format("%s ∈ [A,B,C]", LEFT)));
  }

  @Test
  void shouldParseAnd()
  {
    @SuppressWarnings("unchecked")
    PredicateFactory<String> predicateFactory = mock(PredicateFactory.class);
    when(predicateFactory.equal("A", "B")).thenReturn("X");
    when(predicateFactory.equal("C", "D")).thenReturn("Y");
    when(predicateFactory.and("X", "Y")).thenReturn(PREDICATE);

    QueryParser<String> queryParser = new DefaultQueryParser<>(predicateFactory);
    assertEquals(PREDICATE, queryParser.parse("A = B && C = D"));
  }

  @Test
  void shouldParseOr()
  {
    @SuppressWarnings("unchecked")
    PredicateFactory<String> predicateFactory = mock(PredicateFactory.class);
    when(predicateFactory.equal("A", "B")).thenReturn("X");
    when(predicateFactory.equal("C", "D")).thenReturn("Y");
    when(predicateFactory.or("X", "Y")).thenReturn(PREDICATE);

    QueryParser<String> queryParser = new DefaultQueryParser<>(predicateFactory);
    assertEquals(PREDICATE, queryParser.parse("A = B || C = D"));
  }

  @Test
  void shouldParseParenthesis()
  {
    @SuppressWarnings("unchecked")
    PredicateFactory<String> predicateFactory = mock(PredicateFactory.class);
    when(predicateFactory.equal("A", "B")).thenReturn("X");
    when(predicateFactory.parenthesis("X")).thenReturn(PREDICATE);

    QueryParser<String> queryParser = new DefaultQueryParser<>(predicateFactory);
    assertEquals(PREDICATE, queryParser.parse("(A = B)"));
  }

  private void shouldParse(String logicalOperation, Function<PredicateFactory<String>, BiFunction<String, String, String>> factoryExpectationSetter)
  {
    @SuppressWarnings("unchecked")
    PredicateFactory<String> predicateFactory = mock(PredicateFactory.class);
    when(factoryExpectationSetter.apply(predicateFactory).apply(LEFT, RIGHT)).thenReturn(PREDICATE);

    QueryParser<String> queryParser = new DefaultQueryParser<>(predicateFactory);
    assertEquals(PREDICATE, queryParser.parse(format("%s %s %s", LEFT, logicalOperation, RIGHT)));
  }

  private void shouldParseWithMapping(String logicalOperation, Function<PredicateFactory<String>, BiFunction<String, String, String>> factoryExpectationSetter)
  {
    Mapper mapper = mock(Mapper.class);
    when(mapper.name()).thenReturn(LEFT);
    when(mapper.map(RIGHT)).thenReturn(RIGHT_MAPPED);

    @SuppressWarnings("unchecked")
    PredicateFactory<String> predicateFactory = mock(PredicateFactory.class);
    when(factoryExpectationSetter.apply(predicateFactory).apply(LEFT, RIGHT_MAPPED)).thenReturn(PREDICATE);

    QueryParser<String> queryParser = new DefaultQueryParser<>(predicateFactory, List.of(mapper));
    assertEquals(PREDICATE, queryParser.parse(format("%s %s %s", LEFT, logicalOperation, RIGHT)));
  }
}
