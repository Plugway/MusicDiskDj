package plugway.mc.music.disc.dj.music.downloader;

import io.sfrei.tracksearch.tracks.Track;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import plugway.mc.music.disc.dj.MusicDiskDj;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class MusicDownloader {
    public static File downloadTrack(Track track, int insteadOfDisk) {          //kinda useless
        File file = new File(MusicDiskDj.tempPath+"\\"+insteadOfDisk+"_"+track.getCleanTitle()+".weba");
        try {
            FileUtils.copyURLToFile(new URL(track.getStreamUrl()), file); //replace with smth faster
        } catch (Exception e){
            System.out.println("error when trying to download");
        }
        return file;
    }
    public static File downloadPreview(Track track, int insteadOfDisk){
        File file = new File(MusicDiskDj.tempPath+"\\"+insteadOfDisk+"_"+track.getCleanTitle()+".jpeg");
        try {
            FileUtils.copyURLToFile(new URL(track.getTrackMetadata().getThumbNailUrl()), file);
        } catch (IOException e){
            MusicDiskDj.LOGGER.severe("Error while downloading texture: " + e);
            MusicDiskDj.LOGGER.severe("Stack trace: " + ExceptionUtils.getStackTrace(e));
        }
        return file;
    }
}
