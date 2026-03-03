package com.dragonclient.skins.mixin;

import com.dragonclient.skins.DragonSkinsClient;
import net.minecraft.client.texture.PlayerSkinProvider;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Mixin(PlayerSkinProvider.class)
public class PlayerSkinProviderMixin {
    
    @Inject(method = "loadSkin", at = @At("HEAD"), cancellable = true)
    private void loadCustomSkin(UUID uuid, CallbackInfoReturnable<CompletableFuture<Identifier>> cir) {
        // Try to load from Dragon Skins server first
        String username = getUsername(uuid);
        if (username != null) {
            String skinUrl = DragonSkinsClient.getSkinUrl(username);
            
            // Check if custom skin exists
            CompletableFuture<Identifier> future = CompletableFuture.supplyAsync(() -> {
                try {
                    URL url = new URL(skinUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(1000);
                    conn.setReadTimeout(1000);
                    
                    if (conn.getResponseCode() == 200) {
                        DragonSkinsClient.LOGGER.info("Loading custom skin for: " + username);
                        // Load the custom skin texture
                        return loadTextureFromUrl(skinUrl);
                    }
                } catch (Exception e) {
                    // Custom skin not found, will use default
                }
                
                // Return default skin
                return DefaultSkinHelper.getTexture(uuid);
            });
            
            cir.setReturnValue(future);
        }
    }
    
    private String getUsername(UUID uuid) {
        // Try to get username from game session
        try {
            var client = net.minecraft.client.MinecraftClient.getInstance();
            if (client.getSession() != null) {
                return client.getSession().getUsername();
            }
        } catch (Exception e) {
            // Ignore
        }
        return null;
    }
    
    private Identifier loadTextureFromUrl(String url) {
        // Create a unique identifier for this texture
        return new Identifier("dragonskins", "skin_" + url.hashCode());
    }
}
