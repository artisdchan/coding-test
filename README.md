# Java Spring Boot Coding Interview Project

A ready-to-use Spring Boot project structure for coding interviews.

## 🚀 Quick Start

### Prerequisites
- Java 21 or higher
- Maven 3.6+

### Run the Application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Test the Application
```bash
curl http://localhost:8080/api/health
```

### Run Tests
```bash
mvn test
```

## 📁 Project Structure

```
src/main/java/com/interview/
├── Application.java              # Main Spring Boot application
├── config/                       # Configuration classes
│   └── WebConfig.java           # CORS and web configuration
├── controller/                   # REST API endpoints
│   └── HealthController.java    # Health check endpoint
├── service/                      # Business logic layer
├── repository/                   # Data access layer
├── model/                        # Entity classes
│   └── BaseEntity.java          # Base entity with common fields
├── dto/                         # Data Transfer Objects
│   └── ApiResponse.java         # Standard API response wrapper
└── exception/                    # Exception handling
    ├── ResourceNotFoundException.java
    └── GlobalExceptionHandler.java
```

## 🛠️ Tech Stack

- **Java 21**
- **Spring Boot 3.2.1**
- **Spring Web** - REST API
- **Spring Data JPA** - Database access
- **Spring Validation** - Input validation
- **H2 Database** - In-memory database
- **Lombok** - Reduce boilerplate code
- **JUnit 5** - Testing

## 📊 H2 Database Console

Access the H2 console at: `http://localhost:8080/h2-console`

**Connection Details:**
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: _(leave empty)_
