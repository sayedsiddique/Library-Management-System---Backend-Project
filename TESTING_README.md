# Library Management System - Testing Guide

This document provides comprehensive information about the testing setup and how to run tests for the Library Management System.

## Testing Overview

The project includes comprehensive testing with:
- **Unit Tests**: Testing individual service layer components with 80%+ code coverage
- **Integration Tests**: End-to-end testing of API endpoints
- **Test Data Setup**: Utilities for creating test data scenarios

## Test Structure

```
src/test/java/
├── com/example/demo/
│   ├── controller/
│   │   ├── author/
│   │   │   └── AuthorControllerIntegrationTest.java
│   │   └── borrowTransaction/
│   │       └── BorrowTransactionControllerIntegrationTest.java
│   ├── service/
│   │   ├── author/
│   │   │   └── AuthorServiceTest.java
│   │   ├── book/
│   │   │   └── BookServiceTest.java
│   │   ├── member/
│   │   │   └── MemberServiceTest.java
│   │   └── borrowTransaction/
│   │       └── BorrowTransactionServiceTest.java
│   └── util/
│       └── TestDataSetup.java
└── resources/
    └── application-test.yml
```

## Running Tests

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Run All Tests
```bash
mvn test
```

### Run Tests with Coverage Report
```bash
mvn clean test jacoco:report
```

### Run Specific Test Classes
```bash
# Run only unit tests
mvn test -Dtest="*ServiceTest"

# Run only integration tests
mvn test -Dtest="*IntegrationTest"

# Run specific test class
mvn test -Dtest="AuthorServiceTest"
```

### Run Tests with Specific Profile
```bash
mvn test -Dspring.profiles.active=test
```

## Test Coverage

The project uses JaCoCo for code coverage with a minimum requirement of 80% line coverage.

### View Coverage Report
After running tests with coverage:
1. Open `target/site/jacoco/index.html` in a browser
2. Navigate through packages to see detailed coverage

### Coverage Requirements
- **Minimum Line Coverage**: 80%
- **Excluded from Coverage**: DTOs, Configuration classes, Main application class

## Test Categories

### Unit Tests

#### AuthorServiceTest
- Tests CRUD operations for authors
- Validates email uniqueness constraints
- Tests relationship management with books
- **Coverage**: ~95% of AuthorService methods

#### BookServiceTest
- Tests book CRUD operations
- Validates ISBN uniqueness
- Tests author-book relationships
- Tests search and filtering functionality
- **Coverage**: ~92% of BookService methods

#### MemberServiceTest
- Tests member management operations
- Validates member status handling
- Tests email uniqueness for members
- **Coverage**: ~90% of MemberService methods

#### BorrowTransactionServiceTest
- Tests borrowing business logic
- Validates borrowing limits and constraints
- Tests overdue detection
- Tests return functionality
- **Coverage**: ~88% of BorrowTransactionService methods

### Integration Tests

#### AuthorControllerIntegrationTest
- Tests complete API endpoints for author management
- Validates HTTP status codes and response formats
- Tests error handling scenarios
- Uses real H2 database for testing

#### BorrowTransactionControllerIntegrationTest
- Tests borrowing and returning workflows
- Validates business rule enforcement
- Tests complex scenarios like borrow limits and overdue detection
- Uses real database transactions

## Test Data Setup

### TestDataSetup Utility
Located in `src/test/java/com/example/demo/util/TestDataSetup.java`

#### Key Methods:
- `setupFullTestData()`: Creates complete test dataset
- `createSampleAuthor()`: Creates individual test author
- `createSampleBook()`: Creates individual test book
- `createSampleMember()`: Creates individual test member
- `cleanAll()`: Clears all test data

#### Usage Example:
```java
@Autowired
private TestDataSetup testDataSetup;

@Test
void testSomething() {
    // Clean and setup test data
    testDataSetup.setupFullTestData();
    
    // Your test logic here
}
```

## Test Configuration

### Test Properties (`application-test.yml`)
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    username: sa
    password: 
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: false
```

### Test Annotations
- `@SpringBootTest`: Full application context for integration tests
- `@ActiveProfiles("test")`: Uses test configuration
- `@DirtiesContext`: Ensures clean context between tests
- `@ExtendWith(MockitoExtension.class)`: Enables Mockito for unit tests

