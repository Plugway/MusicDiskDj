package plugway.mc.music.disc.dj.music.disks;

import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.util.Identifier;

import java.awt.*;

public class Disk {
    private MusicDiscItem disk;
    private Identifier id;
    public Disk(MusicDiscItem disk, Identifier id){
        this.disk = disk;
        this.id = id;
    }

    public String getAuthor() {
        return I18n.translate(disk.getTranslationKey()+".desc").split(" - ")[0];
    }

    public String getName() {
        return I18n.translate(disk.getTranslationKey()+".desc").split(" - ")[1];
    }
    public Identifier getId(){
        return id;
    }
}
