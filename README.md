# Sprint 4 - Spring Boot Tasca 2 - Nivell 2

API REST con Spring Boot para gestionar frutas y proveedores, utilizando persistencia con MySQL, Spring Data JPA y Docker Compose.

## Tecnologías utilizadas

- Java 21
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA / Hibernate
- MySQL 8.4
- H2 Database (tests de integración)
- Jakarta Validation
- Maven
- JUnit
- Mockito
- MockMvc
- Docker y Docker Compose
- IntelliJ IDEA
- Git
- GitHub

## Funcionalidades

- Crear, listar, buscar por ID, actualizar y eliminar frutas.
- Crear, listar, actualizar y eliminar proveedores.
- Asociar cada fruta con un proveedor.
- Filtrar frutas por proveedor mediante.
- Comprobar que el proveedor existe antes de asociarlo a una fruta o filtrar sus frutas.
- Evitar la creación de proveedores con nombres duplicados.
- Impedir la actualización de un proveedor si el nombre pertenece a otro proveedor.
- Impedir la eliminación de un proveedor que tiene frutas asociadas.
- Persistencia con MySQL y Spring Data JPA.
- Validación de los datos de entrada.
- DTOs para las peticiones y respuestas.
- Gestión global de errores con respuestas JSON y códigos HTTP.

## Arquitectura

Organizada por funcionalidades y responsabilidades:

- **Controller**: gestiona las peticiones y respuestas HTTP.
- **Service**: contiene la lógica de negocio.
- **Repository**: gestiona el acceso a los datos mediante Spring Data JPA.
- **Model**: representa las entidades `Fruit` y `Provider`.
- **DTO**: controla los datos de entrada y salida de la API.
- **Exception**: contiene las excepciones personalizadas y la gestión global de errores.

## Persistencia

MySQL como base de datos principal ejecutada mediante Docker Compose.

Las entidades `Fruit` y `Provider` se gestionan mediante JPA. Cada fruta está asociada a un proveedor.

Los repositorios extienden `JpaRepository`, lo que permite realizar operaciones CRUD y definir consultas sin escribir manualmente SQL.

La base de datos utiliza un volumen de Docker para conservar los datos entre reinicios de los contenedores.

Para los tests de integración se usa una base de datos H2 en memoria, separada de MySQL.

## Pruebas

Comprobado de forma manual y automática.

### Manuales

PowerShell:

- Crear y consultar proveedores.
- Crear frutas asociadas a proveedores.
- Filtrar frutas por `providerId`.
- `404 Not Found` cuando el proveedor no existe.
- `400 Bad Request` al intentar eliminar un proveedor con frutas asociadas.
- Persistencia de los datos en MySQL.
- Comprobación de la relación mediante una consulta SQL `JOIN`.
- Spring Boot y MySQL ejecutándose en contenedores Docker.
- Conservar los datos después de hacer cambios en Docker Compose.

### Automáticos

- Tests de servicios con JUnit y Mockito.
- Tests de la capa web con MockMvc.
- Tests de validación y gestión de errores.
- Tests de integración de repositorios con Spring Data JPA y H2.

Los tests de integración utilizan el perfil `test`, definido en `src/test/resources/application-test.properties`, para no modificar la base de datos MySQL.

## TDD

Seguido TDD para las funcionalidades.

Primero he creado tests para definir el comportamiento esperado, después he implementado el código necesario para hacerlos pasar.

### Refactorizaciones y registro de commits

No esta hecho el commit de todas las refactorizaciones, porque me he olvidado de hacerlos cuando tocaba.

## Docker

Incluye un `Dockerfile` multi-stage y un archivo `compose.yaml` para ejecutar la API junto a MySQL.

La primera etapa utiliza Maven y Java 21 para compilar la aplicación y generar el JAR.

La segunda etapa utiliza Java 21 JRE para ejecutar el JAR, sin incluir Maven en la imagen final.

Docker Compose inicia los servicios:

- **mysql**: MySQL 8.4, accesible desde el ordenador en `localhost:3307`.
- **api**: aplicación Spring Boot, accesible en `http://localhost:8080`.

La comunicación interna entre contenedores utiliza `mysql:3306`.

### Configuración de variables de entorno

Desde la raíz del repositorio, copiar el archivo de ejemplo:

```powershell
Copy-Item .env.example .env
```

Editar `.env` y establecer valores para `MYSQL_ROOT_PASSWORD`, `MYSQL_DATABASE`, `MYSQL_USER` y `MYSQL_PASSWORD`.

El archivo `.env` está ignorado. El archivo `.env.example` sirve solo como plantilla.

### Construir y arrancar los contenedores

Desde la raíz:

```powershell
docker compose up -d --build
```

Comprobar su estado:

```powershell
docker compose ps
```

Consultar los logs de Spring Boot:

```powershell
docker compose logs api --tail=50
```

Para detener los contenedores sin eliminar el volumen de MySQL:

```powershell
docker compose down
```

> No usar `docker compose down -v` si se quieren conservar los datos, porque elimina los volúmenes gestionados por Compose.

## Ejecución de la aplicación

- Desde IntelliJ IDEA, con MySQL disponible y la variable de entorno `DB_PASSWORD` configurada.
- Mediante Maven, desde `fruit-api-MySql`, con la configuración de MySQL correspondiente.
- Mediante Docker Compose, que ejecuta conjuntamente Spring Boot y MySQL.
- Realizando peticiones HTTP desde PowerShell o `curl.exe`.

Para ejecutar la API desde IntelliJ mientras Docker ejecuta MySQL, la conexión configurada apunta a `localhost:3307`. Para ejecutarla dentro de Docker, Compose proporciona la URL interna `jdbc:mysql://mysql:3306/fruit_api_mysql`.

##UML
