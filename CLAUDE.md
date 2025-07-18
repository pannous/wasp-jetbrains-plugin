# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview
This is a JetBrains IntelliJ IDEA plugin for the Wasp programing language  framework, built with Kotlin and Gradle. The plugin currently appears to be in early development stages with basic scaffolding in place.

## Architecture
- **Language**: Kotlin with Java compatibility
- **Build System**: Gradle with Kotlin DSL
- **Plugin Framework**: IntelliJ Platform Plugin SDK
- **Target IDE**: IntelliJ IDEA any version 2025.2
- **JVM Target**: Java 19
- **Plugin ID**: `com.pannous.wasp-jetbrains-plugin`

The project follows standard IntelliJ plugin structure:
- `src/main/kotlin/com/pannous/waspjetbrainsplugin/` - Main plugin source code
- `src/main/resources/META-INF/plugin.xml` - Plugin configuration
- `build.gradle.kts` - Build configuration with IntelliJ plugin setup

## Development Commands

### Build & Development
```bash
./gradlew build                    
# Build plugin ZIP for distribution
./gradlew buildPlugin             
./gradlew runIde                  
./gradlew test                    
# Run all checks and tests
./gradlew check                   
```

### Plugin Development
```bash
./gradlew prepareSandbox          # Prepare IDE sandbox with plugin
./gradlew verifyPlugin            # Validate plugin configuration
./gradlew buildSearchableOptions  # Build searchable options index
./gradlew instrumentCode          # Instrument code for debugging
```

### IDE Integration
```bash
./gradlew idea                    # Generate IDEA project files
./gradlew cleanIdea               # Clean IDEA project files
./gradlew openIdea                # Open project in IDEA
```

### Plugin Distribution
```bash
./gradlew buildPlugin             # Build plugin ZIP for installation
./gradlew signPlugin              # Sign plugin (requires env vars)
./gradlew publishPlugin           # Publish to marketplace (requires token)
./gradlew runPluginVerifier       # Verify compatibility with IDE builds
```

### Installing Plugin in Real IDE
1. Run `./gradlew buildPlugin` to create the plugin ZIP
2. Open IntelliJ IDEA → File → Settings → Plugins
3. Click gear icon → Install Plugin from Disk...
4. Select `build/distributions/wasp-jetbrains-plugin-1.0-SNAPSHOT.zip`
5. Restart IntelliJ IDEA

### Development Configuration
- Persistent sandbox directory configured at `./sandbox/`
- To use your local IDE installation, uncomment `localPath.set()` in build.gradle.kts

## Plugin Configuration
- **Compatibility**: IDE builds 222 to 232.*
- **Dependencies**: `com.intellij.modules.platform` (core platform)
- **Extensions**: Currently empty - ready for implementation
- **Vendor**: Placeholder configuration needs updating

## Development Notes
