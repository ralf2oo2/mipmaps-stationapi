package net.ralf2oo2.mipmaps.config;

import net.fabricmc.loader.api.FabricLoader;
import net.glasslauncher.mods.gcapi3.api.PreConfigSavedListener;
import net.glasslauncher.mods.gcapi3.impl.GlassYamlFile;
import net.minecraft.client.Minecraft;

public class ConfigChangedListener implements PreConfigSavedListener {
    @Override
    public void onPreConfigSaved(int source, GlassYamlFile oldValues, GlassYamlFile newValues) {
        if(oldValues.getInt("level", 0) != newValues.getInt("level", 0) || oldValues.getBoolean("debugColors", false) != newValues.getBoolean("debugColors", false)) {
            Minecraft.class.cast(FabricLoader.getInstance().getGameInstance()).textureManager.reload();
        }
    }
}
