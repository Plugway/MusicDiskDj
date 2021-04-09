package plugway.mc.music.disc.dj.music.downloader;

import io.sfrei.tracksearch.tracks.Track;
import org.apache.commons.io.FileUtils;
import plugway.mc.music.disc.dj.MusicDiskDj;

import java.io.File;
import java.net.URL;


public class MusicDownloader {
    public static File downloadTrack(Track track, int insteadOfDisk) {          //kinda useless
        File file = new File(MusicDiskDj.tempPath+"\\"+insteadOfDisk+"_"+track.getCleanTitle()+".weba");
        try {
            FileUtils.copyURLToFile(new URL(track.getStreamUrl()), file); //replace with smth faster
        } catch (Exception e){
            System.out.println("pizdec");
        }
        return file;
    }
    public static File downloadPreview(Track track, int insteadOfDisk){
        File file = new File(MusicDiskDj.tempPath+"\\"+insteadOfDisk+"_"+track.getCleanTitle()+".jpg");
        try {
            FileUtils.copyURLToFile(new URL(track.getTrackMetadata().getThumbNailUrl()), file);
        } catch (Exception e){
            System.out.println("pizdec");
        }
        return file;
    }
}
