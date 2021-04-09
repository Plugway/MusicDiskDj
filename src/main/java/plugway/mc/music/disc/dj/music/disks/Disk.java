package plugway.mc.music.disc.dj.music.disks;

import net.minecraft.util.Identifier;

public class Disk {
    private String author;
    private String name;
    private Identifier id;
    public Disk(String author, String name, Identifier id){
        this.author = author;
        this.name = name;
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public String getName() {
        return name;
    }

    public Identifier getId(){
        return id;
    }
}
