# money_transfer

## What is it?

This is pplication for money transfer. This is standalone application, has api service with funtions: create account, check account, make trasnfer. Please see [api](https://github.com/oleg-sta/money_transfer/tree/master/src/main/java/ru/test/account/api)

# Table of Contests

- [What is it?](#what-is-it)
- [Settings](#settings)
- [IT tests](#it-test)

## Settings
To build application you should have:
* maven 3.3.9 or above
* java 1.8 or above

To compile:

mvn clean install

To start:

java -jar target/moneytransfer-1.0-SNAPSHOT.jar

Wait for startup.

Please see (#it-test) for checking

## IT tests

There are integration tests for manual start. For each test application should be started without any modification in data. See test in [dir](https://github.com/oleg-sta/money_transfer/tree/master/src/test/java/ru/test/account/api) classes with *IT suffix.
