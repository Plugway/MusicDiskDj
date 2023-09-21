package plugway.mc.music.disc.dj.search;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkValidator {
    private static Pattern youtubePattern = Pattern.compile("^(https?\\:\\/\\/)?(www\\.)?(youtube\\.com|youtu\\.?be)\\/watch\\?v=([^&]+)", Pattern.CASE_INSENSITIVE);
    private static Pattern soundcloudPattern = Pattern.compile("^(https?\\:\\/\\/)?(www\\.)?soundcloud\\.com\\/.+", Pattern.CASE_INSENSITIVE);

    public static boolean isValidYTLink(String link){
        Matcher matcher = youtubePattern.matcher(link);
        return matcher.matches();
    }
    public static String getYTId(String link){
        Matcher matcher = youtubePattern.matcher(link);
        if (matcher.matches())
            return matcher.group(4);
        return null;
    }
    public static boolean isValidSCLink(String link){
        Matcher matcher = soundcloudPattern.matcher(link);
        return matcher.matches();
    }
}
