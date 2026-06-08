# Keycloak Cl@ve Connector

Este proyecto proporciona un conector profesional (Identity Provider) para Keycloak que facilita la integración con la pasarela **Cl@ve 2.0 / EIDAS** de la Administración General del Estado de España.

## Características Profesionales

- Implementación basada en SAML 2.0.
- Preparado para la integración como Service Provider (SP) en Keycloak.
- Nombre del proveedor: `Cl@ve`.
- ID del proveedor: `clave-saml`.
- **Soporte EIDAS**: Inyección automática de la extensión `SPType` (Public/Private) requerida por el nodo eIDAS.
- **Nivel de Aseguramiento (LoA)**: Configuración configurable del LoA solicitado (Low, Substantial, High).
- **Extensión RequestedAttributes**: Capacidad de solicitar atributos específicos en la AuthnRequest según el estándar eIDAS.
- **Mapeo de Atributos**: Incluye un `Clave Attribute Importer` con constantes para atributos estándar (DNI, Nombre, Apellidos, Fecha de Nacimiento).
- **Internacionalización**: Soporte completo para mensajes en Inglés y Español.
- **Configuración Optimizada**: Valores por defecto ajustados para Cl@ve (firmas activas, RSA_SHA256, Persistent NameID).

## Requisitos

- Java 17+
- Maven 3.x
- Keycloak 24.x o superior

## Compilación

Para generar el artefacto profesional:

```bash
mvn clean package
```

1. Copia `target/keycloak-clave-connector-1.0.0-SNAPSHOT.jar` al directorio `providers/` de Keycloak.
2. Ejecuta `kc.sh build` (opcional según el modo de despliegue).
3. Inicia Keycloak.

## Configuración Detallada

### Parámetros del Proveedor de Identidad
El archivo JAR se generará en `target/keycloak-clave-connector-1.0.0-SNAPSHOT.jar`.

- **eIDAS SP Type**:
  - `Public`: Para organismos de la Administración Pública.
  - `Private`: Para entidades privadas autorizadas.
- **eIDAS Level of Assurance**:
  - `Substantial`: Valor por defecto, recomendado para la mayoría de trámites.
  - `High`: Para trámites que requieran el máximo nivel de seguridad.
- **eIDAS Requested Attributes**:
  - Lista separada por comas de URIs de atributos. Por defecto incluye el identificador personal, nombre y apellidos.

### Mapeo de Atributos (Cl@ve Attributes)
1. Copia el archivo JAR a la carpeta `providers/` de Keycloak.
2. Ejecuta `kc.sh build` (si usas Quarkus) y reinicia el servicio.

Cl@ve devuelve una serie de atributos en la respuesta SAML. Puedes mapearlos usando el **Cl@ve User Attribute Mapper** incluido:

1. Crea un nuevo Identity Provider de tipo **Cl@ve**.
2. Parámetros clave:
   - **Service Provider Entity ID**: Tu identificador oficial registrado en Cl@ve.
   - **Single Sign-On Service URL**: URL del nodo eIDAS / Cl@ve.
   - **eIDAS SP Type**: Selecciona si tu organización es `public` o `private`.
   - **eIDAS Level of Assurance**: Selecciona el nivel mínimo requerido (ej. `Sustancial`).

### Mapeo de Atributos

Para importar datos como el DNI, añade un "Mapper" de tipo **Cl@ve Attribute Importer** al proveedor configurado. Los atributos comunes enviados por Cl@ve incluyen:
- `http://eidas.europa.eu/attributes/naturalperson/PersonIdentifier` (DNI/NIE)
- `http://eidas.europa.eu/attributes/naturalperson/CurrentGivenName` (Nombre)
- `http://eidas.europa.eu/attributes/naturalperson/CurrentFamilyName` (Apellidos)

## Desarrollo y Calidad

El proyecto sigue los estándares de desarrollo de Keycloak, utilizando:
- **JBoss Logging** para el sistema de trazas.
- **JUnit 5 y Mockito** para pruebas unitarias.
- **SPI de Keycloak** para la extensibilidad.

---
*Desarrollado para garantizar integraciones seguras y eficientes con la administración pública española.*
