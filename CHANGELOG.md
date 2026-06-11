# Changelog - Cl@ve Keycloak Connector

All notable changes to this project are documented in this file.

## [1.2.0] - 2026-06-11

### Added
- **Dynamic SAML Binding Support**: Now respects the administrator's choice of Redirect or POST binding for AuthnRequests.
- **Enhanced eIDAS Extensions**: Implemented support for `RequestedAttributes` in SAML AuthnRequests, mandatory for some eIDAS integrations.
- **Robust Configuration**: Introduced `ClaveSAMLIdentityProviderConfig` for better encapsulation of Cl@ve-specific settings.
- **Predefined Attribute Constants**: Added common eIDAS attribute URIs (DNI, Name, DOB) to `ClaveAttributeMapper` for easier configuration.
- **Improved Observability**: Added detailed debug logging for SAML request parameters.
- **Expanded Test Suite**: Added new tests for XML generation and configuration logic.

### Fixed
- **Binding Logic**: Corrected hardcoded Redirect binding to use the configured preference.

## [1.1.0] - 2026-06-05

### Added
- **Professional i18n Support**: Full translation to English and Spanish for the Keycloak administration console, including labels, tooltips, and dropdown options.
- **Level of Assurance (LoA) Configuration**: New capability to select the required eIDAS LoA (Low, Substantial, High) from the UI, with automatic injection into SAML `RequestedAuthnContext`.
- **Custom Attribute Mapper**: Added `ClaveAttributeMapper` (`clave-user-attribute-mapper`) to simplify mapping Spanish specific attributes (DNI, given name, etc.) to user profiles.
- **Unit Testing Suite**: Implemented comprehensive tests for `EidasNodeGenerator` and `ClaveIdentityProviderFactory` using JUnit 5.

### Changed
- **Architecture Refactoring**: Moved `EidasNodeGenerator` to a top-level class for better maintainability and testability.
- **Logging Upgrade**: Switched from `java.util.logging` to `org.jboss.logging` to align with Keycloak's logging standards.
- **Enhanced Documentation**: Overhauled `README.md` with detailed integration guides, attribute tables, and configuration tips.
- **SAML Security Defaults**: Refined default configuration to include persistent NameID policy and mandatory signed requests.

### Technical Details
- Registered SPI services in `META-INF/services`.
- Added `theme-resources/messages` for localization.
- Validated with Keycloak 24 APIs.
