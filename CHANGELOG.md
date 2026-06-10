# Changelog - Cl@ve Keycloak Connector

All notable changes to this project are documented in this file.

## [1.2.0] - 2026-06-10

### Added
- **Dynamic Requested Attributes**: Configurable list of eIDAS attributes to be requested in the SAML AuthnRequest.
- **Enhanced LoA Support**: Improved handling of Level of Assurance with better configuration defaults.
- **SAML Binding Flexibility**: Automatic detection and support for both POST and Redirect bindings.
- **Professional Attribute Mapper**: `ClaveAttributeMapper` now includes predefined constants for standard eIDAS attributes (PersonIdentifier, GivenName, FamilyName, DateOfBirth).
- **Extended Test Suite**: Added verification for requested attributes and identity provider initialization.

### Changed
- **Logging Improvements**: Added detailed debug logging for SAML request parameters to aid in production troubleshooting.
- **Improved i18n**: Expanded translations for new configuration properties.

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
