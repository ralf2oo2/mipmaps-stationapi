package net.ralf2oo2.mipmaps.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.LivingEntity;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    // TODO: figure out how to fix the black fading when blending between mips
//    @WrapOperation(
//            method = "renderFrame",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/client/render/WorldRenderer;render(Lnet/minecraft/entity/LivingEntity;ID)I",
//                    ordinal = 0
//            )
//    )
//    int mipmaps_enableBlending(WorldRenderer instance, LivingEntity livingEntity, int layer, double tickDelta, Operation<Integer> original) {
//
//        GL11.glEnable(GL11.GL_BLEND);
//        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//
//        int result = original.call(instance, livingEntity, layer, tickDelta);
//
//        GL11.glDisable(GL11.GL_BLEND);
//
//        return result;
//    }
}
