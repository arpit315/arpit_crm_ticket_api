# CRM Ticketing API

A Spring Boot 3 CRM Ticketing API built using Hibernate ORM, MySQL, Apache Kafka, Redis, Caffeine Cache, and a custom LRU Cache implementation.

## Features

### Ticket Management

* Create Ticket
* Update Ticket
* Delete Ticket
* Get Ticket By ID
* Get All Tickets

### Agent Management

* Create Agent
* Update Agent
* Delete Agent
* Get Agent By ID
* Get All Agents

### Comment Management

* Create Comment
* Update Comment
* Delete Comment
* Get Comment By ID
* Get All Comments

---

## Event Driven Architecture (Kafka)

The application publishes ticket events to Kafka whenever a ticket is created or updated.

### Kafka Producer

Publishes events to:

```text
ticket-topic
```

### Kafka Consumer

Consumes events from:

```text
ticket-topic
```

and stores them in the Ticket History table.

---

## Ticket History Tracking

A dedicated history table stores all ticket events consumed from Kafka.

### Stored Information

* Ticket ID
* Action (CREATE / UPDATE)
* Title
* Status
* Priority
* Assigned Agent ID
* Event Timestamp

This provides a complete audit trail of ticket activity.

---

## Caching Strategy

The application implements multiple caching strategies.

### Redis Cache

Used for Ticket fetch operations.

#### Cache Flow

```text
Redis Cache
    ↓ miss
DAO
    ↓
Redis Cache Update
```

#### Applied On

* TicketService.findById()
* TicketService.create()
* TicketService.update()
* TicketService.delete()

---

### Caffeine Cache

Used for fetching all tickets.

#### Applied On

```java
TicketService.findAll()
```

#### Cache Flow

```text
Caffeine Cache
    ↓ miss
DAO
```

---

### Custom LRU Cache

A custom Least Recently Used (LRU) cache implementation is used for Agent fetch operations.

#### Applied On

```java
AgentService.findById()
```

#### Cache Flow

```text
LRU Cache
    ↓ miss
DAO
```

---

## Technology Stack

### Backend

* Java 21
* Spring Boot 3
* Hibernate ORM
* MySQL

### Messaging

* Apache Kafka

### Caching

* Redis
* Caffeine
* Custom LRU Cache

### Documentation

* Swagger / OpenAPI

### Build Tool

* Maven

---

## Project Structure

```text
src/main/java/com/arpit/crm_ticketing_api

├── cache
│   ├── CacheConfig
│   ├── LruCache
│   └── RedisCacheService
│
├── config
│   ├── DataSourceConfig
│   ├── HibernateConfig
│   └── RedisConfig
│
├── controller
│
├── dao
│   ├── AgentDao
│   ├── CommentDao
│   ├── TicketDao
│   └── TicketHistoryDao
│
├── dto
│
├── entity
│   ├── Agent
│   ├── Comment
│   ├── Ticket
│   └── TicketHistory
│
├── kafka
│   ├── producer
│   │   └── TicketProducer
│   └── consumer
│       └── TicketConsumer
│
├── service
│
└── exception
```

---

## API Documentation

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

OpenAPI Docs:

```text
http://localhost:8080/v3/api-docs
```

---

## Future Enhancements

* Redis Cluster Support
* Kafka Dead Letter Queue (DLQ)
* Distributed Tracing
* Metrics and Monitoring
* Spring Security & JWT Authentication
