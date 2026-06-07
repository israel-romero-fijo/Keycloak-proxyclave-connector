# Changelog - Cl@ve Keycloak Connector

All notable changes to this project are documented in this file.

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

## [1.1.0-SNAPSHOT] - 2026-06-07

### Added
- **eIDAS Requested Attributes Support**: Integrated support for `RequestedAttributes` in the eIDAS extension of the SAML `AuthnRequest`. This allows fine-grained control over the data requested from the eIDAS node.
- **Enhanced Configuration UI**: Added a new field for comma-separated eIDAS attribute URIs with localized tooltips.
- **Standard eIDAS Constants**: Included `PERSON_IDENTIFIER`, `GIVEN_NAME`, and `FAMILY_NAME` constants in `ClaveAttributeMapper` to improve developer experience and facilitate attribute mapping.

### Changed
- **Generator Refactoring**: Enhanced `EidasNodeGenerator` to handle complex XML structures for multiple eIDAS extensions within a single generator.
- **Expanded Test Suite**: Added verification for the new XML generation logic and configuration handling.
