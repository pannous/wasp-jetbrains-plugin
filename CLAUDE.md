# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview
This is a JetBrains IntelliJ IDEA plugin for the Wasp programing language https://github.com/pannous/wasp/, built with Kotlin and Gradle

## Architecture
- **Language**: Kotlin with Java compatibility
- **Build System**: Gradle with Kotlin DSL
- **Plugin Framework**: IntelliJ Platform Plugin SDK
- **Target IDE**: IntelliJ IDEA any version 2025.2
- **JVM Target**: Java 19
- **Plugin ID**: `com.pannous.wasp-jetbrains-plugin`

The project follows standard IntelliJ plugin structure:
- `src/main/kotlin/com/pannous/wasp/` - Main source code of language definitions for plugin 
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

### Test Structure
Parser tests are located in `src/test/kotlin/com/pannous/wasp/WaspParserTest.kt` and follow these patterns:
- **Test Framework**: Extends `ParsingTestCase` from IntelliJ Platform
- **Test Data Path**: `src/test/resources/testData`
- **Test Method Pattern**: `fun testXxxYyy()` with descriptive names
- **Code Parsing**: Use `parseCode(code: String)` helper method that creates PSI file and returns AST node
- **End-to-end Tests**: Prefer simple tests that check complex machinery: 
	- `parseCode("var x = 42")` should return a valid AST node for variable declaration
	- `parseCode("function foo(x) { return 42+1; }")` should return a valid AST function declaration node with one parameter
- **Test Categories**: 
  - Basic literals (string, number, boolean, null, array, map)
  - Statements (keyword, identifier, variable declaration, function calls)
  - Complex structures (nested blocks, arithmetic expressions)
  - Malformed input handling
  - Element type and token type validation
- **Test File Coverage**: Tests verify parser methods exist and element types are properly defined
- **Run Tests**: Use `./gradlew test` to verify all tests pass

### Commit Workflow
After completing any coding task:
1. **Always commit your changes** with meaningful messages describing what you implemented and why
2. **Break large changes into small, focused commits** - each commit should represent a single logical change
3. Use conventional commit format with scope indicators:
   - `feat(minor):` - small additions that don't change architecture
   - `feat(major):` - significant new capabilities  
   - `arch:` - structural/architectural changes that touch multiple components
   - `refactor:` - reorganizing without changing functionality
   - `test:` - test-only changes
   - `fix:` - bug fixes
   - `chore:` - maintenance tasks
4. Examples: `feat(minor): add null literal parsing`, `arch: restructure parser for assignments`, `test: add structural AST validation`
5. Focus on the intent and purpose, not just the technical changes
6. Use claude@pannous.com as author
7. Do NOT sign the commits with Co-Authored … nor your name (claude)
8. Commit immediately after successful completion of each focused task

## Memories
- wow
- I'm using speech recognition so you can always assume when you encounter an unexpected word which matches vaguely another word that makes sense that it was a misspelling