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
import static com.tn.query.ComparisonOperator.NOT_EQUAL;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.tn.query.node.ComparisonNode;
import com.tn.query.node.Equal;
import com.tn.query.node.GreaterThan;
import com.tn.query.node.GreaterThanOrEqual;
import com.tn.query.node.In;
import com.tn.query.node.LessThan;
import com.tn.query.node.LessThanOrEqual;
import com.tn.query.node.NotEqual;

class ComparisonOperatorTest
{
  @Test
  void testMatchAndParseEqual()
  {
    String query = "left = right";

    assertMatches(EQUAL, query);
    assertParse(new Equal("left", "right"), query);
  }

  @Test
  void testMatchAndParseNotEqual()
  {
    String query = "left != right";

    assertMatches(NOT_EQUAL, query);
    assertParse(new NotEqual("left", "right"), query);
  }

  @Test
  void testMatchAndParseGreaterThan()
  {
    String query = "left > right";

    assertMatches(GREATER_THAN, query);
    assertParse(new GreaterThan("left", "right"), query);
  }

  @Test
  void testMatchAndParseGreaterOrEqualThan()
  {
    String query = "left >= right";

    assertMatches(GREATER_THAN_OR_EQUAL, query);
    assertParse(new GreaterThanOrEqual("left", "right"), query);
  }

  @Test
  void testMatchAndParseLessThan()
  {
    String query = "left < right";

    assertMatches(LESS_THAN, query);
    assertParse(new LessThan("left", "right"), query);
  }

  @Test
  void testMatchAndParseLessOrEqualThan()
  {
    String query = "left <= right";

    assertMatches(LESS_THAN_OR_EQUAL, query);
    assertParse(new LessThanOrEqual("left", "right"), query);
  }

  @Test
  void testMatchAndParseIn()
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
