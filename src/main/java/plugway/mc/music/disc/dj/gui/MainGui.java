package plugway.mc.music.disc.dj.gui;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import io.sfrei.tracksearch.tracks.Track;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.commons.io.FileUtils;
import org.lwjgl.glfw.GLFW;
import plugway.mc.music.disc.dj.MusicDiskDj;
import plugway.mc.music.disc.dj.files.FileManager;
import plugway.mc.music.disc.dj.gui.handlers.ProgressBarHandler;
import plugway.mc.music.disc.dj.gui.handlers.Status;
import plugway.mc.music.disc.dj.gui.handlers.StatusHandler;
import plugway.mc.music.disc.dj.gui.handlers.StatusLabelHandler;
import plugway.mc.music.disc.dj.gui.widgets.WClickablePlainPanel;
import plugway.mc.music.disc.dj.image.PreviewProvider;
import plugway.mc.music.disc.dj.image.TextureCreator;
import plugway.mc.music.disc.dj.music.converter.MusicConverter;
import plugway.mc.music.disc.dj.music.disks.Disk;
import plugway.mc.music.disc.dj.music.disks.MinecraftDiskProvider;
import plugway.mc.music.disc.dj.music.downloader.MusicDownloader;
import plugway.mc.music.disc.dj.resourcepacks.ResourcePackHandler;
import plugway.mc.music.disc.dj.search.MusicSearchProvider;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class MainGui extends LightweightGuiDescription {
    private static MainScreen mainScreen;
    private static final int resultsCount = 20;
    private static int choosedResult = -1;
    private static WTextField searchField = new WTextField(Text.translatable("musicdiskdj.name.field.suggestion"));
    //buttons
    private static WButton searchButton = new WButton(Text.translatable("gui.recipebook.search_hint"));
    private static WButton removeButton = new WButton(toText("-"));
    private static WButton addButton = new WButton(toText("+"));
    private static WButton makeCoolButton = new WButton(Text.translatable("structure_block.mode.save"));
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
    private static Identifier blankTexture = new Identifier("mcmddj", "textures/blank_debug1.png");
    private static StatusHandler statusHandler = new StatusHandler(new ProgressBarHandler(WBar.Direction.RIGHT, 0, 100),
            new StatusLabelHandler());

    private enum inTheAreaOf {
        results, disks
    }

    public MainGui() {
        WPlainPanel root = new WPlainPanel();
        root.setSize(640,360);
        setRootPanel(root);

        searchField.setEditable(true);
        searchField.setMaxLength(50);
        root.add(searchField, 16, 16, 331, 1);

        searchButton.setOnClick(performSearch());
        root.add(searchButton, searchField.getX()+searchField.getWidth() + 10,searchField.getY(), 65, 20);


        //RESULTS BLOCK
        WPlainPanel resultLabelPanel = new WPlainPanel();
        resultLabelPanel.setBackgroundPainter(BackgroundPainter.createColorful(0));
        resultLabelPanel.setSize(searchField.getWidth(), 20);
        WLabel resultLabel = new WLabel(Text.translatable("musicdiskdj.name.label.result"));
        resultLabel.setVerticalAlignment(VerticalAlignment.CENTER);
        resultLabelPanel.add(resultLabel, 4, 0, resultLabelPanel.getWidth()-8, resultLabelPanel.getHeight());
        root.add(resultLabelPanel, searchField.getX(), searchField.getY()+searchField.getHeight()-2, resultLabelPanel.getWidth(), resultLabelPanel.getHeight());

        WPlainPanel resultPanel = new WPlainPanel();
        resultPanel.setSize(405, 200);
        WScrollPanel resultScrollPanel = new WScrollPanel(resultPanel);
        resultScrollPanel.setScrollingHorizontally(TriState.FALSE);
        resultScrollPanel.setBackgroundPainter(BackgroundPainter.createColorful(0));
        root.add(resultScrollPanel, resultLabelPanel.getX(), resultLabelPanel.getY()+resultLabelPanel.getHeight()-1, 405, 258);

        for (int i = 0; i < resultsCount; i++){
            Track track = MusicSearchProvider.getEmptyTrack();
            try {
                track = latestTracks.get(i);
            } catch (Exception ignored){}
            //var isEmptyTrack = MusicSearchProvider.isEmptyTrack(track);

            results[i] = new WClickablePlainPanel();
            results[i].setAllowToClick(false);
            results[i].setOnClick(choose(i, results, inTheAreaOf.results)).onClick(0, 0, GLFW.GLFW_MOUSE_BUTTON_1);
            preview[i] = new WSprite(blankTexture);
            name[i] =  new WLabel(toText(""));
            views[i] = new WLabel(toText(""));
            duration[i] = new WLabel(toText(""));
            channel[i] = new WLabel(toText(""));

            results[i].add(preview[i], 3, 3, 90, 50);
            results[i].add(name[i], preview[i].getX()+preview[i].getWidth()+10, preview[i].getY()+3);
            results[i].add(channel[i], preview[i].getX()+preview[i].getWidth()+10, name[i].getY()+10);
            results[i].add(views[i], preview[i].getX()+preview[i].getWidth()+10, channel[i].getY()+10);
            results[i].add(duration[i], preview[i].getX()+preview[i].getWidth()+10, views[i].getY()+10);

            resultPanel.add(results[i], 5, 5+i*60, 388, 56);//386 60
        }
        resultPanel.setSize(405, 6+resultsCount*60);//move to update results for dynamic results count
        updateResults();


        //MUSIC DISKS BLOCK
        WPlainPanel disksPanel = new WPlainPanel();
        disksPanel.setSize(120, 535);
        WScrollPanel disksScrollPanel = new WScrollPanel(disksPanel);
        disksScrollPanel.setScrollingHorizontally(TriState.FALSE);
        disksScrollPanel.setBackgroundPainter(BackgroundPainter.createColorful(0));
        root.add(disksScrollPanel, root.getWidth() - 190 - 15, 15, 190, root.getHeight()-30);

        for (int i = 0; i < musicDisksCount; i++){
            Track track = musicDisks.get(i);
            Disk disk = MinecraftDiskProvider.disks[i];
            disks[i] = new WClickablePlainPanel();
            disks[i].setAllowToClick(false);
            disks[i].setOnClick(choose(i, disks, inTheAreaOf.disks));
            disks[i].setAllowToClick(true);
            disksPreview[i] = new WSprite(disk.getId());
            if (MusicSearchProvider.isEmptyTrack(track)){
                diskAuthor[i] = new WLabel(toText(disk.getAuthor()));
                diskName[i] = new WLabel(toText(disk.getName()));
            } else {
                diskAuthor[i] = new WLabel(toText(track.getTrackMetadata().getChannelName()));
                diskName[i] = new WLabel(cutStringTo(20, track.getTitle()));
            }
            diskAuthor[i].setVerticalAlignment(VerticalAlignment.CENTER);
            diskName[i].setVerticalAlignment(VerticalAlignment.CENTER);

            disks[i].add(disksPreview[i], 3, 0, 35, 35);
            disks[i].add(diskAuthor[i], disksPreview[i].getX()+disksPreview[i].getWidth()+3, disksPreview[i].getY()+4);
            disks[i].add(diskName[i], diskAuthor[i].getX(), diskAuthor[i].getY()+diskAuthor[i].getHeight()-8);

            disksPanel.add(disks[i], 5, 5+i*39, 173, 0);
        }
        var disksSpacer = new WPlainPanel();
        disksSpacer.setSize(0, 0);
        disksPanel.add(disksSpacer, 5, musicDisksCount*40, 0, 5); //space after last disk in list 4pixels

        //turning on outline
        choose(choosedResult, results, inTheAreaOf.results).run();
        choose(choosedMusicDisk, disks, inTheAreaOf.disks).run();


        //operation buttons and bottom left part

        removeButton.setOnClick(removeTrackFromDisks(removeButton));
        root.add(removeButton, resultScrollPanel.getX()+resultScrollPanel.getWidth()-20, disksScrollPanel.getY()+disksScrollPanel.getHeight()-20, 20, 20);

        addButton.setOnClick(addTrackToDisks(addButton));
        root.add(addButton, removeButton.getX()-25, removeButton.getY(), 20, 20);

        makeCoolButton.setOnClick(createResourcePack(makeCoolButton, addButton, removeButton, searchButton));
        root.add(makeCoolButton, addButton.getX() - 55, removeButton.getY(), 50, 20);

        root.add(statusHandler.getProgressBar(), resultScrollPanel.getX(), removeButton.getY(), 300, 6);
        statusHandler.getStatusLabel().setVerticalAlignment(VerticalAlignment.BOTTOM).setHorizontalAlignment(HorizontalAlignment.LEFT);
        root.add(statusHandler.getStatusLabel(), statusHandler.getProgressBar().getX(), removeButton.getY(), statusHandler.getProgressBar().getWidth(), removeButton.getHeight());


        root.validate(this);

    }
    public static void open(){
        if (mainScreen == null){
            mainScreen = new MainScreen(new MainGui());
        }
        MinecraftClient.getInstance().setScreen(mainScreen);
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
            makeCoolButton.setEnabled(false);
            addButton.setEnabled(false);
            removeButton.setEnabled(false);
            searchButton.setEnabled(false);
            Thread searchThread = new Thread(() -> {
                String query = searchField.getText();
                if (query.equals(""))
                    return;

                statusHandler.getProgressBarHandler().setSectionsCount(2);
                statusHandler.setStatus(1, Status.searching);//status
                latestTracks = MusicSearchProvider.musicSearch(query);

                updateResults();

                makeCoolButton.setEnabled(true);
                addButton.setEnabled(true);
                removeButton.setEnabled(true);
                searchButton.setEnabled(true);
            });
            searchThread.start();
        };
    }
    private static void updateResults(){
        statusHandler.setStatus(Status.updatingResults);
        statusHandler.getProgressBarHandler().nextSection();
        for (int i = 0; i < resultsCount; i++){
            statusHandler.setStatus((i+1d)/resultsCount);
            Track track = MusicSearchProvider.getEmptyTrack();
            try {
                track = latestTracks.get(i);
            } catch (Exception ignored){}
            var isEmptyTrack = MusicSearchProvider.isEmptyTrack(track);
            name[i].setText(cutStringTo(42, track.getTitle()));
            if (!isEmptyTrack){
                views[i].setText(toPrettyString(track.getTrackMetadata().getStreamAmount(), " views"));
                duration[i].setText(toText(track.durationFormatted()));
                var ident = PreviewProvider.getIdentifier(track.getTrackMetadata().getThumbNailUrl(), "result_" + i);
                preview[i].setImage(ident);
                channel[i].setText(toText(track.getTrackMetadata().getChannelName()));

                results[i].setAllowToClick(true);
            } else {
                views[i].setText(toText(""));
                duration[i].setText(toText(""));
                preview[i].setImage(blankTexture);
                channel[i].setText(toText(""));
                results[i].setAllowToClick(false);
            }
            results[i].setBackgroundPainter(null);
            choosedResult = -1;
        }
        if(statusHandler.getProgressBarHandler().isLastSection())
            statusHandler.reset();
    }
    private static void updateDisks(){
        for (int i = 0; i < musicDisksCount; i++) {
            Track track = musicDisks.get(i);
            Disk disk = MinecraftDiskProvider.disks[i];
            if (MusicSearchProvider.isEmptyTrack(track)) {
                diskAuthor[i].setText(toText(disk.getAuthor()));
                diskName[i].setText(toText(disk.getName()));
            } else {
                diskAuthor[i].setText(toText(track.getTrackMetadata().getChannelName()));
                diskName[i].setText(cutStringTo(20, track.getTitle()));
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
                    statusHandler.getProgressBarHandler().setSectionsCount(3);
                    statusHandler.setStatus(Status.cpCopyFiles);
                    FileUtils.copyDirectory(new File(MusicDiskDj.templatePath), new File(MusicDiskDj.resultPath));
                    File langFile = new File(MusicDiskDj.resultPath+"\\assets\\minecraft\\lang\\en_us.json");
                    String langFileContent = FileUtils.readFileToString(langFile, StandardCharsets.UTF_8);
                    //downloading files
                    statusHandler.getProgressBarHandler().nextSection();
                    for (int i = 0; i < musicDisksCount; i++){
                        statusHandler.setStatus((i+1d)/musicDisksCount);
                        Track track = musicDisks.get(i);
                        if (MusicSearchProvider.isEmptyTrack(track))
                            continue;

                        //dealing with music
                        statusHandler.setStatus(Status.cpDownloadingMusic);
                        File oggTrackFile = MusicConverter.downloadOgg(track, i);
                        //File oggTrackFile = new File(MusicDiskDj.tempPath+"\\"+trackFile.getName().split("\\.")[0]+".ogg");
                        //converter.mp3ToOgg(trackFile, oggTrackFile);
                        FileUtils.copyFile(oggTrackFile, new File(MusicDiskDj.resultPath+
                                "\\assets\\minecraft\\sounds\\records\\"+ MinecraftDiskProvider.disks[i].getName().toLowerCase()+".ogg"));

                        //dealing with textures
                        statusHandler.setStatus(Status.cpCreatingTexture);
                        File previewFile = MusicDownloader.downloadPreview(track, i);
                        Image preview = ImageIO.read(previewFile);
                        TextureCreator.modifyTexture(preview, new File(MusicDiskDj.resultPath+
                                "\\assets\\minecraft\\"+ MinecraftDiskProvider.disks[i].getId().toString().split(":")[1]));

                        //dealing with names
                        langFileContent = langFileContent.replaceAll(MinecraftDiskProvider.disks[i].getAuthor()+" - "+
                                MinecraftDiskProvider.disks[i].getName(), track.getCleanTitle());
                    }
                    statusHandler.getProgressBarHandler().nextSection();
                    FileUtils.write(langFile, langFileContent, Charset.defaultCharset());

                    //adding to archive
                    statusHandler.setStatus(1.0/4, Status.cpCreatingArchive);
                    File outputArchive = new File(MusicDiskDj.tempPath+"\\mcmddj_result.zip");
                    FileManager.archiveDirContents(new File(MusicDiskDj.resultPath), outputArchive);

                    //copying archive to resource packs
                    statusHandler.setStatus(1.0/4*2);
                    ResourcePackHandler.DisableResourcePack("file/mcmddj_result.zip");
                    FileUtils.copyFile(outputArchive, new File(MusicDiskDj.mcDirectoryPath+"\\resourcepacks\\mcmddj_result.zip"));
                    ResourcePackHandler.EnableResourcePack("file/mcmddj_result.zip");

                    //collecting garbage
                    statusHandler.setStatus(1.0/4*3);
                    FileUtils.cleanDirectory(new File(MusicDiskDj.tempPath));
                    new File(MusicDiskDj.resultPath).mkdir();

                    statusHandler.reset();
                    makeCoolButton.setEnabled(true);
                    addButton.setEnabled(true);
                    removeButton.setEnabled(true);
                    searchButton.setEnabled(true);
                } catch (Exception e){e.printStackTrace();}

            });
            creationThread.start();
        };
    }
    private static Text cutStringTo(int charNum, String string){
        if (string.length() > charNum)
            string = string.substring(0,charNum) + "...";
        return toText(string);
    }
    private static Text toPrettyString(Long number, String endsWith){
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');
        DecimalFormat formatter = new DecimalFormat("###,###", symbols);
        return toText(formatter.format(number) + endsWith);
    }
    private static Text toText(String string){
        return Text.of(string);
    }

}
