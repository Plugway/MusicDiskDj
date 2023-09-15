package plugway.mc.music.disc.dj.gui.widgets;

import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

public class WClickablePlainPanel extends WPlainPanel {
    private Runnable onClick;
    private boolean allowToClick = false;

    @Override
    public InputResult onClick(int x, int y, int button) {
        super.onClick(x, y, button);
        if (allowToClick && this.isWithinBounds(x, y)) {
            MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            if (this.onClick != null) {
                this.onClick.run();
                return InputResult.PROCESSED;
            }
        }
        return InputResult.IGNORED;
    }
    public WClickablePlainPanel setOnClick(Runnable onClick) {
        this.onClick = onClick;
        return this;
    }
    public boolean isAllowToClick(){
        return allowToClick;
    }
    public void setAllowToClick(boolean allowToClick){
        this.allowToClick = allowToClick;
    }
}
