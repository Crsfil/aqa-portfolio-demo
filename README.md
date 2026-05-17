# AQA Portfolio Demo

Portfolio monorepo with a small test target application and Java AQA test framework.

## Projects

- `construction-demo-app` - demo web application with UI, REST API and RBAC roles.
- `java-aqa-demo-framework` - Java test framework with Selenide, REST Assured, JUnit 5 and Allure.

## Quick Start

Start the application:

```bash
cd construction-demo-app
npm start
```

Run tests in another terminal:

```bash
cd java-aqa-demo-framework
gradle clean smoke -DbaseUrl=http://localhost:3000 -Dheadless=true
```

## Demo Users

| Username | Password | Role |
|---|---|---|
| `admin` | `admin123` | `ADMIN` |
| `manager` | `manager123` | `MANAGER` |
| `viewer` | `viewer123` | `VIEWER` |

## What This Project Shows

- UI autotests with Page Object.
- API autotests with REST Assured clients.
- JUnit 5 tags: `smoke`, `regression`, `ui`, `api`.
- Allure reporting.
- Test data builders.
- RBAC checks: positive and negative scenarios.
- Separate test target app and test framework.
