# Keycloak Cl@ve Connector

Este proyecto proporciona un conector profesional (Identity Provider) para Keycloak que facilita la integración con la pasarela **Cl@ve 2.0 / EIDAS** de la Administración General del Estado de España.

## Características Profesionales

- **Implementación SAML 2.0 Nativa**: Construido sobre las APIs de Keycloak para una máxima compatibilidad.
- **Soporte eIDAS Completo**: Inyección automática de la extensión `<eidas:SPType>` requerida.
- **Gestión de LoA**: Configuración sencilla del *Level of Assurance* (Bajo, Sustancial, Alto) directamente desde la consola.
- **Internacionalización (i18n)**: Interfaz de configuración disponible en Español e Inglés.
- **Mapeador de Atributos Personalizado**: Incluye `Cl@ve Attribute Importer` para facilitar la extracción de datos del ciudadano (DNI, nombre, apellidos).
- **Seguridad por Defecto**: Configurado con algoritmos de firma modernos (RSA_SHA256) y políticas de NameID persistentes.
- **Registro de Logs Avanzado**: Uso de JBoss Logging para una trazabilidad clara y depuración simplificada.

## Requisitos

- Java 17+
- Maven 3.x
- Keycloak 24.x o superior

## Compilación

Para generar el artefacto profesional:

```bash
mvn clean package
```

El archivo JAR se generará en `target/keycloak-clave-connector-1.0.0-SNAPSHOT.jar`.

## Instalación

1. Copia el archivo JAR a la carpeta `providers/` de Keycloak.
2. Ejecuta `kc.sh build` (si usas Quarkus) y reinicia el servicio.

## Configuración en Keycloak

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
