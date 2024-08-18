package com.tn.query;

import static java.util.Calendar.APRIL;
import static java.util.Calendar.MILLISECOND;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.Test;

class MapperTest
{
  @Test
  void shouldMapToBoolean()
  {
    assertEquals(Boolean.TRUE, Mapper.toBoolean("test").map("true"));
    assertEquals(Boolean.FALSE, Mapper.toBoolean("test").map("false"));
    assertEquals(Boolean.FALSE, Mapper.toBoolean("test").map(null));
    assertEquals(Boolean.FALSE, Mapper.toBoolean("test").map("X"));
  }

  @Test
  void shouldMapToByte()
  {
    assertEquals((byte)1, Mapper.toByte("test").map("1"));
    assertThrows(QueryException.class, () -> Mapper.toByte("test").map("X"));
  }

  @Test
  void shouldMapToChar()
  {
    assertEquals('x', Mapper.toChar("test").map("x"));
    assertThrows(QueryException.class, () -> Mapper.toChar("test").map("XX"));
  }

  @Test
  void shouldMapToDate()
  {
    assertEquals(new GregorianCalendar(2022, APRIL, 24).getTime(), Mapper.toDate("test").map("2022-04-24"));
    assertEquals(new GregorianCalendar(2022, APRIL, 24, 18, 46).getTime(), Mapper.toDate("test").map("2022-04-24T18:46"));
    assertEquals(new GregorianCalendar(2022, APRIL, 24, 18, 46, 12).getTime(), Mapper.toDate("test").map("2022-04-24T18:46:12"));

    Calendar calendar = new GregorianCalendar(2022, APRIL, 24, 18, 46, 12);
    calendar.add(MILLISECOND, 123);
    assertEquals(calendar.getTime(), Mapper.toDate("test").map("2022-04-24T18:46:12.123"));

    assertThrows(QueryException.class, () -> Mapper.toDate("test").map("X"));
    assertThrows(QueryException.class, () -> Mapper.toDate("test").map("2022-04-24T18:46:XX"));
  }

  @Test
  void shouldMapToDouble()
  {
    assertEquals(12.34, Mapper.toDouble("test").map("12.34"));
    assertThrows(QueryException.class, () -> Mapper.toDouble("test").map("X"));
  }

  @Test
  void shouldMapToEnum()
  {
    assertEquals(Result.SUCCESS, Mapper.toEnum("test", Result.class).map("SUCCESS"));
  }

  @Test
  void shouldMapToEnumWithInvalidValue()
  {
    assertThrows(QueryException.class, () -> Mapper.toEnum("test", Result.class).map("UNKNOWN"));
  }

  @Test
  void shouldMapToFloat()
  {
    assertEquals(12.34F, Mapper.toFloat("test").map("12.34"));
    assertThrows(QueryException.class, () -> Mapper.toFloat("test").map("X"));
  }

  @Test
  void shouldMapToInt()
  {
    assertEquals(12, Mapper.toInt("test").map("12"));
    assertThrows(QueryException.class, () -> Mapper.toInt("test").map("X"));
  }

  @Test
  void shouldMapToLocalDate()
  {
    assertEquals(LocalDate.of(2022, Month.APRIL, 24), Mapper.toLocalDate("test").map("2022-04-24"));
    assertThrows(QueryException.class, () -> Mapper.toLocalDate("test").map("X"));
  }

  @Test
  void shouldMapToLocalDateTime()
  {
    assertEquals(LocalDateTime.of(2022, Month.APRIL, 24, 18, 46), Mapper.toLocalDateTime("test").map("2022-04-24T18:46"));
    assertEquals(LocalDateTime.of(2022, Month.APRIL, 24, 18, 46, 12), Mapper.toLocalDateTime("test").map("2022-04-24T18:46:12"));
    assertEquals(LocalDateTime.of(2022, Month.APRIL, 24, 18, 46, 12, 532 * 1_000_000), Mapper.toLocalDateTime("test").map("2022-04-24T18:46:12.532"));
    assertThrows(QueryException.class, () -> Mapper.toLocalDateTime("test").map("X"));
  }

  @Test
  void shouldMapToLong()
  {
    assertEquals(12L, Mapper.toLong("test").map("12"));
    assertThrows(QueryException.class, () -> Mapper.toLong("test").map("X"));
  }

  @Test
  void shouldMapToShort()
  {
    assertEquals((short)12, Mapper.toShort("test").map("12"));
    assertThrows(QueryException.class, () -> Mapper.toShort("test").map("X"));
  }

  @Test
  void shouldMapToString()
  {
    assertNull(Mapper.toString("test").map(null));
    assertEquals("test", Mapper.toString("test").map("test"));
    assertEquals("test", Mapper.toString("test").map("'test'"));
    assertEquals("test", Mapper.toString("test").map("\"test\""));
    assertEquals("test's", Mapper.toString("test").map("\"test's\""));
  }

  @Test
  void testEquality()
  {
    new EqualsTester()
      .addEqualityGroup(Mapper.toString("test"), Mapper.toString("test"))
      .addEqualityGroup(Mapper.toString("X"))
      .addEqualityGroup(Mapper.toInt("test"))
      .testEquals();
  }

  @Test
  void testToString()
  {
    assertEquals(
      "test: java.lang.String",
      Mapper.toString("test").toString()
    );
  }

  private enum Result
  {
    SUCCESS,
    FAILURE
  }
}
