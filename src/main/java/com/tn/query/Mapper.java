package com.tn.query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.function.Function;

public class Mapper extends Named
{
  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-M-d");
  private static final SimpleDateFormat DATE_TIME_MINUTES_FORMAT = new SimpleDateFormat("yyyy-M-d'T'H:m");
  private static final SimpleDateFormat DATE_TIME_SECONDS_FORMAT = new SimpleDateFormat("yyyy-M-d'T'H:m:s");
  private static final SimpleDateFormat DATE_TIME_MILLISECONDS_FORMAT = new SimpleDateFormat("yyyy-M-d'T'H:m:s.S");
  private static final int MAX_DATE_LENGTH = 10;

  private static final String QUOTE_SINGLE = "'";
  private static final String QUOTE_DOUBLE = "\"";
  private static final char TIME_SEPARATOR = ':';
  private static final char MILLI_SEPARATOR = '.';
  private static final int TIME_WITH_MINUTES = 1;

  private final String type;
  private final Function<String, Object> map;

  private Mapper(String name, String type, Function<String, Object> map)
  {
    super(name);
    this.type = type;
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
    return new Mapper(name, boolean.class.getCanonicalName(), Boolean::parseBoolean);
  }

  public static Mapper toByte(String name)
  {
    return new Mapper(name, byte.class.getCanonicalName(), Byte::parseByte);
  }

  public static Mapper toChar(String name)
  {
    return new Mapper(
      name,
      char.class.getCanonicalName(),
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
      Date.class.getCanonicalName(),
      s -> isPossibleDate(s) ? parseDate(s) : parseDateTime(s)
    );
  }

  public static Mapper toDouble(String name)
  {
    return new Mapper(name, double.class.getCanonicalName(), Double::parseDouble);
  }

  public static Mapper toEnum(String name, Class<? extends Enum<?>> enumType)
  {
    return new Mapper(name, enumType.getCanonicalName(), s -> parseEnum(enumType, s));
  }

  public static Mapper toFloat(String name)
  {
    return new Mapper(name, float.class.getCanonicalName(), Float::parseFloat);
  }

  public static Mapper toInt(String name)
  {
    return new Mapper(name, int.class.getCanonicalName(), Integer::parseInt);
  }

  public static Mapper toLocalDate(String name)
  {
    return new Mapper(name, LocalDate.class.getCanonicalName(), LocalDate::parse);
  }

  public static Mapper toLocalDateTime(String name)
  {
    return new Mapper(name, LocalDateTime.class.getCanonicalName(), LocalDateTime::parse);
  }

  public static Mapper toLong(String name)
  {
    return new Mapper(name, long.class.getCanonicalName(), Long::parseLong);
  }

  public static Mapper toShort(String name)
  {
    return new Mapper(name, short.class.getCanonicalName(), Short::parseShort);
  }

  public static Mapper toString(String name)
  {
    return new Mapper(name, String.class.getCanonicalName(), Mapper::parseString);
  }

  @Override
  public boolean equals(Object other)
  {
    return this == other || (
      other != null &&
      getClass().equals(other.getClass()) &&
      Objects.equals(this.name(), ((Mapper)other).name()) &&
      Objects.equals(this.type, ((Mapper)other).type)
    );
  }

  @Override
  public int hashCode()
  {
    return this.name().hashCode();
  }

  @Override
  public String toString()
  {
    return name() + ": " + this.type;
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

  private static Enum<?> parseEnum(Class<? extends Enum<?>> enumType, String s)
  {
    for (Enum<?> enumValue : enumType.getEnumConstants())
    {
      if (enumValue.toString().equals(s))
      {
        return enumValue;
      }
    }

    throw new QueryParseException("Unable to parse enum: " + enumType.getCanonicalName() + ", value: " + s);
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

  private static String parseString(String s)
  {
    if (s == null) return null;

    if (s.startsWith(QUOTE_SINGLE)) s = s.substring(QUOTE_SINGLE.length());
    if (s.endsWith(QUOTE_SINGLE)) s = s.substring(0, s.length() - QUOTE_SINGLE.length());

    if (s.startsWith(QUOTE_DOUBLE)) s = s.substring(QUOTE_DOUBLE.length());
    if (s.endsWith(QUOTE_DOUBLE)) s = s.substring(0, s.length() - QUOTE_DOUBLE.length());

    return s;
  }
}
