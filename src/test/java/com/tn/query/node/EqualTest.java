package com.tn.query.node;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.Test;

class EqualTest
{
  @Test
  void testEquality()
  {
    new EqualsTester()
      .addEqualityGroup(new Equal("A", "B"), new Equal("A", "B"))
      .addEqualityGroup(new Equal("X", "B"))
      .addEqualityGroup(new Equal("A", "X"))
      .testEquals();
  }
  
  @Test
  void testToString()
  {
    assertEquals("A = B", new Equal("A", "B").toString());
  }
}
