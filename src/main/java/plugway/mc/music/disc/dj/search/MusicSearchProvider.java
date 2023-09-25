package plugway.mc.music.disc.dj.search;

import io.sfrei.tracksearch.clients.soundcloud.SoundCloudClient;
import io.sfrei.tracksearch.clients.youtube.YouTubeClient;
import io.sfrei.tracksearch.tracks.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class MusicSearchProvider {
    private static YouTubeClient clientYT = null;
    private static SoundCloudClient clientSC = null;
    private static List<Track> failedSearch = new ArrayList<>();
    private static final Track failedTrack = SoundCloudTrack.builder().title("Search failed!").build();
    private static final Track emptyTrack = SoundCloudTrack.builder().title("").duration(Duration.ZERO).build();

    public static List<Track> musicSearch(String query){
        if (clientYT == null)
            clientYT = new YouTubeClient();
        if (clientSC == null)
            clientSC = new SoundCloudClient();
        List<Track> trackList = new ArrayList<>();
        try{
            var id = LinkValidator.getYTId(query);
            if (id != null){
                trackList.add(YouTubeMetadata.getTrack(id));
                return trackList;
            }
            if (LinkValidator.isValidSCLink(query)){
                //System.out.println("cool sc link: " + query);
            }
            return  mergeTrackList(new ArrayList<>(clientYT.getTracksForSearch(query)), new ArrayList<>(clientSC.getTracksForSearch(query)));
        } catch (Exception e){
            System.out.println("Search failed!");
            if (failedSearch.size() == 0)
                failedSearch.add(failedTrack);
            return failedSearch;
        }
    }

//    public static List<Track> musicSearch(List<String> links){
//        return failedSearch;
//    }
    private static List<Track> mergeTrackList(List<Track> tracksYT, List<Track> tracksSC){
        List<Track> trackList = new ArrayList<>();
        int groupSize = 5;
        int indexYT = 0;
        int indexSC = 0;

        while (indexYT < tracksYT.size() || indexSC < tracksSC.size()) {
            for (int i = 0; i < groupSize && indexYT < tracksYT.size(); i++) {
                trackList.add(tracksYT.get(indexYT));
                indexYT++;
            }
            for (int i = 0; i < groupSize && indexSC < tracksSC.size(); i++) {
                trackList.add(tracksSC.get(indexSC));
                indexSC++;
            }
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
    public static Track getFailedTrack(){ return failedTrack; }
    public static Track getEmptyTrack(){
        return emptyTrack;
    }
    public static boolean isEmptyTrack(Track track){
        return track.getDuration() == null || track.getDuration().isNegative() || track.getDuration().isZero();
    }
}