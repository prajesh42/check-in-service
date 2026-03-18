# Check-In Service

Check-In Service is a Spring Boot backend for recording factory employee check-in/check-out events, publishing event messages to Kafka, notifying employees by email, and syncing worked hours to a legacy system.

## Overview

This service exposes a single API endpoint that toggles employee status between `CHECKED_IN` and `CHECKED_OUT`.

1. Employee check-in/check-out is persisted in PostgreSQL.
2. A check-in event is published to Kafka.
3. For `CHECKED_OUT` events, the consumer sends an email with tracked hours.
4. For `CHECKED_OUT` events, the consumer syncs hours to a legacy API.
5. If event processing fails, the payload is stored for investigation.

## Architecture

- API Layer: `CheckInController`
- Domain/Business Logic: `CheckInServiceImpl`
- Persistence: Spring Data JPA repositories (`EMPLOYEE`, `CHECK_IN_DETAILS`, `CHECK_IN_EVENT`)
- Messaging: Kafka producer/consumer on topic `checkedin-event`
- Integration: Legacy REST endpoint + email sender
- Resilience: Resilience4j (`CircuitBreaker`, `Retry`, `RateLimiter`) on legacy integration

## Technology Stack

- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Spring Kafka
- Spring Mail
- Resilience4j
- PostgreSQL
- Apache Kafka + Zookeeper
- Maven

## Project Structure

```text
src/main/java/com/siemens
  |- controller
  |- service
  |- kafka
  |- repo
  |- dto
  |- model
  |- config
  `- CheckInServiceApplication.java
```

## Prerequisites

- JDK 17+
- Maven 3.9+
- PostgreSQL running on `localhost:5432`
- Kafka and Zookeeper (or use provided `docker-compose.yml`)

## Quick Start

1. Start Kafka and Zookeeper:

```bash
docker compose up -d
```

2. Create the PostgreSQL database:

```sql
CREATE DATABASE checkin_db;
```

3. Update credentials in `src/main/resources/application.properties`:

- `spring.datasource.username`
- `spring.datasource.password`
- `spring.mail.username`
- `spring.mail.password`

4. Start the service:

```bash
mvn spring-boot:run
```

5. Trigger check-in/check-out:

```bash
curl -X GET "http://localhost:8080/check-in" -H "employeeId: EMP001"
```

## API

### `GET /check-in`

Records employee check-in/check-out based on current state.

- Header: `employeeId` (required)
- Response: `202 Accepted`

## Kafka Event Flow

- Produced topic: `checkedin-event`
- Consumer group: `kafkacheckinevent`
- Event type values: `CHECKED_IN`, `CHECKED_OUT`

Example event payload:

```json
{
  "type": "CHECKED_OUT",
  "relationId": "f3f4fbe0-5e53-4c61-8a70-9ec7ac3ad7e3",
  "employeeId": "EMP001",
  "employeeEmail": "employee@company.com",
  "hours": 8.5,
  "occuredAt": "2026-03-18T21:15:30.123+01:00"
}
```

## Configuration

Key properties are managed in `src/main/resources/application.properties`:

- Application: `spring.application.name`, `app.checkedin.event`
- DataSource: `spring.datasource.*`
- JPA/Hibernate: `spring.jpa.*`
- Kafka: `spring.kafka.*`
- SMTP Mail: `spring.mail.*`
- Resilience4j: `resilience4j.circuitbreaker.*`, `resilience4j.retry.*`, `resilience4j.ratelimiter.*`

## Operational Notes

- The service expects employee records to exist in the `EMPLOYEE` table before check-in requests.
- Legacy-system processing failures are persisted in `CHECK_IN_EVENT` as failed payloads.
- Current `docker-compose.yml` provisions Kafka and Zookeeper only; PostgreSQL is managed separately.
