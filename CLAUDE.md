# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## CRITICAL COMMIT RULES - NEVER VIOLATE
**NEVER add Co-Authored-By: Claude lines to commits under any circumstances**
**NEVER sign commits with my name (claude) - use claude@pannous.com as author only**

## Project Overview
This is a JetBrains IntelliJ IDEA plugin for the Wasp programing language https://github.com/pannous/wasp/, built with Kotlin and Gradle

## Architecture
- **Language**: Kotlin with Java compatibility
- **Build System**: Gradle with Kotlin DSL
- **Structure**: 
  - `src/wasp/` - Main source code
  - `test/wasp/` - Test files
  - `build.gradle.kts` - Build configuration

## Development Commands
- `./gradlew test` - Run tests
- `./gradlew build` - Build project  
- `./gradlew runIde` - Run IDE with plugin

## Development Notes

### Test Structure
Parser tests are located in `src/test/kotlin/com/pannous/wasp/WaspParserTest.kt` and follow these patterns:
- **Test Framework**: Extends `ParsingTestCase` from IntelliJ Platform
- **Test Data Path**: `src/test/resources/testData`
- **Test Method Pattern**: `fun testXxxYyy()` with descriptive names
- **Code Parsing**: Use `parse(code: String)` helper method that creates PSI file and returns AST nodes
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

## Memories
Secret memory keyword = abjoijnnoin
 <!--
This part should currently not be read by Claude code because it should be commented out

- Only when the short messages from the Tests are not sufficient to understand the error you can use
  ./gradlew test --full-stacktrace -q 
OR
  ./gradlew test --debug --stacktrace
-->
