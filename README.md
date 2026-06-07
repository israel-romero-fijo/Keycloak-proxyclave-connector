# Keycloak Cl@ve Connector

Este proyecto proporciona un conector (Identity Provider) para Keycloak que facilita la integración con la pasarela Cl@ve (Cl@ve 2.0 / EIDAS) de la Administración General del Estado de España.

## Características Profesionales

- **Implementación Robusta**: Basada en SAML 2.0 con soporte completo para **Cl@ve 2.0** y Keycloak 24+.
- **Certificados de Curva Elíptica (EC)**: Soporte nativo para el uso de certificados EC en la firma de peticiones (ECDSA_SHA256/384/512).
- **Soporte EIDAS Completo**: Inyección automática de la extensión `SPType` y gestión de Niveles de Seguridad (LoA).
- **Internacionalización (i18n)**: Interfaz de configuración disponible en Inglés y Español.
- **Mapeo de Atributos**: Incluye un `ClaveAttributeMapper` especializado para facilitar la importación de atributos eIDAS.
- **Logging Estándar**: Uso de JBoss Logging para una integración perfecta con los logs de Keycloak.
- **Configuración Optimizada**: Valores por defecto ajustados para la normativa española de administración electrónica.

## Requisitos

- Java 17+
- Maven 3.x
- Keycloak 24.x o superior

## Compilación

Para compilar el proyecto y generar el archivo JAR:

```bash
mvn clean package
```

El archivo resultante estará en `target/keycloak-clave-connector-1.1.0-SNAPSHOT.jar`.

## Instalación

1. Copia el archivo JAR generado al directorio `providers/` de tu instalación de Keycloak.
2. Reinicia Keycloak (o ejecuta `kc.sh build` si estás en modo optimizado).

## Configuración en Keycloak

1. Accede a la consola de administración de Keycloak.
2. Ve a la sección **Identity Providers**.
3. Haz clic en **Add provider** y selecciona **Cl@ve** de la lista.
4. Parámetros específicos:
   - **eIDAS SP Type**: Selecciona `public` o `private`.
   - **eIDAS Level of Assurance**: Selecciona el nivel mínimo requerido (Low, Substantial, High).

### Certificados de Curva Elíptica (EC)

Este conector permite el uso de algoritmos basados en curvas elípticas para la firma de mensajes SAML. Para utilizarlo:
1. Asegúrate de que tu Reino (Realm) en Keycloak tiene una clave de tipo `EC` configurada.
2. En la configuración del proveedor Cl@ve, selecciona un algoritmo de firma compatible (ej. `ECDSA_SHA256`).
3. El conector seleccionará automáticamente la clave EC activa del reino para realizar la firma.

### Mapeo de Atributos

Puedes añadir mappers de tipo **Cl@ve User Attribute Mapper** para extraer información como el DNI/NIE, nombre o apellidos desde la aserción SAML de Cl@ve hacia el perfil del usuario en Keycloak.

## Desarrollo y Tests

El proyecto incluye tests unitarios con JUnit 5. Para ejecutarlos:

```bash
mvn test
```

## Licencia

Este proyecto se distribuye bajo licencia Apache 2.0.
