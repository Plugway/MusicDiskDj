package plugway.mc.music.disc.dj.music.disks;

import net.minecraft.item.MusicDiscItem;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class MinecraftDiskProvider {
    public static final List<Disk> disks = getDisks();
    public static final int musicDisksCount = disks.size();
    private static List<Disk> getDisks(){
        List<Disk> disks = new ArrayList<>();
        List<MusicDiscItem> discs = Registries.ITEM.stream()
                .filter(entry -> entry.getRegistryEntry().getKey().get().getValue().getNamespace().equals("minecraft") && entry instanceof MusicDiscItem)
                .map(entry -> (MusicDiscItem)entry)
                .toList();
        for (MusicDiscItem disc:discs) {
            disks.add(new Disk(disc, new Identifier("minecraft","textures/item/"+disc.getTranslationKey().split("\\.")[2]+".png")));
        }
        return disks;
    }
}
