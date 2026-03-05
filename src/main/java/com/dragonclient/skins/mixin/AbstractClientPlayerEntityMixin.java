package com.dragonclient.skins.mixin;

import com.dragonclient.skins.DragonSkinsClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.HttpURLConnection;
import java.net.URL;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin {
    
    @Inject(method = "getSkinTexture", at = @At("HEAD"), cancellable = true)
    private void getCustomSkin(CallbackInfoReturnable<Identifier> cir) {
        AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) (Object) this;
        String username = player.getGameProfile().getName();
        
        if (username != null) {
            String skinUrl = DragonSkinsClient.getSkinUrl(username);
            
            // Check if custom skin exists
            try {
                URL url = new URL(skinUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(500);
                conn.setReadTimeout(500);
                conn.setRequestMethod("HEAD");
                
                if (conn.getResponseCode() == 200) {
                    DragonSkinsClient.LOGGER.info("✓ Custom skin found for: " + username);
                    
                    // Create identifier for this skin
                    Identifier skinId = new Identifier("dragonskins", "skins/" + username.toLowerCase());
                    
                    // Load the texture from URL on main thread
                    MinecraftClient client = MinecraftClient.getInstance();
                    if (client != null && client.getTextureManager() != null) {
                        // Check if already loaded
                        if (client.getTextureManager().getTexture(skinId) == null) {
                            client.execute(() -> {
                                try {
                                    DragonSkinsClient.LOGGER.info("Loading skin texture from: " + skinUrl);
                                    client.getTextureManager().registerTexture(
                                        skinId,
                                        new net.minecraft.client.texture.PlayerSkinTexture(
                                            null,
                                            skinUrl,
                                            net.minecraft.client.util.DefaultSkinHelper.getTexture(player.getGameProfile().getId()),
                                            true,
                                            null
                                        )
                                    );
                                    DragonSkinsClient.LOGGER.info("✓ Skin texture registered: " + skinId);
                                } catch (Exception e) {
                                    DragonSkinsClient.LOGGER.error("Failed to register skin: " + e.getMessage());
                                }
                            });
                        }
                        
                        cir.setReturnValue(skinId);
                        return;
                    }
                }
                conn.disconnect();
            } catch (Exception e) {
                // Silently fail and use default skin
            }
        }
    }
}
