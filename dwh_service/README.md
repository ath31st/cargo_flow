# DWH Service

## Overview

The DWH (Data Warehouse) Service is responsible for providing statistical data about the company's
operations. Access to this service is restricted to users with the roles `LOGIST` and `ADMIN`. It
offers the capability to retrieve statistics such as the number of completed, canceled, and started
trips since the beginning of the day, as well as the number of tasks created since the beginning of
the day. Data is requested via REST API from the Logist Service.

## Prerequisites

- Java 17 or higher
- Maven 3.6.3 or higher
- Docker

## Features

- Role-based Access: Only users with `LOGIST` or `ADMIN` roles can access the service.
- Statistical Data Retrieval: Retrieve statistics about the company's operations, including the
  number of completed, canceled, and started trips, as well as the number of tasks created since the
  beginning of the day.
- Integration with Logist Service: Data is requested from the Logist Service via REST API.

## Technologies Used

- Spring Boot: Application framework
- Spring Kafka: Messaging with Kafka
- Spring Security OAuth2: Security and authorization
- MongoDB: Database for storing statistical data
- Maven: Dependency management and build tool
