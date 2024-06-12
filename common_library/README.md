# Common Library

## Overview

The Common Library provides shared dependencies and utilities for other projects within the system.

## Prerequisites

- Java 17 or higher
- Maven 3.6.3 or higher

## Features

- Lombok: Library for reducing boilerplate code
- Spring Security OAuth2 Jose: Dependency for OAuth2 support
- Apache Tomcat Embed Core: Embedded Tomcat for web applications
- Spring Data Commons: Common abstractions and utilities for Spring Data
- Jackson Annotations and Databind: JSON serialization and deserialization

## Technologies Used

- Lombok
- Spring Security OAuth2 Jose
- Apache Tomcat Embed Core
- Spring Data Commons
- Jackson Annotations
- Jackson Databind

## Installation

The Common Library is intended to be included as a dependency in other projects. It does not require
separate installation.

## Usage

To use the Common Library, include it as a dependency in your project's `pom.xml` file:

```xml

<dependency>
    <groupId>ru.gnivc.common</groupId>
    <artifactId>common</artifactId>
    <version>1.0.0</version>
</dependency>
```
