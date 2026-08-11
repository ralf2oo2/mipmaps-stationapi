package net.ralf2oo2.mipmaps.config;

import net.glasslauncher.mods.gcapi3.api.ConfigEntry;
import net.glasslauncher.mods.gcapi3.api.ConfigRoot;

public class Config {
    @ConfigRoot(value = "config", visibleName = "Mipmap Config")
    public static ConfigFields CONFIG = new ConfigFields();

    public static class ConfigFields{
        @ConfigEntry(name = "Mipmap Level")
        public MipmapLevel level = MipmapLevel.OFF;

        @ConfigEntry(name = "Debug Colors")
        public Boolean debugColors = false;
    }
}
