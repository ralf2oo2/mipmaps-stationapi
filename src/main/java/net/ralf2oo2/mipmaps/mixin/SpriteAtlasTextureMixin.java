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

import java.nio.file.Path;


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
            if(Config.CONFIG.bias != 0.0f) {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
                GL11.glTexParameterf(
                        GL11.GL_TEXTURE_2D,
                        GL14.GL_TEXTURE_LOD_BIAS,
                        Config.CONFIG.bias
                );
            }
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

    @WrapOperation(
            method = "save",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/modificationstation/stationapi/api/client/texture/TextureUtil;writeAsPNG(Ljava/nio/file/Path;Ljava/lang/String;IIII)V"
            )
    )
    private void mipmaps_saveMipmaps(Path directory, String prefix, int textureId, int scales, int width, int height, Operation<Void> original) {
        if(Config.CONFIG.level.isEnabled()) {
            original.call(directory, prefix, textureId, Config.CONFIG.level.getLevel(), width, height);
        }
        original.call(directory, prefix, textureId, scales, width, height);
    }
}
