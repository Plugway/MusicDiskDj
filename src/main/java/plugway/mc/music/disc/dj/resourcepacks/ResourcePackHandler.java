package plugway.mc.music.disc.dj.resourcepacks;

import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;
import org.apache.commons.lang3.exception.ExceptionUtils;
import plugway.mc.music.disc.dj.MusicDiskDj;

import java.util.concurrent.ExecutionException;

public class ResourcePackHandler {
    public static void DisableResourcePack(String name){
        ResourcePackManager manager = MinecraftClient.getInstance().getResourcePackManager();
        ResourcePackProfile profile = manager.getProfile(name);
        if (profile != null){
            manager.disable(name);
        }
        reloadResources();
    }

    public static void EnableResourcePack(String name) {
        ResourcePackManager manager = MinecraftClient.getInstance().getResourcePackManager();
        ResourcePackProfile profile = manager.getProfile(name);
        if (profile != null){
            manager.enable(name);
        }
        reloadResources();
    }

    private static void reloadResources() {
        try {
            MinecraftClient.getInstance().reloadResources().get();
        } catch (InterruptedException | ExecutionException e){
            MusicDiskDj.LOGGER.severe("Error while reloading resources: " + e);
            MusicDiskDj.LOGGER.severe("Stack trace: " + ExceptionUtils.getStackTrace(e));
        }
    }
}
