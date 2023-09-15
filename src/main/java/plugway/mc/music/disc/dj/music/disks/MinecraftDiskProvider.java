package plugway.mc.music.disc.dj.music.disks;

import net.minecraft.util.Identifier;

public class MinecraftDiskProvider {
    public static final Disk[] disks = generateDisks();
    public static final int musicDisksCount = 16;
    private static Disk[] generateDisks(){
        Disk[] disks = new Disk[musicDisksCount];
        disks[0] = new Disk("C418", "13", new Identifier("minecraft","textures/item/music_disc_13.png"));
        disks[1] = new Disk("C418", "cat", new Identifier("minecraft","textures/item/music_disc_cat.png"));
        disks[2] = new Disk("C418", "blocks", new Identifier("minecraft","textures/item/music_disc_blocks.png"));
        disks[3] = new Disk("C418", "chirp", new Identifier("minecraft","textures/item/music_disc_chirp.png"));
        disks[4] = new Disk("C418", "far", new Identifier("minecraft","textures/item/music_disc_far.png"));
        disks[5] = new Disk("C418", "mall", new Identifier("minecraft","textures/item/music_disc_mall.png"));
        disks[6] = new Disk("C418", "mellohi", new Identifier("minecraft","textures/item/music_disc_mellohi.png"));
        disks[7] = new Disk("C418", "stal", new Identifier("minecraft","textures/item/music_disc_stal.png"));
        disks[8] = new Disk("C418", "strad", new Identifier("minecraft","textures/item/music_disc_strad.png"));
        disks[9] = new Disk("C418", "ward", new Identifier("minecraft","textures/item/music_disc_ward.png"));
        disks[10] = new Disk("C418", "11", new Identifier("minecraft","textures/item/music_disc_11.png"));
        disks[11] = new Disk("C418", "wait", new Identifier("minecraft","textures/item/music_disc_wait.png"));
        disks[12] = new Disk("Lena Raine", "Pigstep", new Identifier("minecraft","textures/item/music_disc_pigstep.png"));
        disks[13] = new Disk("Lena Raine", "otherside", new Identifier("minecraft","textures/item/music_disc_otherside.png"));
        disks[14] = new Disk("Samuel Åberg", "5", new Identifier("minecraft","textures/item/music_disc_5.png"));
        disks[15] = new Disk("Aaron Cherof", "Relic", new Identifier("minecraft","textures/item/music_disc_relic.png"));
        return disks;
    }
}
