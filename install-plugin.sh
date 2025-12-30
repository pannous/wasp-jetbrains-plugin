#!/bin/bash

set -e

# Configuration
PLUGIN_NAME="wasp-jetbrains-plugin"
BUILD_DIR="build/distributions"
# Use the latest zip file in distributions (handles versioned names)
PLUGIN_ZIP=$(ls -t ${BUILD_DIR}/${PLUGIN_NAME}-*.zip 2>/dev/null | head -1)

# Determine which IDE to install to
IDE="${1:-CLion}"

# Find IDE config directory
IDE_CONFIG=$(ls -d ~/Library/Application\ Support/JetBrains/${IDE}* 2>/dev/null | sort -V | tail -1)

if [ -z "$IDE_CONFIG" ]; then
    echo "$IDE config directory not found. Please run $IDE at least once to create the config directory."
    echo "Looking for: ~/Library/Application Support/JetBrains/${IDE}*"
    echo ""
    echo "Usage: $0 [IDE_NAME]"
    echo "  Default IDE: CLion"
    echo "  Examples: $0 CLion"
    echo "           $0 IntelliJIdea"
    echo "           $0 PyCharm"
    exit 1
fi

PLUGINS_DIR="${IDE_CONFIG}/plugins"

echo "Found $IDE config: $IDE_CONFIG"
echo "Plugins directory: $PLUGINS_DIR"

# Build the plugin
echo "Building plugin..."
JAVA_HOME=/Users/me/Library/Java/JavaVirtualMachines/openjdk-21.0.1/Contents/Home ./gradlew buildPlugin --no-daemon

# Check if plugin was built
if [ ! -f "$PLUGIN_ZIP" ]; then
    echo "Error: Plugin build failed. Expected file: $PLUGIN_ZIP"
    exit 1
fi

# Create plugins directory if it doesn't exist
mkdir -p "$PLUGINS_DIR"

# Remove old version if exists
OLD_PLUGIN="${PLUGINS_DIR}/${PLUGIN_NAME}"
if [ -d "$OLD_PLUGIN" ]; then
    echo "Removing old plugin version..."
    rm -rf "$OLD_PLUGIN"
fi

# Extract plugin to plugins directory
echo "Installing plugin to $PLUGINS_DIR..."
unzip -q "$PLUGIN_ZIP" -d "$PLUGINS_DIR"

echo ""
echo "✓ Plugin installed successfully!"
echo "✓ Location: $PLUGINS_DIR/${PLUGIN_NAME}"
echo ""
echo "Please restart $IDE to load the plugin."
