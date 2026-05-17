# Java AQA Demo Framework

Portfolio project with UI and API autotests for `construction-demo-app`.

The framework demonstrates:
- Java 17;
- Selenide/Selenium UI tests;
- REST Assured API tests;
- JUnit 5 tags: `smoke`, `regression`, `ui`, `api`;
- Allure reports;
- Page Object;
- API clients;
- test data builders;
- environment config through system properties.

## Tested app

Start the demo application first:

```bash
cd ../construction-demo-app
npm start
```

App URL:

```text
http://localhost:3000
```

## Run tests

This project requires JDK 17 and Gradle.

All tests:

```bash
gradle clean test
```

Smoke:

```bash
gradle clean smoke
```

Regression:

```bash
gradle clean regression
```

Only API:

```bash
gradle clean apiTests
```

Only UI:

```bash
gradle clean uiTests
```

Custom environment:

```bash
gradle clean smoke -DbaseUrl=http://localhost:3000 -Dbrowser=chrome -Dheadless=true
```

## Allure

Generate and open report:

```bash
gradle allureServe
```

## Project structure

```text
src/test/java/portfolio/aqa
  api        REST Assured clients
  config     environment config
  model      request/response DTOs
  pages      Selenide Page Objects
  testdata   users and builders
  tests
    api      API tests
    ui       UI tests
```

## Test users

| Username | Password | Role |
|---|---|---|
| `admin` | `admin123` | `ADMIN` |
| `manager` | `manager123` | `MANAGER` |
| `viewer` | `viewer123` | `VIEWER` |

## What to say at interview

This is an educational mini framework that imitates a team AQA project:
- UI tests are separated from API tests;
- technical details are hidden in Page Objects and API clients;
- test data is created through builders;
- tests are grouped by JUnit tags;
- Allure report contains readable steps, screenshots and request/response data.
