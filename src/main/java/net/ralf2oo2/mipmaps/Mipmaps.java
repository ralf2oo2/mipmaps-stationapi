package net.ralf2oo2.mipmaps;

import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.event.keyboard.KeyStateChangedEvent;
import net.modificationstation.stationapi.api.client.texture.SpriteAtlasTexture;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import java.nio.file.Files;
import java.nio.file.Path;


public class Mipmaps {
    @Entrypoint.Logger
    public static Logger LOGGER;

//    @EventListener
//    public void onKeyStateChanged(KeyStateChangedEvent event) {
//        if(Keyboard.getEventKeyState() && Keyboard.getEventKey() == Keyboard.KEY_O) {
//            SpriteAtlasTexture atlas = StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE);
//            Path path = FabricLoader.getInstance().getGameDir().resolve("screenshots").resolve("debug");
//            try {
//                Files.createDirectories(path);
//                atlas.save(atlas.getId(), path);
//            } catch (Exception e) {
//                LOGGER.error("Could not create directory for extracting atlases. {}", e.getMessage());
//            }
//        }
//    }
}
