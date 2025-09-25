# Project Name

Automated tests for the Player Controller as part of the technical assignment for SPRIBE.

---

## Pre-requisites
- Java 11+
- Gradle

---

## How to run locally and see the report results

Since the Allure results are stored in the build folder, you first need to run the tests and only after that generate and view the report.

```bash
./gradlew clean test
allure serve build\allure-results
```

---

## Summary of tests

Tests: 30

Positive: 14

Negative: 16

Critical bugs found: 10

---

## Bugs

1.User created via GET request — API allows creating a user with GET method, expected 405 Method Not Allowed.
2.Duplicate login — creating a user with the same login overwrites the existing one instead of throwing an error (200 instead of 400).
3.Duplicate screenName — API allows creating two users with the same screenName (200 instead of 400).
4.Invalid age validation — user with age 60 (outside the allowed range [17–59]) is created successfully (200 instead of 400).
5.Invalid password accepted — user is created with an invalid password, API returns 200 instead of 400.
6.Invalid gender validation — sending an invalid gender value returns 200 instead of 400.
7.Invalid JSON Schema — fields age, gender, role, screenName are returned as null while schema expects specific types (integer or string).
8.Wrong status code for delete — deleting a non-existent user returns 403 Forbidden instead of 404 Not Found.
9.Invalid update status — updating a user with invalid age value returns 200 instead of 400.
10.Incorrect role validation — user creation with role admin fails under supervisor rights, response 403 instead of expected 200.
