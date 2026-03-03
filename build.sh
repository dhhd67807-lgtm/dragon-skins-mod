#!/bin/bash

echo "Building Dragon Skins Mod..."

# Check if Gradle is installed
if ! command -v gradle &> /dev/null; then
    echo "Gradle not found, using gradlew..."
    if [ ! -f "gradlew" ]; then
        echo "Downloading Gradle wrapper..."
        gradle wrapper
    fi
    ./gradlew build
else
    gradle build
fi

if [ $? -eq 0 ]; then
    echo ""
    echo "✓ Build successful!"
    echo "Mod JAR: build/libs/dragon-skins-mod-1.0.0.jar"
    echo ""
    echo "The launcher will automatically install this mod when you upload a custom skin."
else
    echo "✗ Build failed!"
    exit 1
fi
