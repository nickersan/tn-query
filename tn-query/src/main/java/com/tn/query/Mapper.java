package com.tn.query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.function.Function;

public class Mapper extends Named
{
  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-M-d");
  private static final SimpleDateFormat DATE_TIME_MINUTES_FORMAT = new SimpleDateFormat("yyyy-M-d'T'H:m");
  private static final SimpleDateFormat DATE_TIME_SECONDS_FORMAT = new SimpleDateFormat("yyyy-M-d'T'H:m:s");
  private static final SimpleDateFormat DATE_TIME_MILLISECONDS_FORMAT = new SimpleDateFormat("yyyy-M-d'T'H:m:s.S");
  private static final int MAX_DATE_LENGTH = 10;
  private static final char TIME_SEPARATOR = ':';
  private static final char MILLI_SEPARATOR = '.';
  private static final int TIME_WITH_MINUTES = 1;

  private final Function<String, Object> map;

  private Mapper(String name, Function<String, Object> map)
  {
    super(name);
    this.map = map;
  }

  public Object map(String object)
  {
    try
    {
      return this.map.apply(object);
    }
    catch (Exception e)
    {
      throw e instanceof QueryException ? (QueryException)e : new QueryException("Failed to map: " + this.name() + ", value: " + object, e);
    }
  }

  public static Mapper toBoolean(String name)
  {
    return new Mapper(name, Boolean::parseBoolean);
  }

  public static Mapper toByte(String name)
  {
    return new Mapper(name, Byte::parseByte);
  }

  public static Mapper toChar(String name)
  {
    return new Mapper(
      name,
      s -> {
        if (s.length() != 1) throw new QueryException("Failed to map: " + name + ", value: " + s);
        return s.charAt(0);
      }
    );
  }

  public static Mapper toDate(String name)
  {
    return new Mapper(
      name,
      s -> isPossibleDate(s) ? parseDate(s) : parseDateTime(s)
    );
  }

  public static Mapper toDouble(String name)
  {
    return new Mapper(name, Double::parseDouble);
  }

  public static Mapper toFloat(String name)
  {
    return new Mapper(name, Float::parseFloat);
  }

  public static Mapper toInt(String name)
  {
    return new Mapper(name, Integer::parseInt);
  }

  public static Mapper toLocalDate(String name)
  {
    return new Mapper(name, LocalDate::parse);
  }

  public static Mapper toLocalDateTime(String name)
  {
    return new Mapper(name, LocalDateTime::parse);
  }

  public static Mapper toLong(String name)
  {
    return new Mapper(name, Long::parseLong);
  }

  public static Mapper toShort(String name)
  {
    return new Mapper(name, Short::parseShort);
  }

  private static boolean isPossibleDate(String s)
  {
    return s.length() <= MAX_DATE_LENGTH;
  }

  private static Date parseDate(String s)
  {
    try
    {
      return DATE_FORMAT.parse(s);
    }
    catch (ParseException e)
    {
      throw new QueryParseException("Invalid date: " + s, e);
    }
  }

  private static Date parseDateTime(String s)
  {
    try
    {
      if (s.chars().filter(c -> c == TIME_SEPARATOR).count() == TIME_WITH_MINUTES)
      {
        return DATE_TIME_MINUTES_FORMAT.parse(s);
      }
      else
      {
        return s.indexOf(MILLI_SEPARATOR) > -1 ? DATE_TIME_MILLISECONDS_FORMAT.parse(s) : DATE_TIME_SECONDS_FORMAT.parse(s);
      }
    }
    catch (ParseException e)
    {
      throw new QueryParseException("Invalid date-time: " + s, e);
    }
  }
}
