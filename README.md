# tn-query

TN Query provides a safe & simple predicate string.

## Usage

To use, create an instance of an implementation of `com.tn.query.QueryParser` and then simply call `QueryParser.parse(String)`.

There are various implementations of `com.tn.query.QueryParser` including Java, JDBC and JPA - see each for the particulars of create their implementations of 
`com.tn.query.QueryParser`.

To create a custom implementation of `com.tn.query.QueryParser`, it is recommended `com.tn.query.AbstractQueryParser` be extended.  The typical pattern is that variable 
name-to-value-type mappers are provided; these are used to parse the string values from the query into actual values that can be evaluated when a query is executed.

`com.tn.query.AbstractQueryParser` requires methods that create the nodes in the resulting query tree be implemented.  For example, an implementation of 
`com.tn.query.AbstractQueryParser` that returns Java predicates could take the form:

```
  public Predicate<T> equals(String left, Object right)
  {
    return target -> Objects.equals(getFieldValueFrom(target), right);
  }
```

## Query Syntax

### Logical Operators

### Boolean Operators



## Build