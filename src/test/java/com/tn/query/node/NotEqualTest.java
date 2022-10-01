package com.tn.query.node;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.Test;

class NotEqualTest
{
  @Test
  void testEquality()
  {
    new EqualsTester()
      .addEqualityGroup(new NotEqual("A", "B"), new NotEqual("A", "B"))
      .addEqualityGroup(new NotEqual("X", "B"))
      .addEqualityGroup(new NotEqual("A", "X"))
      .testEquals();
  }

  @Test
  void testToString()
  {
    assertEquals("A != B", new NotEqual("A", "B").toString());
  }
}
