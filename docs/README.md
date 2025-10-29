# Healthcare Management System Documentation

## Overview
The Healthcare Management System is a Spring Boot-based backend application that manages doctors, patients, appointments, and medical records. This system provides RESTful APIs for managing healthcare services.

## Technical Stack
- Java 17+
- Spring Boot 3+
- Spring Security with JWT
- Spring Data JPA
- H2 Database
- SpringDoc OpenAPI (Swagger)
- Lombok
- Bean Validation

## Setup Instructions

### Prerequisites
- JDK 17 or higher
- Maven 3.6+
- Git

### Building the Application
1. Clone the repository
2. Navigate to the project directory
3. Run: `mvn clean install`
4. Start the application: `mvn spring-boot:run`

The application will start on `http://localhost:8080`.

All working URLs (default development configuration):

- Root (redirects to Swagger UI): http://localhost:8080/
- H2 Console: http://localhost:8080/h2-console
- OpenAPI JSON (v3): http://localhost:8080/v3/api-docs
- Swagger UI (common variants; try these):
    - http://localhost:8080/swagger-ui.html
    - http://localhost:8080/swagger-ui/index.html
    - http://localhost:8080/swagger-ui/
- Authentication endpoints (public):
    - POST http://localhost:8080/api/auth/register/doctor
    - POST http://localhost:8080/api/auth/register/patient
    - POST http://localhost:8080/api/auth/login

Notes:
- Root (`/`) has been enabled and now redirects to the Swagger UI so you can open the API UI by visiting http://localhost:8080/.
- If a particular Swagger path returns 403, use `/v3/api-docs` (this is always permitted) or use the root URL which redirects to the UI.

### Database Configuration
The application uses H2 in-memory database with the following configuration:
- Console URL: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:healthcaredb
- Username: sa
- Password: (empty)

## API Documentation

### Authentication APIs

#### Register Doctor
```http
POST /api/auth/register/doctor
```
Request Body:
```json
{
    "name": "Dr. John Doe",
    "specialization": "Cardiologist",
    "email": "john.doe@example.com",
    "phone": "+1234567890",
    "password": "StrongP@ss123"
}
```

#### Register Patient
```http
POST /api/auth/register/patient
```
Request Body:
```json
{
    "name": "Jane Smith",
    "email": "jane.smith@example.com",
    "phone": "+1234567890",
    "password": "StrongP@ss123",
    "dob": "1990-01-01",
    "medicalHistory": "No major issues"
}
```

#### Login
```http
POST /api/auth/login
```
Request Body:
```json
{
    "email": "user@example.com",
    "password": "StrongP@ss123"
}
```

### Doctor APIs

#### Search Doctors by Specialization
```http
GET /api/doctors/specialization/{specialization}?page=0&size=10
```

#### Get Doctor's Available Slots
```http
GET /api/doctors/{id}/availability
```

#### Update Doctor's Available Slots
```http
PUT /api/doctors/{id}/availability
```
Request Body:
```json
[
    "2025-10-30T09:00:00",
    "2025-10-30T10:00:00",
    "2025-10-30T11:00:00"
]
```

### Appointment APIs

#### Book Appointment
```http
POST /api/appointments/book
```
Request Body:
```json
{
    "patientId": 1,
    "doctorId": 1,
    "appointmentTime": "2025-10-30T09:00:00"
}
```

#### Cancel Appointment
```http
POST /api/appointments/{id}/cancel?userId=1
```

#### Get Patient's Appointments
```http
GET /api/appointments/patient/{patientId}
```

#### Get Doctor's Schedule
```http
GET /api/appointments/doctor/{doctorId}
```

#### Add Prescription
```http
POST /api/appointments/{id}/prescription?doctorId=1
```
Request Body:
```text
Prescription details and notes...
```

## Security

### JWT Authentication
- All protected endpoints require a JWT token
- Token format: Bearer {token}
- Token expiration: 24 hours

### Password Requirements
- Minimum 8 characters
- At least one uppercase letter
- At least one lowercase letter
- At least one number
- At least one special character (@#$%^&+=)

### Role-Based Access
- ROLE_DOCTOR: Full access to doctor features
- ROLE_PATIENT: Limited access to patient features

## Validation Rules

### Appointments
- Cannot book appointments in the past
- Minimum 1 hour advance booking required
- No double booking for the same time slot
- Only doctor or patient can cancel their appointments
- Only doctors can add prescriptions

### User Registration
- Valid email required
- Strong password required
- Valid phone number format
- Date of birth must be in the past (for patients)

## Error Handling
The API uses standard HTTP status codes and returns error responses in the following format:
```json
{
    "success": false,
    "message": "Error description",
    "data": {
        "field1": "Error detail 1",
        "field2": "Error detail 2"
    }
}
```

Common Status Codes:
- 200: Success
- 400: Bad Request
- 401: Unauthorized
- 403: Forbidden
- 404: Not Found
- 500: Internal Server Error

## Testing the Application

### Swagger UI
Access the Swagger UI documentation at:

```
http://localhost:8080/swagger-ui.html
```

### Sample Test Flow
1. Register a doctor account
2. Register a patient account
3. Login with doctor credentials
4. Update doctor's available slots
5. Login with patient credentials
6. Search for doctors by specialization
7. Book an appointment
8. View appointments
9. Login as doctor
10. Add prescription to the appointment

## Monitoring and Management
- H2 Console: http://localhost:8080/h2-console
- API Documentation: http://localhost:8080/api-docs
- Swagger UI: http://localhost:8080/swagger-ui.html

## Best Practices
1. Always include the JWT token in the Authorization header
2. Use strong passwords
3. Book appointments well in advance
4. Keep medical records up to date
5. Regular monitoring of appointments

## Support
For any technical issues or questions, please:
1. Check the Swagger documentation
2. Review error messages
3. Ensure proper request format
4. Verify authentication token

## License
This project is proprietary and confidential.