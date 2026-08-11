package net.ralf2oo2.mipmaps.mixin;

import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.render.model.BakedModel;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.BakedModelRendererImpl;
import net.ralf2oo2.mipmaps.config.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BakedModelRendererImpl.class)
public class BakedModelRendererImplMixin {
    @Inject(method = "renderGuiItemModel", at = @At("RETURN"))
    private void mipmaps_setFilters(ItemStack stack, int x, int y, BakedModel model, CallbackInfo ci){
        StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).setFilter(false, Config.CONFIG.level.isEnabled());
    }
}
