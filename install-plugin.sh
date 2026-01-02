#!/bin/bash

# Auto-install Wasp Plugin to all IntelliJ-based IDEs

set -e

echo "🚀 Installing Wasp Plugin..."

# Build the plugin
./gradlew buildPlugin

# Find the built plugin (auto-detect latest version)
PLUGIN_ZIP=$(ls -t build/distributions/wasp-jetbrains-plugin-*.zip 2>/dev/null | head -1)

if [ -z "$PLUGIN_ZIP" ] || [ ! -f "$PLUGIN_ZIP" ]; then
    echo "❌ Error: Plugin not found in build/distributions/"
    echo "Build may have failed"
    exit 1
fi

# Extract version from filename
VERSION=$(basename "$PLUGIN_ZIP" | sed 's/wasp-jetbrains-plugin-//' | sed 's/.zip//')
echo "✅ Plugin built successfully: $PLUGIN_ZIP (v$VERSION)"

# Auto-detect all JetBrains IDE installations
JETBRAINS_BASE="$HOME/Library/Application Support/JetBrains"
FOUND_DIRS=()

if [ -d "$JETBRAINS_BASE" ]; then
    # Find all IDE directories (IntelliJ, CLion, RustRover, etc.)
    for ide_dir in "$JETBRAINS_BASE"/*; do
        if [ -d "$ide_dir" ]; then
            plugin_dir="$ide_dir/plugins"
            # Check if this looks like a real IDE installation
            if [ -d "$ide_dir" ]; then
                FOUND_DIRS+=("$plugin_dir")
            fi
        fi
    done
fi

if [ ${#FOUND_DIRS[@]} -eq 0 ]; then
    echo "❌ No JetBrains IDE installations found"
    echo "Plugin is available at: $PLUGIN_ZIP"
    echo "Install manually via: Settings → Plugins → ⚙️ → Install Plugin from Disk"
    exit 1
fi

echo "📦 Found ${#FOUND_DIRS[@]} JetBrains IDE installation(s)"

# Install to each found IDE
for plugin_dir in "${FOUND_DIRS[@]}"; do
    ide_name=$(echo "$plugin_dir" | sed 's|.*/JetBrains/||' | sed 's|/plugins||')
    echo "📥 Installing to $ide_name..."

    # Create plugins directory if it doesn't exist
    mkdir -p "$plugin_dir"

    # Remove old installation if exists
    if [ -d "$plugin_dir/wasp-jetbrains-plugin" ]; then
        rm -rf "$plugin_dir/wasp-jetbrains-plugin"
    fi

    # Unzip plugin to plugins directory
    unzip -q "$PLUGIN_ZIP" -d "$plugin_dir/"

    echo "   ✅ Installed to $ide_name"
done

echo ""
echo "✨ Installation complete!"
echo ""
echo "🔄 Restarting RustRover..."

# Kill RustRover to ensure plugin is loaded
pkill -f "RustRover" 2>/dev/null || true
sleep 2

# Restart RustRover if it exists
if [ -d "/Applications/RustRover.app" ]; then
    echo "   Starting RustRover..."
    open -a "RustRover" 2>/dev/null || echo "   ℹ️  RustRover failed to start"
fi

echo ""
echo "📋 Plugin installed (v$VERSION)"
echo "   Open any .wasp file to test"
echo ""
echo "To verify installation:"
echo "   Settings → Plugins → Installed → search for 'Wasp'"
echo ""
