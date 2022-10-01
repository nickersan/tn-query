package com.tn.query.node;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.Test;
class ParenthesisTest
{
  @Test
  void testEquality()
  {
    new EqualsTester()
      .addEqualityGroup(new Parenthesis(new Equal("A", "B")), new Parenthesis(new Equal("A", "B")))
      .addEqualityGroup(new Parenthesis(new Equal("X", "B")))
      .addEqualityGroup(new Parenthesis(new Equal("A", "X")))
      .testEquals();
  }

  @Test
  void testToString()
  {
    assertEquals("(A = B)", new Parenthesis(new Equal("A", "B")).toString());
  }
}
