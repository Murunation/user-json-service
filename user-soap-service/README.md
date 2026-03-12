# Lab06 – JSON Service & SOAP Service Integration

## Project Overview

Энэхүү лабораторийн ажил нь Service-Oriented Architecture (SOA) зарчмыг ашиглан JSON REST Service болон SOAP Service хоорондын интеграц хийх зорилготой.

Систем нь дараах үндсэн 3 хэсгээс бүрдэнэ.

1. User SOAP Service – Authentication service
2. User JSON Service – User profile management
3. Simple Frontend – Login, Register, Profile UI

---

# System Architecture

Architecture:

Frontend  
↓  
JSON REST Service  
↓  
SOAP Authentication Service  
↓  
Database

JSON service нь profile CRUD үйлдлийг хийдэг.

SOAP service нь:
- user register
- login
- token generation
- token validation

JSON service нь authentication хийхдээ SOAP service-ийн ValidateToken operation-ийг ашигладаг.

---

# Services

## 1. SOAP Service (Authentication)

SOAP operations:

- RegisterUser
- LoginUser
- ValidateToken

Login амжилттай бол token үүсгэн буцаана.

JSON service нь энэ token-ийг SOAP service-ээр шалгана.

---

## 2. JSON REST Service (User Profile)

REST API:

POST /users  
GET /users/{id}  
PUT /users/{id}  
DELETE /users/{id}

User profile fields:

- name
- email
- phone
- bio

---

# Authentication Flow

1. User login хийж SOAP service-ээс token авна
2. Frontend token-ийг хадгална
3. REST API дуудах үед token header-ээр дамжина
4. JSON service middleware SOAP ValidateToken дуудаж шалгана
5. Token valid бол request-ийг зөвшөөрнө

---

# Database Design

Энэ төсөлд **Independent Database architecture** сонгосон.

SOAP Service  
→ Auth database

JSON Service  
→ Profile database

Advantages:

- service independence
- security separation
- scalability

Одоогийн лабораторийн хувьд өгөгдөл memory дээр хадгалагдсан.

---

# How to Run

## Requirements

- Java 21
- Maven
- Eclipse IDE

---

## Run SOAP Service

Project:

user-soap-service

Run:

UserSoapServiceApplication

SOAP service URL:

http://localhost:8081/ws

---

## Run JSON Service

Project:

user-json-service

Run:

UserJsonServiceApplication

REST API:

http://localhost:8080/users

---

# Example API Usage

Create User

POST /users
