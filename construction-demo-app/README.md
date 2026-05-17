# Construction Demo App

Small demo web application for AQA portfolio tests.

It contains:
- static UI for login, projects and documents;
- REST API;
- RBAC roles: `ADMIN`, `MANAGER`, `VIEWER`;
- positive and negative scenarios for UI/API autotests.

## Run

```bash
npm start
```

Open:

```text
http://localhost:3000
```

## Test users

| Username | Password | Role |
|---|---|---|
| `admin` | `admin123` | `ADMIN` |
| `manager` | `manager123` | `MANAGER` |
| `viewer` | `viewer123` | `VIEWER` |

## API examples

Health:

```bash
curl http://localhost:3000/api/health
```

Login:

```bash
curl -X POST http://localhost:3000/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"manager","password":"manager123"}'
```

Create document:

```bash
curl -X POST http://localhost:3000/api/documents \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer <token>' \
  -d '{"projectId":101,"title":"New inspection act","type":"ACT"}'
```

RBAC negative case:

```bash
curl http://localhost:3000/api/admin/users \
  -H 'Authorization: Bearer <viewer-token>'
```

Expected status: `403 Forbidden`.
