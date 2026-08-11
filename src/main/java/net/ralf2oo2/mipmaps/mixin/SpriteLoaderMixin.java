package net.ralf2oo2.mipmaps.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.SpriteContents;
import net.modificationstation.stationapi.api.client.texture.SpriteLoader;
import net.modificationstation.stationapi.api.util.Identifier;
import net.ralf2oo2.mipmaps.config.Config;
import net.ralf2oo2.mipmaps.mixininterface.MipmapHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(SpriteLoader.class)
public class SpriteLoaderMixin {
    @ModifyExpressionValue(
            method = "stitch",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/concurrent/CompletableFuture;completedFuture(Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;"
            )
    )
    private CompletableFuture<Void> mipmaps_replaceCompletedFuture(CompletableFuture<Void> original, List<SpriteContents> sprites, Executor executor, @Local(ordinal = 0) Map<Identifier, Sprite> map) {
        if(Config.CONFIG.level.isEnabled()) {
            return CompletableFuture.runAsync(() -> map.values().forEach(spritex -> ((MipmapHolder)spritex.getContents()).mipmaps_generateMipmaps(Config.CONFIG.level.getLevel())), executor);
        } else {
            return original;
        }
    }
}
