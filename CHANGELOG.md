# Changelog

Todos los cambios notables en este proyecto serán documentados en este archivo.

## [1.1.0] - 2024-05-24

### Añadido
- Soporte completo para **Cl@ve 2.0**.
- Soporte para **Certificados de Curva Elíptica (EC)** y algoritmos ECDSA.
- Selección dinámica de claves basada en el algoritmo de firma configurado.
- Soporte para Internacionalización (i18n) en la consola de administración (Inglés y Español).
- Nuevo `ClaveAttributeMapper` para facilitar la extracción de atributos SAML de Cl@ve.
- Soporte para configuración del Nivel de Seguridad (LoA) eIDAS desde la UI.

### Cambiado
- Refactorización de `EidasNodeGenerator` para mejorar la testabilidad.
- Migración de `java.util.logging` a `org.jboss.logging` para alinearse con los estándares de Keycloak.
- Mejora de los tests unitarios eliminando el uso de reflexión.
- Actualización de `README.md` con instrucciones detalladas de configuración profesional.

## [1.0.0] - 2024-01-15

### Añadido
- Versión inicial del conector Cl@ve para Keycloak.
- Implementación básica de SAML 2.0.
- Soporte para extensión eIDAS SPType.
