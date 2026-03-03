package com.dragonclient.skins.mixin;

import com.dragonclient.skins.DragonSkinsClient;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.MinecraftClient;
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
            
            // Check if custom skin exists and load it
            CompletableFuture<Identifier> future = CompletableFuture.supplyAsync(() -> {
                try {
                    URL url = new URL(skinUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(2000);
                    conn.setReadTimeout(2000);
                    
                    if (conn.getResponseCode() == 200) {
                        DragonSkinsClient.LOGGER.info("Loading custom skin for: " + username + " from " + skinUrl);
                        
                        // Create identifier for this skin
                        Identifier skinId = new Identifier("dragonskins", "skins/" + username.toLowerCase());
                        
                        // Load the texture from URL
                        MinecraftClient client = MinecraftClient.getInstance();
                        client.execute(() -> {
                            try {
                                // Register the skin texture from URL
                                client.getTextureManager().registerTexture(
                                    skinId,
                                    new net.minecraft.client.texture.PlayerSkinTexture(
                                        null,
                                        skinUrl,
                                        DefaultSkinHelper.getTexture(uuid),
                                        true,
                                        null
                                    )
                                );
                                DragonSkinsClient.LOGGER.info("✓ Custom skin loaded: " + username);
                            } catch (Exception e) {
                                DragonSkinsClient.LOGGER.error("Failed to register skin texture: " + e.getMessage());
                            }
                        });
                        
                        return skinId;
                    } else {
                        DragonSkinsClient.LOGGER.debug("No custom skin found for " + username + " (HTTP " + conn.getResponseCode() + ")");
                    }
                } catch (Exception e) {
                    DragonSkinsClient.LOGGER.debug("Could not load custom skin for " + username + ": " + e.getMessage());
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
            var client = MinecraftClient.getInstance();
            if (client.getSession() != null) {
                return client.getSession().getUsername();
            }
        } catch (Exception e) {
            DragonSkinsClient.LOGGER.error("Failed to get username: " + e.getMessage());
        }
        return null;
    }
}
