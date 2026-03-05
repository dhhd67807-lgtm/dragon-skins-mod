package com.dragonclient.skins.mixin;

import com.dragonclient.skins.DragonSkinsClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public class AbstractClientPlayerEntityMixin {
    
    @Inject(method = "getSkinTextures", at = @At("RETURN"))
    private void onGetSkinTextures(CallbackInfoReturnable<Identifier> cir) {
        // Just log for now to verify the mod loads
        DragonSkinsClient.LOGGER.info("Skin texture requested!");
    }
}
