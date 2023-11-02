package plugway.mc.music.disc.dj.gui.text;

import net.minecraft.text.Text;

import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class TextUtils {
    public static Text toText(String string){
        return Text.of(string);
    }
    public static Text cutStringTo(int charNum, String string){
        if (string.length() > charNum)
            string = string.substring(0,charNum) + "...";
        return toText(string);
    }
    public static Text toPrettyNumber(Long number, String endsWith){
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');
        DecimalFormat formatter = new DecimalFormat("###,###", symbols);
        return toText(formatter.format(number) + endsWith);
    }
    public static String getTrueTitle(String title, String channelName){
        if (title.contains(" - "))
            return title;
        if (title.contains("-"))
            return title.replaceFirst("-", " - ");
        return  channelName + " - " + title;
    }
    public static String fixEncoding(String input){
        try {
            return new String(input.getBytes("windows-1251"), StandardCharsets.UTF_8);
        } catch (Exception ignored){}       //Never occurs
        return input;
    }
}
