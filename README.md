# Company Service

This repository contains the Company Service, which is built using the following technologies:

- **Spring Boot 3**
- **Java 17**
- **PostgreSQL**
- **Spring Data ORM**
- **Feign Client for REST Communication**
- **Resilience4j for Retry Mechanisms**
- **Docker**
- **Docker Compose**

## Overview

The Company Service manages company, employee, and area data. The main entities and their relationships are as follows:

### Entities

- **Area**
    - `Long id`
    - `String name`
    - `String description`

- **Company**
    - `Long id`
    - `String name`
    - `String ruc`
    - `List<Employee> employees`

- **Employee**
    - `Long id`
    - `String name`
    - `String lastname`
    - `Company company`
    - `Area area`

### Relationships

- One `Company` has many `Employees`
- One `Employee` belongs to one `Area`
- One `Area` has many `Employees`

## Functionality

### Endpoints

- **`GET /companies`**
    - Retrieves all companies with pagination support.

- **`GET /companies/{id}`**
    - Retrieves a single company by its ID.

- **`GET /products/company/{companyId}`**
    - Retrieves all products owned by a company. This endpoint communicates with the Product Service via REST using Feign Client and implements resilience and retries.
    - The service first authenticates with the Product Service to obtain a token, which is then used to access all endpoints.

- **`GET /employees/company/{companyId}`**
    - Retrieves all employees for a company with pagination support.

## Authentication

- This service does not require authentication for its endpoints.
- However, it consumes authentication to retrieve data from another service (Product Service).
    - Simple authentication (username and password) is used to obtain a JWT token valid for 1 hour.
    - When the token expires, a new one is obtained automatically. This process is handled internally.

## Containerization

- Docker is used to containerize the service.
- Docker Compose is used to manage the multi-container setup.

## Setup Instructions

### Prerequisites

- Ensure you have Docker and Docker Compose installed.
- Ensure PostgreSQL is installed and running.

### Running the Application

1. Clone the repository:
    ```bash
    git clone <repository-url>
    ```
2. Navigate to the project directory:
    ```bash
    cd company-service
    ```
3. Start the application using Docker Compose:
    ```bash
    docker-compose up --build
    ```

This will set up the network, create the necessary volumes, build the Docker image, and start the container running the application.

## Endpoints

### Get Companies

Retrieve a list of companies with pagination support.

- **Endpoint:** `{{base_url_company_service}}/api/v1/companies?page=0&size=5`
- **Example URL:** `http://35.223.99.149:8080/api/v1/companies?page=0&size=5`

**Curl Command:**
```sh
curl -X GET "{{base_url_company_service}}/api/v1/companies?page=0&size=5" -H "accept: application/json"
```

### Get Employees
Retrieve a list of employees for a specific company with pagination support.

- **Endpoint:** `{{base_url_company_service}}/api/v1/companies/1/employees?page=0&size=5`
- **Example URL**: `http://35.223.99.149:8080/api/v1/companies/1/employees?page=0&size=5`

**Curl Command:**
```sh
curl -X GET "{{base_url_company_service}}/api/v1/companies/1/employees?page=0&size=5" -H "accept: application/json"
```

### Get Products (External Service Communication)
Retrieve a list of products owned by a company. This endpoint communicates with an external product service.

- **Endpoint:** `{{base_url_company_service}}/api/v1/companies/1/products?page=0&size=5`
- **Example URL**: `http://35.223.99.149:8080/api/v1/companies/1/products?page=0&size=5`

**Curl Command:**
```sh
curl -X GET "{{base_url_company_service}}/api/v1/companies/1/products?page=0&size=5" -H "accept: application/json"
```

### Get One Company
Retrieve details of a single company by its ID.

- **Endpoint:** `{{base_url_company_service}}/api/v1/companies/1`
- **Example URL:** `http://35.223.99.149:8080/api/v1/companies/1`

**Curl Command:**
```sh
curl -X GET "{{base_url_company_service}}/api/v1/companies/1" -H "accept: application/json"
```
