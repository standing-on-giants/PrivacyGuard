# Data-Modelling-Project

## Prerequisites
- Java Development Kit (JDK) 17 or higher
- [PostgreSQL](https://www.postgresql.org/download/)
- [Maven](https://maven.apache.org/download.cgi)

## Setup

### 1. Clone the Repository
```bash
git clone https://github.com/siddharthmaram/Data-Modelling-Project.git
cd DataModellingProject
```

### 2. Database Configuration
Then run the files `src/main/resources/create.sql` and `src/main/resources/insert.sql`. You can use the following command:
```bash
psql -U <USER> -d postgres -f <FILE_PATH>
```

Next, open the `src/main/resources/application.properties` file and update the database credentials to match your local PostgreSQL setup:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/hospitalmanagementsystem
spring.datasource.username=your_postgres_username
spring.datasource.password=your_postgres_password
```

## Execution
Open your terminal in the project root folder and run:
```bash
mvn spring-boot:run
```
The application will start on the port 8080.

## Project Structure
```
DataModellingProject/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/DataModellingProject/
│       │       ├── config/          # ModelMapper configurations
│       │       ├── controller/      # REST API endpoints mapping
│       │       ├── dto/             # Data Transfer Objects (Requests & Responses)
│       │       ├── model/           # JPA Entities mapped to the database
│       │       ├── repository/      # Spring Data JPA interfaces
│       │       ├── service/         # Business logic and mapping execution
│       │       ├── specification/   # Dynamic Criteria Builder logic for queries
│       │       ├── security/        # Authenication and user session
│       │       └── DataModellingProjectApplication.java
│       └── resources/
│           └── application.properties # Database and application settings
├── pom.xml                          # Maven dependencies and build config
└── README.md
```
