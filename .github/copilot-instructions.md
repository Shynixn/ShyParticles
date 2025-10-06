# Minecraft Plugin Development Guidelines

This instruction file contains the coding guidelines and architectural patterns for developing Minecraft plugins following the ShyCommandSigns project structure and conventions.

## Project Structure & Organization

### Root Directory Structure
```
project-root/
├── build.gradle.kts              # Main build configuration
├── settings.gradle.kts           # Project settings
├── gradle.properties             # Gradle properties
├── gradlew / gradlew.bat         # Gradle wrapper scripts
├── LICENSE                       # License file
├── README.md                     # Project documentation
├── src/                          # Source code
│   └── main/
│       ├── java/                 # Kotlin/Java source files
│       └── resources/            # Plugin resources
├── docs/                         # Documentation
│   └── wiki/                     # Wiki documentation
│       ├── mkdocs.yml            # MkDocs configuration
│       └── docs/                 # Markdown documentation files
├── build/                        # Build artifacts (auto-generated)
└── gradle/                       # Gradle wrapper files
```

### Source Code Organization
Always organize source code under the package structure:
```
src/main/java/com/github/[author]/[projectname]/
├── contract/                     # Interfaces and contracts
├── entity/                       # Data classes and entities
├── enumeration/                  # Enums and constants
├── event/                        # Custom events
├── impl/                         # Implementation classes
│   ├── commandexecutor/          # Command executors
│   ├── listener/                 # Event listeners
│   └── service/                  # Service implementations
├── [ProjectName]Plugin.kt        # Main plugin class
├── [ProjectName]DependencyInjectionModule.kt # DI module
└── [ProjectName]LanguageImpl.kt  # Language implementation
```

### Resource Structure
```
src/main/resources/
├── plugin.yml                   # Main plugin descriptor
├── plugin-folia.yml             # Folia variant descriptor
├── plugin-legacy.yml            # Legacy variant descriptor
├── config.yml                   # Default configuration
├── lang/                        # Language files
│   └── en_us.yml                # Default language
└── sign/                        # Sign templates
    └── sample_sign.yml           # Sample sign configuration
```

### Actual Coding

Follow the convention in the similar project found in the 'knowledge' folder.

