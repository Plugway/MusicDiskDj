package plugway.mc.music.disc.dj.gui.color;

import io.github.cottonmc.cotton.gui.widget.WLabel;

public class ColorUtils {
    /**
     * Shifts RGB of color by value
     * @param value Shift value from -255 to 255
     * @param color ARGB color
     * @return ARGB color
     */
    public static int shiftColor(int value, int color){//move to color utils
        int alpha = (color >> 24) & 0xFF;
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;

        red += value;
        green += value;
        blue += value;

        red = java.lang.Math.max(java.lang.Math.min(red, 255), 0);
        green = java.lang.Math.max(java.lang.Math.min(green, 255), 0);
        blue = java.lang.Math.max(java.lang.Math.min(blue, 255), 0);

        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }
    public static void updateYTColor(WLabel youTubeAvaiLabel, boolean connected){
        if (connected)
            youTubeAvaiLabel.setColor(Colors.RedYT.getColor(), Colors.RedYT.getColor());
        else
            youTubeAvaiLabel.setColor(Colors.White.getColor(), Colors.White.getColor());

    }
    public static void updateSCColor(WLabel soundCloudAvaiLabel, boolean connected){
        if (connected)
            soundCloudAvaiLabel.setColor(Colors.BrownSC.getColor(), Colors.BrownSC.getColor());
        else
            soundCloudAvaiLabel.setColor(Colors.White.getColor(), Colors.White.getColor());
    }
}
