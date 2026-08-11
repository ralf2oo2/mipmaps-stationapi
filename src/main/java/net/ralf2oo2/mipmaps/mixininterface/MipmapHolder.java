package net.ralf2oo2.mipmaps.mixininterface;

import net.modificationstation.stationapi.api.client.texture.NativeImage;

public interface MipmapHolder {
    NativeImage[] mipmaps_getMipmapLevels();

    void mipmaps_upload(int offsetX, int offsetY, int unpackSkipPixels, int unpackSkipRows, NativeImage[] images);

    void mipmaps_generateMipmaps(int mipmapLevels);
}
