# Keycloak Cl@ve Connector

Este proyecto proporciona un conector profesional (Identity Provider) para Keycloak que facilita la integración con la pasarela **Cl@ve 2.0 / EIDAS** de la Administración General del Estado de España.

## Características Profesionales

- Implementación basada en SAML 2.0.
- Preparado para la integración como Service Provider (SP) en Keycloak.
- Nombre del proveedor: `Cl@ve`.
- ID del proveedor: `clave-saml`.
- **Soporte EIDAS**: Inyección automática de la extensión `SPType` (Public/Private) requerida por el nodo eIDAS.
- **Atributos Solicitados**: Configuración de `RequestedAttributes` en la extensión eIDAS del AuthnRequest.
- **Nivel de Aseguramiento (LoA)**: Configuración configurable del LoA solicitado (Low, Substantial, High).
- **Mapeo de Atributos**: Incluye un `Clave Attribute Importer` optimizado con constantes para atributos eIDAS comunes.
- **Internacionalización**: Soporte para mensajes en Inglés y Español.
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

1. Copia `target/keycloak-clave-connector-1.1.0-SNAPSHOT.jar` al directorio `providers/` de Keycloak.
2. Ejecuta `kc.sh build` (opcional según el modo de despliegue).
3. Inicia Keycloak.

## Configuración Detallada

### Parámetros del Proveedor de Identidad

- **eIDAS SP Type**:
  - `Public`: Para organismos de la Administración Pública.
  - `Private`: Para entidades privadas autorizadas.
- **eIDAS Level of Assurance**:
  - `Substantial`: Valor por defecto, recomendado para la mayoría de trámites.
  - `High`: Para trámites que requieran el máximo nivel de seguridad.
- **eIDAS Requested Attributes**:
  - Lista de URIs de atributos separados por comas. Ejemplo:
    `http://eidas.europa.eu/attributes/naturalperson/PersonIdentifier, http://eidas.europa.eu/attributes/naturalperson/CurrentGivenName`

### Mapeo de Atributos (Cl@ve Attributes)

Cl@ve devuelve una serie de atributos en la respuesta SAML. Puedes mapearlos usando el **Cl@ve User Attribute Mapper** incluido.

Atributos comunes (disponibles como constantes en el mapper):
- `http://eidas.europa.eu/attributes/naturalperson/PersonIdentifier` (DNI/NIE)
- `http://eidas.europa.eu/attributes/naturalperson/CurrentGivenName` (Nombre)
- `http://eidas.europa.eu/attributes/naturalperson/CurrentFamilyName` (Apellidos)

## Desarrollo y Calidad

El proyecto sigue los estándares de desarrollo de Keycloak:
- **JBoss Logging** para el sistema de trazas.
- **JUnit 5 y Mockito** para pruebas unitarias.
- **Extensiones eIDAS**: Generación dinámica de `SPType` y `RequestedAttributes`.

---
*Desarrollado para garantizar integraciones seguras y eficientes con la administración pública española.*
