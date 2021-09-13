package com.tn.query;

public interface QueryParser<T>
{
  T parse(String s) throws QueryParseException;
}
