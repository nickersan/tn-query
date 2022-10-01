package com.tn.query.node;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.Test;

class LessThanTest
{
  @Test
  void testEquality()
  {
    new EqualsTester()
      .addEqualityGroup(new LessThan("A", "B"), new LessThan("A", "B"))
      .addEqualityGroup(new LessThan("X", "B"))
      .addEqualityGroup(new LessThan("A", "X"))
      .testEquals();
  }
  
  @Test
  void testToString()
  {
    assertEquals("A < B", new LessThan("A", "B").toString());
  }
}
