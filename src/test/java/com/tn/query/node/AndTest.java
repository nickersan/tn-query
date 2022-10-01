package com.tn.query.node;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.Test;

class AndTest
{
  @Test
  void testEquality()
  {
    Equal left = new Equal("A", "B");
    Equal right = new Equal("C", "D");

    new EqualsTester()
      .addEqualityGroup(new And(left, right), new And(left, right))
      .addEqualityGroup(new And(new NotEqual("A", "B"), right))
      .addEqualityGroup(new And(left, new NotEqual("A", "B")))
      .testEquals();
  }
  
  @Test
  void testToString()
  {
    assertEquals(
      "A = B && C = D",
      new And(new Equal("A", "B"), new Equal("C", "D")).toString()
    );
  }
}
