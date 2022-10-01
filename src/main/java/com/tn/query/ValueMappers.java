package com.tn.query;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.toUnmodifiableList;

import java.lang.reflect.Field;
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
  private static final String JACOCO_FIELD = "$jacocoData";

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
      .filter(field -> !JACOCO_FIELD.equals(field.getName()) && !ignored.contains(field.getName()))
      .map(toMapper(overrides))
      .filter(Objects::nonNull)
      .collect(toUnmodifiableList());
  }

  private static Function<Field, Mapper> toMapper(Map<String, Function<String, Mapper>> overrides)
  {
    return field ->
    {
      Function<String, Mapper> mapperFactory = overrides.get(field.getName());

      if (mapperFactory != null) return mapperFactory.apply(field.getName());

      if (boolean.class.equals(field.getType()) || Boolean.class.equals(field.getType())) return Mapper.toBoolean(field.getName());
      if (byte.class.equals(field.getType()) || Byte.class.equals(field.getType())) return Mapper.toByte(field.getName());
      if (char.class.equals(field.getType()) || Character.class.equals(field.getType())) return Mapper.toChar(field.getName());
      if (double.class.equals(field.getType()) || Double.class.equals(field.getType())) return Mapper.toDouble(field.getName());
      if (float.class.equals(field.getType()) || Float.class.equals(field.getType())) return Mapper.toFloat(field.getName());
      if (int.class.equals(field.getType()) || Integer.class.equals(field.getType())) return Mapper.toInt(field.getName());
      if (long.class.equals(field.getType()) || Long.class.equals(field.getType())) return Mapper.toLong(field.getName());
      if (short.class.equals(field.getType()) || Short.class.equals(field.getType())) return Mapper.toShort(field.getName());

      if (String.class.equals(field.getType())) return Mapper.toString(field.getName());
      if (Date.class.equals(field.getType())) return Mapper.toDate(field.getName());
      if (LocalDate.class.equals(field.getType())) return Mapper.toLocalDate(field.getName());
      if (LocalDateTime.class.equals(field.getType())) return Mapper.toLocalDateTime(field.getName());

      return null;
    };
  }
}
