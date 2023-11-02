package plugway.mc.music.disc.dj.music.downloader;

import io.sfrei.tracksearch.tracks.Track;
import io.sfrei.tracksearch.tracks.YouTubeTrack;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import plugway.mc.music.disc.dj.MusicDiskDj;
import plugway.mc.music.disc.dj.search.LinkValidator;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class MusicDownloader {
    public static File downloadTrack(Track track, int insteadOfDisk){
        File oggTarget = track instanceof YouTubeTrack ?
                new File(MusicDiskDj.cachePath+"\\"+ LinkValidator.getYTId(track.getUrl())+".ogg") :
                new File(MusicDiskDj.tempPath+"\\"+insteadOfDisk+"_"+track.getCleanTitle()+".ogg");
        if (!oggTarget.exists())
            try {
                Runtime.getRuntime().exec(MusicDiskDj.modDirectoryPath+"\\yt-dlp.exe --extract-audio --audio-format vorbis --output \""+oggTarget.getName().substring(0, oggTarget.getName().length()-4)+"\" "+track.getUrl(),
                        null, oggTarget.getParentFile()).waitFor();    //make command for mono --postprocessor-args "-ac 1"
            } catch (IOException | InterruptedException e){
                MusicDiskDj.LOGGER.severe("Something went seriously wrong when downloading music via yt-dlp: " + e);
                MusicDiskDj.LOGGER.severe("Stack trace: " + ExceptionUtils.getStackTrace(e));
            }
        return oggTarget;
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
