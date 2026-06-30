###### Enunciado del Examen ######

El estudiante debe desarrollar **uno de los siguientes microservicios** e integrarlo a la infraestructura existente (Eureka Server, API Gateway, Login Service, Destination Service y Traveler Service).  
Cada microservicio debe tener **máximo 3 tablas** y cumplir con los criterios de evaluación establecidos.

---

## Opciones de Microservicio

### Opción 1: Microservicio de Itinerario
- **Descripción:** Permite registrar los lugares que el usuario desea visitar en su viaje.  
- **Alcance:**  
  - Se deben clasificar los lugares como **Hoteles, Tours o Sitios Turísticos**.  
  - El itinerario debe estar asociado a un viaje existente (Traveler Service).  
- **Ejemplo:**  
  - Viaje a "Santiago" → Itinerario: Hotel Plaza, Tour Viña Concha y Toro, Cerro San Cristóbal.  

---

### Opción 2: Microservicio de Reseñas
- **Descripción:** Permite registrar reseñas y notas de los lugares visitados.  
- **Alcance:**  
  - Cada reseña debe estar asociada a un destino (Destination Service).  
  - El usuario autenticado (Login Service) debe ser el autor de la reseña.  
- **Ejemplo:**  
  - Usuario: Juan Pérez → Reseña: "Hotel Plaza" → Nota: 4/5, Comentario: "Muy buena atención".  

---

###### Arquitectura de Microservicios - Proyecto DUOC ######

Este proyecto implementa una arquitectura basada en **Spring Boot + Spring Cloud** con servicios independientes, gestionados mediante **Eureka Server** y expuestos a través de un **API Gateway**.  
Cada servicio tiene su propia base de datos MySQL, orquestada con **Docker Compose**.

---

## Servicios Actuales

### 1. Eureka Server
- **ArtifactId:** `eurekaserver`
- **Puerto:** `8761` (por defecto, configurable en `application.yml`)
- **Tecnologías:**
  - Spring Boot 4.0.6
  - Spring Cloud Netflix Eureka Server
- **Base de datos:** No aplica

---

### 2. API Gateway
- **ArtifactId:** `gateway`
- **Puerto:** `9999` (habitual, configurable en `application.yml`)
- **Tecnologías:**
  - Spring Boot 3.2.5
  - Spring Cloud Gateway
  - Eureka Client
- **Base de datos:** No aplica

---

### 3. Login Service
- **ArtifactId:** `login`
- **Puerto:** `9000`
- **Tecnologías:**
  - Spring Boot 4.0.6
  - Spring Security + JWT
  - Spring Data JPA
  - Flyway (migraciones)
  - Eureka Client
  - Swagger (OpenAPI)
- **Base de datos:** MySQL (`mysql_login`)
  - Host: `localhost`
  - Puerto: `3310`
  - DB: `login`
  - Usuario: `examen`
  - Password: `examen123`

---

### 4. Destination Service
- **ArtifactId:** `destination`
- **Puerto:** `9001`
- **Tecnologías:**
  - Spring Boot 4.0.7
  - Spring Data JPA
  - Flyway
  - Spring WebFlux
  - Spring Security
  - Eureka Client
  - Swagger (OpenAPI)
- **Base de datos:** MySQL (`mysql_destination`)
  - Host: `localhost`
  - Puerto: `3311`
  - DB: `destination`
  - Usuario: `examen`
  - Password: `examen123`

---

### 5. Traveler Service
- **ArtifactId:** `traveler`
- **Puerto:** `9002`
- **Tecnologías:**
  - Spring Boot
  - Spring Data JPA
  - Spring WebClient (para consumir otros servicios)
  - Eureka Client
  - Swagger (OpenAPI)
- **Base de datos:** MySQL (`mysql_traveler`)
  - Host: `localhost`
  - Puerto: `3312`
  - DB: `traveler`
  - Usuario: `examen`
  - Password: `examen123`

---

## Bases de Datos (Docker Compose)

- **mysql_login** → puerto `3310`, DB `login`
- **mysql_destination** → puerto `3311`, DB `destination`
- **mysql_traveler** → puerto `3312`, DB `traveler`

Cada servicio tiene su propia base de datos aislada, con usuario `examen` y contraseña `examen123`.

---

## Flujo General

1. **Eureka Server** registra todos los microservicios.  
2. **API Gateway** expone las rutas y enruta las peticiones hacia cada servicio.  
3. **Login Service** gestiona autenticación y generación de tokens JWT.  
4. **Destination Service** administra destinos y validaciones.  
5. **Traveler Service** consume Login y Destination para crear y listar viajes.  

---

## Estado Actual
- 5 microservicios activos (Eureka, Gateway, Login, Destination, Traveler).  
- 3 bases de datos MySQL independientes.  
- Seguridad basada en **JWT**.  
- Documentación de APIs con **Swagger/OpenAPI**.  



###### Checklist de Implementación para el examen ######

1. **Repositorio GitHub**
   - Subir el proyecto entregado en formato `.rar` a GitHub.  
   - Compartir como colaborador al correo: `vi.cofref@profesor.duoc.cl`.  
   - Realizar commits constantes y progresivos para cumplir con **IE 2.5.1**.

2. **Modelado de Base de Datos**
   - Crear máximo 3 tablas con entidades JPA.  
   - Definir relaciones simples (OneToMany o ManyToOne).  
   - Incluir migraciones con **Flyway** para inicializar la base de datos.

3. **Arquitectura CSR**
   - Implementar **Controller – Service – Repository/Model**.  
   - Separar responsabilidades en paquetes por capa.  
   - CRUD completo para las entidades principales.

4. **Validación y Seguridad**
   - Todas las respuestas deben usar la estructura `ApiResponse`.  
   - Validar token mediante `AuthService` en cada endpoint.  
   - Aplicar **Bean Validation** en DTOs (ejemplo: `@NotNull`, `@Size`, `@Email`).  

5. **Comunicación entre Microservicios**
   - El nuevo microservicio debe comunicarse con:  
     - **Login Service** → Validación de token.  
     - **Destination Service** → Validación de destino (Reseñas).  
     - **Traveler Service** → Validación de viaje (Itinerario).  
   - Usar **WebClient** para consumo de endpoints remotos.  

6. **Configuración YML**
   - Definir `application.yml` con:  
     - Puerto único (ejemplo: `9003`).  
     - Configuración de DB MySQL (usuario `examen`, password `examen123`).  
     - Registro en Eureka.  

7. **Docker Compose**
   - Agregar un nuevo servicio MySQL en `docker-compose.yml`.  
   - Ejemplo:  
     ```yaml
     mysql_itinerary:
       image: mysql:8.0
       container_name: mysql_itinerary
       environment:
         MYSQL_ROOT_PASSWORD: root
         MYSQL_DATABASE: itinerary
         MYSQL_USER: examen
         MYSQL_PASSWORD: examen123
       ports:
         - "3313:3306"
       volumes:
         - mysql_itinerary_data:/var/lib/mysql
     ```
   - Agregar volumen correspondiente.

8. **API Gateway**
   - Configurar ruta en `application.yml` del Gateway:  
     ```yaml
     spring:
       cloud:
         gateway:
           routes:
             - id: itinerary
               uri: lb://itinerary
               predicates:
                 - Path=/api/v1/itinerary/**
     ```

9. **Documentación y Pruebas**
   - Documentar endpoints con **Swagger**.  
   - Incluir ejemplos de request/response en la documentación.  
   - Implementar pruebas unitarias con **Spring Boot Test**.  

10. **README del Proyecto**
    - Explicar el modelado de entidades y relaciones (**IE 2.1.3**).  
    - Explicar el flujo de comunicación entre microservicios (**IE 2.4.2**).  
    - Explicar aportes personales y commits (**IE 2.5.2**).  
    - Explicar pruebas unitarias (**IE 3.1.2**).  
    - Explicar documentación técnica con Swagger (**IE 3.2.2**).  

---

 **Importante:**  
El estudiante o los estudiantes deben elegir **solo una opción (Itinerario o Reseñas)** y cumplir con todos los criterios de evaluación.

###### Criterios a evaluar de acuerdo a la rubrica ######

IE 1.1.1 Diseña endpoints REST aplicando principios de arquitectura distribuida, asignando métodos HTTP y nombres semánticos según la responsabilidad del Indicador de Evaluación. 

IE 1.2.1 Estructura el microservicio aplicando el patrón CSR (Controller– Service– Repository/Model), con separación real de responsabilidades y paquetes por capa. 

IE 1.3.1 Implementa mecanismos de validación de datos de entrada, asegurando la integridad de la información procesada por el microservicio. 

IE 1.3.2 Aplica estructuras de manejo de para controlar errores y mantener la estabilidad del servicio. 

IE 2.1.1 Modela esquemas de base de datos normalizados y coherentes con el dominio, definiendo entidades JPA, atributos y relaciones según principios de diseño relacional y desacoplamiento, e integra operaciones CRUD completas conectando repositorios JpaRepository con la capa de servicio y los controladores REST, garantizando retornos JSON consistentes, validaciones aplicadas y funcionamiento estable del flujo interno del microservicio. 

IE 2.2.1 Implementa reglas de negocio y validaciones con Bean Validation en la capa de servicio, asegurando integridad de datos, coherencia del dominio y cumplimiento de restricciones funcionales definidas para el contexto del proyecto. 

IE 2.3.1 Implementa manejo global y específico de excepciones, integrando logs estructurados para trazabilidad del sistema, garantizando respuestas claras, códigos HTTP adecuados y registro de eventos que permitan analizar errores en tiempo de ejecución. 

IE 2.4.1 Implementa comunicación REST entre microservicios mediante consumo o exposición de datos remotos, utilizando DTOs, controlando errores remotos y asegurando interoperabilidad completa en los flujos distribuidos del sistema, respetando convenciones REST y coherencia de datos. 

IE 2.5.1 Gestiona un repositorio GitHub ordenado, con commits técnicos, progresivos y distribuidos equitativamente entre los integrantes. 

IE 3.1.1 Desarrolla pruebas unitarias para validar la lógica del microservicio, aplicando correctamente frameworks de testing y asegurando cobertura mínima sobre funciones y reglas de negocio. 

IE 3.2.1 Elabora documentación técnica clara, estructurada y trazable utilizando Swagger, describiendo modelos, parámetros, respuestas, rutas, ejemplos funcionales y comportamiento real del microservicio. 

IE 3.3.1 Configura correctamente los archivos YAML, propiedades de entorno, rutas del API Gateway y perfiles, y ejecuta el despliegue del ecosistema de microservicios en entornos locales o remotos (Docker), validando puertos, conexiones, disponibilidad y logs de ejecución, y describiendo el funcionamiento final del sistema distribuido.

IE 2.1.3 Explica con claridad el modelado de entidades, relaciones y estructura del microservicio, justificando decisiones técnicas, normalización, consistencia del dominio y coherencia entre capas del patrón CSR. (ARCHIVO README EN EL PROYECTO)

IE 2.4.2 Explica el flujo de comunicación entre microservicios, demostrando la correcta obtención y uso de información remota, justificando su manejo y su impacto en el flujo del negocio, y realiza cambios en la comunicación remota comprobando la correcta obtención y uso de la información. (ARCHIVO README EN EL PROYECTO)

IE 2.5.2 Explica su aporte personal al proyecto, justificando commits, tareas asignadas y participación individual en el desarrollo del proyecto semestral, respaldando su relato con evidencia en las herramientas colaborativas utilizadas. (ARCHIVO README EN EL PROYECTO EN CASO DE TRABAJR EN PAREJAS)

IE 3.1.2 Explica con claridad el funcionamiento de las pruebas unitarias generadas en el proyecto, justificando decisiones técnicas y relacionándolas con el dominio del microservicio, e integra nuevas pruebas unitarias en tiempo acotado, aplicando correctamente la lógica requerida y manteniendo la consistencia del proyecto. (ARCHIVO README EN EL PROYECTO)

IE 3.2.2 Explica la documentación técnica generada con Swagger/OpenAPI, recorriendo rutas, respuestas, modelos y ejemplos, y demostrando consistencia entre la documentación y el comportamiento real del código. (ARCHIVO README EN EL PROYECTO)