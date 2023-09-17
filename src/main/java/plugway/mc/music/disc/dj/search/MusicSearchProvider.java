package plugway.mc.music.disc.dj.search;

import io.sfrei.tracksearch.clients.MultiSearchClient;
import io.sfrei.tracksearch.clients.youtube.YouTubeClient;
import io.sfrei.tracksearch.tracks.*;
import io.sfrei.tracksearch.tracks.metadata.SoundCloudTrackFormat;
import io.sfrei.tracksearch.tracks.metadata.SoundCloudTrackInfo;
import io.sfrei.tracksearch.tracks.metadata.SoundCloudTrackMetadata;
import io.sfrei.tracksearch.tracks.metadata.TrackMetadata;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class MusicSearchProvider {
    private static YouTubeClient client = new YouTubeClient();   //add soundcloud support
    private static List<Track> failedSearch = new ArrayList<>();
    private static final Track failedTrack = SoundCloudTrack.builder().title("Search failed!").build();
    private static final Track emptyTrack = SoundCloudTrack.builder().title("").duration(Duration.ZERO).build();

    public static List<Track> musicSearch(String query){
        List<Track> trackList;
        try{
            trackList = new ArrayList<>(client.getTracksForSearch(query));
//            var test2 = trackList.get(0).getUrl();
//            var test = YouTubeTrack.builder().url(test2).build();
//            var test3 = client.loadTrackInfo(test);
//            System.out.println(test3);
        } catch (Exception e){
            System.out.println("Search failed!");
            if (failedSearch.size() == 0)
                failedSearch.add(failedTrack);
            return failedSearch;
        }

        return trackList;
    }
    public static List<Track> getEmptyList(int size){
        List<Track> emptyList = new ArrayList<>();
        for (int i = 0; i < size; i++){
            emptyList.add(emptyTrack);
        }
        return emptyList;
    }
    public static Track getEmptyTrack(){
        return emptyTrack;
    }
    public static boolean isEmptyTrack(Track track){
        return track.getDuration() == null || track.getDuration().isNegative() || track.getDuration().isZero();
    }
}