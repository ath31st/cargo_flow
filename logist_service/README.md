# Logist Service

## Overview

The Logist Service is responsible for managing logistics tasks and trips within the system. Only
users with the role `LOGIST` can access this service. It allows for the creation and viewing of
tasks and trips, manages trip events via Kafka or RabbitMQ, and tracks vehicle geolocation points
during trips.

## Prerequisites

- Java 17 or higher
- Maven 3.6.3 or higher
- Docker
- PostgreSQL

## Features

- Role-based Access: Only users with LOGIST role can access the service.
- Task Management: Create and view logistics tasks, including start and end points, driver
  information, cargo description, and vehicle info.
- Trip Management: Create and view trips associated with tasks, with pagination support.
- Trip Events: Handle events such as trip creation, start, end, cancellation, breakdowns, and
  accidents via Kafka or RabbitMQ.
- Geolocation Tracking: Store and retrieve vehicle geolocation points during trips via Kafka.

## Technologies Used

- Spring Boot: Application framework
- Spring Data JPA: Data access
- Spring Kafka: Messaging with Kafka
- Spring Security OAuth2: Security and authorization
- PostgreSQL: Database
- Flyway: Database migration
- Maven: Dependency management and build tool