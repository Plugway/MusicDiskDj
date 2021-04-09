package plugway.mc.music.disc.dj.search;

import io.sfrei.tracksearch.clients.MultiSearchClient;
import io.sfrei.tracksearch.clients.youtube.YouTubeClient;
import io.sfrei.tracksearch.tracks.*;
import io.sfrei.tracksearch.tracks.metadata.SoundCloudTrackMetadata;

import java.util.ArrayList;
import java.util.List;

public class MusicSearchProvider {
    private static YouTubeClient client = new YouTubeClient();   //add soundcloud support
    private static List<Track> failedSearch = new ArrayList<>();
    private static final Track failedTrack = new SoundCloudTrack("Search failed!",
            (long)0, "",
            new SoundCloudTrackMetadata("","", (long)0, ""));
    private static Track emptyTrack = new SoundCloudTrack("",
            (long)0, "",
            new SoundCloudTrackMetadata("","", (long)0, ""));

    public static List<Track> musicSearch(String query){
        List<Track> trackList;
        try{
            trackList = new ArrayList<>(client.getTracksForSearch(query).getTracks());
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
        return track.getTrackMetadata().getStreamAmount() == 0 && track.getLength() == 0;
    }
}