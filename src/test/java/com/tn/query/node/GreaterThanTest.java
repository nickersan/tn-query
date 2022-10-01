package com.tn.query.node;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.Test;

class GreaterThanTest
{
  @Test
  void testEquality()
  {
    new EqualsTester()
      .addEqualityGroup(new GreaterThan("A", "B"), new GreaterThan("A", "B"))
      .addEqualityGroup(new GreaterThan("X", "B"))
      .addEqualityGroup(new GreaterThan("A", "X"))
      .testEquals();
  }
  
  @Test
  void testToString()
  {
    assertEquals("A > B", new GreaterThan("A", "B").toString());
  }
}
