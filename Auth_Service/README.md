**Multi-Tenant Loan Origination and Credit Risk Management System**

A Spring Boot microservices-based Loan Origination and Credit Risk Management System designed to manage tenants, customers, loans, authentication, and automated credit-risk assessment. The system uses API Gateway, Eureka Service Discovery, JWT authentication, PostgreSQL, and REST/Feign-based communication between microservices.

🚀 Features
🔐 JWT-based authentication and authorization
🏢 Multi-tenant architecture
👤 Customer management
💰 Loan application and management
📊 Rule-based credit/risk assessment
🌐 Centralized API Gateway
🔎 Eureka-based service discovery
🗄️ PostgreSQL database for each major service
🔄 Inter-service communication
🛡️ Exception handling and validation
📡 REST APIs for frontend/backend integration
📈 Actuator-based service monitoring
🏗️ System Architecture
                         ┌──────────────────────┐
                         │      Frontend        │
                         │ React / Angular      │
                         └──────────┬───────────┘
                                    │
                                    │ HTTP/REST
                                    ▼
                         ┌──────────────────────┐
                         │     API Gateway      │
                         │       :8080          │
                         │                      │
                         │ JWT Validation       │
                         │ Request Routing      │
                         └──────────┬───────────┘
                                    │
                  ┌─────────────────┼─────────────────┐
                  │                 │                 │
                  ▼                 ▼                 ▼
          ┌─────────────┐   ┌─────────────┐   ┌─────────────┐
          │Auth Service │   │Tenant       │   │Customer     │
          │   :8081     │   │Service      │   │Service      │
          │             │   │   :8083     │   │   :8084     │
          └──────┬──────┘   └──────┬──────┘   └──────┬──────┘
                 │                 │                 │
                 ▼                 ▼                 ▼
             auth_db          tenant_db         customer_db

                                    │
                                    ▼
                            ┌───────────────┐
                            │ Loan Service  │
                            │    :8085      │
                            └───────┬───────┘
                                    │
                                    │ Feign/REST
                                    ▼
                            ┌───────────────┐
                            │ Credit/Risk   │
                            │ Service       │
                            └───────┬───────┘
                                    │
                                    ▼
                              credit_risk_db


                         ┌──────────────────────┐
                         │   Eureka Server      │
                         │       :8761          │
                         │ Service Discovery    │
                         └──────────────────────┘
📦 Microservices
1. API Gateway

Port: 8080

The API Gateway acts as the single entry point for client requests.

Responsibilities
Route requests to appropriate microservices
Validate JWT tokens
Handle authentication at the gateway level
Hide internal service URLs from the frontend
Provide a centralized entry point

Example:

/api/auth/**       → AUTH-SERVICE
/api/tenants/**    → TENANT-SERVICE
/api/customers/**  → CUSTOMER-SERVICE
/api/loans/**      → LOAN-SERVICE
/api/credit/**     → CREDIT-RISK-SERVICE
🔐 2. Auth Service

Port: 8081

Handles user authentication and registration.

Responsibilities
User registration
User login
Password management
JWT token generation
User roles
Authentication-related exceptions
Main Components
auth-service
│
├── controller
│   └── AuthController
│
├── service
│   ├── AuthService
│   └── AuthServiceImpl
│
├── entity
│   ├── User
│   └── Role
│
├── repository
│   └── UserRepository
│
├── dto
│   ├── LoginRequest
│   ├── RegisterRequest
│   └── AuthResponse
│
├── security
│   └── JwtService
│
└── exception
🏢 3. Tenant Service

Port: 8083

Manages organizations/financial institutions registered on the platform.

Responsibilities
Create tenant
Get tenant
Get all tenants
Update tenant
Deactivate tenant
Maintain tenant information
Example
Tenant
│
├── tenantId
├── tenantCode
├── tenantName
├── email
├── phone
├── address
├── website
└── status
👤 4. Customer Service

Port: 8084

Manages customers belonging to individual tenants.

Responsibilities
Create customer
Retrieve customer
Retrieve customers by tenant
Update customer
Deactivate customer
Maintain customer employment and income information
Customer relationship
Tenant
   │
   ├── Customer 1
   ├── Customer 2
   ├── Customer 3
   └── Customer N

Each customer contains a tenantId to identify the organization they belong to.

💰 5. Loan Service

Port: 8085

Responsible for loan applications and loan lifecycle management.

Responsibilities
Create loan application
Retrieve loan
Update loan
Manage loan status
Connect loan with customer and tenant
Send loan information to Credit/Risk Service
Loan lifecycle
PENDING
   │
   ▼
CREDIT_ASSESSMENT
   │
   ├───────────────┐
   ▼               ▼
APPROVED        REJECTED
   │
   ▼
DISBURSED
   │
   ▼
CLOSED
📊 6. Credit/Risk Service

Responsible for automated rule-based credit assessment.

Responsibilities
Receive loan information
Evaluate applicant financial information
Calculate risk indicators
Calculate Debt-to-Income ratio
Determine risk level
Return assessment to Loan Service

Example:

Loan
 │
 ├── Loan Amount
 ├── Monthly Income
 ├── Existing Obligations
 └── Tenure
       │
       ▼
 Credit/Risk Service
       │
       ▼
 Rule-Based Assessment
       │
       ├── LOW
       ├── MEDIUM
       └── HIGH
🔄 Inter-Service Flow
Loan Application Flow
Frontend
   │
   ▼
API Gateway
   │
   ▼
Loan Service
   │
   ├──────► Customer Service
   │
   │        Get customer information
   │
   ▼
Credit/Risk Service
   │
   │ Rule-based assessment
   ▼
Risk Result
   │
   ▼
Loan Service
   │
   ▼
Update Loan Status
   │
   ▼
API Gateway
   │
   ▼
Frontend
🔎 Eureka Service Discovery

Port: 8761

Eureka is used for service registration and discovery.

                 Eureka Server
                     :8761
                       │
       ┌───────────────┼────────────────┐
       │               │                │
       ▼               ▼                ▼
 AUTH-SERVICE    CUSTOMER-SERVICE   LOAN-SERVICE
       │
       ▼
TENANT-SERVICE
       │
       ▼
CREDIT-RISK-SERVICE

Instead of hardcoding service IP addresses, services can communicate using registered service names.

🗄️ Database Architecture

Each major service has its own PostgreSQL database.

PostgreSQL
│
├── auth_db
│      └── users
│
├── tenant_db
│      └── tenants
│
├── customer_db
│      └── customers
│
├── loan_db
│      └── loans
│
└── credit_risk_db
       └── credit_assessments
Database principle

Each microservice owns its own database.

Auth Service       → auth_db
Tenant Service     → tenant_db
Customer Service   → customer_db
Loan Service       → loan_db
Credit/Risk        → credit_risk_db

Services should communicate through APIs rather than directly accessing another service's database.

🔐 Authentication Flow
User
 │
 │ POST /api/auth/login
 ▼
API Gateway
 │
 ▼
Auth Service
 │
 ├── Validate credentials
 │
 └── Generate JWT
       │
       ▼
     Client
       │
       │ Authorization: Bearer <JWT>
       ▼
API Gateway
       │
       ├── Validate JWT
       │
       └── Forward request
              │
              ▼
       Target Microservice

The API Gateway performs JWT validation before forwarding protected requests.

🛠️ Technologies Used
Backend
Java 21
Spring Boot
Spring MVC
Spring Data JPA
Spring Validation
Spring Security
JWT
Spring Cloud
Spring Cloud Gateway
Netflix Eureka
OpenFeign/REST communication
Lombok
Database
PostgreSQL
Hibernate
JPA
Development & Testing
IntelliJ IDEA
Maven
Git
GitHub
Postman

📁 Overall Project Structure
Multi-Tenant-Loan-Origination-System/
│
├── API_Gateway/
│
├── Auth_Service/
│
├── Tenant_Service/
│
├── Customer_Service/
│
├── Loan_Service/
│
├── Credit_Risk_Service/
│
├── Eureka_Server/
│
└── README.md
⚙️ Prerequisites

Make sure the following are installed:

Java 21
Maven
PostgreSQL
Git
IntelliJ IDEA or another Java IDE
Postman
🗄️ PostgreSQL Setup

Create the required databases:

CREATE DATABASE auth_db;

CREATE DATABASE tenant_db;

CREATE DATABASE customer_db;

CREATE DATABASE loan_db;

CREATE DATABASE credit_risk_db;

Update each service's application.yml with your PostgreSQL credentials.

Example:

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/customer_db
    username: postgres
    password: postgres
▶️ Running the Project

Start the services in this general order:

1. Eureka Server
       ↓
2. Auth Service
       ↓
3. Tenant Service
       ↓
4. Customer Service
       ↓
5. Credit/Risk Service
       ↓
6. Loan Service
       ↓
7. API Gateway

Then verify Eureka:

http://localhost:8761

All microservices should appear as registered services.

🧪 API Testing

The APIs can be tested using Postman.

Example authentication:

POST /api/auth/login

After receiving the JWT:

Authorization: Bearer <JWT_TOKEN>

Use the token for protected requests through the API Gateway.

Example:

POST http://localhost:8080/api/customers

and:

POST http://localhost:8080/api/loans
🔗 Example Loan → Credit/Risk Flow
POST /api/loans
        │
        ▼
   API Gateway
        │
        ▼
   Loan Service
        │
        │ Customer information
        ▼
 Customer Service
        │
        ▼
   Loan Service
        │
        │ Credit Assessment Request
        ▼
 Credit/Risk Service
        │
        ▼
 Risk Assessment
        │
        ├── LOW
        ├── MEDIUM
        └── HIGH
        │
        ▼
   Loan Service
        │
        ▼
 Update Loan Status
🔮 Future Enhancements

The project can be extended with:

Notification Service
Email notifications
Kafka-based asynchronous communication
Document Service
Redis caching
Advanced credit-risk models
Admin dashboard
Customer portal
Loan officer dashboard
Docker containerization
Kubernetes deployment
Centralized logging
Distributed tracing

👨‍💻 Author
Viraj Gorde
Interested in Java Backend Development, Spring Boot, Microservices, REST APIs, and Cloud Technologies.
