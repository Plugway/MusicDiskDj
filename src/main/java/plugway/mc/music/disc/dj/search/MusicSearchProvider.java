package plugway.mc.music.disc.dj.search;

import io.sfrei.tracksearch.clients.soundcloud.SoundCloudClient;
import io.sfrei.tracksearch.clients.youtube.YouTubeClient;
import io.sfrei.tracksearch.exceptions.TrackSearchException;
import io.sfrei.tracksearch.tracks.*;
import plugway.mc.music.disc.dj.MusicDiskDj;
import plugway.mc.music.disc.dj.gui.MainGui;
import plugway.mc.music.disc.dj.gui.handlers.Status;
import plugway.mc.music.disc.dj.gui.handlers.StatusHandler;

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
        List<Track> trackList = new ArrayList<>();
        List<Track> trackListYT = new ArrayList<>();
        List<Track> trackListSC = new ArrayList<>();
        try{
            var id = LinkValidator.getYTId(query);
            if (id != null){
                trackList.add(YouTubeMetadata.getTrack(id));
                return trackList;
            }
            if (LinkValidator.isValidSCLink(query)){
                //System.out.println("cool sc link: " + query);
            }
            if (clientYT == null && clientSC == null)
                throw new NullClientsException("0 clients connected");
            if (clientYT != null)
                trackListYT = new ArrayList<>(clientYT.getTracksForSearch(query));
            if (clientSC != null)
                trackListSC = new ArrayList<>(clientSC.getTracksForSearch(query));
            return  mergeTrackList(trackListYT, trackListSC);
        } catch (TrackSearchException | NullClientsException e){
            if (e instanceof TrackSearchException) {
                MusicDiskDj.LOGGER.info("Search failed: " + e);
            } else {
                MusicDiskDj.LOGGER.info("All clients are null, try to reconnect: " + e);
            }
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
    public static void connect(MainGui gui, StatusHandler statusHandler){
        statusHandler.getProgressBarHandler().setSectionsCount(2);
        statusHandler.setStatus(1, Status.connectingYT);//status
        try {
            Thread clientYTThread = new Thread(() -> {
                clientYT = new YouTubeClient();
            });
            clientYTThread.start();
            int sleepCount = 10;//sleepCount*sleepTime = overall sleep time
            while (sleepCount > 0 && clientYTThread.isAlive()){
                Thread.sleep(1000);
                sleepCount--;
            }
            if (clientYTThread.isAlive()){
                clientYTThread.stop();
                throw new ClientConnectException("YT client connect error");
            }
            gui.colorYTConnected();
        } catch (ClientConnectException | InterruptedException e){
            MusicDiskDj.LOGGER.info("Error while connecting: " + e);
            clientYT = null;
            gui.colorYTFailedToConnect();
        }

        statusHandler.getProgressBarHandler().nextSection();
        statusHandler.setStatus(1, Status.connectingSC);//status
        try {
            Thread clientSCThread = new Thread(() -> {
                clientSC = new SoundCloudClient();
            });
            clientSCThread.start();
            int sleepCount = 10;
            while (sleepCount > 0 && clientSCThread.isAlive()){
                Thread.sleep(1000);
                sleepCount--;
            }
            if (clientSCThread.isAlive()){
                clientSCThread.stop();
                throw new ClientConnectException("SC client connect error");
            }
            gui.colorSCConnected();
        } catch (ClientConnectException | InterruptedException e){
            MusicDiskDj.LOGGER.info("Error while connecting: " + e);
            clientSC = null;
            gui.colorSCFailedToConnect();
        }
        statusHandler.reset();
    }
}