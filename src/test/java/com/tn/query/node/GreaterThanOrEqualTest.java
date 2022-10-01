package com.tn.query.node;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.Test;

class GreaterThanOrEqualTest
{
  @Test
  void testEquality()
  {
    new EqualsTester()
      .addEqualityGroup(new GreaterThanOrEqual("A", "B"), new GreaterThanOrEqual("A", "B"))
      .addEqualityGroup(new GreaterThanOrEqual("X", "B"))
      .addEqualityGroup(new GreaterThanOrEqual("A", "X"))
      .testEquals();
  }
  
  @Test
  void testToString()
  {
    assertEquals("A >= B", new GreaterThanOrEqual("A", "B").toString());
  }
}
