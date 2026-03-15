# Hibernate Entity Lifecycle with @Transactional and Kafka Events

```java
@Override
@Transactional
public UserResponse createUser(UserRequest userRequest) {
    User user = userMapper.toEntity(userRequest);
    User savedUser = userRepository.save(user);
    UserResponse response = userMapper.toResponse(savedUser);
    userEventProducer.sendUserCreatedEvent(response);
    return response;
}
```

## Step 1️⃣ — `userMapper.toEntity(userRequest)`

```java
User user = userMapper.toEntity(userRequest);
```

* **Hibernate Lifecycle:** `Transient`
* Object is new, not yet associated with persistence context.
* No DB operations yet.

## Step 2️⃣ — `userRepository.save(user)`

```java
User savedUser = userRepository.save(user);
```

* **With @Transactional:**

  * Entity becomes `Persistent / Managed`.
  * Tracked in **Persistence Context (first-level cache)**.
  * Hibernate marks for insertion; SQL executes at **flush/commit**.
  * Dirty checking ensures subsequent changes are persisted.
* **Without @Transactional:**

  * Each `save()` may trigger immediate SQL.
  * No persistence context lifecycle for subsequent changes.
  * No automatic rollback if an exception occurs.

## Step 3️⃣ — `userMapper.toResponse(savedUser)`

```java
UserResponse response = userMapper.toResponse(savedUser);
```

* Maps entity to DTO.
* Does **not affect DB or entity lifecycle**.

## Step 4️⃣ — `userEventProducer.sendUserCreatedEvent(response)`

```java
//userEventProducer.sendUserCreatedEvent(response);
```

✅ **Danger:** Sent before DB commit.

* If transaction rolls back later, Kafka event is already out → **data inconsistency**.

**Correct Pattern:** Send event after commit:

```java
/*TransactionSynchronizationManager.registerSynchronization(
    new TransactionSynchronizationAdapter() {
        @Override
        public void afterCommit() {
            userEventProducer.sendUserCreatedEvent(response);
        }
    }
);*/
```

---

## Hibernate Entity Lifecycle in This Example

| Step                                                 | Entity State         | What happens                                     |
|------------------------------------------------------|----------------------|--------------------------------------------------|
| `userMapper.toEntity()`                              | Transient            | Object created, not tracked by Hibernate         |
| `userRepository.save(user)`                          | Persistent / Managed | Tracked in persistence context; insert scheduled |
| `user.setSomething()` (if any changes before commit) | Dirty                | Hibernate detects changes automatically          |
| Transaction commits                                  | Detached             | SQL executed; entity leaves persistence context  |
| Event sent after commit                              | Detached             | Safe, entity already persisted                   |

---

## With @Transactional

* Persistence context lives for method duration.
* Dirty checking occurs automatically.
* Exceptions trigger rollback.
* Guarantees atomicity: DB + side effects must coordinate.
* Kafka event must be sent **after commit** for consistency.

## Without @Transactional

* Persistence context may be non-existent or short-lived.
* SQL may execute immediately on `save()`.
* No automatic rollback for multiple DB operations.
* Dirty checking won’t work; updates after save() may not persist.
* Kafka event becomes risky; DB consistency is not guaranteed.

---

## 🔑 Key Takeaways

* **Entity States:** Transient → Persistent → Detached → Removed
* **Persistence Context:** Tracks entities, dirty checking, flush on commit
* **Transactional Management:** Ensures atomicity of DB operations, handles rollback automatically
* **Side Effects:** Kafka / external calls must happen after commit
* **Bug in example:** Kafka event sent before commit → inconsistent data
* **Correct Usage:**

  * Keep `@Transactional` at service layer
  * Use `TransactionSynchronizationManager` for after-commit actions
