package com.tn.query.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.tn.query.Mapper;
import com.tn.query.QueryParser;

class JdbcQueryParserTest
{
  private final QueryParser<JdbcPredicate> queryParser = new JdbcQueryParser(
    Map.of(
      "booleanValue", "boolean",
      "byteValue", "byte",
      "charValue", "char"
    ),
    List.of(
      Mapper.toBoolean("booleanValue"),
      Mapper.toByte("byteValue"),
      Mapper.toChar("charValue"),
      Mapper.toDate("dateValue"),
      Mapper.toDouble("doubleValue"),
      Mapper.toFloat("floatValue"),
      Mapper.toInt("intValue"),
      Mapper.toLocalDate("localDateValue"),
      Mapper.toLocalDateTime("localDateTimeValue"),
      Mapper.toLong("longValue"),
      Mapper.toShort("shortValue")
    )
  );

  @Test
  void testParsebyte() throws Exception
  {
    assertPredicate(
      this.queryParser.parse("byteValue = true"),
      "byte = ?",
      preparedStatement -> verify(preparedStatement).setBoolean(1, true)
    );

    assertPredicate(
      this.queryParser.parse("byteValue != true"),
      "NOT byte = ?",
      preparedStatement -> verify(preparedStatement).setBoolean(1, true)
    );

    assertPredicate(
      this.queryParser.parse("byteValue > true"),
      "byte > ?",
      preparedStatement -> verify(preparedStatement).setBoolean(1, true)
    );

    assertPredicate(
      this.queryParser.parse("byteValue >= true"),
      "byte >= ?",
      preparedStatement -> verify(preparedStatement).setBoolean(1, true)
    );

    assertPredicate(
      this.queryParser.parse("byteValue < true"),
      "byte < ?",
      preparedStatement -> verify(preparedStatement).setBoolean(1, true)
    );

    assertPredicate(
      this.queryParser.parse("byteValue <= true"),
      "byte <= ?",
      preparedStatement -> verify(preparedStatement).setBoolean(1, true)
    );

    assertPredicate(
      this.queryParser.parse("byteValue ∈ [true, false]"),
      "byte IN (?, ?)",
      preparedStatement -> {
        verify(preparedStatement).setBoolean(1, true);
        verify(preparedStatement).setBoolean(2, false);
      }
    );
  }

  @Test
  void testParseByte() throws Exception
  {
    assertPredicate(
      this.queryParser.parse("byteValue = 1"),
      "byte = ?",
      preparedStatement -> verify(preparedStatement).setByte(1, (byte)1)
    );

    assertPredicate(
      this.queryParser.parse("byteValue != 1"),
      "NOT byte = ?",
      preparedStatement -> verify(preparedStatement).setByte(1, (byte)1)
    );

    assertPredicate(
      this.queryParser.parse("byteValue > 1"),
      "byte > ?",
      preparedStatement -> verify(preparedStatement).setByte(1, (byte)1)
    );

    assertPredicate(
      this.queryParser.parse("byteValue >= 1"),
      "byte >= ?",
      preparedStatement -> verify(preparedStatement).setByte(1, (byte)1)
    );

    assertPredicate(
      this.queryParser.parse("byteValue < 1"),
      "byte < ?",
      preparedStatement -> verify(preparedStatement).setByte(1, (byte)1)
    );

    assertPredicate(
      this.queryParser.parse("byteValue <= 1"),
      "byte <= ?",
      preparedStatement -> verify(preparedStatement).setByte(1, (byte)1)
    );

    assertPredicate(
      this.queryParser.parse("byteValue ∈ [1, 2]"),
      "byte IN (?, ?)",
      preparedStatement -> {
        verify(preparedStatement).setByte(1, (byte)1);
        verify(preparedStatement).setByte(2, (byte)2);
      }
    );
  }

  @Test
  void testParseChar() throws Exception
  {
    assertPredicate(
      this.queryParser.parse("charValue = b"),
      "char = ?",
      preparedStatement -> verify(preparedStatement).setString(1, "b")
    );

    assertPredicate(
      this.queryParser.parse("charValue != b"),
      "NOT char = ?",
      preparedStatement -> verify(preparedStatement).setString(1, "b")
    );

    assertPredicate(
      this.queryParser.parse("charValue > b"),
      "char > ?",
      preparedStatement -> verify(preparedStatement).setString(1, "b")
    );

    assertPredicate(
      this.queryParser.parse("charValue >= b"),
      "char >= ?",
      preparedStatement -> verify(preparedStatement).setString(1, "b")
    );

    assertPredicate(
      this.queryParser.parse("charValue < b"),
      "char < ?",
      preparedStatement -> verify(preparedStatement).setString(1, "b")
    );

    assertPredicate(
      this.queryParser.parse("charValue <= b"),
      "char <= ?",
      preparedStatement -> verify(preparedStatement).setString(1, "b")
    );

    assertPredicate(
      this.queryParser.parse("charValue ∈ [a, b, c]"),
      "char IN (?, ?, ?)",
      preparedStatement -> {
        verify(preparedStatement).setString(1, "a");
        verify(preparedStatement).setString(2, "b");
        verify(preparedStatement).setString(3, "c");
      }
    );
  }

  private void assertPredicate(JdbcPredicate predicate, String expectedSql, Assertion<PreparedStatement> assertion) throws Exception
  {
    assertEquals(expectedSql, predicate.toSql());

    PreparedStatement preparedStatement = mock(PreparedStatement.class);
    predicate.setValues(preparedStatement);

    assertion.run(preparedStatement);
  }

  @FunctionalInterface
  private interface Assertion<T>
  {
    void run(T value) throws Exception;
  }
}
