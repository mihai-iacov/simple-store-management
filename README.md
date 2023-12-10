# simple-store-management
The Simple Store Management is a sample Spring Boot application that provides a CRUD API for managing products in a store. It offers common CRUD operations, as well as extra operations for updating products to showcase Spring Security with different user roles.

## How to Run

### Prerequisites

- Java Development Kit (JDK) 17 or higher
- Maven (Can use the included Maven Wrapper from the Git repository)

### Steps
1. Clone the repository:
```bash
git clone https://github.com/mihai-iacov/simple-store-management.git
```
2. Navigate to the project directory:
```bash
cd simple-store-management
```
3. Build the project:
```bash
mvn clean install
```
4. Run the project:
```bash
mvn spring-boot:run
```
5. Open the following URL in a browser to access the Swagger UI:
```
http://localhost:8080/swagger-ui.html
```

## Authentication notes
There are three users available for authentication:
- admin:admin - has access to all endpoints (CRUD + special updates)
- commercial:commercial - has access to read endpoints, as well as updating the price
- warehouse:warehouse - has access to read endpoints, as well as updating the stock

## Testing
The project contains unit tests for the service layer logic, as well as integration tests for the controller layer. The tests can be run with the following command:
```bash
mvn test
```
