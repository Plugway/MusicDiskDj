package plugway.mc.music.disc.dj.config;

import plugway.mc.music.disc.dj.music.disks.MinecraftDiskProvider;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationManager {
    private static final plugway.mc.music.disc.dj.config.McmddjConfig CONFIG = plugway.mc.music.disc.dj.config.McmddjConfig.createAndLoad();
    private static List<String> disks;

    public static void init(){
        disks = CONFIG.disks();
    }
    public static void set(String trackId, int diskNum){
        disks.set(diskNum, trackId);
        CONFIG.disks(disks);
    }
    public static List<String> getAll(){
        return disks;
    }
    public static void saveConfig(){
        CONFIG.save();
    }
    public static List<String> getEmpty(){
        var result = new ArrayList<String>();
        for (int i = 0; i < MinecraftDiskProvider.musicDisksCount; i++) {
            result.add("");
        }
        return result;
    }
    public static void clear(){
        disks = getEmpty();
        CONFIG.disks(disks);
    }
}
