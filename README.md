# tn-query

TN Query provides a safe & simple predicate string.

## Why?

RESTful web-services often provide access to domain data, which usually needs to be filtered in some way.  TN Query makes it easy to build query strings from URL query parameters 
and allows complex queries to be passed as a single parameter.

For example, given a service that returns people, you could have the following URLs:
```
https://people.somedomain.com?firstName=John

https://people.somedomain.com?q=firstName%3DJohn%26%26lastName%3DSmith
```

Note: when passing a complex query as a URL query parameter it must be escaped according to the usual URL escaping rules.

## Usage

To use, create an instance of an implementation of `com.tn.query.QueryParser` and then simply call `QueryParser.parse(String)`.

There are various implementations of `com.tn.query.QueryParser` including Java, JDBC and JPA - see each for the particulars of create their implementations of 
`com.tn.query.QueryParser`.

To create a custom implementation of `com.tn.query.QueryParser`, it is recommended `com.tn.query.AbstractQueryParser` be extended.  The typical pattern is that variable 
name-to-value-type mappers are provided; these are used to parse the string values from the query into actual values that can be evaluated when a query is executed.

`com.tn.query.AbstractQueryParser` requires methods that create the nodes in the resulting query tree be implemented.  For example, an implementation of 
`com.tn.query.AbstractQueryParser` that returns Java predicates could take the form:

```java
  public Predicate<T> equals(String left, Object right)
  {
    return target -> Objects.equals(getFieldValueFrom(target), right);
  }
```

## Implementations

* [tn-query-java](https://github.com/nickersan/tn-query-java#readme)
* [tn-query-jdbc](https://github.com/nickersan/tn-query-jdbc#readme)
* [tn-query-jpa](https://github.com/nickersan/tn-query-jpa#readme)

## Query Syntax

All queries follow the typical predicate structure of `[left] [comparison operator] [right]`.  Multiple predicates can be combined with logical operators and parenthesis used to 
group predicates according.

For example, given a service that returns people, a predicate could take the following form:
```
((firstName = John && sex = MALE) || (firstName = Jane && sex = FEMALE)) && lastName = Smith
```
### Comparison Operators
| Symbol | Description                                                                                               |
|--------|-----------------------------------------------------------------------------------------------------------|
| `=`    | True when one value is semantically equal to another; otherwise false.                                    |
| `!=`   | True when one value is semantically **not** equal to another; otherwise false.                            |
| `≈`    | True when one value is a string and like another string using `*` as a wildcard; otherwise false.         |
| `!≈`   | True when one value is a string and **not** like another string using `*` as a wildcard; otherwise false. |
| `>`    | True when one value is greater than another; otherwise false.                                             |
| `>=`   | True when one value is greater than or equals to another; otherwise false.                                |
| `<`    | True when one value is less than another; otherwise false.                                                |
| `<=`   | True when one value is less than or equals to another; otherwise false.                                   |
| `∈`    | True when one value is in another; otherwise false.                                                       |

### Logical Operators
| Symbol | Description                             |
|--------|-----------------------------------------|
| `&&`   | Performs a logic and on two predicates. |
| `\|\|` | Performs a logic or on two predicates.  |
