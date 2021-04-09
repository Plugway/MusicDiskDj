package plugway.mc.music.disc.dj.keybinds;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.LiteralText;
import org.lwjgl.glfw.GLFW;
import plugway.mc.music.disc.dj.gui.MainGui;

public class OpenMenu {
    private static KeyBinding openMenu;
    public static void initOpenMenu(){
        openMenu = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.musicdiskdj.openmenu",
                InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_J,
                "category.musicdiskdj.test"));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenu.wasPressed()) {
                assert client.player != null;

                MainGui.open();

            }
        });
    }
}
