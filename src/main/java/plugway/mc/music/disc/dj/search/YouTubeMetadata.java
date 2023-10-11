package plugway.mc.music.disc.dj.search;

import io.sfrei.tracksearch.tracks.Track;
import io.sfrei.tracksearch.tracks.YouTubeTrack;
import io.sfrei.tracksearch.tracks.metadata.YouTubeTrackMetadata;
import plugway.mc.music.disc.dj.MusicDiskDj;
import plugway.mc.music.disc.dj.gui.handlers.StatusHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class YouTubeMetadata {
    public static Track getTrack(String videoId) {
        return getTracks(new ArrayList<>(Collections.singleton(videoId)), false, null).get(0);
    }

    public static List<Track> getTracks(List<String> videoIds, boolean useStatusHandler, StatusHandler statusHandler){
        List<Track> trackList = new ArrayList<>();
        for (int i = 0; i < videoIds.size(); i++) {
            trackList.add(null);
        }
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        if (useStatusHandler)
            statusHandler.getProgressBarHandler().setMaxBumpValue(videoIds.size());
        for(var i = 0; i < videoIds.size(); i++){
            String videoId = videoIds.get(i);
            int index = i;
            executorService.submit(() -> {
                System.out.println(index + " thread started with id:" + videoId);
                try{
                    if (useStatusHandler)
                        statusHandler.getProgressBarHandler().bump();
                    if (videoId.equals(""))//if empty -> skip
                        throw new Exception();
                    String videoUrl = "https://www.youtube.com/watch?v="+videoId;
                    String command = MusicDiskDj.modDirectoryPath+"\\yt-dlp.exe";// --skip-download --print title --print duration --print thumbnail --print channel --print view_count --no-warnings " + videoUrl;
                    List<String> commandList = Arrays.asList(command, "--skip-download", "--print", "title", "--print", "duration", "--print", "thumbnail", "--print", "channel", "--print", "view_count", "--no-warnings", videoUrl);
                    ProcessBuilder processBuilder = new ProcessBuilder(commandList);
                    Process process = processBuilder.start();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    String[] videoInfo = new String[5];
                    process.waitFor();
                    int localIndex = 0;
                    while ((line = reader.readLine()) != null) {
                        videoInfo[localIndex++] = line;
                    }
                    Track track = YouTubeTrack.builder()
                            .url(videoUrl)
                            .title(videoInfo[0])
                            .trackMetadata(YouTubeTrackMetadata.of(videoInfo[3], "", Long.parseLong(videoInfo[4]), videoInfo[2].split("\\?")[0]))
                            .duration(Duration.ofSeconds(Long.parseLong(videoInfo[1]))).build();
                    trackList.set(index, track);
                }catch (Exception e){
                    trackList.set(index, MusicSearchProvider.getEmptyTrack());
                }
                System.out.println(index + " thread ended with result:" + trackList.get(index).toString());
            });
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return trackList;
    }

}
