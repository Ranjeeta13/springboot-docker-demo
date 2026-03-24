# Spring Boot Database Interaction Guide

A practical, interview-ready guide to how a Spring Boot application interacts with databases—from low-level JDBC to high-level JPA—and how queries are generated and executed internally.

---

## Table of Contents

* [1. Ways to Interact with the Database](#1-ways-to-interact-with-the-database)
* [2. How Methods Generate Queries (Spring Data JPA)](#2-how-methods-generate-queries-spring-data-jpa)
* [3. How Hibernate Converts JPQL to SQL](#3-how-hibernate-converts-jpql-to-sql)
* [4. SQL Files for Initialization (schema.sql / data.sql)](#4-sql-files-for-initialization-schemasql--datasql)
* [5. Summary & Interview Answers](#5-summary--interview-answers)

---

## 1. Ways to Interact with the Database

### 1.1 JDBC (Java Database Connectivity)

* Lowest-level API
* Manual SQL and resource handling

```java
Connection con = dataSource.getConnection();
PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE id=?");
ps.setInt(1, 1);
ResultSet rs = ps.executeQuery();
```

**Pros**

* Full control
* Minimal abstraction overhead

**Cons**

* Boilerplate-heavy
* Manual mapping and resource management

---

### 1.2 Spring JDBC (JdbcTemplate)

* Thin abstraction over JDBC
* Handles resource management and exceptions

```java
@Autowired
JdbcTemplate jdbcTemplate;

public User getUser(int id) {
    return jdbcTemplate.queryForObject(
        "SELECT * FROM users WHERE id=?",
        new Object[]{id},
        (rs, rowNum) -> new User(rs.getInt("id"), rs.getString("name"))
    );
}
```

**Pros**

* Cleaner than raw JDBC
* Good performance

**Cons**

* Still SQL-centric

---

### 1.3 Spring Data JPA (Hibernate under the hood)

#### 1.3.1 Derived Query Methods

```java
User findByEmail(String email);
List<User> findByAgeGreaterThan(int age);
```

**Pros**

* No SQL required
* Fast to write and readable

---

#### 1.3.2 JPQL (Java Persistence Query Language)

```java
@Query("SELECT u FROM User u WHERE u.email = :email")
User findUserByEmail(@Param("email") String email);
```

**Pros**

* Works with entities (database-agnostic)

---

#### 1.3.3 Native SQL Queries

```java
@Query(value = "SELECT * FROM users WHERE email = :email", nativeQuery = true)
User findUserByEmailNative(@Param("email") String email);
```

**Pros**

* Full SQL power (DB-specific features)

---

#### 1.3.4 Pagination & Sorting

```java
Page<User> findAll(Pageable pageable);
```

---

#### 1.3.5 Specifications (Dynamic Queries)

```java
Specification<User> spec = (root, query, cb) ->
    cb.equal(root.get("email"), "test@gmail.com");
```

---

#### 1.3.6 Criteria API

```java
CriteriaBuilder cb = entityManager.getCriteriaBuilder();
CriteriaQuery<User> cq = cb.createQuery(User.class);
```

**Cons**

* Verbose

---

### 1.4 EntityManager (Low-level JPA)

```java
@PersistenceContext
EntityManager em;

User user = em.find(User.class, 1);
```

---

### 1.5 Spring Data JDBC

* Simpler alternative to JPA
* No proxies, no lazy loading

---

### 1.6 Reactive DB Access (R2DBC)

```java
Flux<User> users = databaseClient
    .sql("SELECT * FROM users")
    .fetch()
    .all();
```

---

### Summary

| Approach      | Query Style  | Complexity | Use Case         |
|---------------|--------------|------------|------------------|
| JDBC          | Raw SQL      | High       | Full control     |
| JdbcTemplate  | SQL          | Medium     | Performance apps |
| JPA Derived   | No SQL       | Very Low   | CRUD apps        |
| JPQL          | Object-based | Low        | Complex queries  |
| Native Query  | SQL          | Medium     | DB-specific      |
| Specification | Dynamic      | Medium     | Filters/search   |
| Criteria API  | Dynamic      | High       | Enterprise-level |
| EntityManager | Mixed        | Medium     | Custom control   |
| R2DBC         | Reactive     | Medium     | Async apps       |

---

## 2. How Methods Generate Queries (Spring Data JPA)

Spring Data JPA uses **query method parsing**.

### Structure

#### 2.1 Prefix (Operation)

| Prefix   | Meaning |
|----------|---------|
| findBy   | SELECT  |
| getBy    | SELECT  |
| readBy   | SELECT  |
| countBy  | COUNT   |
| existsBy | EXISTS  |
| deleteBy | DELETE  |

#### 2.2 Field Names

```java
User findByEmail(String email);
```

* Field must exist in the entity

#### 2.3 Keywords (Conditions)

| Keyword     | Meaning |
|-------------|---------|
| And         | AND     |
| Or          | OR      |
| GreaterThan | >       |
| LessThan    | <       |
| Like        | LIKE    |
| Containing  | %value% |
| Between     | BETWEEN |
| In          | IN      |

### Example

```java
List<User> findByAgeGreaterThanAndEmailContaining(int age, String email);
```

```sql
SELECT * FROM users
WHERE age > ? AND email LIKE %?%
```

### Nested Property Example

```java
List<Order> findByUserEmail(String email);
```

```sql
SELECT * FROM orders o
JOIN users u ON o.user_id = u.id
WHERE u.email = ?
```

### Common Utilities

```java
User findByEmailIgnoreCase(String email);
List<User> findTop5ByOrderByAgeDesc();
boolean existsByEmail(String email);
long countByAgeGreaterThan(int age);
```

### When to Avoid

* Very long method names
* Complex joins/logic

Use:

* `@Query` (JPQL)
* Specifications
* Criteria API

---

## 3. How Hibernate Converts JPQL to SQL

### Flow

```text
Repository Method
  → Spring Data parses method
  → Generates JPQL
  → Hibernate parses JPQL (AST)
  → Maps Entity → Table
  → Generates SQL
  → Executes via JDBC
  → Maps Result → Entity
```

### Example

```java
User findByEmail(String email);
```

**JPQL**

```sql
SELECT u FROM User u WHERE u.email = :email
```

**SQL**

```sql
SELECT * FROM users WHERE email = ?
```

### Steps

1. JPQL parsing (AST creation)
2. Entity-to-table mapping via metadata
3. SQL generation using dialect
4. Parameter binding
5. Execution via JDBC
6. ResultSet → Entity mapping

### Key Concepts

* Persistence Context (1st-level cache)
* Dialect (DB-specific SQL)
* Query plan caching

### Interview Traps

* Spring does **not** generate SQL directly
* Hibernate converts JPQL → SQL
* Hibernate uses JDBC internally

---

## 4. SQL Files for Initialization (schema.sql / data.sql)

### What It Is

* **Database initialization**, not a query API

### Configuration

```properties
spring.sql.init.mode=always
```

### Startup Flow

```text
Spring Boot → Reads SQL → Executes via JDBC
```

### Purpose

* `schema.sql` → create tables
* `data.sql` → seed data

### Key Difference

| Feature  | Purpose         |
|----------|-----------------|
| data.sql | Initialization  |
| JDBC/JPA | Runtime queries |

### Recommendation

* Use for local/dev only
* For production, use migration tools (e.g., Flyway, Liquibase)

---

## 5. Summary & Interview Answers

### Short Answer

> In Spring Boot, database interaction can be done via JDBC, JdbcTemplate, and Spring Data JPA. Spring Data JPA generates JPQL from method names, Hibernate converts JPQL to SQL using entity metadata and dialect, executes it via JDBC, and maps results back to entities.

### Decision Guide

```text
Simple query?
  → Derived methods

Moderate complexity?
  → JPQL (@Query)

Dynamic filters?
  → Specifications

DB-specific optimization?
  → Native SQL

Performance-critical / bulk?
  → JDBC / JdbcTemplate
```