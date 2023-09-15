package plugway.mc.music.disc.dj.music.converter;


import io.sfrei.tracksearch.tracks.Track;
import plugway.mc.music.disc.dj.MusicDiskDj;

import java.io.File;

public class MusicConverter {
    /*private static final int channels = 1;                                //jave garbage
    private int bitrate;
    private int samplingRate;

    private AudioAttributes audioAttr = new AudioAttributes();
    private EncodingAttributes encoAttrs = new EncodingAttributes();
    private Encoder encoder = new Encoder();
    private String oggCodec = "vorbis";

    public MusicConverter(int bitrate, int samplingRate){
        this.bitrate = bitrate;
        this.samplingRate = samplingRate;

        audioAttr.setBitRate(bitrate);
        audioAttr.setChannels(channels);
        audioAttr.setSamplingRate(samplingRate);
        audioAttr.setCodec(oggCodec);

        encoAttrs.setFormat("ogg");
        encoAttrs.setAudioAttributes(audioAttr);
    }

    public int getBitrate() {
        return bitrate;
    }

    public int getSamplingRate() {
        return samplingRate;
    }

    public void mp3ToOgg(File source, File target){
        try {
            encoder.encode(source, target, encoAttrs);
        } catch (Exception ignored){}
    }*/
    public static File downloadOgg(Track track, int insteadOfDisk){                         //youtube-dl masterpiece
        //File target = new File(MusicDiskDj.tempPath+"\\"+insteadOfDisk+"_"+track.getCleanTitle()+".weba");
        File oggTarget = new File(MusicDiskDj.tempPath+"\\"+insteadOfDisk+"_"+track.getCleanTitle()+".ogg");
        try {
            Runtime.getRuntime().exec(MusicDiskDj.modDirectoryPath+"\\yt-dlp.exe --extract-audio --audio-format vorbis --output \""+oggTarget.getName().substring(0, oggTarget.getName().length()-4)+"\" "+track.getUrl(),
                    null, oggTarget.getParentFile()).waitFor();    //make command for mono --postprocessor-args "-ac 1"
        } catch (Exception e){
            e.printStackTrace();
        }
        return oggTarget;
    }
}
