package plugway.mc.music.disc.dj.utils;

public class SystemChecker {
    public static String getOSName() {
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("win")) {
            return "Windows";
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("mac")) {
            return "Linux/Unix/Mac";
        } else {
            return "Unknown";
        }
    }
}
