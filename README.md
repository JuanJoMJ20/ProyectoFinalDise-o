# FitTrack Pro - Sistema Integral de Gestión y Asesoría Fitness

Este repositorio contiene el código fuente completo (Backend y Frontend) para el sistema **FitTrack Pro**, un proyecto arquitectónico diseñado para gestionar atletas, planes de entrenamiento (ej. hipertrofia), metas de peso y pagos/suscripciones.

## 📖 Caso de Estudio y Requisitos

**FitTrack Pro** nace de la necesidad de centralizar el ecosistema fitness, permitiendo a los atletas realizar un seguimiento riguroso de sus medidas, rutinas y suscripciones desde una plataforma unificada. 

### Requisitos Funcionales
1. **Gestión de Usuarios:** El sistema debe permitir registrar usuarios (atletas) especificando su nombre, fecha de nacimiento, peso actual y meta de peso.
2. **Asignación de Rutinas:** El sistema debe permitir asignar planes de entrenamiento (ej. "Plan Hipertrofia") a los usuarios.
3. **Seguimiento de Progreso:** El usuario debe poder actualizar su `pesoActual` para medir el acercamiento a su `metaPeso`.
4. **Gestión de Pagos y Suscripciones:** El sistema debe simular el procesamiento de una transacción financiera y extender la fecha de vencimiento de la suscripción del atleta.
5. **Notificaciones Automáticas:** El sistema debe disparar eventos asíncronos cuando se asignan rutinas o se vencen pagos.

### Requisitos No Funcionales
1. **Rendimiento:** Las consultas principales a la API REST deben responder en un tiempo óptimo (ej. <200ms).
2. **Seguridad y Validación:** La API debe validar que no ingresen datos corruptos (ej. pesos negativos o fechas en el futuro) usando `Jakarta Validation` y devolver códigos HTTP 400 estructurados.
3. **Escalabilidad:** El backend debe estar diseñado con inyección de dependencias y arquitectura en capas para permitir el fácil acoplamiento de nuevas pasarelas de pago.
4. **Mantenibilidad:** El código debe usar DTOs para separar las entidades de la base de datos de los contratos de la API.

---

## 🛠️ Explicación de las Tecnologías Elegidas

Para asegurar una arquitectura robusta, escalable y moderna, se seleccionó el siguiente Stack Tecnológico:

### 1. Spring Boot (Java 17) - Backend
**¿Por qué?** Spring Boot ofrece un ecosistema maduro y altamente estructurado. Nos permite aplicar patrones de diseño (Inyección de Dependencias, Capas, DTOs, Eventos) de forma nativa. Su integración con Spring Data JPA simplifica enormemente la persistencia relacional, haciéndolo ideal para la lógica financiera y de suscripciones.

### 2. PostgreSQL / MySQL - Base de Datos
**¿Por qué?** La relación entre un Usuario, su Suscripción, sus Transacciones y sus Múltiples Planes de Entrenamiento es estrictamente relacional. Las bases de datos SQL garantizan la integridad referencial (ACID), lo cual es crítico cuando se manejan pagos y fechas de vencimiento.

### 3. React + Vite + TailwindCSS - Frontend
**¿Por qué?** 
- **React** permite construir la interfaz de usuario modularizada (componentes reutilizables). 
- **Vite** reemplaza herramientas lentas y ofrece un entorno de desarrollo ultra-rápido.
- **TailwindCSS** nos permitió implementar rápidamente un diseño deportivo, moderno y en modo oscuro (`bg-slate-900`, `text-emerald-400`) sin tener que saltar entre cientos de archivos CSS.

---

## 🚀 Instrucciones de Instalación y Ejecución

Sigue estos pasos para arrancar el proyecto en un entorno local:

### Requisitos Previos
- Java JDK 17 o superior.
- Node.js (v18 o superior).
- Base de datos PostgreSQL ejecutándose en el puerto 5432 (crear base de datos llamada `fittrack`).

### Paso 1: Levantar el Backend (API REST)
1. Abre una terminal y navega a la carpeta del backend:
   ```bash
   cd fittrack-backend
   ```
2. Asegúrate de configurar tus credenciales en `src/main/resources/application.properties`:
   ```properties
   spring.datasource.username=postgres
   spring.datasource.password=TuContraseña
   ```
3. Inicia el servidor usando el Wrapper de Maven (o ejecutando el `FitTrackApplication.java` en tu IDE):
   ```bash
   ./mvnw spring-boot:run
   ```
   *El servidor iniciará en `http://localhost:8080`. Se ejecutarán los `DatabaseSeeder` para crear el usuario "Juan Jo" y los planes de entrenamiento por defecto.*

### Paso 2: Levantar el Frontend (Cliente)
1. Abre **una nueva terminal** y navega a la carpeta del frontend:
   ```bash
   cd fittrack-frontend
   ```
2. Instala las dependencias de Node:
   ```bash
   npm install
   ```
3. Arranca el servidor de desarrollo Vite:
   ```bash
   npm run dev
   ```
4. Abre tu navegador y dirígete a `http://localhost:5173`. Verás el Dashboard consumiendo directamente la API de Spring Boot.

---
*Desarrollado como Proyecto Final de Diseño Arquitectónico.*
