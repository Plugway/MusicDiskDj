package plugway.mc.music.disc.dj.search;

import io.sfrei.tracksearch.clients.youtube.YouTubeClient;
import io.sfrei.tracksearch.tracks.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class MusicSearchProvider {
    private static YouTubeClient client = new YouTubeClient();   //add soundcloud support
    private static List<Track> failedSearch = new ArrayList<>();
    private static final Track failedTrack = SoundCloudTrack.builder().title("Search failed!").build();
    private static final Track emptyTrack = SoundCloudTrack.builder().title("").duration(Duration.ZERO).build();

    public static List<Track> musicSearch(String query){
        List<Track> trackList = new ArrayList<>();
        try{
            var id = LinkValidator.getYTId(query);
            if (id != null){
                trackList.add(YouTubeMetadata.getTrack(id));
            }
            else if (LinkValidator.isValidSCLink(query)){
                //System.out.println("cool sc link: " + query);
            }
            else
                trackList = new ArrayList<>(client.getTracksForSearch(query));
        } catch (Exception e){
            System.out.println("Search failed!");
            if (failedSearch.size() == 0)
                failedSearch.add(failedTrack);
            return failedSearch;
        }
        return trackList;
    }

//    public static List<Track> musicSearch(List<String> links){
//        return failedSearch;
//    }
    public static List<Track> getEmptyList(int size){
        List<Track> emptyList = new ArrayList<>();
        for (int i = 0; i < size; i++){
            emptyList.add(emptyTrack);
        }
        return emptyList;
    }
    public static Track getFailedTrack(){ return failedTrack; }
    public static Track getEmptyTrack(){
        return emptyTrack;
    }
    public static boolean isEmptyTrack(Track track){
        return track.getDuration() == null || track.getDuration().isNegative() || track.getDuration().isZero();
    }
}