package plugway.mc.music.disc.dj.keybinds;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import plugway.mc.music.disc.dj.gui.MainGui;
import plugway.mc.music.disc.dj.gui.MainScreen;

public class OpenMenu {
    private static KeyBinding openMenu;
    private static MainScreen mainScreen;
    public static void initOpenMenu(){
        openMenu = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.musicdiskdj.openmenu",
                InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_J,
                "category.musicdiskdj.test"));
        System.out.println("Keybind created");
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenu.wasPressed()) {
                assert client.player != null;

                open();

            }
        });
    }
    private static void open(){
        if (mainScreen == null){
            mainScreen = new MainScreen(new MainGui());
        }
        MinecraftClient.getInstance().setScreen(mainScreen);
    }
    public static void resetGUI(){
        mainScreen = null;
    }
}
