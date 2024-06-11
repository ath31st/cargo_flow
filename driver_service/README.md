# Driver Service

## Overview

The Driver Service is responsible for managing tasks and trips for drivers within the system. Only
users with the role `DRIVER` can access this service. It allows for the retrieval of driver tasks,
creation of new trips within tasks, sending of trip events, and sending of trip points.

## Prerequisites

- Java 17 or higher
- Maven 3.6.3 or higher
- Docker

## Features

- Role-based Access: Only users with `DRIVER` role can access the service.
- Task Management: Retrieve tasks assigned to the driver.
- Trip Management: Create new trips within tasks.
- Trip Events: Send trip events such as trip started, and other events.
- Trip Points: Send geolocation points for trips.

## Technologies Used

- Spring Boot: Application framework
- Spring Kafka: Messaging with Kafka
- Spring Security OAuth2: Security and authorization
- Maven: Dependency management and build tool
