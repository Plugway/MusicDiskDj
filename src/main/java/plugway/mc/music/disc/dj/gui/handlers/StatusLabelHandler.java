package plugway.mc.music.disc.dj.gui.handlers;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import net.minecraft.text.Text;

public class StatusLabelHandler {
    private WLabel statusLabel;
    private Status currentStatus;

    public StatusLabelHandler(){
        currentStatus = Status.ready;
        this.statusLabel = new WLabel(getTranslatable(Status.ready));
    }

    public WLabel getStatusLabel(){
        return statusLabel;
    }

    public Status getCurrentStatus(){
        return currentStatus;
    }

    public void setCurrentStatus(Status currentStatus){
        this.currentStatus = currentStatus;
        statusLabel.setText(getTranslatable(currentStatus));
    }
    public void reset(){
        currentStatus = Status.ready;
        statusLabel.setText(getTranslatable(Status.ready));
    }

    private static Text getTranslatable(Status status){
        return Text.translatable("musicdiskdj.name.label.status."+status.name());
    }
}
