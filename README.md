# Keycloak Cl@ve Connector

Este proyecto proporciona un conector (Identity Provider) para Keycloak que facilita la integración con la pasarela Cl@ve (Cl@ve 2.0 / EIDAS) de la Administración General del Estado de España.

## Características

- Implementación basada en SAML 2.0.
- Preparado para la integración como Service Provider (SP) en Keycloak.
- Nombre del proveedor: `Cl@ve`.
- ID del proveedor: `clave-saml`.
- **Soporte EIDAS**: Inyección automática de la extensión `SPType` (Public/Private) requerida por el nodo eIDAS.
- **Configuración Optimizada**: Valores por defecto ajustados para Cl@ve (firmas activas, RSA_SHA256).

## Requisitos

- Java 17+
- Maven 3.x
- Keycloak 24.x (o compatible con las APIs de SAML)

## Compilación

Para compilar el proyecto y generar el archivo JAR:

```bash
mvn clean package
```

El archivo resultante estará en `target/keycloak-clave-connector-1.0.0-SNAPSHOT.jar`.

## Instalación

1. Copia el archivo JAR generado al directorio `providers/` de tu instalación de Keycloak.
2. Reinicia Keycloak (o ejecuta `kc.sh build` si es necesario en modo producción).

## Configuración en Keycloak

1. Accede a la consola de administración de Keycloak.
2. Ve a la sección **Identity Providers**.
3. Haz clic en **Add provider** y selecciona **Cl@ve** de la lista.
4. Configura los parámetros:
   - **Service Provider Entity ID**: El Entity ID que usarás para este SP (debes registrarlo en Cl@ve).
   - **Single Sign-On Service URL**: La URL del IdP de Cl@ve (entorno de pruebas o producción).
   - **eIDAS SP Type**: Selecciona `public` o `private` según tu tipo de organización (nuevo campo específico).
   - **Sign Documents**: Activado por defecto.
   - **Signature Algorithm**: RSA_SHA256 (por defecto).
   - **Force Authentication**: Activado por defecto.

### Integración EIDAS

El conector inyecta automáticamente el bloque `<eidas:SPType>` en la solicitud de autenticación SAML.

## Resolución de Problemas

Si encuentras errores de firma, verifica que has importado correctamente el certificado público de Cl@ve en la configuración del Identity Provider (Validate Signature = ON) y que tienes configuradas las claves del reino (Realm Keys) correctamente para firmar las peticiones.
