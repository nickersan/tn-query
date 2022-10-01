package com.tn.query.node;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.Test;

class LessThanOrEqualTest
{
  @Test
  void testEquality()
  {
    new EqualsTester()
      .addEqualityGroup(new LessThanOrEqual("A", "B"), new LessThanOrEqual("A", "B"))
      .addEqualityGroup(new LessThanOrEqual("X", "B"))
      .addEqualityGroup(new LessThanOrEqual("A", "X"))
      .testEquals();
  }
  
  @Test
  void testToString()
  {
    assertEquals("A <= B", new LessThanOrEqual("A", "B").toString());
  }
}
