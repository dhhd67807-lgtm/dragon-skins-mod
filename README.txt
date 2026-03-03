Dragon Skins Mod - Built-in Custom Skins for Dragon Client
===========================================================

This is a simple Fabric mod that enables custom skins from the Dragon Client launcher.

HOW TO BUILD:
-------------
1. Make sure you have Java 17+ installed
2. Run: ./build.sh
3. The mod will be built to: build/libs/dragon-skins-mod-1.0.0.jar

The launcher will automatically install this mod to Fabric instances when you upload a custom skin.

HOW IT WORKS:
-------------
- Intercepts Minecraft's skin loading
- Checks http://localhost:25585/skins/{username}.png first
- Falls back to default skin if custom skin not found
- Works with all Fabric versions 1.16+

NO CONFIGURATION NEEDED - Just build and the launcher handles the rest!
