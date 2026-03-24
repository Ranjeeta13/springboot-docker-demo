[![Spring Boot CI/CD](https://github.com/Ranjeeta13/springboot-docker-demo/actions/workflows/main.yml/badge.svg)](https://github.com/Ranjeeta13/springboot-docker-demo/actions/workflows/main.yml)

# 🚀 Spring Boot + MySQL + Kafka (Dockerized) Demo

This repository documents the **complete journey** of building a Spring Boot user service from scratch to a containerized, event-driven setup.

---

## 📖 What this project does (start → end)

1. Build a REST API for users in Spring Boot.
2. Persist data in MySQL with Spring Data JPA.
3. Package the app using Maven.
4. Dockerize the app using a `Dockerfile`.
5. Run app + MySQL together using Docker Compose.
6. Add Kafka for event-driven integration.
7. Publish a `USER_CREATED` event whenever user(s) are created.
8. Keep CI in GitHub Actions for automated build validation.

---

## 🛠 Tech Stack

- Java 21
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Spring Kafka
- MySQL 8
- Maven Wrapper (`./mvnw`)
- Docker / Docker Compose

---

## 📂 Project Structure

```text
springboot-docker-demo/
├── src/main/java/com/example/userservice/
│   ├── controller/        # REST endpoints
│   ├── dto/               # Request/Response DTOs
│   ├── entity/            # JPA entities
│   ├── kafka/             # Kafka producer(s)
│   ├── mapper/            # DTO <-> Entity mapping
│   ├── repository/        # Spring Data repositories
│   └── service/           # Business logic
├── src/main/resources/
│   ├── application.properties
│   └── application-docker.properties
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

## ⚙️ Step 1: Run locally (without Docker)

### Prerequisites
- Java 21
- Docker (for MySQL/Kafka if you don’t run them natively)

### Build

```bash
./mvnw clean package
```

### Run app

```bash
./mvnw spring-boot:run
```

API base URL:

```text
http://localhost:8080
```

Health check style endpoint from this repo:

```text
GET /hello
```

---

## 🐳 Step 2: Dockerize and run with Compose

`docker-compose.yml` starts:
- `mysql-db` (MySQL database)
- `kafka` (single-node KRaft broker)
- `spring-app` (your Spring Boot API)

### Start all services

```bash
docker compose up --build
```

### Stop all services

```bash
docker compose down
```

---

## 🗄 Database Configuration

### Local profile (`application.properties`)
- MySQL on `localhost:3306`

### Docker profile (`application-docker.properties`)
- Database host resolved by Docker DNS via environment variables

Important env vars used in compose:
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

---

## 📨 Kafka Integration (what was added)

Kafka support has been integrated end-to-end:

1. Added `spring-kafka` dependency in `pom.xml`.
2. Added a producer service: `UserEventProducer`.
3. On successful user creation in service layer, app publishes:
Topic: `user_created`
Event type: `USER_CREATED`
Payload fields: `userId`, `name`, `email`, `age`
4. Added Kafka producer properties in:
`application.properties` (local)
`application-docker.properties` (docker)
5. Added Kafka broker service in `docker-compose.yml` and wired app with:
`SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092`

---

## 🔌 API Quick Test

You can use Postman/curl to create users and trigger Kafka events.

Example create request:

```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Alice","email":"alice@example.com","age":24}'
```

When successful, the app saves to MySQL and publishes `USER_CREATED` to Kafka.

---

## 🧪 Testing & CI

### Local

```bash
./mvnw test
```

### GitHub Actions

On push, CI workflow validates build steps (compile/package pipeline).

---

## 🧠 Notes / Next Improvements

- Add `USER_DELETED` events for delete APIs.
- Introduce typed event classes instead of raw `Map` payloads.
- Add Kafka consumer example or a second microservice subscriber.
- Add integration tests using Testcontainers (MySQL + Kafka).
- Add retries / DLQ strategy for production-grade event handling.

---

## 👩‍💻 Author workflow summary

If you’re learning from this repo, the practical order is:

1. Build CRUD service.
2. Add DTO mapping and validations.
3. Connect MySQL.
4. Dockerize app.
5. Compose app + DB.
6. Add Kafka dependency + producer.
7. Publish domain events from service layer.
8. Run everything together in Docker Compose.

That gives you a clear **monolith-to-event-driven** progression in one project.