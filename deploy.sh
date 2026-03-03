#!/bin/bash

# Dragon Skins Mod - GitHub Deployment Script
# This script will help you deploy the mod to GitHub

echo "🐉 Dragon Skins Mod - GitHub Deployment"
echo "========================================"
echo ""

# Check if gh CLI is installed
if ! command -v gh &> /dev/null; then
    echo "❌ GitHub CLI (gh) is not installed."
    echo "📦 Install it with: brew install gh"
    echo ""
    echo "Or manually create the repository:"
    echo "1. Go to https://github.com/new"
    echo "2. Create repository: dragon-skins-mod"
    echo "3. Run these commands:"
    echo ""
    echo "   git remote add origin https://github.com/YOUR_USERNAME/dragon-skins-mod.git"
    echo "   git push -u origin main"
    echo "   git tag -a v1.0.0 -m 'Release v1.0.0'"
    echo "   git push origin v1.0.0"
    echo ""
    exit 1
fi

# Check if logged in to GitHub
if ! gh auth status &> /dev/null; then
    echo "🔐 Please login to GitHub first:"
    gh auth login
fi

echo "📦 Creating GitHub repository..."
gh repo create dragon-skins-mod --public --source=. --description="Custom skins mod for Dragon Client - Supports Fabric/Forge/Quilt (1.8.9-1.21.11)" --push

if [ $? -eq 0 ]; then
    echo "✅ Repository created and code pushed!"
    echo ""
    echo "🏷️  Creating release tag v1.0.0..."
    git tag -a v1.0.0 -m "Release v1.0.0 - Support for Fabric/Forge/Quilt 1.8.9-1.21.11"
    git push origin v1.0.0
    
    echo ""
    echo "🚀 Creating GitHub release..."
    gh release create v1.0.0 \
        --title "Dragon Skins Mod v1.0.0" \
        --notes "## Dragon Skins Mod v1.0.0

Custom skins for Dragon Client - Works with Fabric, Forge, and Quilt!

### Supported Versions
- **Fabric**: 1.14.4 → 1.21.11 (36 versions)
- **Forge**: 1.8.9 → 1.21.11 (41 versions)
- **Quilt**: 1.18.2 → 1.21.11 (25 versions)

### Features
✅ Load custom skins from Dragon Client launcher
✅ Works with all Minecraft versions
✅ Supports Fabric, Forge, and Quilt
✅ Automatic fallback to default skins
✅ No configuration needed

### Installation
The Dragon Client launcher automatically installs this mod when you upload a custom skin.

For manual installation, download the mod for your Minecraft version and mod loader, then place it in your mods folder.

### Building
The GitHub Actions workflow will automatically build all 103 mod versions and attach them to this release within a few minutes."
    
    echo ""
    echo "✅ Release created! GitHub Actions is now building all mod versions..."
    echo "📊 Check the progress at: https://github.com/$(gh repo view --json nameWithOwner -q .nameWithOwner)/actions"
    echo ""
    echo "🎉 Deployment complete!"
else
    echo "❌ Failed to create repository. Please create it manually."
fi
