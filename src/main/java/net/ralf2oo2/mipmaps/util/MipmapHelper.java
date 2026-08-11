package net.ralf2oo2.mipmaps.util;

import net.modificationstation.stationapi.api.client.texture.NativeImage;
import net.modificationstation.stationapi.api.util.Util;

public class MipmapHelper {
    private static final float[] COLOR_FRACTIONS = Util.make(new float[256], (list) -> {
        for(int i = 0; i < list.length; ++i) {
            list[i] = (float)Math.pow((float)i / 255.0F, 2.2);
        }
    });

    public static NativeImage[] getMipmapLevelsImages(NativeImage[] originals, int mipmap) {
        if (mipmap + 1 <= originals.length) {
            return originals;
        } else {
            NativeImage[] nativeImages = new NativeImage[mipmap + 1];
            nativeImages[0] = originals[0];
            boolean hasAlpha = hasAlpha(nativeImages[0]);

            for(int i = 1; i <= mipmap; ++i) {
                if (i < originals.length) {
                    nativeImages[i] = originals[i];
                } else {
                    NativeImage src = nativeImages[i - 1];
                    NativeImage dst = new NativeImage(src.getWidth() >> 1, src.getHeight() >> 1, false);
                    int w = dst.getWidth();
                    int h = dst.getHeight();

                    for(int x = 0; x < w; ++x) {
                        for(int y = 0; y < h; ++y) {
                            dst.setColor(x, y, blend(
                                    src.getColor(x * 2, y * 2),
                                    src.getColor(x * 2 + 1, y * 2),
                                    src.getColor(x * 2, y * 2 + 1),
                                    src.getColor(x * 2 + 1, y * 2 + 1),
                                    hasAlpha
                            ));
                        }
                    }

                    nativeImages[i] = dst;
                }
            }

            return nativeImages;
        }
    }

    private static boolean hasAlpha(NativeImage image) {
        for(int i = 0; i < image.getWidth(); ++i) {
            for(int j = 0; j < image.getHeight(); ++j) {
                if (((image.getColor(i, j) >> 24) & 0xFF) < 255) {
                    return true;
                }
            }
        }
        return false;
    }

    private static int blend(int c1, int c2, int c3, int c4, boolean checkAlpha) {
        if (checkAlpha) {
            float a1 = ((c1 >> 24) & 0xFF) / 255.0F;
            float a2 = ((c2 >> 24) & 0xFF) / 255.0F;
            float a3 = ((c3 >> 24) & 0xFF) / 255.0F;
            float a4 = ((c4 >> 24) & 0xFF) / 255.0F;

            float totalWeight = a1 + a2 + a3 + a4;

            if (totalWeight <= 0.001F) {
                return 0;
            }

            float r = (getColorFraction(c1 & 0xFF) * a1 +
                               getColorFraction(c2 & 0xFF) * a2 +
                               getColorFraction(c3 & 0xFF) * a3 +
                               getColorFraction(c4 & 0xFF) * a4) / totalWeight;

            float g = (getColorFraction((c1 >> 8) & 0xFF) * a1 +
                               getColorFraction((c2 >> 8) & 0xFF) * a2 +
                               getColorFraction((c3 >> 8) & 0xFF) * a3 +
                               getColorFraction((c4 >> 8) & 0xFF) * a4) / totalWeight;

            float b = (getColorFraction((c1 >> 16) & 0xFF) * a1 +
                               getColorFraction((c2 >> 16) & 0xFF) * a2 +
                               getColorFraction((c3 >> 16) & 0xFF) * a3 +
                               getColorFraction((c4 >> 16) & 0xFF) * a4) / totalWeight;

            float avgAlpha = totalWeight * 0.25F;

            int alphaOut = (int)(avgAlpha * 255.0F);

            if (alphaOut < 96) {
                return 0;
            }

            int redOut   = (int)(Math.pow(r, 1.0 / 2.2) * 255.0F);
            int greenOut = (int)(Math.pow(g, 1.0 / 2.2) * 255.0F);
            int blueOut  = (int)(Math.pow(b, 1.0 / 2.2) * 255.0F);

            return (alphaOut << 24) | (blueOut << 16) | (greenOut << 8) | redOut;
        } else {
            int a = getColorComponent(c1, c2, c3, c4, 24);
            int b = getColorComponent(c1, c2, c3, c4, 16);
            int g = getColorComponent(c1, c2, c3, c4, 8);
            int r = getColorComponent(c1, c2, c3, c4, 0);
            return (a << 24) | (b << 16) | (g << 8) | r;
        }
    }

    private static int getColorComponent(int c1, int c2, int c3, int c4, int bitShift) {
        float f = getColorFraction((c1 >> bitShift) & 0xFF);
        float g = getColorFraction((c2 >> bitShift) & 0xFF);
        float h = getColorFraction((c3 >> bitShift) & 0xFF);
        float i = getColorFraction((c4 >> bitShift) & 0xFF);
        float average = (float)((float)Math.pow((f + g + h + i) * 0.25, 0.45454545454545453));
        return (int)(average * 255.0);
    }

    private static float getColorFraction(int value) {
        return COLOR_FRACTIONS[value & 255];
    }
}
