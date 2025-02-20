# University Scheduling API Layout (Final)

**Base URL:** `/api/v1` (Batch endpoints marked with ★)

## Excel File Management
- **POST** `/excel` - Import schedule file
- **GET** `/excel` - List imports
- **GET** `/excel/{id}` - Get import
- **DELETE** `/excel/{id}` - Remove import

## Schedule Management

### Admin
- **GET** `/schedules/conflicts`
- **GET** `/schedules/room-conflicts`
- **GET** `/schedules/session/{sessionId}/conflicts`
- **POST** `/schedules/batch` ★ - Bulk create schedules

### Teacher
- **GET** `/schedules/teachers/{teacherId}`
- **GET** `/schedules/teachers/{teacherId}/hours`
- **GET** `/schedules/teachers/{teacherId}/subjects`
- **GET** `/schedules/teachers/{teacherId}/groups`

### Student
- **GET** `/schedules/students/{studentId}`
- **GET** `/schedules/branches/{branchId}/sessions`

## Session Management
- **POST** `/sessions`
- **POST** `/sessions/batch` ★ - Bulk create
- **PUT** `/sessions/{id}`
- **PUT** `/sessions/batch` ★ - Bulk update
- **DELETE** `/sessions/{id}`
- **GET** `/sessions`
- **GET** `/sessions/{id}`

## Rattrapage Management

### Teacher
- **POST** `/rattrapage`
- **GET** `/rattrapage/teachers/{teacherId}`

### Admin
- **GET** `/rattrapage`
- **PUT** `/rattrapage/{id}/approve`
- **PUT** `/rattrapage/{id}/reject`
- **PUT** `/rattrapage/batch` ★ - Bulk status update

## Room Management

### Admin/Tech
- **POST** `/rooms`
- **POST** `/rooms/batch` ★ - Bulk create
- **PUT** `/rooms/{id}`
- **PUT** `/rooms/batch` ★ - Bulk update
- **DELETE** `/rooms/{id}`

### Common
- **GET** `/rooms`
- **GET** `/rooms/{id}`
- **GET** `/rooms/available`

## Branch Management

### Admin
- **POST** `/branches`
- **POST** `/branches/batch` ★ - Bulk create
- **PUT** `/branches/{id}`
- **PUT** `/branches/batch` ★ - Bulk update
- **DELETE** `/branches/{id}`

### Common
- **GET** `/branches`
- **GET** `/branches/{id}`
- **GET** `/branches/{id}/students`

## Tutorial Groups (TD)

### Admin
- **POST** `/td`
- **POST** `/td/batch` ★ - Bulk create
- **PUT** `/td/{id}`
- **PUT** `/td/batch` ★ - Bulk update
- **DELETE** `/td/{id}`
- **POST** `/td/{id}/schedules`

### Common
- **GET** `/td`
- **GET** `/td/{id}`
- **GET** `/td/{id}/tp`
- **GET** `/td/{id}/students`

## Practical Groups (TP)

### Admin
- **POST** `/tp`
- **POST** `/tp/batch` ★ - Bulk create
- **PUT** `/tp/{id}`
- **PUT** `/tp/batch` ★ - Bulk update
- **DELETE** `/tp/{id}`
- **POST** `/tp/{id}/schedules`

### Common
- **GET** `/tp`
- **GET** `/tp/{id}`
- **GET** `/tp/{id}/students`

## Issue Signals

### Teacher
- **POST** `/signals`
- **GET** `/signals/teachers/{teacherId}`

### Admin
- **GET** `/signals`
- **PUT** `/signals/{id}/resolve`
- **PUT** `/signals/batch/resolve` ★ - Bulk resolve

## Notifications

### Admin
- **POST** `/notifications/broadcast`
- **POST** `/notifications/teachers`
- **POST** `/notifications/students`
- **POST** `/notifications/branches`
- **POST** `/notifications/td`
- **POST** `/notifications/tp`
- **POST** `/notifications/batch` ★ - Bulk create

### Common
- **GET** `/notifications`
- **GET** `/notifications/unread`
- **PUT** `/notifications/{id}/read`
- **DELETE** `/notifications/{id}`

## Key Features ✅
- **Batch Operations:** POST for bulk creation, PUT for bulk updates. Consistent `/batch` suffix for clarity.
- **Role-Based Access:** Admin, Teacher, Student, and Technician roles clearly separated.
- **RESTful Design:** Resource-centric endpoints with proper HTTP verbs.
- **Scalability:** Bulk endpoints handle large-scale operations efficiently.

This layout is ready for implementation and fully aligns with your system requirements. It’s clean, consistent, and scalable for future enhancements. 🚀
