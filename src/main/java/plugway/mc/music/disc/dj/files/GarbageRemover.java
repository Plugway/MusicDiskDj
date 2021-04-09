package plugway.mc.music.disc.dj.files;

import org.apache.commons.io.FileUtils;
import plugway.mc.music.disc.dj.MusicDiskDj;

import java.io.File;
import java.io.IOException;

public class GarbageRemover {
    public static void remove() throws IOException {
        FileUtils.cleanDirectory(new File(MusicDiskDj.tempPath));
    }
}
