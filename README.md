# msa-customer-notes

Microservicio Spring Boot (Gradle) para notas de clientes.

Características:
- Paquete base: `com.customer.note`
- Spring Boot Web, Spring Data JPA, H2 (en memoria)
- Lombok

Ejecutar:
- `./gradlew bootRun` (Windows: `gradlew.bat bootRun`)

Endpoints:
- `GET /api/notes` — listar todas
- `GET /api/notes/{id}` — obtener por id
- `GET /api/notes/customer/{nodeId}` — notas de cliente
- `POST /api/notes` — crear (JSON: `nodeId`, `content`)
- `PUT /api/notes/{id}` — actualizar
- `DELETE /api/notes/{id}` — eliminar
