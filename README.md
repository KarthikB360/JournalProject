# JournalProject
 Sample Journal demo project using Spring Boot Microservices
 
 This project contains two spring boot microservices i.e., user and journal service.
 User service is for user management and Journal service is for journaling user events.
 
 User service contains:
 • Includes functionalities for user registration, user details retrieval, updating and
   deleting user information and managing user roles.
 • Exposed RESTful API endpoints for CRUD operations.
 • Integrated Spring Security for role-based authentication.
 • Publishes user events to a Kafka topic named “user-events” for journaling.
 
 Journal service contains:
 • Consumed events from user-events Kafka topic, log a message and record them in h2 dB.
 • Implemented RESTful API endpoints to retrieve journals.
 • Incorporate role-based access control to retrieve journals.
 
 For documentation swagger is integrated.
 
 Steps to start the service:
 1. Start zookeeper
 2. Start Kafka
 3. Start user service
 4. Start jounal service
 
 For user creation and genration of token endpoint authorization not required, for other endpoints ADMIN role required.
 For any queries please check swagger
 UserService: http://localhost:8181/swagger-ui/index.html
 JournalService: http://localhost:8282/swagger-ui/index.html

