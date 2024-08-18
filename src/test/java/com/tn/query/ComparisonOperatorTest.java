package com.tn.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static com.tn.query.ComparisonOperator.EQUAL;
import static com.tn.query.ComparisonOperator.GREATER_THAN;
import static com.tn.query.ComparisonOperator.GREATER_THAN_OR_EQUAL;
import static com.tn.query.ComparisonOperator.IN;
import static com.tn.query.ComparisonOperator.LESS_THAN;
import static com.tn.query.ComparisonOperator.LESS_THAN_OR_EQUAL;
import static com.tn.query.ComparisonOperator.LIKE;
import static com.tn.query.ComparisonOperator.NOT_EQUAL;
import static com.tn.query.ComparisonOperator.NOT_LIKE;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.tn.query.node.ComparisonNode;
import com.tn.query.node.Equal;
import com.tn.query.node.GreaterThan;
import com.tn.query.node.GreaterThanOrEqual;
import com.tn.query.node.In;
import com.tn.query.node.LessThan;
import com.tn.query.node.LessThanOrEqual;
import com.tn.query.node.Like;
import com.tn.query.node.NotEqual;
import com.tn.query.node.NotLike;

class ComparisonOperatorTest
{
  @Test
  void shouldMatchAndParseEqual()
  {
    String query = "left = right";

    assertMatches(EQUAL, query);
    assertParse(new Equal("left", "right"), query);
  }

  @Test
  void shouldMatchAndParseNotEqual()
  {
    String query = "left != right";

    assertMatches(NOT_EQUAL, query);
    assertParse(new NotEqual("left", "right"), query);
  }

  @Test
  void shouldMatchAndParseGreaterThan()
  {
    String query = "left > right";

    assertMatches(GREATER_THAN, query);
    assertParse(new GreaterThan("left", "right"), query);
  }

  @Test
  void shouldMatchAndParseGreaterOrEqualThan()
  {
    String query = "left >= right";

    assertMatches(GREATER_THAN_OR_EQUAL, query);
    assertParse(new GreaterThanOrEqual("left", "right"), query);
  }

  @Test
  void shouldMatchAndParseLessThan()
  {
    String query = "left < right";

    assertMatches(LESS_THAN, query);
    assertParse(new LessThan("left", "right"), query);
  }

  @Test
  void shouldMatchAndParseLessOrEqualThan()
  {
    String query = "left <= right";

    assertMatches(LESS_THAN_OR_EQUAL, query);
    assertParse(new LessThanOrEqual("left", "right"), query);
  }

  @Test
  void shouldMatchAndParseLike()
  {
    String query = "left ≈ right";

    assertMatches(LIKE, query);
    assertParse(new Like("left", "right"), query);
  }

  @Test
  void shouldMatchAndParseNotLike()
  {
    String query = "left !≈ right";

    assertMatches(NOT_LIKE, query);
    assertParse(new NotLike("left", "right"), query);
  }

  @Test
  void shouldMatchAndParseIn()
  {
    String query = "left ∈ right";

    assertMatches(IN, query);
    assertParse(new In("left", "right"), query);
  }

  private void assertMatches(ComparisonOperator expectedComparisonOperator, String queryPart)
  {
    assertTrue(expectedComparisonOperator.matches(queryPart));

    Stream.of(ComparisonOperator.values())
      .filter(comparisonOperator -> expectedComparisonOperator != comparisonOperator)
      .forEach(comparisonOperator -> assertFalse(comparisonOperator.matches(queryPart), "False matched: " + comparisonOperator));
  }

  private void assertParse(ComparisonNode expectedComparisonNode, String queryPart)
  {
    assertEquals(expectedComparisonNode, ComparisonOperator.parseNode(queryPart));
  }
}
