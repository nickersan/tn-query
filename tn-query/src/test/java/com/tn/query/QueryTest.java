package com.tn.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static com.tn.query.Query.parse;

import org.junit.jupiter.api.Test;

import com.tn.query.node.And;
import com.tn.query.node.Equal;
import com.tn.query.node.GreaterThan;
import com.tn.query.node.LessThanOrEqual;
import com.tn.query.node.NotEqual;
import com.tn.query.node.Or;
import com.tn.query.node.Parenthesis;

class QueryTest
{
  @Test
  void testParse()
  {
    assertEquals(
      new Equal("a", "b"),
      parse("a = b")
    );
  }

  @Test
  void testParseAnd()
  {
    assertEquals(
      new And(
        new Equal("a", "b"),
        new NotEqual("c", "d")
      ),
      parse("a = b && c != d")
    );
  }

  @Test
  void testParseMultipleLogicalOperators()
  {
    assertEquals(
      new And(
        new Or(
          new And(
            new Equal("a", "b"),
            new NotEqual("c", "d")
          ),
          new GreaterThan("e", "f")
        ),
        new LessThanOrEqual("g", "h")
      ),
      parse("a = b && c != d || e > f && g <= h")
    );
  }

  @Test
  void testParseMultipleLogicalOperatorsWithParenthesis()
  {
    assertEquals(
      new And(
        new Equal("a", "b"),
        new Parenthesis(
          new Or(
            new NotEqual("c", "d"),
            new Parenthesis(
              new And(
                new GreaterThan("e", "f"),
                new LessThanOrEqual("g", "h")
              )
            )
          )
        )
      ),
      parse("a = b && (c != d || (e > f && g <= h))")
    );
  }

  @Test
  void testParseEmpty()
  {
    assertThrows(QueryParseException.class, () -> parse(""));
    assertThrows(QueryParseException.class, () -> parse(" "));
  }

  @Test
  void testParseInvalid()
  {
    assertThrows(QueryParseException.class, () -> parse("INVALID"));
    assertThrows(QueryParseException.class, () -> parse("a = b c = d"));
  }

  @Test
  void testParseInvalidAnd()
  {
    assertThrows(QueryParseException.class, () -> parse("&& a = b"));
    assertThrows(QueryParseException.class, () -> parse("a && a = b"));
    assertThrows(QueryParseException.class, () -> parse("a = b &&"));
    assertThrows(QueryParseException.class, () -> parse("a = b && a"));
  }

  @Test
  void testParseInvalidOr()
  {
    assertThrows(QueryParseException.class, () -> parse("|| a = b"));
    assertThrows(QueryParseException.class, () -> parse("a || a = b"));
    assertThrows(QueryParseException.class, () -> parse("a = b ||"));
    assertThrows(QueryParseException.class, () -> parse("a = b || a"));
  }

  @Test
  void testParseInvalidParenthesis()
  {
    assertThrows(QueryParseException.class, () -> parse("()"));
    assertThrows(QueryParseException.class, () -> parse("("));
  }
}
