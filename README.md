# project0
First project RestApi from revature program with java

# Backend Project with Javalin, JDBC, and PostgreSQL

This is a simple backend project developed in **Java** using **Javalin** as the web framework, **JDBC** for connecting to a **PostgreSQL** database, and built with **Maven**.  
It’s designed for learning purposes and focuses on understanding the backend development flow without using heavier frameworks like Spring.

---

## Prerequisites

- Java 17 or higher  
- Maven 3.8 or higher  
- PostgreSQL
- Dbeaver (optional if you want to see data)
- Postman

---

## How to Run the Application

First, compile and package the project. From the root directory of the project, run:

```bash
mvn clean package
```
This will generate a .jar file inside the target/ directory.

Database Connection / Configuration

You need to manually update the following values in the source code to connect to your own PostgreSQL database:

String url = "jdbc:postgresql://localhost:5432/your_database_name";
String user = "your_username";
String password = "your_password";

# Running Unit Tests

To run the unit tests included in the project, simply execute:
``` bash 
mvn test

```


