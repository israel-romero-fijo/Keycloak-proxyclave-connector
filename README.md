# Keycloak Cl@ve Connector

Este proyecto proporciona un conector (Identity Provider) para Keycloak que facilita la integración con la pasarela Cl@ve (Cl@ve 2.0 / EIDAS) de la Administración General del Estado de España.

## Características

- Implementación basada en SAML 2.0.
- Preparado para la integración como Service Provider (SP) en Keycloak.
- Nombre del proveedor: `Cl@ve`.
- ID del proveedor: `clave-saml`.

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
3. Haz clic en **Add provider** y selecciona **Cl@ve** de la lista (debería aparecer si la instalación fue correcta).
4. Configura los parámetros habituales de SAML:
   - **Service Provider Entity ID**: El Entity ID que usarás para este SP (debes registrarlo en Cl@ve).
   - **Single Sign-On Service URL**: La URL del IdP de Cl@ve (entorno de pruebas o producción).
   - **Principal Type**: Normalmente `Attribute [Name]`.
   - **NameID Policy Format**: Cl@ve suele requerir `Persistent` o `Transient`.
   - **Sign Documents**: Actívalo si Cl@ve requiere solicitudes firmadas (habitual).
   - **Signature Algorithm**: RSA_SHA256.

### Integración EIDAS

Para la integración con EIDAS, asegúrate de solicitar los atributos necesarios (Minimum Data Set) en la configuración de atributos del proveedor o mediante un mapper.

## Desarrollo

Este conector extiende `SAMLIdentityProvider` de Keycloak. Si necesitas lógica específica para validar respuestas EIDAS (e.g. validación de firmas específica, parsing de atributos complejos), puedes extender la clase `ClaveIdentityProvider`.
