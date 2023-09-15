package plugway.mc.music.disc.dj.gui.delegates;
import net.minecraft.screen.PropertyDelegate;

public class ProgressBarDelegate implements PropertyDelegate {
    private int currentValue;
    private int maxValue;

    public ProgressBarDelegate(int initialValue, int maxValue) {
        this.currentValue = initialValue;
        this.maxValue = maxValue;
    }

    @Override
    public int get(int index) {
        if (index == 0) {
            return currentValue;
        } else if (index == 1) {
            return maxValue;
        }
        return 0;
    }

    @Override
    public void set(int index, int value) {
        if (index == 0) {
            currentValue = value;
        } else if (index == 1) {
            maxValue = value;
        }
    }

    @Override
    public int size() {
        return 2; // Number of properties managed by this delegate
    }
}