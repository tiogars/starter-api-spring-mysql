# Copilot instructions for starter-api-spring-mysql

This repo is a Spring Boot 3.5 (Java 21) REST API backed by MySQL with a simple, opinionated layering and CI/CD via GitHub Actions. Use the conventions below to stay productive and consistent with the codebase.

## Architecture and conventions
- Layers and packages:
  - controllers: REST endpoints, minimal logic (e.g. `SampleController`, `RouteDefinitionController`).
  - service: business logic and mapping between persistence and API models (e.g. `SampleCreateService`, `SampleCrudService`, `RouteService`).
  - repository: Spring Data JPA repositories (`JpaRepository<..., Long>`), e.g. `SampleRepository`.
  - entities: JPA entities (DB shape), e.g. `SampleEntity`.
  - models: API/domain models (response shape) with Bean Validation annotations, e.g. `Sample`.
  - forms: request DTOs for create/update, e.g. `SampleCreateForm`, `RouteCreateForm`, `RouteUpdateForm`.
- Don’t expose entities from controllers. Map entity ⇄ model in services.
- Validation:
  - Input DTOs → convert to model in service, then validate model with `jakarta.validation.Validator`.
  - On validation errors, throw `ConstraintViolationException`; `GlobalExceptionHandler` returns `ErrorResponse { status, error, message, violations }`.
- External base libs from GitHub Packages:
  - `fr.tiogars:architecture-create-service` and `fr.tiogars:architecture-select-service` provide base classes (e.g., `AbstractCreateService`, `AbstractCreateForm`, `AbstractEntity`). Ensure Maven can read GitHub Packages (see `settings.xml.example`).
- OpenAPI/Swagger via springdoc; annotate controllers with `@Tag`, methods with `@Operation`/`@ApiResponses` when useful.

## Typical data flow
HTTP → controller (request DTO) → service (toModel, validate, toEntity, CRUD via repository) → service (toModel) → controller returns model.

## Adding a new resource (example)
1. Create JPA entity in `entities/` and API model in `models/` (put Bean Validation on model fields).
2. Create request forms in `forms/` (e.g., `ThingCreateForm`, `ThingUpdateForm`).
3. Create repository `ThingRepository extends JpaRepository<ThingEntity, Long>`.
4. Create service with mappings:
   - toModel(Entity) and toEntity(Form/Model)
   - validate(Model) using `Validator`; throw `ConstraintViolationException` on failures.
5. Create controller endpoints that accept forms and return models. Example patterns:
   - `POST /sample` with `SampleCreateForm` → returns `Sample`.
   - `GET /routes`, `POST /routes`, `GET /routes/{id}`, `PUT /routes/{id}`, `DELETE /routes/{id}`.
6. Document with Swagger annotations and keep controller logic thin.

## Runtime configuration and endpoints
- App port: 8080 (`application.yml`). Actuator: `/actuator/{health,info}`; Swagger UI: `/swagger-ui/index.html`.
- Default MySQL URL: `jdbc:mysql://localhost:3306/starterdb` (user `root`, password `rootpassword`). Override via env vars when containerized.

## Local dev, builds, and tests (Windows PowerShell examples)
- Quick path with helper script:
  - `./cicd.ps1 setup` (configure Maven for GitHub Packages)
  - `./cicd.ps1 build`, `./cicd.ps1 test`, `./cicd.ps1 docker-build`, `./cicd.ps1 release <patch|minor|major>`
- Maven wrapper:
  - Compile: `./mvnw.cmd compile`  •  Test: `./mvnw.cmd test`  •  Run: `./mvnw.cmd spring-boot:run`
  - Full verify: `./mvnw.cmd clean verify`
- Docker/Compose:
  - `docker-compose up -d` brings up MySQL and API variants; API images expose 8080 (mapped to 8081 in compose for `api`/`api-local`).

## CI/CD notes
- Workflows build/test on push/PR; on tags (`v*.*.*`) they create releases and publish multi-tagged Docker images to GHCR. See `.github/workflows/README.md` and `ARCHITECTURE.md` for diagrams and triggers.

## Examples in code
- Validation and error shape: see `models/Sample.java`, `exceptions/GlobalExceptionHandler.java`, `exceptions/ErrorResponse.java`.
- CRUD mapping patterns: `service/SampleCrudService.java`, `service/RouteService.java`.
- Controller style and Swagger tags: `controllers/SampleController.java`, `controllers/RouteDefinitionController.java`.

## Project-specific tips
- Prefer returning models from controllers; keep mapping logic centralized in services.
- Keep endpoints simple (no class-level `@RequestMapping` prefixes are used in current controllers).
- Follow Conventional Commits for commit messages.

If anything here is unclear or you notice a convention that isn’t captured, open an issue or ask to refine this document.