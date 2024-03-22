# Tool Rental Application

The Tool Rental Application is a Java Spring Boot application designed to manage tools and rental agreements for a tool rental service.

## Overview

The application provides functionalities such as tool management, tool checkout, exception handling, and unit testing.
API documentation will be available at http://localhost:8080/swagger-ui/index.html .

## Prerequisites
- Java 17
- Maven 
- MySQL


## Installation and Setup

To run the Tool Rental Application locally:

1. Clone the repository. <br>
```bash
git clone <repository_url>
```
2. Build the project using Maven.
```bash
cd ToolRentalApplication
mvn clean install
```
3. Run the application using Maven or your preferred IDE.
```bash
mvn spring-boot:run
```
4. Access the application via `http://localhost:8080`.

## Usage

### API Endpoints

- Create Tool: `POST /tools`
- Get Tool by Code: `GET /tools/{toolCode}`
- Checkout Tool: `POST /tools/checkout`

### Swagger UI

Access the Swagger UI at `http://localhost:8080/swagger-ui.html`.

### Example Requests

#### Create Tool

```http
POST /tools
Content-Type: application/json

{
  "toolCode": "CHNS",
  "toolTypeCharges": {
    "toolType": "CHAINSAW",
    "dailyCharge": 1.99,
    "weekdayCharge": true,
    "weekendCharge": false,
    "holidayCharge": true
  },
  "brand": "Stihl"
}
```
#### Checkout Tool
```
POST /tools/checkout
Content-Type: application/json

{
  "toolCode": "CHNS",
  "rentalDays": 5,
  "discountPercent": 10,
  "checkoutDate": "2023-09-03"
}
```

## Testing

The application includes unit tests to validate its functionality. To run the tests, use Maven:

```bash
mvn test
```

## Dependencies

The Tool Rental Application relies on the following dependencies:

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [MySQL Connector](https://dev.mysql.com/downloads/connector/j/)
- [Lombok](https://projectlombok.org/)
- [Springdoc OpenAPI](https://springdoc.org/)
- [Flyway](https://documentation.red-gate.com/flyway/flyway-cli-and-api/concepts/migrations)
