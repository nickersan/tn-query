package com.tn.query.node;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.Test;

class NotLikeTest
{
  @Test
  void testEquality()
  {
    new EqualsTester()
      .addEqualityGroup(new NotLike("A", "B"), new NotLike("A", "B"))
      .addEqualityGroup(new NotLike("X", "B"))
      .addEqualityGroup(new NotLike("A", "X"))
      .testEquals();
  }

  @Test
  void testToString()
  {
    assertEquals("A !≈ B", new NotLike("A", "B").toString());
  }
}
