# RentMyThing Backend

A Spring Boot REST API for managing users, products, and rentals for the RentMyThing platform.

---

## Table of Contents

- [Project Structure](#project-structure)
- [Setup & Requirements](#setup--requirements)
- [Configuration](#configuration)
- [API Endpoints](#api-endpoints)
- [Security & CORS](#security--cors)
- [How to Run](#how-to-run)
- [Testing](#testing)
- [License](#license)

---

## Project Structure

```
rentmything/
  ├── src/
  │   └── main/
  │       ├── java/
  │       │   └── rentmything/
  │       │       └── rentmything/
  │       │           ├── Configure/
  │       │           │   ├── CorsConfig.java
  │       │           │   └── Configure.java
  │       │           ├── controller/
  │       │           │   ├── NearbyController.java
  │       │           │   ├── ProductController.java
  │       │           │   └── ...
  │       │           ├── Dto/
  │       │           │   ├── ProductDto.java
  │       │           │   └── RegisterDto.java
  │       │           ├── Mapper/
  │       │           │   └── RegisterMapper.java
  │       │           ├── model/
  │       │           │   ├── Product.java
  │       │           │   ├── User.java
  │       │           │   └── Role.java
  │       │           ├── repository/
  │       │           │   ├── ProductRepository.java
  │       │           │   └── UserRepository.java
  │       │           ├── service/
  │       │           │   └── AppUserService.java
  │       │           └── RentmythingApplication.java
  │       └── resources/
  │           └── application.properties
  ├── pom.xml
  └── ...
```

---

## Setup & Requirements

- **Java 17+**
- **Maven**
- **MySQL** (or compatible database)

---

## Configuration

Edit `src/main/resources/application.properties` with your database and OAuth2 credentials:

```properties
spring.application.name=rentmything

# Database Connection
spring.datasource.url=jdbc:mysql://localhost:3306/rentmything
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# OAuth2 (Google)
spring.security.oauth2.client.registration.google.client-id=YOUR_GOOGLE_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_GOOGLE_CLIENT_SECRET
```

---

## API Endpoints

### User & Owner

- `POST /rentMyThing/RegAsOwner` — Register as owner
- `POST /rentMyThing/RegAsCustomer` — Register as customer

### Product

- `POST /rentMyThing/addProduct` — Add a product (owner only)
- `GET /rentMyThing/getOwnerProducts?email=owner@email.com` — Get products by owner
- `POST /rentMyThing/requestProductRental` — Request rental

### Nearby

- `GET /rentMyThing/findNearbyOwners?latitude=...&longitude=...` — Find owners within 5km

---

## Security & CORS

- **CORS** is configured in `Configure/CorsConfig.java` to allow requests from your frontend (e.g., `http://localhost:3000`).
- **Security** is configured in `Configure/Configure.java` using Spring Security, with OAuth2 login and logout endpoints.

---

## How to Run

1. **Start MySQL database.**
2. **Build and run the backend:**
   ```sh
   ./mvnw spring-boot:run
   ```
   or
   ```sh
   mvn spring-boot:run
   ```

3. **Access the API:**  
   - Backend: [http://localhost:8080](http://localhost:8080)

---

## Testing

Run backend tests with:
```sh
./mvnw test
```
or
```sh
mvn test
```

---

## License

This project is for educational/demo purposes.  
Feel free to fork and modify!

---

## Author

- [Your Name](https://github.com/yourusername)
