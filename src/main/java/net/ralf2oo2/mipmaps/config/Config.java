package net.ralf2oo2.mipmaps.config;

import net.glasslauncher.mods.gcapi3.api.ConfigEntry;
import net.glasslauncher.mods.gcapi3.api.ConfigRoot;

public class Config {
    @ConfigRoot(value = "config", visibleName = "Mipmap Config")
    public static ConfigFields CONFIG = new ConfigFields();

    public static class ConfigFields{
        @ConfigEntry(name = "Mipmap Level")
        public MipmapLevel level = MipmapLevel.OFF;

        @ConfigEntry(name = "Mipmap Bias")
        public Float bias = 0.0f;

        @ConfigEntry(name = "Mipmap Overrides", description = "Allows resourcepacks to override the mips of textures.")
        public Boolean overrides = true;

        @ConfigEntry(name = "Debug Colors")
        public Boolean debugColors = false;

        @ConfigEntry(name = "Debug Logging", description = "Extra logs when creating mipmaps")
        public Boolean debugLogging = false;
    }
}
