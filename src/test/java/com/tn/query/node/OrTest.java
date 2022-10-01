package com.tn.query.node;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.Test;

class OrTest
{
  @Test
  void testEquality()
  {
    Equal left = new Equal("A", "B");
    Equal right = new Equal("C", "D");

    new EqualsTester()
      .addEqualityGroup(new Or(left, right), new Or(left, right))
      .addEqualityGroup(new Or(new NotEqual("A", "B"), right))
      .addEqualityGroup(new Or(left, new NotEqual("A", "B")))
      .testEquals();
  }
  
  @Test
  void testToString()
  {
    assertEquals(
      "A = B || C = D",
      new Or(new Equal("A", "B"), new Equal("C", "D")).toString()
    );
  }
}
