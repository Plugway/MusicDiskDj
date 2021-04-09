package plugway.mc.music.disc.dj.gui;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import io.sfrei.tracksearch.tracks.Track;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.apache.commons.io.FileUtils;
import org.lwjgl.glfw.GLFW;
import plugway.mc.music.disc.dj.MusicDiskDj;
import plugway.mc.music.disc.dj.files.FileManager;
import plugway.mc.music.disc.dj.gui.widgets.WClickablePlainPanel;
import plugway.mc.music.disc.dj.image.TextureCreator;
import plugway.mc.music.disc.dj.music.converter.MusicConverter;
import plugway.mc.music.disc.dj.music.disks.Disk;
import plugway.mc.music.disc.dj.music.disks.MinecraftDiskProvider;
import plugway.mc.music.disc.dj.music.downloader.MusicDownloader;
import plugway.mc.music.disc.dj.search.MusicSearchProvider;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.nio.charset.Charset;
import java.sql.Time;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class MainGui extends LightweightGuiDescription {
    private static final int resultsCount = 25;
    private static int choosedResult = -1;
    private static WTextField searchField = new WTextField(new TranslatableText("musicdiskdj.name.field.suggestion"));
    private static WClickablePlainPanel[] results = new WClickablePlainPanel[resultsCount];
    private static WSprite[] preview = new WSprite[resultsCount];
    private static WLabel[] name = new WLabel[resultsCount];
    private static WLabel[] views = new WLabel[resultsCount];
    private static WLabel[] duration = new WLabel[resultsCount];
    private static WLabel[] channel = new WLabel[resultsCount];
    private static List<Track> latestTracks = MusicSearchProvider.getEmptyList(resultsCount);

    private static final int musicDisksCount = MinecraftDiskProvider.musicDisksCount;
    private static int choosedMusicDisk = -1;
    private static WClickablePlainPanel[] disks = new WClickablePlainPanel[musicDisksCount];
    private static WSprite[] disksPreview = new WSprite[musicDisksCount];
    private static WLabel[] diskName = new WLabel[musicDisksCount];
    private static WLabel[] diskAuthor = new WLabel[musicDisksCount];
    private static List<Track> musicDisks = MusicSearchProvider.getEmptyList(musicDisksCount);

    private enum inTheAreaOf {
        results, disks
    }

    public MainGui() {
        WPlainPanel root = new WPlainPanel();
        root.setSize(640,360);
        setRootPanel(root);

        searchField.setEditable(true);
        searchField.setMaxLength(50);
        root.add(searchField, 10, 20, 331, 1);

        WButton searchButton = new WButton(new TranslatableText("gui.recipebook.search_hint"));
        searchButton.setOnClick(performSearch());
        root.add(searchButton, searchField.getX()+searchField.getWidth()+ 10,searchField.getY(), 60, 20);


        //RESULTS BLOCK
        WLabel resultLabel = new WLabel(new TranslatableText("musicdiskdj.name.label.result"));
        root.add(resultLabel, searchField.getX()+5, searchField.getY()+30);

        WPlainPanel resultPanel = new WPlainPanel();
        resultPanel.setSize(386, 1470);
        WScrollPanel resultScrollPanel = new WScrollPanel(resultPanel);
        resultScrollPanel.setScrollingHorizontally(TriState.FALSE);
        resultScrollPanel.setBackgroundPainter(BackgroundPainter.createColorful(0));
        root.add(resultScrollPanel, searchField.getX()+7, resultLabel.getY()+20, 386, 250);

        for (int i = 0; i < resultsCount; i++){
            Track track = MusicSearchProvider.getEmptyTrack();
            try {
                track = latestTracks.get(i);
            } catch (Exception ignored){}

            results[i] = new WClickablePlainPanel();
            results[i].setAllowToClick(false);
            results[i].setOnClick(choose(i, results, inTheAreaOf.results)).onClick(0, 0, GLFW.GLFW_MOUSE_BUTTON_1);
            results[i].setAllowToClick(true);
            preview[i] = new WSprite(new Identifier("mcmddj", "textures/blank.png"));
            name[i] =  new WLabel(cutStringTo(42, track.getTitle()));
            if (track.getTrackMetadata().getStreamAmount() != 0 || track.getLength() != 0){
                views[i] = new WLabel(toPrettyString(track.getTrackMetadata().getStreamAmount()));
                duration[i] = new WLabel(new Time(track.getLength()*1000).toString());
                results[i].setAllowToClick(true);
            } else {
                views[i] = new WLabel("");
                duration[i] = new WLabel("");
                results[i].setAllowToClick(false);
            }
            channel[i] = new WLabel(track.getTrackMetadata().getChannelName());

            results[i].add(preview[i], 0, 3, 90, 50);
            results[i].add(name[i], preview[i].getX()+preview[i].getWidth()+10, preview[i].getY()+3);
            results[i].add(channel[i], preview[i].getX()+preview[i].getWidth()+10, name[i].getY()+10);
            results[i].add(views[i], preview[i].getX()+preview[i].getWidth()+10, channel[i].getY()+10);
            results[i].add(duration[i], preview[i].getX()+preview[i].getWidth()+10, views[i].getY()+10);

            resultPanel.add(results[i], 10, 10+i*73, 350, 56);//386 60
        }


        //MUSIC DISKS BLOCK
        WPlainPanel disksPanel = new WPlainPanel();
        disksPanel.setSize(120, 535);
        WScrollPanel disksScrollPanel = new WScrollPanel(disksPanel);
        disksScrollPanel.setScrollingHorizontally(TriState.FALSE);
        disksScrollPanel.setBackgroundPainter(BackgroundPainter.createColorful(0));
        root.add(disksScrollPanel, searchButton.getX()+searchButton.getWidth()+23, 15, 190, 330);

        for (int i = 0; i < musicDisksCount; i++){
            Track track = musicDisks.get(i);
            Disk disk = MinecraftDiskProvider.disks[i];
            disks[i] = new WClickablePlainPanel();
            disks[i].setAllowToClick(false);
            disks[i].setOnClick(choose(i, disks, inTheAreaOf.disks));
            disks[i].setAllowToClick(true);
            disksPreview[i] = new WSprite(disk.getId());
            if (MusicSearchProvider.isEmptyTrack(track)){
                diskAuthor[i] = new WLabel(disk.getAuthor());
                diskName[i] = new WLabel(disk.getName());
            } else {
                diskAuthor[i] = new WLabel(track.getTrackMetadata().getChannelName());
                diskName[i] = new WLabel(cutStringTo(20, track.getTitle()));
            }
            diskAuthor[i].setVerticalAlignment(VerticalAlignment.CENTER);
            diskName[i].setVerticalAlignment(VerticalAlignment.CENTER);

            disks[i].add(disksPreview[i], 0, 0, 35, 35);
            disks[i].add(diskAuthor[i], preview[i].getX()+preview[i].getWidth()-45, preview[i].getY());
            disks[i].add(diskName[i], diskAuthor[i].getX(), diskAuthor[i].getY()+diskAuthor[i].getHeight()-8);

            disksPanel.add(disks[i], 10, 10+i*40, 156, 0);
        }
        //turning on outline
        choose(choosedResult, results, inTheAreaOf.results).run();
        choose(choosedMusicDisk, disks, inTheAreaOf.disks).run();


        //operation buttons
        WButton removeButton = new WButton(Text.of("-"));
        removeButton.setOnClick(removeTrackFromDisks(removeButton));
        root.add(removeButton, resultScrollPanel.getX()+resultScrollPanel.getWidth()-12, resultScrollPanel.getY()+resultScrollPanel.getHeight()+13, 20, 20);

        WButton addButton = new WButton(Text.of("+"));
        addButton.setOnClick(addTrackToDisks(addButton));
        root.add(addButton, removeButton.getX()-25, removeButton.getY(), 20, 20);

        WButton makeCoolButton = new WButton(new TranslatableText("structure_block.mode.save"));
        makeCoolButton.setOnClick(createResourcePack(makeCoolButton, addButton, removeButton, searchButton));
        root.add(makeCoolButton, addButton.getX() - 55, removeButton.getY(), 50, 20);

        root.validate(this);
    }
    public static void open(){
        MinecraftClient.getInstance().openScreen(new MainScreen(new MainGui()));
    }
    public static Runnable choose(int index, WClickablePlainPanel[] area, inTheAreaOf areaOf){
        return () -> {
            if (index == -1)
                return;
            for (int i = 0; i < area.length; i++){          //maybe remove?
                if(i == index){
                    area[i].setBackgroundPainter(BackgroundPainter.createColorful(0));
                    if (areaOf == inTheAreaOf.results)
                        choosedResult = i;
                    else
                        choosedMusicDisk = i;
                }
                else
                    area[i].setBackgroundPainter(null);
            }
        };
    }
    public static Runnable performSearch(){
        return () -> {
            String query = searchField.getText();
            if (query.equals(""))
                return;
            latestTracks = MusicSearchProvider.musicSearch(query);
            updateResults();
        };
    }
    private static void updateResults(){
        for (int i = 0; i < resultsCount; i++){
            Track track = MusicSearchProvider.getEmptyTrack();
            try {
                track = latestTracks.get(i);
            } catch (Exception ignored){}
            name[i].setText(Text.of(cutStringTo(42, track.getTitle())));
            if (track.getTrackMetadata().getStreamAmount() != 0 || track.getLength() != 0){
                views[i].setText(Text.of(toPrettyString(track.getTrackMetadata().getStreamAmount())));
                duration[i].setText(Text.of(new Time(track.getLength()*1000).toString()));
                results[i].setAllowToClick(true);
            } else {
                views[i].setText(Text.of(""));
                duration[i].setText(Text.of(""));
                results[i].setAllowToClick(false);
            }
            channel[i].setText(Text.of(track.getTrackMetadata().getChannelName()));
            results[i].setBackgroundPainter(null);
            choosedResult = -1;
        }
    }
    private static void updateDisks(){
        for (int i = 0; i < musicDisksCount; i++) {
            Track track = musicDisks.get(i);
            Disk disk = MinecraftDiskProvider.disks[i];
            if (MusicSearchProvider.isEmptyTrack(track)) {
                diskAuthor[i].setText(Text.of(disk.getAuthor()));
                diskName[i].setText(Text.of(disk.getName()));
            } else {
                diskAuthor[i].setText(Text.of(track.getTrackMetadata().getChannelName()));
                diskName[i].setText(Text.of(cutStringTo(20, track.getTitle())));
            }
        }
    }
    private static Runnable addTrackToDisks(WButton addButton){
        return () -> {
            if (choosedMusicDisk == -1 || choosedResult == -1)
                return;
            addButton.setEnabled(false);
            musicDisks.set(choosedMusicDisk, latestTracks.get(choosedResult));
            updateDisks();
            addButton.setEnabled(true);
        };
    }
    private static Runnable removeTrackFromDisks(WButton removeButton){
        return () -> {
            if (choosedMusicDisk == -1)
                return;
            removeButton.setEnabled(false);
            musicDisks.set(choosedMusicDisk, MusicSearchProvider.getEmptyTrack());
            updateDisks();
            removeButton.setEnabled(true);
        };
    }
    private static Runnable createResourcePack(WButton makeCoolButton, WButton addButton, WButton removeButton, WButton searchButton){
        return () -> {
            makeCoolButton.setEnabled(false);
            addButton.setEnabled(false);
            removeButton.setEnabled(false);
            searchButton.setEnabled(false);
            Thread creationThread = new Thread(() -> {
                try {
                    FileUtils.copyDirectory(new File(MusicDiskDj.templatePath), new File(MusicDiskDj.resultPath));
                    File langFile = new File(MusicDiskDj.resultPath+"\\assets\\minecraft\\lang\\en_us.json");
                    String langFileContent = FileUtils.readFileToString(langFile, Charset.defaultCharset());
                    //downloading files
                    for (int i = 0; i < musicDisksCount; i++){
                        Track track = musicDisks.get(i);
                        if (MusicSearchProvider.isEmptyTrack(track))
                            continue;

                        //dealing with music
                        File oggTrackFile = MusicConverter.downloadOgg(track, i);
                        //File oggTrackFile = new File(MusicDiskDj.tempPath+"\\"+trackFile.getName().split("\\.")[0]+".ogg");
                        //converter.mp3ToOgg(trackFile, oggTrackFile);
                        FileUtils.copyFile(oggTrackFile, new File(MusicDiskDj.resultPath+
                                "\\assets\\minecraft\\sounds\\records\\"+ MinecraftDiskProvider.disks[i].getName().toLowerCase()+".ogg"));

                        //dealing with textures
                        File previewFile = MusicDownloader.downloadPreview(track, i);
                        Image preview = ImageIO.read(previewFile);
                        TextureCreator.modifyTexture(preview, new File(MusicDiskDj.resultPath+
                                "\\assets\\minecraft\\"+ MinecraftDiskProvider.disks[i].getId().toString().split(":")[1]));

                        //dealing with names
                        langFileContent = langFileContent.replaceAll(MinecraftDiskProvider.disks[i].getAuthor()+" - "+
                                MinecraftDiskProvider.disks[i].getName(), track.getCleanTitle());
                    }
                    FileUtils.write(langFile, langFileContent, Charset.defaultCharset());

                    //adding to archive
                    File outputArchive = new File(MusicDiskDj.tempPath+"\\mcmddj_result.zip");
                    FileManager.archiveDirContents(new File(MusicDiskDj.resultPath), outputArchive);

                    //copying archive to resource packs
                    FileUtils.copyFile(outputArchive, new File(MusicDiskDj.mcDirectoryPath+"\\resourcepacks\\mcmddj_result.zip"));

                    //collecting garbage
                    FileUtils.cleanDirectory(new File(MusicDiskDj.tempPath));
                    new File(MusicDiskDj.resultPath).mkdir();

                    makeCoolButton.setEnabled(true);
                    addButton.setEnabled(true);
                    removeButton.setEnabled(true);
                    searchButton.setEnabled(true);
                } catch (Exception ignored){}
            });
            creationThread.start();
        };
    }
    private static String cutStringTo(int charNum, String string){
        if (string.length() > charNum)
            string = string.substring(0,charNum) + "...";
        return string;
    }
    private static String toPrettyString(Long number){
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');
        DecimalFormat formatter = new DecimalFormat("###,###", symbols);
        return formatter.format(number);
    }
}
