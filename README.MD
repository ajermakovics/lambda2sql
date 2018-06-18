Lambda2sql (lambda) -> "sql"
==========

[![](https://jitpack.io/v/ajermakovics/lambda2sql.svg)](https://jitpack.io/#ajermakovics/lambda2sql)

Convert Java 8 lambdas to SQL statements.

For example, the following Predicate<Person>:
```
 person -> person.getAge() < 100 && person.getHeight() > 200
```
 
is converted to a string:
 
```
  age < 100 AND height > 200
```
allowing you to write readable queries in a type safe way.

See [Lambda2SqlTest](https://github.com/ajermakovics/lambda2sql/blob/master/src/test/java/lambda2sql/Lambda2SqlTest.java) for more examples or the [Wiki](https://github.com/ajermakovics/lambda2sql/wiki).
	
Usage
---------

```
Lambda2Sql.init(); // initialize on program start, before predicates are created

Predicate<Person> predicate = person -> person.getAge() < 100 && person.getHeight() > 200;

String sql = Lambda2Sql.toSql( predicate ); // age < 100 AND height > 200
```


How it works
---------

It uses [JaQue](https://github.com/TrigerSoft/jaque) to build an expression tree for a lambda. The expression tree is then traversed and converted to a SQL statement. 

Under the hood JaQue depends on the following system property:
`jdk.internal.lambda.dumpProxyClasses`
See [https://bugs.openjdk.java.net/browse/JDK-8023524](https://bugs.openjdk.java.net/browse/JDK-8023524).

When the property is enabled, JVM generated classes for lambdas are saved to disk. JaQue then uses [ASM](http://asm.ow2.org/) to read the .class files and creates expression trees.


Limitations
---------

Current version only works with predicates and supports the following operators: >, >=, <, <=, =, !=, &&, ||, !

Install
-------

You can get the library using JitPack https://jitpack.io/#ajermakovics/lambda2sql/0.3

```gradle
repositories {
    maven { url "https://jitpack.io" }
}
dependencies {
    compile 'com.github.ajermakovics:lambda2sql:0.3'
}	
```

Build
---------

`gradle jar` or `gradle fatjar` to include dependencies.
