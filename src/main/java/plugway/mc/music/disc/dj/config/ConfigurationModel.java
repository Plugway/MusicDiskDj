package plugway.mc.music.disc.dj.config;

import io.wispforest.owo.config.annotation.Config;

import java.util.List;

@Config(name = "mcmddj", wrapperName = "McmddjConfig")
public class ConfigurationModel {
    public List<String> disks = ConfigurationManager.getEmpty();
}
