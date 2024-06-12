# Cargo flow

## Transport automation system (training microservice project)

### Versions:

- Java: 17</br>
- Spring Boot: 3.2.5</br>
- Spring Cloud Netflix – Eureka: 4.1.1</br>
- OAuth 2.0 Resource Server: 6.2.4</br>
- Keycloak: 22.0.0</br>
- Maven: 3.9.6</br>
- Kafka: 3.1.4</br>
- PostgresQL: 42.6.2</br>
- Lombok: 1.18.32</br>
- Flyway: 9.22.3</br>

### Modules:

#### Discovery Service:

- Responsible for service discovery and registration.
- Implemented using Spring Cloud Netflix Eureka.

#### Gateway Service:

- Acts as an API gateway for routing requests.
- Implements routing logic based on service discovery.
- Handles user access levels based on OAuth 2.0 tokens from Keycloak.

#### Logist Service:

- Manages logistics tasks, trips, and events.
- Accessible only to users with the role LOGIST.
- Utilizes Kafka or RabbitMQ for trip event handling.
- Tracks vehicle geolocation points during trips.

#### Driver Service:

- Handles tasks related to drivers.
- Accessible only to users with the role DRIVER.
- Allows retrieval of task data for drivers and creation of new trips within tasks.
- Handles trip events and geolocation points.

#### DWH Service:

- Data Warehouse service for generating company-wide statistics.
- Accessible to users with roles LOGIST and ADMIN.
- Provides statistics on completed, canceled, and started trips since the beginning of the day, as
  well as the number of tasks.
- Retrieves data via REST from the Logist Service.