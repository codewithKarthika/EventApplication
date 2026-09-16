# Event Management System

A simple Event Management System built with:

- React + Vite frontend
- Java Spring Boot REST API
- SQLite database using Spring Data JPA
- Three core functionalities:
  1. Organizer creates an event
  2. Student registers for an event (duplicate registration blocked and capacity enforced)
  3. Student checks in on the event day (registered students only, one check-in only)

## Project structure

```text
event-management-system/
├── backend/
│   ├── pom.xml
│   └── src/main/java/com/example/eventmanagement/
│       ├── EventManagementApplication.java
│       ├── controller/
│       ├── entity/
│       ├── exception/
│       ├── repository/
│       └── service/
├── frontend/
│   ├── package.json
│   ├── index.html
│   └── src/
└── README.md
```

## Run backend

Requirements: Java 17+ and Maven.

```bash
cd backend
mvn spring-boot:run
```

Backend runs on:

```text
http://localhost:8080
```

SQLite database file `event_management.db` is created automatically in the backend directory.

## Run frontend

Requirements: Node.js 18+.

```bash
cd frontend
npm install
npm run dev
```

Frontend normally runs on:

```text
http://localhost:5173
```

## API endpoints

### 1. Create event

`POST /api/events`

Example JSON:

```json
{
  "name": "Java Technical Workshop",
  "eventDate": "2026-09-20",
  "eventTime": "10:00",
  "capacity": 50
}
```

### 2. Register student

`POST /api/registrations`

Example JSON:

```json
{
  "studentId": 1,
  "eventId": 1
}
```

The API blocks:
- duplicate registration for the same student and event
- registration after the event capacity is full

### 3. Check in

`PUT /api/registrations/check-in`

Example JSON:

```json
{
  "studentId": 1,
  "eventId": 1
}
```

The API allows check-in only when:
- the student is registered
- today is the event date
- the student has not already checked in

## Demo flow

1. Create an event.
2. Create/register a student using the student API or the frontend.
3. Register the student for the event.
4. On the event date, use Check In.
5. Refresh the event list to see the registered and checked-in counts.
