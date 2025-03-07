package com.tn.query;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

public class ValueMappers
{
  private ValueMappers() {}

  public static List<Mapper> forFields(Class<?> subject)
  {
    return forFields(subject, emptyList(), emptyMap());
  }

  public static List<Mapper> forFields(Class<?> subject, Collection<String> ignored)
  {
    return forFields(subject, ignored, emptyMap());
  }

  public static List<Mapper> forFields(Class<?> subject, Map<String, Function<String, Mapper>> overrides)
  {
    return forFields(subject, emptyList(), overrides);
  }

  public static List<Mapper> forFields(Class<?> subject, Collection<String> ignored, Map<String, Function<String, Mapper>> overrides)
  {
    return Stream.of(subject.getDeclaredFields())
      .filter(field -> !ignored.contains(field.getName()) && !field.isSynthetic())
      .map(field -> new Field(field.getName(), field.getType()))
      .map(toMapper(overrides))
      .filter(Objects::nonNull)
      .toList();
  }

  public static List<Mapper> forFields(Collection<Field> fields)
  {
    return forFields(fields, emptyMap());
  }

  public static List<Mapper> forFields(Collection<Field> fields, Map<String, Function<String, Mapper>> overrides)
  {
    return fields.stream()
      .map(toMapper(overrides))
      .filter(Objects::nonNull)
      .toList();
  }

  private static Function<Field, Mapper> toMapper(Map<String, Function<String, Mapper>> overrides)
  {
    return field ->
    {
      Function<String, Mapper> mapperFactory = overrides.get(field.name());

      if (mapperFactory != null) return mapperFactory.apply(field.name());

      if (boolean.class.equals(field.type()) || Boolean.class.equals(field.type())) return Mapper.toBoolean(field.name());
      if (byte.class.equals(field.type()) || Byte.class.equals(field.type())) return Mapper.toByte(field.name());
      if (char.class.equals(field.type()) || Character.class.equals(field.type())) return Mapper.toChar(field.name());
      if (double.class.equals(field.type()) || Double.class.equals(field.type())) return Mapper.toDouble(field.name());
      if (float.class.equals(field.type()) || Float.class.equals(field.type())) return Mapper.toFloat(field.name());
      if (int.class.equals(field.type()) || Integer.class.equals(field.type())) return Mapper.toInt(field.name());
      if (long.class.equals(field.type()) || Long.class.equals(field.type())) return Mapper.toLong(field.name());
      if (short.class.equals(field.type()) || Short.class.equals(field.type())) return Mapper.toShort(field.name());

      if (String.class.equals(field.type())) return Mapper.toString(field.name());
      if (Date.class.equals(field.type())) return Mapper.toDate(field.name());
      if (LocalDate.class.equals(field.type())) return Mapper.toLocalDate(field.name());
      if (LocalDateTime.class.equals(field.type())) return Mapper.toLocalDateTime(field.name());

      return null;
    };
  }

  public record Field(String name, Class<?> type) {}
}
