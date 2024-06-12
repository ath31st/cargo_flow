# Cargo Flow Spring Boot Starter

## Overview

The Cargo Flow Spring Boot Starter is a starter project for bootstrapping Spring Boot applications
within the Cargo Flow ecosystem. It provides common dependencies and configurations to streamline
the development process.

## Prerequisites

- Java 17 or higher
- Maven 3.6.3 or higher

## Features

- Spring Boot Starter: Bootstrap Spring Boot applications
- Spring Cloud Commons: Common abstractions and utilities for Spring Cloud
- Common Library: Shared dependencies and utilities for Cargo Flow projects
- Lombok: Library for reducing boilerplate code
- Jackson Databind: JSON serialization and deserialization
- Spring Web MVC: Web application framework for building web applications
- Jakarta Validation API: API for bean validation

## Technologies Used

- Spring Boot
- Spring Cloud Commons
- Common Library
- Lombok
- Jackson Databind
- Spring Web MVC
- Jakarta Validation API

## Installation

The Cargo Flow Spring Boot Starter is intended to be included as a dependency in Spring Boot
projects. It provides common dependencies and configurations, reducing the setup overhead.

To use the Cargo Flow Spring Boot Starter, include it as a dependency in your project's `pom.xml`
file:

```xml

<dependency>
    <groupId>ru.gnivc.starter</groupId>
    <artifactId>cargo-flow-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```