# Contact Management Application

## Introduction
This is a Spring Boot-based application designed to manage contact information efficiently, following best practices for enterprise applications. The application supports core CRUD operations and provides a RESTful API for interacting with the contact data.

---

## Features
### Functional Requirements
1. **Contact Management Operations**:
    - **Retrieve All Contacts**: Supports pagination to retrieve contacts in partitions.
        - Endpoint: `GET /api/contact`
    - **Retrieve a Specific Contact**: Fetch detailed information for a specific contact.
        - Endpoint: `GET /api/contact/{id}`
    - **Delete a Contact**: Remove a specific contact by ID.
        - Endpoint: `DELETE /api/contact/{id}`
    - **Update Contact Details**: Modify the details of a specific contact.
        - Endpoint: `PUT /api/contact/{id}`
    - **Search Contacts**: Search for contacts by first name or last name.
        - Endpoint: `GET /contacts/search?searchKeyword={name}`

2. **Contact Information**:
    - Fields: Name, Email Address, Telephone Number, Postal Address.

### Technical Requirements
- **Database**: Uses an in-memory RDBMS (e.g., H2) for data storage.
- **Persistence Framework**: Utilizes JPA and Hibernate for ORM.
- **RESTful API**: Fully compliant RESTful services.
- **Build Tool**: Gradle is used for building, testing, and running the application.
- **Validation**: Java Bean Validation ensures data integrity and rejects invalid inputs before processing.
- **Exception Handling**: Interceptors and global exception handlers are implemented to log and manage errors effectively.
- **Testing**:
    - Unit Tests to validate individual components.
    - Integration Tests to test API functionality using RESTful services.
- **API Documentation**: Uses OpenAPI/Swagger for API documentation.

---

## Setup Instructions

### Prerequisites
- Java 11 or later
- Gradle 7 or later

### Steps to Run the Application
1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd contact-management
   ```

2. Build the application:
   ```bash
   ./gradlew build
   ```

3. Run the application:
   ```bash
   ./gradlew bootRun
   ```

4. Access the application:
   - Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
   - API Documentation: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## API Endpoints

### Contact Endpoints
| Method | Endpoint             | Description                        |
|--------|----------------------|------------------------------------|
| GET    | `/api/contact`          | Retrieve all contacts with paging. |
| GET    | `/api/contact/{id}`     | Retrieve details of a contact.     |
| DELETE | `/api/contact/{id}`     | Delete a contact.                  |
| PUT    | `/api/contact/{id}`     | Update contact details.            |
| GET    | `/api/contact/search`   | Search contacts by name.           |

---

## Validation
- **Java Bean Validation**:
    - Example:
      ```java
      @NotBlank(message = "Name is required")
      @Size(max = 100, message = "Name must be less than 100 characters")
      private String name;
      ```
- Handles invalid input via custom exception handlers.

---

## Logging and Exception Handling
- **Interceptor**: Logs validation and other exceptions to a log file.
- **Global Exception Handler**: Provides structured error responses for clients.

---

## Testing
- **Unit Tests**:
    - Test individual services, controllers, and repositories.
    - Framework: JUnit.
- **Integration Tests**:
    - Test RESTful APIs using `TestRestTemplate`.
    - Ensure API responses align with specifications.

Run all tests:
```bash
./gradlew test
```

---

## Future Enhancements
- Add user authentication and authorization.
- Support for multiple contact groups.
- Advanced search functionality with filters.

---

## License
This project is licensed under the MIT License. See the LICENSE file for details.

---

## Contributions
Contributions are welcome! Please follow the standard GitHub flow:
1. Fork the repository.
2. Create a new feature branch.
3. Commit your changes.
4. Submit a pull request.

---

## Contact
For issues or questions, contact [hohoanvu1993l@gmail.com].

