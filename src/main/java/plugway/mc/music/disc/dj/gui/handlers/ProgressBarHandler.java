package plugway.mc.music.disc.dj.gui.handlers;

import io.github.cottonmc.cotton.gui.widget.WBar;
import net.minecraft.util.Identifier;
import plugway.mc.music.disc.dj.gui.delegates.ProgressBarDelegate;
import plugway.mc.music.disc.dj.utils.Math;

public class ProgressBarHandler {
    private ProgressBarDelegate properties;
    private WBar progressBar;
    private int minValue;
    private int maxValue;
    private int sectionsCount;
    private int currentSection;
    private int currentValue;

    private int maxBumpValue;
    private int currentBump;

    public ProgressBarHandler(WBar.Direction direction, int minValue, int maxValue){
        this.sectionsCount = 1;
        this.currentSection = 1;
        this.minValue = java.lang.Math.max(minValue, 0);
        this.maxValue = java.lang.Math.max(maxValue, 0);
        properties = new ProgressBarDelegate(this.minValue, this.maxValue);
        this.progressBar = new WBar(new Identifier("mcmddj", "textures/progress_bar_empty.png"),
                new Identifier("mcmddj", "textures/progress_bar_full.png"), 0, 1, direction).setProperties(properties);
    }
    public WBar getProgressBar(){
        return progressBar;
    }
    public int getCurrentValue() {
        return currentValue;
    }
    public void setSectionsCount(int sectionsCount){
        this.sectionsCount = java.lang.Math.max(sectionsCount, 1);
    }
    public void nextSection(){
        currentSection = java.lang.Math.min(currentSection+1, sectionsCount);
        setCurrentValue(0);
        setMaxBumpValue(0);
        currentBump = 0;
    }
    public void setCurrentValue(double currentValue) {
        currentValue = currentValue < 0 ? 0 : currentValue > 1 ? 1 : currentValue;
        var sectionSize = ((double)maxValue-minValue)/sectionsCount;
        this.currentValue = (int)(sectionSize*(currentSection-1)+Math.map(currentValue, 0, 1, 0, sectionSize));
        updateProgressBar();
    }
    public boolean isLastSection(){
        return currentSection == sectionsCount;
    }
    public void reset(){
        this.currentValue = minValue;
        this.sectionsCount = 1;
        this.currentSection = 1;

        setMaxBumpValue(0);
        currentBump = 0;

        updateProgressBar();
    }
    private void updateProgressBar(){
        properties.set(0, currentValue);
        progressBar.setProperties(properties);
    }

    public void setMaxBumpValue(int value){
        maxBumpValue = java.lang.Math.max(value, 0);
    }
    public void bump(){
        setCurrentValue(((double)++currentBump)/maxBumpValue);
    }
}
