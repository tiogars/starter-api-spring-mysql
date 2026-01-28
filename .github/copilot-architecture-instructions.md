# Copilot Architecture Instructions for API Response Wrapping

## Service Layer

- All service methods that expose data for controllers **must return a response wrapper object** (e.g., `FindResponse<T>`) instead of raw lists or optionals.
- The response wrapper should include:
  - The data list (or single item as a list)
  - A count field (number of items)
  - Optionally, metadata (pagination, status, etc.)
- Place response wrapper classes in `fr.tiogars.starter.common.services.dto`.

## Controller Layer

- Controllers should return the response wrapper directly from service calls for consistency and API clarity.
- Do not expose raw entity or model lists in controller responses.
- Use the response wrapper for all find/list/get endpoints.

## DTOs and API Contracts

- All API responses must be documented and consistent.
- Use DTOs for all data exposed by the API; never expose JPA entities directly.

## Example

```java
// In service:
public FindResponse<MyModel> findAll() { ... }

// In controller:
@GetMapping
public FindResponse<MyModel> getAll() {
    return myService.findAll();
}
```

## Testing

- Add/modify tests to assert the structure and content of the response wrapper.

## Migration

- When refactoring, update all usages of service find methods and controller endpoints to use the new response wrapper.

## Review Checklist

- [ ] All find/get/list endpoints use a response wrapper
- [ ] No controller exposes raw lists or optionals
- [ ] Response wrapper includes count and data fields
- [ ] DTOs are used for all API responses