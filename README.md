# SPRIBE TEST ASSIGNMENT

Automated tests for the Player Controller as part of the technical assignment for SPRIBE.  
Framework built with Java 11, TestNG, and RestAssured.

---

## Features
- Positive and negative test coverage for player endpoint
- Critical bug detection with automated tests
- Configurable test execution (threads, base URL)
- Allure reporting
- Logging implemented
- Run tests anywhere using Docker – all dependencies included
- Automated CI/CD with GitHub Actions – builds project on push and pull request

---

## Tech Stack
- Java 11+
- TestNG (with native assertions)
- RestAssured
- Gradle
- Allure reporting
- LOG4J2 for logging

---

## Pre-requisites
- Java 11+
- Gradle
- Allure CLI

---

## Setup manually or using docker

   Clone repository:
   ```bash
   git clone <repo-url>
   cd spribe-test-assignment
   ```

# Manually:  

1. Update config.properties if needed (base URL, retry count)  

2. Run tests:
  ```bash
  ./gradlew clean test
  ```

3. Generate Allure report:  
  ```bash
  allure serve build/allure-results
  ```

---

# Using docker:  

1. Run command:
 ```bash
   docker-compose build
   docker-compose up
   ```
2. Then open index.html file from allure-report package in browser

---

## Summary of tests

Tests: 30

Positive: 14

Negative: 16

Critical bugs found: 10

---

## Bugs that I found

1. User created via GET request — API allows creating a user with GET method, expected 405 Method Not Allowed.  

2. Duplicate login — creating a user with the same login overwrites the existing one instead of throwing an error (200 instead of 400).  

3. Duplicate screenName — API allows creating two users with the same screenName (200 instead of 400).  

4. Invalid age validation — user with age 60 (outside the allowed range [17–59]) is created successfully (200 instead of 400).  

5. Invalid password accepted — user is created with an invalid password, API returns 200 instead of 400.  

6. Invalid gender validation — sending an invalid gender value returns 200 instead of 400.  

7. Invalid JSON Schema — fields age, gender, role, screenName are returned as null while schema expects specific types (integer or string).  

8. Wrong status code for delete — deleting a non-existent user returns 403 Forbidden instead of 404 Not Found.  

9. Invalid update status — updating a user with invalid age value returns 200 instead of 400.  

10. Incorrect role validation — user creation with role admin fails under supervisor rights, response 403 instead of expected 200.  

