# Keycloak Cl@ve Connector

Este proyecto proporciona un conector profesional (Identity Provider) para Keycloak que facilita la integración con la pasarela Cl@ve (Cl@ve 2.0 / EIDAS) de la Administración General del Estado de España.

## Características Profesionales

- **SAML 2.0 Compliant**: Basado en el estándar SAML 2.0 y las librerías oficiales de Keycloak.
- **Inyección de Extensiones eIDAS**: Soporte nativo para el nodo eIDAS mediante la inclusión automática del bloque `<eidas:SPType>`.
- **Configuración de LoA**: Posibilidad de definir el Nivel de Seguridad (Level of Assurance) requerido: Low, Substantial o High.
- **Internacionalización (i18n)**: Soporte completo para inglés y español en la interfaz de administración de Keycloak.
- **Mapeo de Atributos Personalizado**: Incluye un `Clave User Attribute Mapper` para facilitar la importación de datos del ciudadano.
- **Logging Robusto**: Uso de JBoss Logging para una integración perfecta con los logs de Keycloak.
- **Seguridad por Defecto**: Configurado con algoritmos de firma modernos (RSA_SHA256) y políticas de NameID persistentes.

Para ver el historial detallado de mejoras, consulta el [CHANGELOG.md](CHANGELOG.md).

## Requisitos

- Java 17+
- Maven 3.x
- Keycloak 24.x o superior

## Compilación e Instalación

```bash
mvn clean package
```

1. Copia `target/keycloak-clave-connector-1.0.0-SNAPSHOT.jar` al directorio `providers/` de Keycloak.
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

### Mapeo de Atributos (Cl@ve Attributes)

Cl@ve devuelve una serie de atributos en la respuesta SAML. Puedes mapearlos usando el **Cl@ve User Attribute Mapper** incluido:

| Atributo SAML | Descripción |
|---------------|-------------|
| `irisp_dni` | DNI o NIE del ciudadano |
| `irisp_givenname` | Nombre de pila |
| `irisp_surname` | Primer apellido |
| `irisp_surname2` | Segundo apellido |
| `irisp_pseudonym` | Identificador único persistente |

### Ejemplo de configuración de Mapper:
1. Ve a **Identity Providers** -> **Cl@ve** -> **Mappers**.
2. Haz clic en **Add mapper**.
3. Selecciona **Cl@ve User Attribute** como mapper type.
4. **Name**: `DNI Mapper`
5. **User Attribute Name**: `dni`
6. **SAML Attribute Name**: `irisp_dni`

## Contribución y Desarrollo

El proyecto incluye tests unitarios con JUnit 5 y Mockito para asegurar la estabilidad de las extensiones eIDAS y la configuración del factory.

Para ejecutar los tests:
```bash
mvn test
```

## Soporte e Integración

Este conector ha sido diseñado siguiendo las guías de integración de la plataforma Cl@ve y los estándares eIDAS para garantizar la interoperabilidad con los nodos de interoperabilidad europeos.
