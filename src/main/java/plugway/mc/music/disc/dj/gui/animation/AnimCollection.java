package plugway.mc.music.disc.dj.gui.animation;

import io.github.cottonmc.cotton.gui.widget.WTextField;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import plugway.mc.music.disc.dj.MusicDiskDj;
import plugway.mc.music.disc.dj.gui.color.ColorUtils;
import plugway.mc.music.disc.dj.gui.color.Colors;
import plugway.mc.music.disc.dj.utils.Math;

public class AnimCollection {
    /**
     * Animates WTextField suggestion
     * @param field Text field
     * @param blinkSuggestion Blink text
     * @param animSpeed Animation speed (1-100), 0 - default speed (25)
     */
    public static void animateTextFieldAsync(WTextField field, Text blinkSuggestion, int animSpeed) {
        new Thread(() -> {
            int suggestionColor = Colors.Gray50.getColor();
            Text origSuggestion = field.getSuggestion();
            int minColor = Colors.Black4.getColor();
            int maxColor = Colors.LightGray94.getColor();
            int shiftValue = 10;
            int currentColor = suggestionColor;
            int sleepTime = animSpeed <= 0 || animSpeed > 100 ? 50 : (int)Math.map(animSpeed, 0, 100, 0, 200);
            try {
                for (int i = 0; i < 3; i++) {
                    while (currentColor > minColor){//fade out suggestion
                        currentColor = ColorUtils.shiftColor(-shiftValue, currentColor);
                        field.setSuggestionColor(currentColor);
                        Thread.sleep(sleepTime);
                    }
                    field.setSuggestion(blinkSuggestion);
                    while (currentColor < maxColor){//fade in blink
                        currentColor = ColorUtils.shiftColor(shiftValue, currentColor);
                        field.setSuggestionColor(currentColor);
                        Thread.sleep(sleepTime);
                    }
                    while (currentColor > minColor){//fade out blink
                        currentColor = ColorUtils.shiftColor(-shiftValue, currentColor);
                        field.setSuggestionColor(currentColor);
                        Thread.sleep(sleepTime);
                    }
                    field.setSuggestion(origSuggestion);
                    while (currentColor < suggestionColor){//fade in suggestion
                        currentColor = ColorUtils.shiftColor(shiftValue, currentColor);
                        field.setSuggestionColor(currentColor);
                        Thread.sleep(sleepTime);
                    }
                }
            } catch (InterruptedException e){
                MusicDiskDj.LOGGER.info("Animation interrupted.");
                field.setSuggestionColor(suggestionColor);
                field.setSuggestion(origSuggestion);
            }
        }).start();
    }
    public static void exportErrAnimAsync(ButtonWidget exportButton){
        new Thread(() -> {
            exportButton.active = false;
            exportButton.setMessage(Text.translatable("musicdiskdj.name.error"));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                MusicDiskDj.LOGGER.info("Animation interrupted.");
            }
            exportButton.active = true;
            exportButton.setMessage(Text.translatable("musicdiskdj.name.label.button.export"));
        }).start();
    }
    public static void exportDoneAnimAsync(ButtonWidget exportButton){
        new Thread(() -> {
            exportButton.active = false;
            exportButton.setMessage(Text.translatable("musicdiskdj.name.done"));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                MusicDiskDj.LOGGER.info("Animation interrupted.");
            }
            exportButton.active = true;
            exportButton.setMessage(Text.translatable("musicdiskdj.name.label.button.export"));
        }).start();
    }
}
