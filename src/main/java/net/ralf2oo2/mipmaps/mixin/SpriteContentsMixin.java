package net.ralf2oo2.mipmaps.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.modificationstation.stationapi.api.client.resource.metadata.AnimationResourceMetadata;
import net.modificationstation.stationapi.api.client.texture.NativeImage;
import net.modificationstation.stationapi.api.client.texture.SpriteContents;
import net.modificationstation.stationapi.api.client.texture.SpriteDimensions;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.crash.CrashException;
import net.modificationstation.stationapi.api.util.crash.CrashReport;
import net.modificationstation.stationapi.api.util.crash.CrashReportSection;
import net.ralf2oo2.mipmaps.config.Config;
import net.ralf2oo2.mipmaps.mixininterface.MipmapHolder;
import net.ralf2oo2.mipmaps.util.MipmapHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpriteContents.class)
public abstract class SpriteContentsMixin implements MipmapHolder {
    @Shadow
    @Final
    private NativeImage image;
    @Shadow
    @Final
    private int width;
    @Shadow
    @Final
    private int height;
    @Shadow
    @Final
    private Identifier id;

    @Shadow
    protected abstract int getFrameCount();

    @Unique
    private NativeImage[] mipmaps_mipmapLevels;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void mipmaps_init(Identifier id, SpriteDimensions dimensions, NativeImage image, AnimationResourceMetadata metadata, CallbackInfo ci) {
        this.mipmaps_mipmapLevels = new NativeImage[]{this.image};
    }

    @Override
    public void mipmaps_generateMipmaps(int mipmapLevels) {
        try {
            this.mipmaps_mipmapLevels = MipmapHelper.getMipmapLevelsImages(mipmaps_mipmapLevels, mipmapLevels);
        } catch (Throwable throwable) {
            CrashReport crashReport = CrashReport.create(throwable, "Generating mipmaps for frame");
            CrashReportSection crashReportSection = crashReport.addElement("Sprite being mipmapped");
            crashReportSection.add("First frame", () -> {
                StringBuilder stringBuilder = new StringBuilder();
                if (!stringBuilder.isEmpty()) {
                    stringBuilder.append(", ");
                }

                stringBuilder.append(this.image.getWidth()).append("x").append(this.image.getHeight());
                return stringBuilder.toString();
            });
            CrashReportSection crashReportSection2 = crashReport.addElement("Frame being iterated");
            crashReportSection2.add("Sprite name", this.id);
            crashReportSection2.add("Sprite size", () -> this.width + " x " + this.height);
            crashReportSection2.add("Sprite frames", () -> this.getFrameCount() + " frames");
            crashReportSection2.add("Mipmap levels", mipmapLevels);
            throw new CrashException(crashReport);
        }

        if(Config.CONFIG.debugColors) {
            int[] debugColors = new int[]{
                    0xFF0000FF,
                    0xFF00FF00,
                    0xFFFF0000,
                    0xFF00FFFF
            };

            for (int level = 1; level < this.mipmaps_mipmapLevels.length; level++) {
                NativeImage mip = this.mipmaps_mipmapLevels[level];
                int color = debugColors[(level - 1) % debugColors.length];

                for (int x = 0; x < mip.getWidth(); x++) {
                    for (int y = 0; y < mip.getHeight(); y++) {
                        int existing = mip.getColor(x, y);
                        if ((existing >> 24) != 0) {
                            mip.setColor(x, y, color);
                        }
                    }
                }
            }
        }

    }

    @Override
    public NativeImage[] mipmaps_getMipmapLevels() {
        return mipmaps_mipmapLevels;
    }

    @Inject(method = "close", at = @At("RETURN"))
    private void mipmaps_closeMipmaps(CallbackInfo ci) {
        if (this.mipmaps_mipmapLevels != null) {
            for (NativeImage img : this.mipmaps_mipmapLevels) {
                if (img != null) img.close();
            }
        }
    }

    @WrapOperation(
            method = "upload(IIIILnet/modificationstation/stationapi/api/client/texture/NativeImage;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/modificationstation/stationapi/api/client/texture/NativeImage;upload(IIIIIIIZZ)V"
            )
    )
    private void mipmaps_uploadAllLevels(NativeImage instance, int level, int offsetX, int offsetY, int unpackSkipPixels, int unpackSkipRows, int width, int height, boolean mipmap, boolean close, Operation<Void> original) {
        if(Config.CONFIG.level.isEnabled()) {
            this.mipmaps_upload(offsetX, offsetY, unpackSkipPixels, unpackSkipRows, this.mipmaps_mipmapLevels);
        } else {
            original.call(instance, level, offsetX, offsetY, unpackSkipPixels, unpackSkipRows, width, height,mipmap, close);
        }
    }



    @Override
    public void mipmaps_upload(int offsetX, int offsetY, int unpackSkipPixels, int unpackSkipRows, NativeImage[] images) {
        for (int i = 0; i < this.mipmaps_mipmapLevels.length; i++) {
            images[i].upload(i, offsetX >> i, offsetY >> i, unpackSkipPixels >> i, unpackSkipRows >> i, this.width >> i, this.height >> i, this.mipmaps_mipmapLevels.length > 1, false);
        }
    }
}
