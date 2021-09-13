package com.tn.query.node;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.Test;

class InTest
{
  @Test
  void testEquality()
  {
    //noinspection UnstableApiUsage
    new EqualsTester()
      .addEqualityGroup(new In("A", "B"), new In("A", "B"))
      .addEqualityGroup(new In("X", "B"))
      .addEqualityGroup(new In("A", "X"))
      .testEquals();
  }
  
  @Test
  void testToString()
  {
    assertEquals("A ∈ B", new In("A", "B").toString());
  }
}
