package plugway.mc.music.disc.dj.resourcepacks;

import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;

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

    private static void reloadResources(){
        MinecraftClient.getInstance().reloadResources();
    }
}
