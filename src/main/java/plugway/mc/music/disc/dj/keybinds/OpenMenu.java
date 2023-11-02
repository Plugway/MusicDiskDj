package plugway.mc.music.disc.dj.keybinds;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import plugway.mc.music.disc.dj.MusicDiskDj;
import plugway.mc.music.disc.dj.books.TextbookLogic;
import plugway.mc.music.disc.dj.gui.MainGui;
import plugway.mc.music.disc.dj.gui.MainScreen;

public class OpenMenu {
    private static KeyBinding openMenu;
    private static MainScreen mainScreen;
    private static MainGui mainGui;
    public static void initOpenMenu(){
        openMenu = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "musicdiskdj.key.openmenu",
                InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_J,
                "musicdiskdj.name.mcmddj"));
        MusicDiskDj.LOGGER.info("Keybind created");
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenu.wasPressed()) {
                assert client.player != null;

                open();

            }
        });
    }
    private static void open(){
        if (mainScreen == null){
            mainGui = new MainGui();
            mainScreen = new MainScreen(mainGui);
        }
        if (TextbookLogic.isAwaitingExport()){
            mainGui.addToQueue(mainGui.completeExport());
            mainGui.tryToRunNextTask();
        }
        MinecraftClient.getInstance().setScreen(mainScreen);
    }
    public static void resetGUI(){
        mainScreen = null;
        mainGui = null;
    }
}
