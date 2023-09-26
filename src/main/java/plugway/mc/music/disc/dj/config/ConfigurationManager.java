package plugway.mc.music.disc.dj.config;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationManager {
    private static final plugway.mc.music.disc.dj.config.McmddjConfig CONFIG = plugway.mc.music.disc.dj.config.McmddjConfig.createAndLoad();

    public static void add(String trackId, int diskNum){
        switch (diskNum){
            case 1:
                CONFIG.disk1(trackId);
                break;
            case 2:
                CONFIG.disk2(trackId);
                break;
            case 3:
                CONFIG.disk3(trackId);
                break;
            case 4:
                CONFIG.disk4(trackId);
                break;
            case 5:
                CONFIG.disk5(trackId);
                break;
            case 6:
                CONFIG.disk6(trackId);
                break;
            case 7:
                CONFIG.disk7(trackId);
                break;
            case 8:
                CONFIG.disk8(trackId);
                break;
            case 9:
                CONFIG.disk9(trackId);
                break;
            case 10:
                CONFIG.disk10(trackId);
                break;
            case 11:
                CONFIG.disk11(trackId);
                break;
            case 12:
                CONFIG.disk12(trackId);
                break;
            case 13:
                CONFIG.disk13(trackId);
                break;
            case 14:
                CONFIG.disk14(trackId);
                break;
            case 15:
                CONFIG.disk15(trackId);
                break;
            case 16:
                CONFIG.disk16(trackId);
                break;
        }
    }
    public static void clear(){
        CONFIG.disk1("");
        CONFIG.disk2("");
        CONFIG.disk3("");
        CONFIG.disk4("");
        CONFIG.disk5("");
        CONFIG.disk6("");
        CONFIG.disk7("");
        CONFIG.disk8("");
        CONFIG.disk9("");
        CONFIG.disk10("");
        CONFIG.disk11("");
        CONFIG.disk12("");
        CONFIG.disk13("");
        CONFIG.disk14("");
        CONFIG.disk15("");
        CONFIG.disk16("");
    }
}
