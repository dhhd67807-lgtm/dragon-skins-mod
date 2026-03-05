package com.dragonclient.skins;

import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.HttpURLConnection;
import java.net.URL;

public class DragonSkinsClient implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("DragonSkins");
    public static final String SKIN_SERVER = "http://localhost:25585";

    @Override
    public void onInitializeClient() {
        LOGGER.info("===========================================");
        LOGGER.info("Dragon Skins Mod Loaded!");
        LOGGER.info("Skin Server: " + SKIN_SERVER);
        
        // Test server connection
        try {
            URL url = new URL(SKIN_SERVER + "/health");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(1000);
            conn.setReadTimeout(1000);
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                LOGGER.info("✓ Skin server is reachable!");
            } else {
                LOGGER.warn("⚠ Skin server returned: " + responseCode);
            }
            conn.disconnect();
        } catch (Exception e) {
            LOGGER.error("✗ Cannot reach skin server: " + e.getMessage());
            LOGGER.error("Make sure the Dragon Client launcher is running!");
        }
        
        LOGGER.info("===========================================");
    }
    
    public static String getSkinUrl(String username) {
        String url = SKIN_SERVER + "/skins/" + username + ".png";
        LOGGER.info("Getting skin URL for " + username + ": " + url);
        return url;
    }
    
    public static String getCapeUrl(String username) {
        return SKIN_SERVER + "/capes/" + username + ".png";
    }
}
