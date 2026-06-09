# CRM Ticketing API

A Spring Boot REST API for managing CRM tickets, agents, and comments.

## Tech Stack

- Java 21
- Spring Boot 3
- Hibernate ORM
- SessionFactory
- HikariCP Connection Pool
- MySQL
- Lombok
- Swagger / OpenAPI
- SLF4J
- Logback
- Maven

---

## Project Architecture

```
Controller
    ↓
Service
    ↓
DAO
    ↓
Hibernate SessionFactory
    ↓
HikariCP
    ↓
MySQL Database
```

---

## Project Structure

```
src/main/java/com/arpit/crm_ticketing_api

├── config
│   ├── DataSourceConfig
│   └── HibernateConfig
│
├── controller
│   ├── AgentController
│   ├── TicketController
│   └── CommentController
│
├── dao
│   ├── AgentDao
│   ├── TicketDao
│   └── CommentDao
│
├── dto
│   ├── AgentRequest
│   ├── AgentResponse
│   ├── TicketRequest
│   ├── TicketResponse
│   ├── CommentRequest
│   └── CommentResponse
│
├── entity
│   ├── Agent
│   ├── Ticket
│   └── Comment
│
├── enums
│   ├── Department
│   ├── Priority
│   └── TicketStatus
│
├── exception
│   └── ResourceNotFoundException
│
├── service
│   ├── AgentService
│   ├── TicketService
│   └── CommentService
│
└── CrmTicketingApiApplication
```

---

## Features

### Agent Management

- Create Agent
- Get Agent By Id
- Get All Agents
- Update Agent
- Delete Agent

### Ticket Management

- Create Ticket
- Get Ticket By Id
- Get All Tickets
- Update Ticket
- Delete Ticket

### Comment Management

- Create Comment
- Get Comment By Id
- Get All Comments
- Delete Comment

---

## Concepts Implemented

### 1. Enums

Used for:

- Department
- Priority
- TicketStatus

Example:

```java
@Enumerated(EnumType.STRING)
private TicketStatus status;
```

---

### 2. Validation

Request DTO validations using Jakarta Validation.

Examples:

```java
@NotBlank
@NotNull
@Email
```

---

### 3. Service Layer

Business logic is separated from controllers.

Example:

```java
AgentController
    ↓
AgentService
    ↓
AgentDao
```

---

### 4. Swagger Documentation

Swagger UI available at:

```
http://localhost:8080/swagger-ui/index.html
```

Used for API testing and documentation.

---

### 5. Logging

Implemented using:

- SLF4J
- Logback

Example:

```java
private static final Logger logger =
        LoggerFactory.getLogger(TicketService.class);
```

Configuration:

```
src/main/resources/logback.xml
```

---

### 6. Database Indexes

Indexes added for faster queries.

Example:

```java
@Table(
    name = "tickets",
    indexes = {
        @Index(name = "idx_ticket_status",
               columnList = "status"),
        @Index(name = "idx_ticket_priority",
               columnList = "priority")
    }
)
```

---

### 7. Hibernate

Used Hibernate SessionFactory instead of Spring Data JPA repositories.

Example:

```java
Session session =
        sessionFactory.getCurrentSession();
```

---

### 8. HikariCP

Used for database connection pooling.

Configured in:

```java
DataSourceConfig
```

Benefits:

- Faster database access
- Connection reuse
- Better performance

---

### 9. Lombok

Used to reduce boilerplate code.

Annotations used:

```java
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
```

---

### 10. Constructor Injection

Used constructor injection with Lombok.

Example:

```java
@RequiredArgsConstructor
@Service
public class AgentService {

    private final AgentDao agentDao;
}
```

No field injection using `@Autowired`.

---

## Database Configuration

Update `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/crm_ticketing_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

---

## Running the Application

### Clone Repository

```bash
git clone https://github.com/arpit315/arpit_crm_ticket_api.git
```

### Run Application

```bash
mvn spring-boot:run
```

or run:

```
CrmTicketingApiApplication.java
```

from IntelliJ.

---

## API Endpoints

### Agents

| Method | Endpoint |
|----------|----------|
| POST | /api/agents |
| GET | /api/agents |
| GET | /api/agents/{id} |
| PUT | /api/agents/{id} |
| DELETE | /api/agents/{id} |

### Tickets

| Method | Endpoint |
|----------|----------|
| POST | /api/tickets |
| GET | /api/tickets |
| GET | /api/tickets/{id} |
| PUT | /api/tickets/{id} |
| DELETE | /api/tickets/{id} |

### Comments

| Method | Endpoint |
|----------|----------|
| POST | /api/comments |
| GET | /api/comments |
| GET | /api/comments/{id} |
| DELETE | /api/comments/{id} |

---

## Author

**Arpit Kumar**

B.Tech Computer Science Engineering  
Lovely Professional University
