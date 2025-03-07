package com.tn.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

class ValueMappersTest
{
  @Test
  void shouldGetValueMappersFromClass()
  {
    assertEquals(
      List.of(
        Mapper.toBoolean("boolean1"),
        Mapper.toBoolean("boolean2"),
        Mapper.toByte("byte1"),
        Mapper.toByte("byte2"),
        Mapper.toChar("char1"),
        Mapper.toChar("char2"),
        Mapper.toDouble("double1"),
        Mapper.toDouble("double2"),
        Mapper.toFloat("float1"),
        Mapper.toFloat("float2"),
        Mapper.toInt("int1"),
        Mapper.toInt("int2"),
        Mapper.toLong("long1"),
        Mapper.toLong("long2"),
        Mapper.toShort("short1"),
        Mapper.toShort("short2"),
        Mapper.toDate("date"),
        Mapper.toLocalDate("localDate"),
        Mapper.toLocalDateTime("localDateTime"),
        Mapper.toString("s")
      ),
      ValueMappers.forFields(Subject.class)
    );
  }

  @Test
  void shouldGetValueMappersFromClassExcludingIgnored()
  {
    assertEquals(
      List.of(
        Mapper.toBoolean("boolean1"),
        Mapper.toByte("byte1"),
        Mapper.toByte("byte2"),
        Mapper.toChar("char1"),
        Mapper.toChar("char2"),
        Mapper.toDouble("double1"),
        Mapper.toDouble("double2"),
        Mapper.toFloat("float1"),
        Mapper.toFloat("float2"),
        Mapper.toInt("int1"),
        Mapper.toInt("int2"),
        Mapper.toLong("long1"),
        Mapper.toLong("long2"),
        Mapper.toShort("short1"),
        Mapper.toShort("short2"),
        Mapper.toDate("date"),
        Mapper.toLocalDate("localDate"),
        Mapper.toLocalDateTime("localDateTime"),
        Mapper.toString("s")
      ),
      ValueMappers.forFields(Subject.class, List.of("boolean2"))
    );
  }

  @Test
  void shouldGetValueMappersFromClassWithOverride()
  {
    Mapper mapper = mock(Mapper.class);

    @SuppressWarnings("unchecked")
    Function<String, Mapper> mapperFactory = mock(Function.class);
    when(mapperFactory.apply("boolean2")).thenReturn(mapper);

    assertEquals(
      List.of(
        Mapper.toBoolean("boolean1"),
        mapper,
        Mapper.toByte("byte1"),
        Mapper.toByte("byte2"),
        Mapper.toChar("char1"),
        Mapper.toChar("char2"),
        Mapper.toDouble("double1"),
        Mapper.toDouble("double2"),
        Mapper.toFloat("float1"),
        Mapper.toFloat("float2"),
        Mapper.toInt("int1"),
        Mapper.toInt("int2"),
        Mapper.toLong("long1"),
        Mapper.toLong("long2"),
        Mapper.toShort("short1"),
        Mapper.toShort("short2"),
        Mapper.toDate("date"),
        Mapper.toLocalDate("localDate"),
        Mapper.toLocalDateTime("localDateTime"),
        Mapper.toString("s")
      ),
      ValueMappers.forFields(Subject.class, Map.of("boolean2", mapperFactory))
    );
  }

  @Test
  void shouldGetValueMappersFromClassExcludingIgnoredWithOverride()
  {
    Mapper mapper = mock(Mapper.class);

    @SuppressWarnings("unchecked")
    Function<String, Mapper> mapperFactory = mock(Function.class);
    when(mapperFactory.apply("boolean1")).thenReturn(mapper);

    assertEquals(
      List.of(
        mapper,
        Mapper.toByte("byte1"),
        Mapper.toByte("byte2"),
        Mapper.toChar("char1"),
        Mapper.toChar("char2"),
        Mapper.toDouble("double1"),
        Mapper.toDouble("double2"),
        Mapper.toFloat("float1"),
        Mapper.toFloat("float2"),
        Mapper.toInt("int1"),
        Mapper.toInt("int2"),
        Mapper.toLong("long1"),
        Mapper.toLong("long2"),
        Mapper.toShort("short1"),
        Mapper.toShort("short2"),
        Mapper.toDate("date"),
        Mapper.toLocalDate("localDate"),
        Mapper.toLocalDateTime("localDateTime"),
        Mapper.toString("s")
      ),
      ValueMappers.forFields(Subject.class, List.of("boolean2"), Map.of("boolean1", mapperFactory))
    );
  }

  @Test
  void shouldGetValueMappersFromFields()
  {
    assertEquals(
      List.of(
        Mapper.toBoolean("boolean1"),
        Mapper.toBoolean("boolean2"),
        Mapper.toByte("byte1"),
        Mapper.toByte("byte2"),
        Mapper.toChar("char1"),
        Mapper.toChar("char2"),
        Mapper.toDouble("double1"),
        Mapper.toDouble("double2"),
        Mapper.toFloat("float1"),
        Mapper.toFloat("float2"),
        Mapper.toInt("int1"),
        Mapper.toInt("int2"),
        Mapper.toLong("long1"),
        Mapper.toLong("long2"),
        Mapper.toShort("short1"),
        Mapper.toShort("short2"),
        Mapper.toDate("date"),
        Mapper.toLocalDate("localDate"),
        Mapper.toLocalDateTime("localDateTime"),
        Mapper.toString("s")
      ),
      ValueMappers.forFields(
        List.of(
          new ValueMappers.Field("boolean1", boolean.class),
          new ValueMappers.Field("boolean2", Boolean.class),
          new ValueMappers.Field("byte1", byte.class),
          new ValueMappers.Field("byte2", Byte.class),
          new ValueMappers.Field("char1", char.class),
          new ValueMappers.Field("char2", Character.class),
          new ValueMappers.Field("double1", double.class),
          new ValueMappers.Field("double2", Double.class),
          new ValueMappers.Field("float1", float.class),
          new ValueMappers.Field("float2", Float.class),
          new ValueMappers.Field("int1", int.class),
          new ValueMappers.Field("int2", Integer.class),
          new ValueMappers.Field("long1", long.class),
          new ValueMappers.Field("long2", Long.class),
          new ValueMappers.Field("short1", short.class),
          new ValueMappers.Field("short2", Short.class),
          new ValueMappers.Field("date", Date.class),
          new ValueMappers.Field("localDate", LocalDate.class),
          new ValueMappers.Field("localDateTime", LocalDateTime.class),
          new ValueMappers.Field("s", String.class)
        )
      )
    );
  }

  @SuppressWarnings("unused")
  private static class Subject
  {
    private boolean boolean1;
    private Boolean boolean2;
    private byte byte1;
    private Byte byte2;
    private char char1;
    private Character char2;
    private double double1;
    private Double double2;
    private float float1;
    private Float float2;
    private int int1;
    private Integer int2;
    private long long1;
    private Long long2;
    private short short1;
    private Short short2;
    private Date date;
    private LocalDate localDate;
    private LocalDateTime localDateTime;
    private String s;
  }
}
