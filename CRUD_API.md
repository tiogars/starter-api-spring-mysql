# Sample CRUD API

## Overview

The Sample CRUD API provides complete Create, Read, Update, and Delete operations for Sample entities.

## Endpoints

### Create Sample

Creates a new sample with validation.

```
POST /sample
```

#### Request Body

```json
{
  "name": "Sample Name",
  "description": "Sample Description",
  "active": true
}
```

#### Request Fields

- **name** (string, required): Name of the sample (1-10 characters)
- **description** (string, optional): Description of the sample
- **active** (boolean, required): Active status

#### Response

Returns the created Sample object with generated ID and audit fields:

```json
{
  "id": 1,
  "name": "Sample Name",
  "description": "Sample Description",
  "active": true,
  "createdAt": "2024-01-01T00:00:00.000Z",
  "createdBy": "system",
  "updatedAt": "2024-01-01T00:00:00.000Z",
  "updatedBy": "system"
}
```

#### Response Codes

- **200**: Sample created successfully
- **400**: Validation failed - Invalid input data

---

### Update Sample

Updates an existing sample with validation.

```
PUT /sample/{id}
```

#### Path Parameters

- **id** (long, required): ID of the sample to update

#### Request Body

```json
{
  "name": "Updated Sample Name",
  "description": "Updated Description",
  "active": false
}
```

#### Request Fields

- **name** (string, required): Updated name of the sample (1-10 characters)
- **description** (string, optional): Updated description
- **active** (boolean, required): Updated active status

#### Response

Returns the updated Sample object with preserved creation fields and updated audit fields:

```json
{
  "id": 1,
  "name": "Updated Sample Name",
  "description": "Updated Description",
  "active": false,
  "createdAt": "2024-01-01T00:00:00.000Z",
  "createdBy": "originalUser",
  "updatedAt": "2024-01-02T00:00:00.000Z",
  "updatedBy": "system"
}
```

#### Response Codes

- **200**: Sample updated successfully
- **400**: Validation failed - Invalid input data
- **404**: Sample not found

#### Notes

- The `id` in the path takes precedence over any `id` in the request body
- Creation fields (`createdAt`, `createdBy`) are preserved from the original entity
- Update fields (`updatedAt`, `updatedBy`) are automatically set by the service

---

### Get Sample by ID

Retrieves a single sample by its ID.

```
GET /sample/{id}
```

#### Path Parameters

- **id** (long, required): ID of the sample to retrieve

#### Response

Returns the Sample object if found, or null if not found:

```json
{
  "id": 1,
  "name": "Sample Name",
  "description": "Sample Description",
  "active": true,
  "createdAt": "2024-01-01T00:00:00.000Z",
  "createdBy": "system",
  "updatedAt": "2024-01-01T00:00:00.000Z",
  "updatedBy": "system"
}
```

---

### Get All Samples

Retrieves all samples.

```
GET /sample
```

#### Response

Returns an array of all Sample objects:

```json
[
  {
    "id": 1,
    "name": "Sample 1",
    "description": "Description 1",
    "active": true,
    "createdAt": "2024-01-01T00:00:00.000Z",
    "createdBy": "system",
    "updatedAt": "2024-01-01T00:00:00.000Z",
    "updatedBy": "system"
  },
  {
    "id": 2,
    "name": "Sample 2",
    "description": "Description 2",
    "active": false,
    "createdAt": "2024-01-02T00:00:00.000Z",
    "createdBy": "system",
    "updatedAt": "2024-01-02T00:00:00.000Z",
    "updatedBy": "system"
  }
]
```

---

### Delete Sample

Deletes a sample by its ID.

```
DELETE /sample/{id}
```

#### Path Parameters

- **id** (long, required): ID of the sample to delete

#### Response

Returns no content (void).

---

## Validation Rules

### Name Field

- **Required**: Cannot be null, empty, or blank
- **Length**: Must be between 1 and 10 characters
- **Error Messages**:
  - "Name cannot be null"
  - "Name cannot be empty"
  - "Name cannot be blank"
  - "Name must not exceed 10 characters"

### Description Field

- **Optional**: Can be null or empty
- No length restrictions

### Active Field

- **Required**: Boolean value (true or false)

## Examples

### Example 1: Create a Sample

Request:
```bash
curl -X POST http://localhost:8080/sample \
  -H "Content-Type: application/json" \
  -d '{
    "name": "TestSample",
    "description": "This is a test sample",
    "active": true
  }'
```

Response:
```json
{
  "id": 1,
  "name": "TestSample",
  "description": "This is a test sample",
  "active": true,
  "createdAt": "2024-01-01T10:30:00.000Z",
  "createdBy": "system",
  "updatedAt": "2024-01-01T10:30:00.000Z",
  "updatedBy": "system"
}
```

### Example 2: Update a Sample

Request:
```bash
curl -X PUT http://localhost:8080/sample/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "UpdatedName",
    "description": "Updated description",
    "active": false
  }'
```

Response:
```json
{
  "id": 1,
  "name": "UpdatedName",
  "description": "Updated description",
  "active": false,
  "createdAt": "2024-01-01T10:30:00.000Z",
  "createdBy": "system",
  "updatedAt": "2024-01-01T11:45:00.000Z",
  "updatedBy": "system"
}
```

### Example 3: Get a Sample

Request:
```bash
curl -X GET http://localhost:8080/sample/1
```

Response:
```json
{
  "id": 1,
  "name": "UpdatedName",
  "description": "Updated description",
  "active": false,
  "createdAt": "2024-01-01T10:30:00.000Z",
  "createdBy": "system",
  "updatedAt": "2024-01-01T11:45:00.000Z",
  "updatedBy": "system"
}
```

### Example 4: Get All Samples

Request:
```bash
curl -X GET http://localhost:8080/sample
```

Response:
```json
[
  {
    "id": 1,
    "name": "Sample 1",
    "description": "Description 1",
    "active": true,
    "createdAt": "2024-01-01T10:30:00.000Z",
    "createdBy": "system",
    "updatedAt": "2024-01-01T10:30:00.000Z",
    "updatedBy": "system"
  },
  {
    "id": 2,
    "name": "Sample 2",
    "description": "Description 2",
    "active": false,
    "createdAt": "2024-01-02T09:15:00.000Z",
    "createdBy": "system",
    "updatedAt": "2024-01-02T09:15:00.000Z",
    "updatedBy": "system"
  }
]
```

### Example 5: Delete a Sample

Request:
```bash
curl -X DELETE http://localhost:8080/sample/1
```

Response: No content (HTTP 200)

### Example 6: Update with Validation Error

Request:
```bash
curl -X PUT http://localhost:8080/sample/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "ThisNameIsTooLong",
    "description": "Test",
    "active": true
  }'
```

Response (HTTP 400):
```json
{
  "status": 400,
  "message": "Validation failed",
  "errors": [
    "Name must not exceed 10 characters"
  ]
}
```

## Error Handling

All endpoints follow a consistent error response format:

```json
{
  "status": 400,
  "message": "Error description",
  "errors": [
    "Detailed error message 1",
    "Detailed error message 2"
  ]
}
```

Common error scenarios:

- **400 Bad Request**: Validation errors, invalid input data
- **404 Not Found**: Sample with specified ID does not exist (update operations)
- **500 Internal Server Error**: Unexpected server errors

## Additional Features

### Bulk Import

See dedicated endpoint for importing multiple samples:

```
POST /sample/import
```

### Initialize with Mock Data

For testing and development purposes:

```
POST /sample/init
```

### Advanced Search

For complex queries with pagination, sorting, and filtering:

```
POST /sample/search
```

See [SEARCH_API.md](SEARCH_API.md) for detailed search API documentation.

## Swagger/OpenAPI Documentation

Interactive API documentation is available at:

```
http://localhost:8080/swagger-ui/index.html
```

This provides a user-friendly interface to:
- View all available endpoints
- See detailed request/response schemas
- Test API calls directly from the browser
- View validation constraints
