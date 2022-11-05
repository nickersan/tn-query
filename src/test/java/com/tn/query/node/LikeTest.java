package com.tn.query.node;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.Test;

class LikeTest
{
  @Test
  void testEquality()
  {
    new EqualsTester()
      .addEqualityGroup(new Like("A", "B"), new Like("A", "B"))
      .addEqualityGroup(new Like("X", "B"))
      .addEqualityGroup(new Like("A", "X"))
      .testEquals();
  }
  
  @Test
  void testToString()
  {
    assertEquals("A ≈ B", new Like("A", "B").toString());
  }
}
