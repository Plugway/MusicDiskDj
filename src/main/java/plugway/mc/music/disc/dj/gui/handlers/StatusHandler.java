package plugway.mc.music.disc.dj.gui.handlers;

import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.WLabel;

public class StatusHandler {
    private ProgressBarHandler progressBarHandler;
    private StatusLabelHandler statusLabelHandler;


    public StatusHandler(ProgressBarHandler progressBarHandler, StatusLabelHandler statusLabelHandler){
        this.progressBarHandler = progressBarHandler;
        this.statusLabelHandler = statusLabelHandler;
    }

    public ProgressBarHandler getProgressBarHandler(){
        return progressBarHandler;
    }
    public WBar getProgressBar(){
        return progressBarHandler.getProgressBar();
    }
    public StatusLabelHandler getStatusLabelHandler(){
        return statusLabelHandler;
    }
    public WLabel getStatusLabel(){
        return statusLabelHandler.getStatusLabel();
    }
    public void setStatus(double currentProgressValue, Status status){
        progressBarHandler.setCurrentValue(currentProgressValue);
        statusLabelHandler.setCurrentStatus(status);
    }
    public void setStatus(double currentProgressValue){
        progressBarHandler.setCurrentValue(currentProgressValue);
    }
    public void setStatus(Status status){
        statusLabelHandler.setCurrentStatus(status);
    }

    public void reset(){
        progressBarHandler.reset();
        statusLabelHandler.reset();
    }
    public void resetIfLast(){
        if (this.getProgressBarHandler().isLastSection())
            reset();
    }
}
