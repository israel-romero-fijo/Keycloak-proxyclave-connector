# Keycloak Cl@ve Connector

Este proyecto proporciona un conector profesional (Identity Provider) para Keycloak que facilita la integración con la pasarela **Cl@ve 2.0 / EIDAS** de la Administración General del Estado de España.

## Características Profesionales

- Implementación basada en SAML 2.0.
- Preparado para la integración como Service Provider (SP) en Keycloak.
- Nombre del proveedor: `Cl@ve`.
- ID del proveedor: `clave-saml`.
- **Soporte EIDAS**: Inyección automática de la extensión `SPType` (Public/Private) requerida por el nodo eIDAS.
- **Atributos Dinámicos**: Configuración de `RequestedAttributes` para solicitar datos específicos (DNI, Nombre, etc.) al nodo eIDAS.
- **Nivel de Aseguramiento (LoA)**: Configuración configurable del LoA solicitado (Low, Substantial, High).
- **Mapeo de Atributos**: Incluye un `Clave Attribute Importer` con constantes predefinidas para extraer información de la aserción SAML.
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

1. Copia `target/keycloak-clave-connector-1.2.0-SNAPSHOT.jar` al directorio `providers/` de Keycloak.
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
  - Lista de URIs de atributos a solicitar. Por defecto incluye identificador, nombre, apellidos y fecha de nacimiento.

### Mapeo de Atributos (Cl@ve Attributes)
Cl@ve devuelve una serie de atributos en la respuesta SAML. Puedes mapearlos usando el **Cl@ve User Attribute Mapper** incluido:

1. Crea un nuevo Identity Provider de tipo **Cl@ve**.
2. Añade un "Mapper" de tipo **Cl@ve Attribute Importer**.
3. El mapper facilita el uso de los atributos estándar eIDAS:
   - `http://eidas.europa.eu/attributes/naturalperson/PersonIdentifier` (DNI/NIE)
   - `http://eidas.europa.eu/attributes/naturalperson/CurrentGivenName` (Nombre)
   - `http://eidas.europa.eu/attributes/naturalperson/CurrentFamilyName` (Apellidos)
   - `http://eidas.europa.eu/attributes/naturalperson/DateOfBirth` (Fecha de Nacimiento)

## Desarrollo y Calidad

El proyecto sigue los estándares de desarrollo de Keycloak, utilizando:
- **JBoss Logging** para el sistema de trazas con niveles de debug mejorados.
- **JUnit 5 y Mockito** para pruebas unitarias.
- **SPI de Keycloak** para la extensibilidad.

---
*Desarrollado para garantizar integraciones seguras y eficientes con la administración pública española.*
