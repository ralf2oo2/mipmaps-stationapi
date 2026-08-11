package net.ralf2oo2.mipmaps.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.texture.*;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.ralf2oo2.mipmaps.config.Config;
import net.ralf2oo2.mipmaps.mixininterface.MipmapHolder;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(SpriteAtlasTexture.class)
public class SpriteAtlasTextureMixin {
    @WrapOperation(
            method = "upload",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/modificationstation/stationapi/api/client/texture/TextureUtil;prepareImage(III)V"
            )
    )
    private void mipmaps_prepareMipmaps(int id, int width, int height, Operation<Void> original) {
        if(Config.CONFIG.level.isEnabled()) {
            TextureUtil.prepareImage(id, Config.CONFIG.level.getLevel(), width, height);
        } else {
            original.call(id, width, height);
        }
    }

    @ModifyArg(
            method = "applyTextureFilter",
                      at = @At(
                              value = "INVOKE",
                              target = "Lnet/modificationstation/stationapi/api/client/texture/SpriteAtlasTexture;setFilter(ZZ)V"
                      ),
                      index = 1
    )
    private boolean mipmaps_enableMipmapFilter(boolean original) {
        return Config.CONFIG.level.isEnabled() || original;
    }
}
