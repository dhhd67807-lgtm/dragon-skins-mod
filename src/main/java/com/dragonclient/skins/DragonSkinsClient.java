package com.dragonclient.skins;

import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DragonSkinsClient implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("DragonSkins");
    public static final String SKIN_SERVER = "http://localhost:25585";

    @Override
    public void onInitializeClient() {
        LOGGER.info("Dragon Skins loaded - Custom skin server: " + SKIN_SERVER);
    }
    
    public static String getSkinUrl(String username) {
        return SKIN_SERVER + "/skins/" + username + ".png";
    }
    
    public static String getCapeUrl(String username) {
        return SKIN_SERVER + "/capes/" + username + ".png";
    }
}
