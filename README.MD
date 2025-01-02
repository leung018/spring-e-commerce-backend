# Spring E-Commerce Backend

## Introduction

This project aims to develop a Spring Boot backend prototype for e-commerce applications that allows users to create orders. It handles race conditions and transactional concerns when creating orders.

## How to Run

### Prerequisites

- Java 21
- Docker

### Running the Application Locally

```
./gradlew runLocal
```

### Running the Tests Locally

```
./gradlew testLocal
```

See `build.gradle` for more details.

## Engineering Practices

This project includes automated tests and continuous integration.

For testing, the distinction between unit tests and integration tests is not clearly defined in our current setup; some tests directly interact with the database without using fakes or mocks. We have chosen this approach because it is less complex for a smaller-scale project. Should the project expand, we can further separate the integration tests from unit tests, employing fakes or mocks for the unit tests to enhance test isolation and speed.
