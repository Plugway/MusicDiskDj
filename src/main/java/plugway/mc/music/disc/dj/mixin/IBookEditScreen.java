package plugway.mc.music.disc.dj.mixin;

import net.minecraft.client.gui.screen.ingame.BookEditScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;


public interface IBookEditScreen {
    void disableAllMdDjButtons();
    void enableAllMdDjButtons();
}
